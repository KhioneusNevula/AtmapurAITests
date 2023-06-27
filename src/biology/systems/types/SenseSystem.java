package biology.systems.types;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.collect.ImmutableSet;

import actor.Actor;
import actor.IComponentPart;
import actor.IComponentType;
import actor.IMultipart;
import actor.IVisage;
import actor.MultipartActor;
import actor.SentientActor;
import biology.systems.ESystem;
import biology.systems.SystemType;
import mind.Culture;
import mind.concepts.PropertyController;
import mind.concepts.identifiers.TemplateBasedIdentifier;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.concepts.type.SenseProperty;
import mind.concepts.type.SenseProperty.UniqueProperty;
import mind.memory.IKnowledgeBase;
import mind.memory.IPropertyData;
import mind.memory.SenseMemory;
import mind.memory.SenseMemory.TraitsMemory;
import sim.World;

public class SenseSystem extends ESystem {

	private SenseMemory<Actor> memory;
	private World world;
	private Collection<ISensor> senses;
	private int senseDistance;
	private Collection<SenseProperty<?>> propertiesToSense = Set.of();

	public SenseSystem(Actor owner, SenseMemory<Actor> memory, int senseDistance, ISensor... senses) {
		super(SystemType.SENSE, owner);
		this.memory = memory;
		this.world = owner.getWorld();
		this.senses = Set.of(senses);
		this.senseDistance = senseDistance;
	}

	public SenseSystem senseProperties(SenseProperty<?>... properties) {
		this.propertiesToSense = ImmutableSet.<SenseProperty<?>>builder().addAll(propertiesToSense).add(properties)
				.build();
		return this;
	}

	public SenseMemory<Actor> getMemory() {
		return memory;
	}

	public int getSenseDistance() {
		return senseDistance;
	}

	public Collection<ISensor> getSenses() {
		return senses;
	}

	@Override
	public Actor getOwner() {
		return (Actor) super.getOwner();
	}

	@Override
	protected void update(long ticks) {
		super.update(ticks);
		for (ISensor sense : senses) {
			senseInGeneral(sense);
		}
		if (this.memory.maxedOut())
			return;
		Profile self = memory.addProfile(this.getOwner().getUUID(), this.getOwner().getName(), false);
		memory.learnLocation(self, this.getOwner().getLocation());

		Actor focus = this.handleActorSensing();

		if (focus != null && (focus == this.getOwner() || !memory.maxedOut())) {
			this.handlePartByPartTraitSensing(focus);
		}
	}

	private void senseInGeneral(ISensor sense) {
		for (SenseProperty<?> prop : propertiesToSense) {
			Collection<Object> traits = sense.getSensed(world, prop, this, getOwner());
			if (traits == null)
				continue;
			for (Object o : traits) {
				memory.sense(prop, o);
			}
		}

	}

	/**
	 * "Controller" may be null, which will jump to the second if statement branch
	 * 
	 * @param sensedActor
	 * @param prop
	 * @param controller
	 * @return
	 */
	private IPropertyData getOrCreateIdentifier(Actor sensedActor, Property prop, PropertyController controller) {
		if (controller != null) {
			IPropertyData dat = controller.getIdentifier().identifyInfo(prop, sensedActor, sensedActor.getVisage());
			if (!dat.isUnknown()) {
				return dat;
			}
		}
		Collection<ISensor> sensas = sensedActor.getPreferredSensesForHint(prop);
		double denom = sensas.size();
		double numer = 0;
		for (ISensor sensa : this.getSenses()) {
			if (sensas.contains(sensa)) {
				numer++;
			}
		}
		double chance = 0.9 * (numer / denom);
		TemplateBasedIdentifier identifier = new TemplateBasedIdentifier(sensedActor.getSpecies(),
				(a) -> a.getPropertyHint(prop), chance);
		IPropertyData res = IPropertyData.UNKNOWN;
		if (!(res = identifier.identifyInfo(prop, sensedActor, sensedActor.getVisage())).isUnknown()) {
			controller.editIdentifier().addIdentifier(identifier);
			return res;
		}
		return IPropertyData.UNKNOWN;

	}

	private void handlePropertyAssignment(Actor sensedActor) {
		Collection<Property> props = new TreeSet<>(
				this.getOwner().getAsLiving().getMind().getMindMemory().getRecognizedProperties());
		this.getOwner().getAsLiving().getMind().getMindMemory().cultures()
				.forEach((c) -> props.addAll(c.getRecognizedProperties()));
		for (Property prop : props) {

			PropertyController propc = this.getOwner().getAsLiving().getMind().getMindMemory()
					.getPropertyAssociations(prop);
			IPropertyData dat = null;
			IKnowledgeBase key = null;
			if (propc != null) {
				dat = this.getOrCreateIdentifier(sensedActor, prop, propc);
				key = ((SentientActor) this.getOwner()).getMind().getKnowledgeBase();
			}
			if (dat == null || dat.isUnknown()) {
				Map<Culture, PropertyController> assoc = (this.getOwner().getAsLiving().getMind().getMindMemory()
						.getPropertyAssociationsFromCulture(prop));
				for (Map.Entry<Culture, PropertyController> entry : assoc.entrySet()) {

					IPropertyData datau = this.getOrCreateIdentifier(sensedActor, prop, entry.getValue());
					key = entry.getKey();
					if (!datau.isUnknown() && (dat == null ? true : datau.getKnownCount() > dat.getKnownCount())) {
						dat = datau;
						break;
					}
				}
			}
			if (dat == null) {
				IPropertyData datau = this.getOrCreateIdentifier(sensedActor, prop, propc);
				if (!datau.isUnknown())
					dat = datau;

			}
			// if we have properties identified for this dummy
			if (dat != null && !dat.isUnknown()) {
				sensedActor.assignProperty(key, prop, dat);
			}
		}
	}

	/**
	 * return false if a failure occurs (e.g. full memory)
	 * 
	 * @param sensedActor
	 * @param prof
	 * @return
	 */
	private boolean handleGeneralTraitSensing(Actor sensedActor, Profile prof) {

		TraitsMemory<Actor> traits = memory.obtainTraitsForProfile(prof, sensedActor == this.getOwner());
		if (traits == null)
			return false;
		IVisage visage = sensedActor.getVisage();

		for (ISensor sense : senses) {
			Collection<SenseProperty<?>> props = visage.getSensableTraits(sense);
			for (SenseProperty<?> prop : props) {
				Object o = sense.getGeneralTrait(prop, sensedActor, world, this, this.getOwner());
				if (o != null) {
					traits.getGeneralTraits().learnTrait(prop, o);
				}

			}
		}
		return true;
	}

	/**
	 * Returns a "focus" actor; TODO better sorting than just distance
	 * 
	 * @return
	 */
	private Actor handleActorSensing() {
		Actor focus = null;
		for (Actor sensedActor : world.getActors()) {
			if (this.memory.maxedOut() && sensedActor != this.getOwner())
				return null;
			if (sensedActor.distance(this.getOwner()) > this.senseDistance || sensedActor.getVisage() == null)
				continue;
			if (focus == null || sensedActor.distance(this.getOwner()) < focus.distance(this.getOwner()))
				focus = sensedActor;
			Profile prof = this.memory.senseActor(sensedActor, sensedActor == this.getOwner());
			if (this.handleGeneralTraitSensing(sensedActor, prof)) {
				memory.learnLocation(prof, sensedActor.getLocation());
			}
			this.handlePropertyAssignment(sensedActor);
		}
		return focus;
	}

	private void handlePartByPartTraitSensing(Actor focus) {
		Profile prof = this.memory.addProfile(focus.getUUID(), focus.getName().toLowerCase(), focus == this.getOwner());
		TraitsMemory<Actor> traits = memory.obtainTraitsForProfile(prof, false);
		IVisage visage = focus.getVisage();
		IMultipart body = null;
		if (visage.isMultipart()) {
			body = visage.getAsMultipart();
		}

		for (ISensor sense : senses) {
			Collection<SenseProperty<?>> props = visage.getSensableTraits(sense);
			for (SenseProperty<?> prop : props) {
				Object o = null;
				if (body != null) {
					if (!(prop instanceof UniqueProperty)) {
						for (IComponentType partType : body.getPartTypes().values()) {
							o = sense.getSensedTrait((MultipartActor) focus, prop, partType, world, this,
									this.getOwner());
							if (o != null) {
								traits.getOrInitTraits(partType).learnTrait(prop, o);
							}
						}
					}
					for (IComponentPart part : body.getParts()) {
						o = sense.getSpecificSensedTrait((MultipartActor) focus, prop, part, world, this,
								this.getOwner());
						if (o != null) {
							TraitsMemory<Actor>.Traits<Actor> tra = traits.getOrInitTraits(part);
							tra.learnTrait(prop, o);
						}
					}
				}
			}
		}
	}

	public Collection<SenseProperty<?>> getPropertiesToSense() {
		return propertiesToSense;
	}

	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	public String report() {
		return this.getType().toString() + "{d:" + this.senseDistance + ",senses:" + this.senses + "}";
	}

}
