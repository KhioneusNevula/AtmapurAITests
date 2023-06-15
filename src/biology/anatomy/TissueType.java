package biology.anatomy;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import actor.IMaterialLayerType;
import biology.systems.types.ISensor;
import mind.concepts.type.SenseProperty;
import mind.concepts.type.SenseProperty.BasicColor;
import mind.concepts.type.SenseProperty.BasicShape;
import sim.PhysicalState;

public class TissueType implements ITissueLayerType, Cloneable {

	public static final TissueType BONE = layer("bone", 0).setHasBlood(false).setHasNerves(false)
			.setInitialState(PhysicalState.SOLID_WHOLE);
	public static final TissueType BLOOD = layer("blood", 0).setHasBlood(false).setIsBlood(true)
			.setInitialState(PhysicalState.LIQUID)
			.setSensableProperties(Map.of(SenseProperty.COLOR, BasicColor.RED, SenseProperty.SHAPE, BasicShape.LIQUID));
	public static final TissueType NERVES = layer("nerves", 0).setHasNerves(true).setHasBlood(false);
	public static final TissueType MUSCLE = layer("muscle", 3).setMuscular(true).setBundleNames("flesh")
			.setSublayers("nerves");
	public static final TissueType FAT = layer("fat", 4).setHasBlood(false).setBundleNames("flesh");
	public static final TissueType CARTILAGE = layer("cartilage", 0).setSublayers("nerves");
	public static final TissueType SKIN = layer("skin", 5).setBundleNames("flesh").setSublayers("nerves");
	public static final TissueType HAIR = layer("hair", 6).setHasBlood(false)
			.setInitialState(PhysicalState.SOLID_FLEXIBLE);
	public static final TissueType GRAY_MATTER = layer("gray_matter", 1)
			.setSensableProperties(Map.of(SenseProperty.COLOR, BasicColor.LIGHT_GRAY));
	public static final TissueType WHITE_MATTER = FAT.atLayer(2).setName("white_matter").setBundleNames()
			.addSensableProperties(Map.of(SenseProperty.COLOR, BasicColor.WHITE));
	public static final TissueType EYE_FLUID = layer("eye_fluid", 0).setHasBlood(false)
			.setInitialState(PhysicalState.LIQUID);
	public static final TissueType EYE_MUSCLE = MUSCLE.atLayer(1).setName("eye_muscle");
	// nonhuman
	public static final TissueType SUCKER = layer("sucker", 0).setSublayers("nerves").setMuscular(true);
	public static final TissueType WHISKER_HAIR = layer("hair", 6).thatHasNerves().setName("whisker_hair")
			.setInitialState(PhysicalState.SOLID_FLEXIBLE);
	public static final TissueType ESSENCE = layer("essence", 0).setHasBlood(false).setIsBlood(true)
			.setInitialState(PhysicalState.METAPHYSICAL);

	private int layer;
	private boolean hasBlood = true;
	private boolean hasNerves = false;
	private boolean muscular = false;
	private boolean isBlood = false;
	private String name;
	private Set<String> bundleNames = Set.of();
	private Set<String> sublayers = Set.of();
	private PhysicalState initialState = PhysicalState.SQUISHY_WHOLE;
	private Map<SenseProperty<?>, Object> sensables = Map.of();
	private Multimap<ISensor, SenseProperty<?>> potentialSenses = ImmutableMultimap.of();

	/** TODO add materials */

	private TissueType(String name, int layer) {
		this.name = name;
		this.layer = layer;
	}

	private static TissueType layer(String name, int layer) {
		return new TissueType(name, layer);
	}

	public TissueType butNotLifeEssence() {
		return this.copy().setIsBlood(false);
	}

	public TissueType thatIsLifeEssence() {
		return this.copy().setIsBlood(true);
	}

	public TissueType withoutSensableProperties(Collection<SenseProperty<?>> props) {
		Map<SenseProperty<?>, Object> sebs = new TreeMap<>(this.sensables);
		for (SenseProperty<?> prop : props) {
			sebs.remove(prop);
		}
		return this.copy().setSensableProperties(sebs);
	}

	public TissueType withSensableProperties(Map<SenseProperty<?>, Object> map) {
		Map<SenseProperty<?>, Object> mapa = new TreeMap<>(sensables);
		mapa.putAll(map);
		return this.copy().setSensableProperties(mapa);
	}

	public TissueType inInitialState(PhysicalState state) {
		return this.copy().setInitialState(state);
	}

	public TissueType withoutSublayers(String... name) {
		return withoutSublayers(Set.of(name));
	}

	public TissueType withoutSublayers(Collection<String> name) {
		Set<String> ss = new TreeSet<>(sublayers);
		ss.removeAll(name);
		return this.copy().setSublayers(ss);
	}

	public TissueType withSublayers(String... names) {
		return withSublayers(Set.of(names));
	}

	public TissueType withSublayers(Collection<String> names) {
		Set<String> set = new TreeSet<>(sublayers);
		set.addAll(names);
		return this.copy().setSublayers(set);
	}

	public TissueType withoutBundleNames(String... name) {
		return withoutBundleNames(Set.of(name));
	}

	public TissueType withoutBundleNames(Collection<String> name) {
		Set<String> ss = new TreeSet<>(bundleNames);
		ss.removeAll(name);
		return this.copy().setBundleNames(ss);
	}

	public TissueType withBundleNames(String... names) {
		return withBundleNames(Set.of(names));
	}

	public TissueType withBundleNames(Collection<String> names) {
		Set<String> set = new TreeSet<>(bundleNames);
		set.addAll(names);
		return this.copy().setBundleNames(set);
	}

	public TissueType renamedto(String newname) {
		return this.copy().setName(newname);
	}

	public TissueType butNotMuscular() {
		return this.copy().setMuscular(false);
	}

	public TissueType thatIsMuscular() {
		return this.copy().setMuscular(true);
	}

	public TissueType butWithoutNerves() {
		return this.copy().setHasNerves(false);
	}

	public TissueType thatHasNerves() {
		return this.copy().setHasNerves(true);
	}

	public TissueType butWithoutLifeEssence() {
		return this.copy().setHasBlood(false);
	}

	public TissueType thatHasLifeEssence() {
		return this.copy().setHasBlood(true);
	}

	/**
	 * Return a copy of this tissue with the layer it goes above as the given layer
	 * 
	 * @param lowerLayer
	 * @return
	 */
	public TissueType atLayer(int lowerLayer) {
		return this.copy().setLayer(lowerLayer);
	}

	/**
	 * Makes this tissue layer not go above any other layer
	 * 
	 * @return
	 */
	public TissueType aboveNothing() {
		return this.copy().setLayer(0);
	}

	private TissueType setSublayers(String... names) {
		return setSublayers(Set.of(names));
	}

	private TissueType setSublayers(Set<String> names) {
		this.sublayers = ImmutableSet.copyOf(names);
		return this;
	}

	private TissueType setBundleNames(String... names) {
		return setBundleNames(Set.of(names));
	}

	private TissueType setBundleNames(Set<String> names) {
		this.bundleNames = ImmutableSet.copyOf(names);
		return this;
	}

	private TissueType setName(String name) {
		this.name = name;
		return this;
	}

	private TissueType setMuscular(boolean m) {
		this.muscular = m;
		return this;
	}

	private TissueType setHasNerves(boolean h) {
		this.hasNerves = h;
		return this;
	}

	private TissueType setHasBlood(boolean ha) {
		this.hasBlood = ha;
		return this;
	}

	private TissueType setLayer(int other) {
		this.layer = other;
		return this;
	}

	private TissueType setInitialState(PhysicalState initialState) {
		this.initialState = initialState;
		return this;
	}

	private TissueType setIsBlood(boolean is) {
		this.isBlood = is;
		return this;
	}

	private TissueType addSensableProperties(Map<SenseProperty<?>, Object> map) {
		Map<SenseProperty<?>, Object> mapa = new TreeMap<>(sensables);
		mapa.putAll(map);
		return this.setSensableProperties(mapa);
	}

	private TissueType setSensableProperties(Map<SenseProperty<?>, Object> map) {
		sensables = new TreeMap<>();
		this.potentialSenses = MultimapBuilder.hashKeys().treeSetValues().build();
		for (Map.Entry<SenseProperty<?>, Object> entry : map.entrySet()) {
			if (!entry.getKey().getType().isAssignableFrom(entry.getValue().getClass())) {
				throw new IllegalArgumentException("Illegal sense property added " + entry.getKey().getName()
						+ " for value " + entry.getValue() + " when building " + this.getName());
			}
			for (ISensor sensor : entry.getKey().getSensors()) {
				potentialSenses.put(sensor, entry.getKey());
			}
			sensables.put(entry.getKey(), entry.getValue());
		}
		sensables = ImmutableMap.copyOf(sensables);
		potentialSenses = ImmutableMultimap.copyOf(potentialSenses);
		return this;
	}

	@Override
	public int getLayer() {
		return layer;
	}

	@Override
	public boolean hasLifeEssence() {
		return hasBlood;
	}

	@Override
	public boolean hasNerves() {
		return hasNerves;
	}

	@Override
	public boolean isMuscular() {
		return muscular;
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
	public String getName() {
		return name;
	}

	private TissueType copy() {
		try {
			return (TissueType) this.clone();
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
	public boolean isLifeEssence() {
		return isBlood;
	}

	@Override
	public Collection<SenseProperty<?>> getSensableProperties(ISensor sensor) {
		return this.potentialSenses.get(sensor);
	}

	@Override
	public <T> T getTrait(SenseProperty<T> prop) {
		return (T) sensables.get(prop);
	}

	@Override
	public String toString() {
		return "tissue_" + this.getName();
	}
}
