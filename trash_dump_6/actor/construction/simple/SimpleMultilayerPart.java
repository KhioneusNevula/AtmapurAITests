package actor.construction.simple;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Table;

import actor.construction.NutritionType;
import actor.construction.physical.IComponentPart;
import actor.construction.physical.IComponentType;
import actor.construction.physical.IMaterialLayer;
import actor.construction.physical.IMaterialLayerType;
import actor.construction.physical.IPartAbility;
import actor.construction.properties.IAbilityStat;
import actor.construction.properties.ISensableTrait;
import actor.construction.properties.SenseProperty;
import biology.sensing.ISense;

/**
 * A simple part consisting of wrapped layers
 * 
 * @author borah
 *
 */
public class SimpleMultilayerPart implements IComponentPart {

	UUID id;
	IComponentType type;
	private Map<IMaterialLayerType, IMaterialLayer> materialmap;
	private IMaterialLayer material;

	IComponentPart parent;
	private Multimap<String, IComponentPart> subParts;
	private IComponentPart surrounding;
	private Multimap<String, IComponentPart> surroundeds;
	private Map<SenseProperty<?>, Object> sensables;
	private boolean usual = true;
	private Float nutrition;
	private int nutritionTypes = 1;
	private boolean gone;
	private Table<IPartAbility, IAbilityStat<?>, Object> senseStats;
	private Set<ISense> senses;

	public SimpleMultilayerPart(IComponentType type, UUID id, Iterable<IMaterialLayer> materials) {
		this.type = type;
		this.id = id;

		Map<SenseProperty<?>, Object> senseMap = new HashMap<>();

		boolean isSingle = true;
		this.materialmap = new TreeMap<>(Comparator.reverseOrder());
		for (IMaterialLayer material : materials) {
			if (this.material != null && isSingle) {
				this.material = null;
				isSingle = false;
			} else if (this.material == null && isSingle) {
				this.material = material;
			}
			materialmap.put(material.getType(), material);
			if (nutritionTypes % material.getType().getNutritionTypes() != 0) {
				nutritionTypes *= material.getType().getNutritionTypes();
			}
			for (SenseProperty<?> sep : material.getType().getSensableProperties()) {
				senseMap.put(sep, material.getType().getTrait(sep));
			}
		}

		sensables = senseMap;
		this.senses = new HashSet<>(type.getDefaultSenses());
		this.senseStats = HashBasedTable.create();
		for (IPartAbility abil : type.abilities()) {
			for (IAbilityStat<?> stat : type.getDefaultAbilityStats(abil)) {
				senseStats.put(abil, stat, type.getDefaultAbilityStat(abil, stat));
			}
		}

	}

	public SimpleMultilayerPart addProperties(Map.Entry<SenseProperty<?>, Object>... props) {
		for (Map.Entry<SenseProperty<?>, Object> prop : props) {
			sensables.put(prop.getKey(), prop.getValue());
		}
		return this;
	}

	@Override
	public <T extends ISensableTrait> void changeProperty(SenseProperty<T> property, T value) {
		sensables.put(property, value);
	}

	public <T> SimpleMultilayerPart changeAbilityStat(IPartAbility abil, IAbilityStat<T> stat, T val) {
		this.senseStats.put(abil, stat, val);
		return this;
	}

	@Override
	public Collection<ISense> getSenses() {
		return this.senses;
	}

	@Override
	public <T> T getAbilityStat(IPartAbility abil, IAbilityStat<T> type) {
		T o = (T) this.senseStats.get(abil, type);
		if (o == null)
			return this.type.getDefaultAbilityStat(abil, type);
		return o;
	}

	@Override
	public Collection<IAbilityStat<?>> getAbilityStatTypes(IPartAbility abil) {
		return this.senseStats.row(abil).keySet();
	}

	public IComponentType getType() {
		return type;
	}

	public UUID getId() {
		return id;
	}

	@Override
	public Map<IMaterialLayerType, IMaterialLayer> getMaterials() {
		return materialmap;
	}

	@Override
	public IComponentPart getParent() {
		return parent;
	}

	@Override
	public Multimap<String, IComponentPart> getChildParts() {
		return subParts == null ? ImmutableSetMultimap.of() : subParts;
	}

	@Override
	public Multimap<String, IComponentPart> getSurroundeds() {
		return surroundeds == null ? ImmutableSetMultimap.of() : surroundeds;
	}

	@Override
	public IComponentPart getSurrounding() {
		return surrounding;
	}

	/**
	 * Set parent to the body part with this Name and ID
	 * 
	 * @param parent
	 */
	public SimpleMultilayerPart setParent(IComponentPart parent) {
		this.parent = parent;
		return this;
	}

	public SimpleMultilayerPart setNutrition(float nutrition) {
		this.nutrition = nutrition;
		return this;
	}

	@Override
	public float getNutrition() {
		return this.nutrition == null ? this.type.defaultNutritionContent() : nutrition;
	}

	@Override
	public int nutritionTypes() {
		return this.nutritionTypes;
	}

	public void setSurrounding(IComponentPart surrounding) {
		this.surrounding = surrounding;
	}

	public SimpleMultilayerPart addSurrounded(IComponentPart surrounded) {
		if (surroundeds == null)
			surroundeds = MultimapBuilder.treeKeys().hashSetValues().build();
		this.surroundeds.put(surrounded.getType().getName(), surrounded);
		return this;
	}

	public SimpleMultilayerPart addChild(IComponentPart child) {
		if (subParts == null)
			subParts = MultimapBuilder.treeKeys().hashSetValues().build();
		this.subParts.put(child.getType().getName(), child);
		return this;
	}

	@Override
	public <T extends ISensableTrait> T getProperty(SenseProperty<T> property, boolean ignoreType) {
		T obj = sensables == null ? null : (T) sensables.get(property);
		if (obj == null && !ignoreType)
			return this.getType().getTrait(property);
		return obj;
	}

	@Override
	public Collection<SenseProperty<?>> getSensableProperties() {
		return sensables == null ? Set.of() : sensables.keySet();
	}

	@Override
	public boolean isUnusual() {
		return !usual;
	}

	@Override
	public IMaterialLayer getMainMaterial() {
		return material;
	}

	@Override
	public boolean hasOneMaterial() {
		return material != null;
	}

	@Override
	public boolean isGone() {
		return gone;
	}

	@Override
	public String toString() {
		return this.type.getName() + (this.isGone() ? "#" : (usual ? "" : "*")) + "{"
				+ (this.parent == null ? "" : this.parent.getType().getName()) + "}";

	}

	@Override
	public String report() {
		return "{" + this.type.getName() + (this.isGone() ? "#" : (usual ? "" : "*"))
				+ (this.parent == null ? "" : "{p:" + this.parent.getType().getName() + "}") + ",{"
				+ this.materialmap.values() + "}" + ",foodval:" + this.nutrition + "+foodtypes:"
				+ NutritionType.decomposeCombinedValue(this.nutritionTypes(), true) + "}," + this.sensables.values()
				+ "}";
	}

	@Override
	public boolean checkIfUsual() {
		usual = true;
		gone = true;
		for (IMaterialLayer mat : this.materialmap.values()) {
			if (!mat.isUsual()) {
				usual = false;
			}
			if (!mat.getState().gone()) {
				gone = false;
			}
		}
		return usual;
	}

}
