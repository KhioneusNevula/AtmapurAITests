package biology.anatomy;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import actor.IComponentPart;
import biology.systems.types.ISensor;
import mind.concepts.type.SenseProperty;

public class BodyPart implements IComponentPart {
	UUID id;
	IBodyPartType type;
	private Map<ITissueLayerType, Tissue> tissue;
	BodyPart parent;
	private Multimap<String, BodyPart> subParts;
	private BodyPart surrounding;
	private Multimap<String, BodyPart> surroundeds;
	private Map<SenseProperty<?>, Object> sensables;
	private Multimap<ISensor, SenseProperty<?>> potentialSenses;
	private boolean usual = true;

	public BodyPart(IBodyPartType type, Map<String, IBodyPartType> partTypes,
			Map<String, ITissueLayerType> tissueTypes) {
		this.type = type;
		this.id = UUID.randomUUID();
		if (!type.tissueTags().isEmpty()) {
			tissue = new TreeMap<>(Comparator.reverseOrder());
			for (String str : type.tissueTags()) {
				ITissueLayerType layer = tissueTypes.get(str);
				if (layer == null)
					throw new IllegalStateException("tissue key " + str
							+ " does not correspond to any tissue type; error while building body part "
							+ type.getName());
				try {
					tissue.put(layer, new Tissue(layer, tissueTypes));
				} catch (Exception e) {
					throw new RuntimeException("exception while building " + type.getName() + ": " + e.getMessage());
				}
			}
		}

	}

	public IBodyPartType getType() {
		return type;
	}

	public UUID getId() {
		return id;
	}

	public Map<ITissueLayerType, Tissue> getTissue() {
		return tissue == null ? Map.of() : tissue;
	}

	@Override
	public BodyPart getParent() {
		return parent;
	}

	@Override
	public Multimap<String, BodyPart> getChildParts() {
		return subParts == null ? ImmutableSetMultimap.of() : subParts;
	}

	public Multimap<String, BodyPart> getSurroundeds() {
		return surroundeds == null ? ImmutableSetMultimap.of() : surroundeds;
	}

	public BodyPart getSurrounding() {
		return surrounding;
	}

	/**
	 * Set parent to the body part with this Name and ID
	 * 
	 * @param parent
	 */
	protected BodyPart setParent(BodyPart parent) {
		this.parent = parent;
		return this;
	}

	protected void setSurrounding(BodyPart surrounding) {
		this.surrounding = surrounding;
	}

	protected BodyPart addSurrounded(BodyPart surrounded) {
		if (surroundeds == null)
			surroundeds = MultimapBuilder.treeKeys().hashSetValues().build();
		this.surroundeds.put(surrounded.type.getName(), surrounded);
		return this;
	}

	protected BodyPart addChild(BodyPart child) {
		if (subParts == null)
			subParts = MultimapBuilder.treeKeys().hashSetValues().build();
		this.subParts.put(child.type.getName(), child);
		return this;
	}

	protected BodyPart setSensableProperty(SenseProperty<?> prop, Object value) {
		if (this.potentialSenses == null)
			potentialSenses = MultimapBuilder.hashKeys().treeSetValues().build();
		if (this.sensables == null)
			sensables = new TreeMap<>();
		for (ISensor sense : prop.getSensors())
			potentialSenses.put(sense, prop);
		sensables.put(prop, value);
		return this;
	}

	@Override
	public <T> T getProperty(SenseProperty<T> property, boolean ignoreType) {
		T obj = sensables == null ? null : (T) sensables.get(property);
		if (obj == null && !ignoreType)
			return this.type.getTrait(property);
		return obj;
	}

	@Override
	public Collection<SenseProperty<?>> getSensableProperties(ISensor sensor) {
		return potentialSenses == null ? Set.of() : potentialSenses.get(sensor);
	}

	@Override
	public boolean isUnusual() {
		return usual;
	}

	@Override
	public String toString() {
		return this.type.getName() + "{" + (this.parent == null ? "" : this.parent.type.getName()) + "}";

	}

	@Override
	public boolean checkIfUsual() {
		usual = true;
		if (tissue != null) {
			for (Tissue tiss : tissue.values()) {
				if (!tiss.isUsual()) {
					usual = false;
					break;
				}
			}
		}
		return usual;
	}

}