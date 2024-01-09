package biology.anatomy;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Table;

import actor.Actor;
import actor.IComponentPart;
import actor.IMultipart;
import actor.IPartAbility;
import biology.systems.types.ISensor;
import mind.concepts.type.SenseProperty;

/**
 * Add colors/appearances to body parts; add traits
 * 
 * @author borah
 *
 */
public class Body implements IMultipart {

	private Table<String, UUID, BodyPart> bodyParts;
	private Map<UUID, BodyPart> noParentParts;
	private Map<UUID, BodyPart> outermostParts;
	private Map<String, ITissueLayerType> tissueTypes = Map.of();
	private Map<String, IBodyPartType> partTypes = Map.of();
	private Map<String, ITissueLayerType> bloodTypes = Map.of();
	private Multimap<IPartAbility, BodyPart> partsByAbility = MultimapBuilder.hashKeys().treeSetValues().build();
	private UUID rootID;
	private BodyPart rootPart;
	private Actor owner;
	private boolean built;
	private float lifePercent = 1f;
	private ISpeciesTemplate species;
	private Map<SenseProperty<?>, Object> sensables;
	private Multimap<ISensor, SenseProperty<?>> potentialSenses;
	private boolean invisible;

	public Body(Actor owner) {
		this.owner = owner;
	}

	public Body(Actor owner, ISpeciesTemplate template) {
		this.owner = owner;
		this.species = template;
	}

	/**
	 * Set the amount of blood/life essence in this creature
	 * 
	 * @param lifePercent
	 */
	public void setLifePercent(float lifePercent) {
		this.lifePercent = lifePercent;
	}

	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}

	/**
	 * lose an amount of blood of the given percentage
	 */
	public void bleed(float byPercent) {
		this.lifePercent *= (1 - byPercent);
	}

	public ISpeciesTemplate getSpecies() {
		return species;
	}

	@Override
	public boolean isBuilt() {
		return built;
	}

	public void buildBody() {
		if (species != null) {
			System.out.println("Creating body from template of species:" + species.name());
			this.addTissueLayers(species.tissueTypes().values());
			this.addBodyPartTypes(species.partTypes().values());
		}
		System.out.print("Starting to build body for " + owner + ".");
		initializeBody();
		System.out.print(".");
		initializeBodyConnections();
		System.out.println(". Body built for " + owner);
		built = true;
	}

	@Override
	public Map<UUID, BodyPart> getPartsWithoutParent() {
		return noParentParts == null ? Map.of() : noParentParts;
	}

	@Override
	public Map<UUID, BodyPart> getOutermostParts() {
		return outermostParts == null ? Map.of() : outermostParts;
	}

	private void initializeBody() {
		for (Map.Entry<String, IBodyPartType> entry : partTypes.entrySet()) {
			if (bodyParts == null)
				bodyParts = HashBasedTable.<String, UUID, BodyPart>create();
			String key = entry.getKey();
			IBodyPartType partType = entry.getValue();
			int count = partType.count();
			String parentPartStr = partType.getParent();
			if (parentPartStr != null) {
				while (parentPartStr != null) {
					IBodyPartType parentPart = partTypes.get(parentPartStr);
					if (parentPart == null)
						throw new IllegalStateException(
								"no part in body called " + parentPartStr + "; referenced as parent by " + key);
					count *= parentPart.count();
					parentPartStr = parentPart.getParent();
				}
			}
			for (int i = 0; i < count; i++) {
				BodyPart part = new BodyPart(partType, partTypes, tissueTypes);
				bodyParts.put(partType.getName(), part.id, part);
				for (IPartAbility ab : partType.abilities()) {
					this.partsByAbility.put(ab, part);
				}
				for (SenseProperty<?> prop : partType.getSensableProperties()) {
					if (prop.isUnique()) {
						part.setSensableProperty(prop, this.owner.getUUID()); // set this property to uniquely match the
																				// owner's
						// id
					}
				}
				if (partType.isRoot()) {
					if (rootID != null)
						throw new RuntimeException("Existing root " + rootPart.type.getName() + " " + rootID
								+ "; trying to insert new root " + part.type.getName() + " " + part.id);

				}
				if (partType.getParent() == null) {
					if (noParentParts == null)
						noParentParts = new TreeMap<>();
					noParentParts.put(part.id, part);
				}
			}
		}
	}

	private void initializeBodyConnections() {
		if (bodyParts == null)
			throw new RuntimeException("first step incomplete");
		for (String type : bodyParts.rowKeySet()) {
			Collection<BodyPart> parts = bodyParts.row(type).values();
			IBodyPartType partType = partTypes.get(type);
			String parentPart = partType.getParent();
			String surrounding = partType.getSurroundingPart();
			if (parentPart != null) {
				Collection<BodyPart> parentParts = bodyParts.row(parentPart).values();
				Iterator<BodyPart> parentIter = parentParts.iterator();
				BodyPart parent = parentIter.next();
				int count = 0;
				for (BodyPart part : parts) {
					count++;
					if (count <= partType.count()) {
						parent.addChild(part);
						part.setParent(parent);
					} else {
						count = 0;
						if (parentIter.hasNext()) {
							parent = parentIter.next();
						} else {
							break;
						}
					}
				}
			}
			if (surrounding != null) {/*
										 * System.out.println("surrounded " + type + " by " + surrounding);
										 */
				Collection<BodyPart> surroundParts = bodyParts.row(surrounding).values();
				IBodyPartType surroundingType = partTypes.get(surrounding);
				for (BodyPart part : parts) {
					BodyPart surrounder = null;
					BodyPart currentChecked = part.parent;
					while (currentChecked != null) {
						if (currentChecked.type.equals(surroundingType)) {
							surrounder = currentChecked;
							break;
						} else {
							for (BodyPart child : currentChecked.getSurroundeds().get(surrounding)) {
								surrounder = child;
								break;
							}
						}
						currentChecked = currentChecked.parent;
					}

					if (surrounder == null) {
						surrounder = surroundParts.iterator().next();
					}
					part.setSurrounding(surrounder);
					surrounder.addSurrounded(part);
				}
			}
		}

		for (String type : bodyParts.rowKeySet()) {
			Collection<BodyPart> parts = bodyParts.row(type).values();
			IBodyPartType partType = partTypes.get(type);
			String parentPart = partType.getParent();
			String surrounding = partType.getSurroundingPart();
			if (surrounding == null) {
				if (parentPart != null) {
					for (BodyPart part : parts) {
						BodyPart parent = part.parent;
						boolean surrounded = false;
						while (parent != null) {
							if (parent.type.getSurroundingPart() != null) {
								surrounded = true;/*
													 * System.out.println("surrounded " + type + " at " +
													 * parent.type.getName() + " by " +
													 * parent.type.getSurroundingPart());
													 */
								break;
							}
							parent = parent.parent;
						}
						if (!surrounded) {
							(outermostParts == null ? outermostParts = new TreeMap<>() : outermostParts).put(part.id,
									part);/*
											 * System.out.println("unsurrounded " + type);
											 */
						}

					}
				} else {
					for (BodyPart part : parts) {
						(outermostParts == null ? outermostParts = new TreeMap<>() : outermostParts).put(part.id, part);
						/* System.out.println("unsurrounded " + type); */

					}
				}
			}

		}

	}

	protected Body addBodyPartTypes(Iterable<IBodyPartType> parts) {
		Map<String, IBodyPartType> types = new TreeMap<>();
		for (IBodyPartType part : parts) {
			types.put(part.getName(), part);
			if (this.potentialSenses == null) {
				this.potentialSenses = MultimapBuilder.hashKeys().treeSetValues().build();
			}
			for (SenseProperty<?> prop : part.getSensableProperties()) {
				for (ISensor sensor : prop.getSensors()) {
					potentialSenses.put(sensor, prop);
				}
			}
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

	protected Body addBodyPartTypes(IBodyPartType... types) {
		return this.addBodyPartTypes(Set.of(types));
	}

	/**
	 * Run these before adding body parts, because the body needs to recognize
	 * tissue layers before it recognizes parts
	 * 
	 * @param layers
	 * @return
	 */
	protected Body addTissueLayers(Iterable<ITissueLayerType> layers) {
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
	protected Body addTissueLayers(ITissueLayerType... layers) {
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
	public Collection<BodyPart> getPartsWithAbility(IPartAbility ability) {
		return partsByAbility.get(ability);
	}

	@Override
	public <T> T getTrait(SenseProperty<T> prop, IComponentPart part, boolean ignoreType) {
		return part.getProperty(prop, ignoreType);
	}

	@Override
	public <T> T getTrait(SenseProperty<T> property) {
		return sensables == null ? null : (T) sensables.get(property);
	}

	@Override
	public Collection<SenseProperty<?>> getSensableTraits(ISensor sensor) {
		return potentialSenses == null ? Set.of() : potentialSenses.get(sensor);
	}

	@Override
	public Collection<SenseProperty<?>> getSensableTraits(ISensor sensor, IComponentPart part) {
		return part.getSensableProperties(sensor);
	}

	@Override
	public Map<String, IBodyPartType> getPartTypes() {
		return partTypes;
	}

	@Override
	public Collection<BodyPart> getParts() {
		return this.bodyParts.values();
	}

	@Override
	public boolean isInvisible() {
		return invisible;
	}

	@Override
	public Actor getOwner() {
		return this.owner;
	}

	@Override
	public String report() {
		StringBuilder builder = new StringBuilder();
		builder.append("Body of " + this.species + "(" + this.owner + "):\n\t parts:"
				+ (bodyParts == null ? null : this.bodyParts.columnMap().values()) + "\n\t tissuetypes:"
				+ this.tissueTypes + "\n\t sensableProperties:" + this.sensables + "\n");
		if (bodyParts != null && this.potentialSenses != null) {
			builder.append("\tOutermost bodyparts:" + this.outermostParts.values() + "\n");
			builder.append("\tSensable properties for bodypartTypes:\n\t\t{");
			for (IBodyPartType type : this.partTypes.values()) {
				builder.append(type.getName() + "{");
				for (ISensor sense : this.potentialSenses.keySet()) {
					for (SenseProperty<?> prop : type.getSensableProperties(sense)) {
						builder.append(prop + ":" + type.getTrait(prop) + ",");
					}
				}
				builder.append("},");
			}
			builder.append("\n\tUnique sensable properties for just bodyParts:\n\t\t{");
			for (BodyPart part : this.bodyParts.values()) {
				boolean appd = false;
				for (ISensor sense : this.potentialSenses.keySet()) {
					for (SenseProperty<?> prop : part.getSensableProperties(sense)) {
						if (!appd) {
							builder.append(part.type.getName() + "{");
							appd = true;
						}
						builder.append(prop + ":" + part.getProperty(prop, true) + ",");
					}
				}
				if (appd)
					builder.append("},");
			}
			builder.append("}");
		}
		return builder.toString();
	}

}
