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
import biology.systems.ESystem;
import biology.systems.SystemType;
import mind.Culture;
import mind.concepts.PropertyController;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.concepts.type.SenseProperty;
import mind.concepts.type.SenseProperty.UniqueProperty;
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
		Actor focus = null;
		for (Actor a : world.getActors()) {
			if (this.memory.maxedOut() && a != this.getOwner())
				return;
			if (a.distance(this.getOwner()) > this.senseDistance || a.getVisage() == null)
				continue;
			if (focus == null || a.distance(this.getOwner()) < focus.distance(this.getOwner()))
				focus = a;
			Profile prof = this.memory.senseActor(a, false);

			TraitsMemory<Actor> traits = memory.obtainTraitsForProfile(prof, a == this.getOwner());
			if (traits == null)
				return;
			IVisage visage = a.getVisage();

			for (ISensor sense : senses) {
				Collection<SenseProperty<?>> props = visage.getSensableTraits(sense);
				for (SenseProperty<?> prop : props) {
					Object o = sense.getGeneralTrait(prop, a, world, this, this.getOwner());
					if (o != null) {
						traits.getGeneralTraits().learnTrait(prop, o);
					}

				}
			}
			memory.learnLocation(prof, a.getLocation());
			Collection<Property> props = new TreeSet<>(
					this.getOwner().getAsLiving().getMind().getMindMemory().getRecognizedProperties());
			this.getOwner().getAsLiving().getMind().getMindMemory().cultures()
					.forEach((c) -> props.addAll(c.getRecognizedProperties()));
			for (Property prop : props) {
				PropertyController propc = this.getOwner().getAsLiving().getMind().getMindMemory()
						.getPropertyAssociations(prop);
				IPropertyData dat = null;
				Culture key = null;
				if (propc != null) {
					dat = propc.getIdentifier().identifyInfo(prop, a, a.getVisage());
				}
				if (dat == null) {
					Map<Culture, PropertyController> assoc = (this.getOwner().getAsLiving().getMind().getMindMemory()
							.getPropertyAssociationsFromCulture(prop));
					for (Map.Entry<Culture, PropertyController> entry : assoc.entrySet()) {
						dat = entry.getValue().getIdentifier().identifyInfo(prop, a, a.getVisage());
						key = entry.getKey();
						if (dat != null)
							break;
					}
				}
				if (dat != null) {
					a.assignProperty(key, prop, dat);
					key.applyProperty(prof, prop);
					// System.out.println("Identified " + a + " as " + prop + " " + dat);
				}
			}
		}

		if (focus != null) {
			Profile prof = this.memory.addProfile(focus.getUUID(), "unit", false);
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
