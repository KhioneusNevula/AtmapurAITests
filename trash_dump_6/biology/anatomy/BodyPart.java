package biology.anatomy;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Table;

import actor.construction.physical.IComponentPart;
import actor.construction.physical.IPartAbility;
import actor.construction.properties.IAbilityStat;
import actor.construction.properties.ISensableTrait;
import actor.construction.properties.SenseProperty;
import biology.sensing.ISense;
import metaphysical.ISpiritObject;
import metaphysical.ISpiritObject.SpiritType;

public class BodyPart implements IComponentPart {
	UUID id;
	IBodyPartType type;
	private Map<ITissueLayerType, Tissue> tissue;
	BodyPart parent;
	private Multimap<String, BodyPart> subParts;
	private BodyPart surrounding;
	private Multimap<String, BodyPart> surroundeds;
	private Map<SenseProperty<?>, Object> sensables;
	private Table<IPartAbility, IAbilityStat<?>, Object> senseStats;
	private Set<ISense> senses;
	private boolean usual = true;
	private Float nutrition;
	private int nutritionTypes = 1;
	private boolean gone;
	private Tissue mainTissue;
	private Multimap<SpiritType, ISpiritObject> tetheredSpirits = MultimapBuilder.enumKeys(SpiritType.class)
			.hashSetValues().build();

	public BodyPart(IBodyPartType type, Map<String, ITissueLayerType> tissueTypes) {
		this.type = type;
		this.id = UUID.randomUUID();
		this.senses = new HashSet<>(type.getDefaultSenses());
		this.senseStats = HashBasedTable.create();
		if (!type.tissueTags().isEmpty()) {
			tissue = new TreeMap<>(Comparator.reverseOrder());
			for (String str : type.tissueTags()) {
				ITissueLayerType layer = tissueTypes.get(str);
				if (layer == null)
					throw new IllegalStateException("tissue key " + str
							+ " does not correspond to any tissue type; error while building body part "
							+ type.getName());
				try {
					Tissue tiss = new Tissue(layer, tissueTypes);
					tissue.put(layer, tiss);
					if (type.tissueTags().size() == 1) {
						mainTissue = tiss;
					}
					if (nutritionTypes % layer.getNutritionTypes() != 0) {
						nutritionTypes *= layer.getNutritionTypes();
					}
				} catch (Exception e) {
					throw new RuntimeException("exception while building " + type.getName() + ": " + e.getMessage());
				}
			}
		}

	}

	/**
	 * Copy this body part without copying entities connected to it (i.e. other body
	 * parts, tethered spirits)
	 * 
	 * @param atPart
	 * @param sameID whether to give this copy the same id
	 */
	public BodyPart(BodyPart atPart, boolean sameID) {
		this.id = sameID ? atPart.id : UUID.randomUUID();
		this.type = atPart.type;
		this.gone = atPart.gone;
		this.nutrition = atPart.nutrition;
		this.nutritionTypes = atPart.nutritionTypes;
		this.parent = null;
		this.sensables = new HashMap<>(atPart.sensables != null ? atPart.sensables : Collections.emptyMap());
		this.senses = new HashSet<>(atPart.senses != null ? atPart.senses : Collections.emptySet());
		this.senseStats = HashBasedTable.create(atPart.senseStats != null ? atPart.senseStats : ImmutableTable.of());
		this.tissue = new HashMap<>();
		for (Tissue tiss : atPart.tissue.values()) {
			this.tissue.put(tiss.getType(), new Tissue(tiss));
		}
	}

	@Override
	public Collection<ISense> getSenses() {
		return this.senses;
	}

	@Override
	public <T> T getAbilityStat(IPartAbility forab, IAbilityStat<T> type) {
		T th = (T) this.senseStats.get(forab, type);
		if (th == null) {
			return this.type.getDefaultAbilityStat(forab, type);
		}
		return th;
	}

	@Override
	public Collection<IAbilityStat<?>> getAbilityStatTypes(IPartAbility forab) {
		return this.senseStats.row(forab).keySet();
	}

	public IBodyPartType getType() {
		return type;
	}

	public UUID getId() {
		return id;
	}

	@Override
	public Map<ITissueLayerType, Tissue> getMaterials() {
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

	@Override
	public Multimap<String, BodyPart> getSurroundeds() {
		return surroundeds == null ? ImmutableSetMultimap.of() : surroundeds;
	}

	@Override
	public BodyPart getSurrounding() {
		return surrounding;
	}

	@Override
	public float getNutrition() {
		return this.nutrition == null ? this.type.defaultNutritionContent() : this.nutrition;
	}

	public BodyPart setNutrition(float nutrition) {
		this.nutrition = nutrition;
		return this;
	}

	@Override
	public int nutritionTypes() {
		return this.nutritionTypes;
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
		if (this.sensables == null)
			sensables = new TreeMap<>();
		sensables.put(prop, value);
		return this;
	}

	@Override
	public <T extends ISensableTrait> T getProperty(SenseProperty<T> property, boolean ignoreType) {
		T obj = sensables == null ? null : (T) sensables.get(property);
		if (obj == null && !ignoreType)
			return this.type.getTrait(property);
		return obj;
	}

	@Override
	public <T extends ISensableTrait> void changeProperty(SenseProperty<T> property, T value) {
		this.setSensableProperty(property, value);
	}

	@Override
	public Collection<SenseProperty<?>> getSensableProperties() {
		return sensables == null ? Set.of() : sensables.keySet();
	}

	public boolean containsSpirit(ISpiritObject spir) {
		return this.tetheredSpirits.containsEntry(spir.getSpiritType(), spir);
	}

	public Collection<ISpiritObject> getSpirits() {
		return this.tetheredSpirits.values();
	}

	public Collection<ISpiritObject> getSpirits(SpiritType spir) {
		return this.tetheredSpirits.get(spir);
	}

	public boolean removeSpirit(ISpiritObject spiri) {
		return this.tetheredSpirits.remove(spiri.getSpiritType(), spiri);
	}

	public void addSpirit(ISpiritObject spir) {
		this.tetheredSpirits.put(spir.getSpiritType(), spir);
	}

	@Override
	public boolean isUnusual() {
		return usual;
	}

	@Override
	public String report() {
		return "<" + this.type.getName() + (gone ? "#" : (usual ? "" : "*"))
				+ (this.parent == null ? "" : "{p:" + this.parent.type.getName() + "}") + ","
				+ this.getMaterials().values()
				+ (this.sensables != null && !this.sensables.isEmpty() ? ("," + this.sensables) : "")
				+ (!this.tetheredSpirits.isEmpty() ? ("," + this.tetheredSpirits.values()) : "") + ">";
	}

	@Override
	public String toString() {
		return this.type.getName() + (gone ? "#" : (usual ? "" : "*")) + "{"
				+ (this.parent == null ? "" : this.parent.type.getName()) + "}";

	}

	@Override
	public boolean checkIfUsual() {
		usual = true;
		gone = true;
		if (tissue != null) {
			for (Tissue tiss : tissue.values()) {
				if (!tiss.isUsual()) {
					usual = false;
				}
				if (!tiss.getState().gone()) {
					gone = false;
				}
			}
		}
		return usual;
	}

	@Override
	public boolean isGone() {
		return this.gone;
	}

	@Override
	public Tissue getMainMaterial() {
		return mainTissue;
	}

	@Override
	public boolean hasOneMaterial() {
		return mainTissue != null;
	}

	protected BodyPart removeSurrounded(BodyPart surro) {
		if (this.surroundeds == null)
			return this;
		this.surroundeds.remove(surro.type.getName(), surro);
		return this;
	}

	public void removeChild(BodyPart atPart) {
		this.getChildParts().remove(atPart.type.getName(), atPart);
	}

}