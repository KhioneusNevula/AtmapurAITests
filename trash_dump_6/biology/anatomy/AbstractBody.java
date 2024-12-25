package biology.anatomy;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Table;

import actor.Actor;
import actor.construction.physical.IComponentPart;
import actor.construction.physical.IComponentType;
import actor.construction.physical.IPartAbility;
import actor.construction.physical.IPhysicalActorObject;
import actor.construction.properties.ISensableTrait;
import actor.construction.properties.SenseProperty;
import biology.sensing.ISense;
import civilization_and_minds.social.concepts.profile.Profile;
import metaphysical.ISpiritObject;
import metaphysical.ISpiritObject.SpiritType;
import sim.physicality.ExistencePlane;
import sim.physicality.PhysicalState;

/**
 * Add colors/appearances to body parts; add traits
 * 
 * @author borah
 *
 */
public abstract class AbstractBody implements IPhysicalActorObject {

	protected Table<String, UUID, BodyPart> bodyParts;
	protected Map<UUID, BodyPart> noParentParts;
	protected Map<UUID, BodyPart> outermostParts;
	protected Map<String, ITissueLayerType> tissueTypes = Map.of();
	protected Map<String, IBodyPartType> partTypes = Map.of();
	protected Map<String, ITissueLayerType> bloodTypes = Map.of();
	protected Multimap<IPartAbility, BodyPart> partsByAbility = MultimapBuilder.hashKeys().treeSetValues().build();
	protected UUID rootID;
	protected BodyPart rootPart;
	protected Actor owner;
	protected boolean built;
	protected float lifePercent = 1f;
	protected ISpecies species;
	protected int existencePlane = ExistencePlane.PHYSICAL.primeFactor();
	protected int visibilityPlane = ExistencePlane.ALL_PLANES.primeFactor();
	protected BodyPart onlyPart;
	protected int intactParts;
	protected int w;
	protected HitboxType hitbox;
	protected float mass;
	protected Multimap<SpiritType, ISpiritObject> allSpirits = MultimapBuilder.enumKeys(SpiritType.class)
			.hashSetValues().build();
	protected Profile profile;
	protected Multimap<Actor, BodyPart> actorsHeld = MultimapBuilder.hashKeys().hashSetValues().build();
	protected Collection<ISense> senses;

	public AbstractBody(Actor owner, int radius, float mass) {
		this.owner = owner;
		this.hitbox = HitboxType.CIRCLE;
		this.w = radius;
		this.mass = mass;
	}

	public AbstractBody(Actor owner, ISpecies template) {
		this.owner = owner;
		this.species = template;
		this.hitbox = HitboxType.CIRCLE;
		this.w = 10; // TODO species specific radius
		this.mass = 70; // TODO species specific mass
	}

	@Override
	public Profile getProfile() {
		return profile;
	}

	@Override
	public int getHitboxHeight() {
		return w;
	}

	@Override
	public int getHitboxRadius() {
		return w;
	}

	@Override
	public HitboxType getHitboxType() {
		return this.hitbox;
	}

	@Override
	public int getHitboxWidth() {
		return w;
	}

	public float getMass() {
		return mass;
	}

	/**
	 * Set the amount of blood/life essence in this creature
	 * 
	 * @param lifePercent
	 */
	public void setLifePercent(float lifePercent) {
		this.lifePercent = lifePercent;
	}

	/**
	 * lose an amount of blood of the given percentage
	 */
	public void bleed(float byPercent) {
		this.lifePercent *= (1 - byPercent);
	}

	public ISpecies getObjectType() {
		return species;
	}

	@Override
	public boolean isBuilt() {
		return built;
	}

	@Override
	public int sensabilityMode(ISense toSense) {
		return this.visibilityPlane;
	}

	/**
	 * What physicality this body has
	 * 
	 * @return
	 */
	@Override
	public int physicalityMode() {
		return existencePlane;
	}

	@Override
	public void changePhysicality(int newPhysicality) {
		this.existencePlane = newPhysicality;
	}

	@Override
	public void changeSensability(int newVisibility) {
		this.visibilityPlane = newVisibility;
	}

	/**
	 * Produce severed parts when this part is destroyed
	 * 
	 * @param atPart
	 * @return
	 */
	public Stream<SeveredBodyPart> severPart(BodyPart atPart) {
		if (!atPart.isGone()) {
			BodyPart atPartCopy = new BodyPart(atPart, true);
			atPartCopy.parent = atPart.parent;
			atPartCopy.setSurrounding(atPart.getSurrounding());
			this.bodyParts.put(atPart.type.getName(), atPart.id, atPartCopy);
			if (atPart.parent == null) {
				this.noParentParts.put(atPart.id, atPartCopy);
			} else {
				atPartCopy.parent.removeChild(atPart);
				atPartCopy.parent.addChild(atPartCopy);
			}
			if (atPart.getSurrounding() == null)
				this.outermostParts.put(atPart.id, atPartCopy);
			for (IPartAbility ability : atPart.type.abilities()) {
				this.partsByAbility.remove(ability, atPart);
				this.partsByAbility.put(ability, atPartCopy);
			}
			atPartCopy.getMaterials().values().forEach((s) -> s.changeState(PhysicalState.GONE));

			this.updatePart(atPartCopy);
			return Collections.singleton(new SeveredBodyPart(atPart, this, this.w / 2, mass / 2)).stream();
		} else {
			return atPart.getChildParts().values().stream().filter((a) -> !a.isGone()).map((a) -> {

				// a.getMaterials().values().forEach((s) -> s.changeState(PhysicalState.GONE));
				return new SeveredBodyPart(a, this, this.w / 2, mass / 2);
			});
		}
	}

	@Override
	public Map<UUID, BodyPart> getPartsWithoutParent() {
		return noParentParts == null ? Map.of() : noParentParts;
	}

	@Override
	public Stream<BodyPart> getOutermostParts() {
		return outermostParts == null ? Set.<BodyPart>of().stream() : outermostParts.values().stream();
	}

	/**
	 * IF this body part is exposed to the outside. TODO take holes into account
	 * 
	 * @param part
	 * @return
	 */
	public boolean isExposed(BodyPart part) {
		if (this.outermostParts != null && this.outermostParts.containsValue(part))
			return true;
		BodyPart curP = part;
		boolean exposed = true;
		IComponentType.Face holeface = null;
		IComponentType.Side holeside = null;
		while (curP.getSurrounding() != null) {
			curP = curP.getSurrounding();
			if (!curP.isGone()) {
				exposed = false;
				break;
			}
		}
		return exposed;
	}

	/**
	 * Get parts that are not Gone
	 * 
	 * @return
	 */
	public Stream<BodyPart> getIntactParts() {
		return this.getParts().filter((a) -> !a.isGone());
	}

	@Override
	public Stream<BodyPart> getExposedParts() {
		return this.getIntactParts().filter((a) -> this.isExposed(a));

	}

	@Override
	public <A extends ISensableTrait> A getGeneralProperty(SenseProperty<A> property, boolean ignoreType) {
		return null;
	}

	@Override
	public Stream<SenseProperty<?>> getGeneralSensableProperties() {
		return Collections.<SenseProperty<?>>emptySet().stream();
	}

	protected AbstractBody addBodyPartTypes(Iterable<IBodyPartType> parts) {
		Map<String, IBodyPartType> types = new TreeMap<>();
		for (IBodyPartType part : parts) {
			types.put(part.getName(), part);
		}
		if (partTypes.isEmpty())
			partTypes = ImmutableMap.copyOf(types);
		else {
			Map<String, IBodyPartType> mapa = new TreeMap<>(partTypes);
			mapa.putAll(types);
			partTypes = ImmutableMap.<String, IBodyPartType>builder().putAll(mapa).build();
		}
		return this;
	}

	protected AbstractBody addBodyPartTypes(IBodyPartType... types) {
		return this.addBodyPartTypes(Set.of(types));
	}

	/**
	 * Run these before adding body parts, because the body needs to recognize
	 * tissue layers before it recognizes parts
	 * 
	 * @param layers
	 * @return
	 */
	protected AbstractBody addTissueLayers(Iterable<ITissueLayerType> layers) {
		Map<String, ITissueLayerType> types = new TreeMap<>();
		Map<String, ITissueLayerType> bloodTypes = new TreeMap<>();
		for (ITissueLayerType layer : layers) {
			types.put(layer.getName(), layer);
			if (layer.isLifeEssence())
				bloodTypes.put(layer.getName(), layer);
		}
		if (tissueTypes.isEmpty())
			tissueTypes = ImmutableMap.copyOf(types);
		else {
			Map<String, ITissueLayerType> mapa = new TreeMap<>(tissueTypes);
			mapa.putAll(types);
			tissueTypes = ImmutableMap.<String, ITissueLayerType>builder().putAll(mapa).build();
		}
		if (!bloodTypes.isEmpty()) {
			if (this.bloodTypes.isEmpty()) {
				this.bloodTypes = ImmutableMap.copyOf(bloodTypes);
			} else {
				Map<String, ITissueLayerType> mapa = new TreeMap<>(this.bloodTypes);
				mapa.putAll(bloodTypes);
				this.bloodTypes = ImmutableMap.<String, ITissueLayerType>builder().putAll(mapa).build();
			}
		}
		return this;
	}

	/**
	 * Run these before adding body parts, because the body needs to recognize
	 * tissue layers before it recognizes parts
	 * 
	 * @param layers
	 * @return
	 */
	protected AbstractBody addTissueLayers(ITissueLayerType... layers) {
		return addTissueLayers(Set.of(layers));
	}

	/**
	 * Gets the types of life essence in this entity -- what bleeds from tissue
	 * which hasLifeEssence, and if enough of this is gone, the entity dies
	 */
	public Map<String, ITissueLayerType> getLifeEssenceTypes() {
		return bloodTypes;
	}

	/**
	 * Amount of blood/life substance in this creature
	 * 
	 * @return
	 */
	public float getLifePercent() {
		return lifePercent;
	}

	@Override
	public Collection<Actor> getHeld() {
		return this.actorsHeld.keySet();
	}

	@Override
	public Actor getHeld(IComponentPart byPart) {
		return this.actorsHeld.entries().stream().filter((a) -> a.getValue().equals(byPart)).map((a) -> a.getKey())
				.findFirst().orElse(null);
	}

	@Override
	public Collection<BodyPart> getHoldingParts(Actor a) {
		return this.actorsHeld.get(a);
	}

	@Override
	public boolean isHolding(Actor a) {
		return this.actorsHeld.containsKey(a);
	}

	@Override
	public boolean pickUp(IComponentPart withPart, Actor actor) {

		return this.actorsHeld.put(actor, (BodyPart) withPart);
	}

	@Override
	public boolean putDown(Actor actor) {
		return !this.actorsHeld.get(actor).isEmpty();
	}

	@Override
	public Actor putDown(IComponentPart partHolding) {
		Iterator<Entry<Actor, BodyPart>> iterator = this.actorsHeld.entries().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Actor, BodyPart> ent = iterator.next();
			if (ent.getValue().equals(partHolding)) {
				iterator.remove();
				return ent.getKey();
			}
		}
		return null;
	}

	@Override
	public Collection<BodyPart> getPartsWithAbility(IPartAbility ability) {
		return partsByAbility.get(ability);
	}

	@Override
	public Map<String, IBodyPartType> getPartTypes() {
		return partTypes;
	}

	@Override
	public Stream<BodyPart> getParts() {
		return this.bodyParts.values().stream();
	}

	@Override
	public boolean hasSinglePart() {
		return this.bodyParts.size() == 1;
	}

	@Override
	public void updatePart(IComponentPart part) {
		if (!this.bodyParts.containsValue(part)) {
			throw new IllegalArgumentException();
		}
		BodyPart bpart = (BodyPart) part;
		bpart.getSpirits().forEach((s) -> s.onPartUpdate(bpart));
		bpart.checkIfUsual();
		boolean gone = bpart.isGone();
		if (gone && !bpart.type.isHole()) {
			intactParts--;
		}
	}

	@Override
	public boolean completelyDestroyed() {
		return this.intactParts <= 0;
	}

	@Override
	public IComponentPart mainComponent() {
		if (!hasSinglePart())
			throw new UnsupportedOperationException();
		return this.onlyPart;
	}

	@Override
	public boolean containsSpirit(ISpiritObject spir, IComponentPart part) {
		if (part instanceof BodyPart bpart) {
			return bpart.containsSpirit(spir);
		}
		throw new IllegalArgumentException();
	}

	@Override
	public boolean containsSpirit(ISpiritObject spirit) {

		return this.allSpirits.containsEntry(spirit.getSpiritType(), spirit);
	}

	@Override
	public Collection<? extends ISpiritObject> getContainedSpirits(IComponentPart part) {
		if (part instanceof BodyPart bpart) {
			return bpart.getSpirits();
		}
		throw new IllegalArgumentException();
	}

	@Override
	public Collection<? extends ISpiritObject> getContainedSpirits(SpiritType type) {
		return this.allSpirits.get(type);
	}

	@Override
	public void removeSpirit(ISpiritObject spirit) {
		if (!spirit.isTetheredToWhole()) {
			for (IComponentPart pa : spirit.getTethers()) {
				if (pa instanceof BodyPart part) {
					part.removeSpirit(spirit);
				} else {
					throw new IllegalArgumentException();
				}
			}
		}
		this.allSpirits.remove(spirit.getSpiritType(), spirit);
	}

	@Override
	public void tetherSpirit(ISpiritObject spirit, Collection<IComponentPart> tethers) {
		if (tethers != null) {
			for (IComponentPart pa : tethers) {
				if (pa instanceof BodyPart part) {
					part.addSpirit(spirit);
				} else {
					throw new IllegalArgumentException();
				}
			}
		}
		this.allSpirits.put(spirit.getSpiritType(), spirit);
	}

	@Override
	public Actor getOwner() {
		return this.owner;
	}

	@Override
	public String report() {
		StringBuilder builder = new StringBuilder();
		builder.append("Body " + (this.completelyDestroyed() ? "#" : "") + " of " + this.species + "(" + this.owner
				+ "):\n\t parts:"
				+ (bodyParts == null ? "{}"
						: this.bodyParts.values().stream().map((a) -> a.report()).collect(Collectors.toSet()))
				+ "\n\t intact(" + this.intactParts + "):"
				+ (bodyParts == null ? "{}" : this.getIntactParts().collect(Collectors.toList()).toString())
				+ "\n\t exposed:"
				+ (bodyParts == null ? "{}" : this.getExposedParts().collect(Collectors.toList()).toString())
				+ "\n\t tissuetypes:" + this.tissueTypes);

		return builder.toString();
	}

	@Override
	public String toString() {
		return "body_" + this.species + (this.completelyDestroyed() ? "#" : "") + "(" + this.owner + ")";
	}

}
