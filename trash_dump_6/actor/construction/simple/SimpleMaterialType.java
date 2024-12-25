package actor.construction.simple;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import actor.construction.INutritionType;
import actor.construction.NutritionType;
import actor.construction.physical.IMaterialLayerType;
import actor.construction.properties.ISensableTrait;
import actor.construction.properties.SenseProperty;
import actor.construction.properties.SenseProperty.BasicColor;
import actor.construction.properties.SenseProperty.BasicSmell;
import actor.construction.properties.SenseProperty.BasicTexture;
import sim.physicality.PhysicalState;

public class SimpleMaterialType implements IMaterialLayerType, Cloneable {
	/** TODO add materials and also add other physical properties */
	/** food material */
	public static final SimpleMaterialType VEGGIE_MATERIAL = layer("veggie", 1)
			.setSensableProperties(
					ImmutableMap.of(SenseProperty.SMELL, BasicSmell.FOODY, SenseProperty.TEXTURE, BasicTexture.SQUISHY))
			.setNutritionTypes(NutritionType.VEGETABLE);

	public static final SimpleMaterialType MEATY_MATERIAL = layer("meaty", 0)
			.setSensableProperties(
					ImmutableMap.of(SenseProperty.SMELL, BasicSmell.FOODY, SenseProperty.TEXTURE, BasicTexture.FIRM))
			.setNutritionTypes(NutritionType.MEAT);

	public static final SimpleMaterialType STONE_MATERIAL = layer("rock", 0)
			.setSensableProperties(ImmutableMap.of(SenseProperty.SMELL, BasicSmell.EARTHY, SenseProperty.TEXTURE,
					BasicTexture.HARD, SenseProperty.COLOR, BasicColor.GRAY));

	private int layer;
	private String name;
	private Set<String> bundleNames = Set.of();
	private Set<String> sublayers = Set.of();
	private PhysicalState initialState = PhysicalState.HARD_SOLID_WHOLE;
	private Map<SenseProperty<?>, ISensableTrait> sensables = Map.of();
	private int nutrition = NutritionType.EATS_UNEATABLE.primeFactor();

	protected SimpleMaterialType(String name, int layer) {
		this.name = name;
		this.layer = layer;
	}

	protected static SimpleMaterialType layer(String name, int layer) {
		return new SimpleMaterialType(name, layer);
	}

	public static MaterialLayerBuilder builder(String name, int layer) {
		return new MaterialLayerBuilder(name, layer);
	}

	public SimpleMaterialType withoutSensableProperties(Collection<SenseProperty<? extends ISensableTrait>> props) {
		Map<SenseProperty<?>, ISensableTrait> sebs = new TreeMap<>(this.sensables);
		for (SenseProperty<?> prop : props) {
			sebs.remove(prop);
		}
		return this.copy().setSensableProperties(sebs);
	}

	public SimpleMaterialType withSensableProperties(
			Map<SenseProperty<? extends ISensableTrait>, ? extends ISensableTrait> map) {
		Map<SenseProperty<?>, ISensableTrait> mapa = new TreeMap<>(sensables);
		mapa.putAll(map);
		return this.copy().setSensableProperties(mapa);
	}

	public SimpleMaterialType inInitialState(PhysicalState state) {
		return this.copy().setInitialState(state);
	}

	public SimpleMaterialType withoutSublayers(String... name) {
		return withoutSublayers(Set.of(name));
	}

	public SimpleMaterialType withoutSublayers(Collection<String> name) {
		Set<String> ss = new TreeSet<>(sublayers);
		ss.removeAll(name);
		return this.copy().setSublayers(ss);
	}

	public SimpleMaterialType withSublayers(String... names) {
		return withSublayers(Set.of(names));
	}

	public SimpleMaterialType withSublayers(Collection<String> names) {
		Set<String> set = new TreeSet<>(sublayers);
		set.addAll(names);
		return this.copy().setSublayers(set);
	}

	public SimpleMaterialType withoutBundleNames(String... name) {
		return withoutBundleNames(Set.of(name));
	}

	public SimpleMaterialType withoutBundleNames(Collection<String> name) {
		Set<String> ss = new TreeSet<>(bundleNames);
		ss.removeAll(name);
		return this.copy().setBundleNames(ss);
	}

	public SimpleMaterialType withBundleNames(String... names) {
		return withBundleNames(Set.of(names));
	}

	public SimpleMaterialType withBundleNames(Collection<String> names) {
		Set<String> set = new TreeSet<>(bundleNames);
		set.addAll(names);
		return this.copy().setBundleNames(set);
	}

	public SimpleMaterialType renamedto(String newname) {
		return this.copy().setName(newname);
	}

	/**
	 * Return a copy of this tissue with the layer it goes above as the given layer
	 * 
	 * @param lowerLayer
	 * @return
	 */
	public SimpleMaterialType atLayer(int lowerLayer) {
		return this.copy().setLayer(lowerLayer);
	}

	/**
	 * Makes this tissue layer not go above any other layer
	 * 
	 * @return
	 */
	public SimpleMaterialType aboveNothing() {
		return this.copy().setLayer(0);
	}

	public SimpleMaterialType withNutritionTypes(int nutritionTypes) {
		return this.copy().setNutritionTypes(nutritionTypes);
	}

	public SimpleMaterialType withNutritionTypes(INutritionType... types) {
		return this.copy().setNutritionTypes(types);
	}

	protected SimpleMaterialType setSublayers(String... names) {
		return setSublayers(Set.of(names));
	}

	protected SimpleMaterialType setSublayers(Set<String> names) {
		this.sublayers = ImmutableSet.copyOf(names);
		return this;
	}

	protected SimpleMaterialType setBundleNames(String... names) {
		return setBundleNames(Set.of(names));
	}

	protected SimpleMaterialType setBundleNames(Set<String> names) {
		this.bundleNames = ImmutableSet.copyOf(names);
		return this;
	}

	protected SimpleMaterialType setName(String name) {
		this.name = name;
		return this;
	}

	protected SimpleMaterialType setLayer(int other) {
		this.layer = other;
		return this;
	}

	protected SimpleMaterialType setInitialState(PhysicalState initialState) {
		this.initialState = initialState;
		return this;
	}

	protected SimpleMaterialType addSensableProperties(Map<SenseProperty<?>, ? extends ISensableTrait> map) {
		Map<SenseProperty<?>, ISensableTrait> mapa = new TreeMap<>(sensables);
		mapa.putAll(map);
		return this.setSensableProperties(mapa);
	}

	protected SimpleMaterialType setSensableProperties(Map<SenseProperty<?>, ? extends ISensableTrait> map) {
		sensables = new TreeMap<>();

		for (Map.Entry<SenseProperty<?>, ? extends ISensableTrait> entry : map.entrySet()) {
			if (!entry.getKey().getType().isAssignableFrom(entry.getValue().getClass())) {
				throw new IllegalArgumentException("Illegal sense property added " + entry.getKey().getName()
						+ " for value " + entry.getValue() + " when building " + this.getName());
			}
			sensables.put(entry.getKey(), entry.getValue());
		}
		sensables = ImmutableMap.copyOf(sensables);
		return this;
	}

	protected SimpleMaterialType setNutritionTypes(int nutritionTypes) {
		this.nutrition = nutritionTypes;
		return this;
	}

	protected SimpleMaterialType setNutritionTypes(INutritionType... types) {
		return setNutritionTypes(INutritionType.combine(types));
	}

	@Override
	public int getLayer() {
		return layer;
	}

	@Override
	public Collection<String> getSublayers() {
		return sublayers;
	}

	@Override
	public Collection<String> getBundleNames() {
		return bundleNames;
	}

	@Override
	public <A extends ISensableTrait> A getProperty(SenseProperty<A> property) {
		return (A) this.sensables.get(property);
	}

	@Override
	public String getName() {
		return name;
	}

	protected SimpleMaterialType copy() {
		try {
			return (SimpleMaterialType) this.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int compareTo(IMaterialLayerType o) {
		return layer - o.getLayer();
	}

	@Override
	public PhysicalState initialState() {
		return initialState;
	}

	@Override
	public int getNutritionTypes() {
		return nutrition;
	}

	@Override
	public Collection<SenseProperty<? extends ISensableTrait>> getSensableProperties() {
		return this.sensables.keySet();
	}

	@Override
	public <T extends ISensableTrait> T getTrait(SenseProperty<T> prop) {
		return (T) sensables.get(prop);
	}

	@Override
	public String toString() {
		return "material_" + this.getName();
	}

	protected <T extends SimpleMaterialType> T cast() {
		return (T) this;
	}

	public static class MaterialLayerBuilder {
		private SimpleMaterialType at;

		private MaterialLayerBuilder(String name, int layer) {
			at = new SimpleMaterialType(name, layer);
		}

		public MaterialLayerBuilder setSublayers(String... names) {
			at.setSublayers(names);
			return this;
		}

		public MaterialLayerBuilder setSublayers(Set<String> names) {
			at.setSublayers(names);
			return this;
		}

		public MaterialLayerBuilder setBundleNames(String... names) {
			at.setBundleNames(names);
			return this;
		}

		public MaterialLayerBuilder setBundleNames(Set<String> names) {
			at.setBundleNames(names);
			return this;
		}

		public MaterialLayerBuilder setName(String name) {
			at.setName(name);
			return this;
		}

		public MaterialLayerBuilder setLayer(int other) {
			at.setLayer(other);
			return this;
		}

		public MaterialLayerBuilder setInitialState(PhysicalState initialState) {
			at.setInitialState(initialState);
			return this;
		}

		public MaterialLayerBuilder addSensableProperties(Map<SenseProperty<?>, ISensableTrait> map) {
			at.addSensableProperties(map);
			return this;
		}

		public MaterialLayerBuilder setSensableProperties(Map<SenseProperty<?>, ISensableTrait> map) {
			at.setSensableProperties(map);
			return this;
		}

		public MaterialLayerBuilder setNutritionTypes(int nutritionTypes) {
			at.setNutritionTypes(nutritionTypes);
			return this;
		}

		public MaterialLayerBuilder setNutritionTypes(INutritionType... nutritionTypes) {
			at.setNutritionTypes(nutritionTypes);
			return this;
		}

		public SimpleMaterialType build() {
			return at;
		}
	}
}
