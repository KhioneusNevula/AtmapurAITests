package biology.anatomy;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import actor.construction.properties.ISensableTrait;
import actor.construction.properties.SenseProperty;
import actor.construction.properties.SenseProperty.BasicColor;
import actor.construction.properties.SenseProperty.BasicShape;
import actor.construction.simple.SimpleMaterialType;
import sim.physicality.PhysicalState;

public class TissueType extends SimpleMaterialType implements ITissueLayerType {

	public static final TissueType BONE = layer("bone", 0).setHasBlood(false).setHasNerves(false)
			.setInitialState(PhysicalState.HARD_SOLID_WHOLE);
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
			.setInitialState(PhysicalState.FLEXIBLE_SOLID);
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
			.setInitialState(PhysicalState.FLEXIBLE_SOLID);
	public static final TissueType ESSENCE = layer("essence", 0).setHasBlood(false).setIsBlood(true)
			.setInitialState(PhysicalState.METAPHYSICAL);

	private boolean hasBlood = true;
	private boolean hasNerves = false;
	private boolean muscular = false;
	private boolean isBlood = false;

	private TissueType(String name, int layer) {
		super(name, layer);
	}

	public static TissueType layer(String name, int layer) {
		return new TissueType(name, layer).setInitialState(PhysicalState.SQUISHY_SOLID_WHOLE);
	}

	public TissueType butNotLifeEssence() {
		return this.copy().setIsBlood(false);
	}

	public TissueType thatIsLifeEssence() {
		return this.copy().setIsBlood(true);
	}

	public TissueType withoutSensableProperties(Collection<SenseProperty<?>> props) {
		return (TissueType) super.withoutSensableProperties(props);
	}

	public TissueType withSensableProperties(Map<SenseProperty<?>, ? extends ISensableTrait> map) {
		return (TissueType) super.withSensableProperties(map);
	}

	public TissueType inInitialState(PhysicalState state) {
		return (TissueType) super.inInitialState(state);
	}

	public TissueType withoutSublayers(String... name) {
		return (TissueType) super.withoutSublayers(name);
	}

	public TissueType withoutSublayers(Collection<String> name) {
		return (TissueType) super.withoutSublayers(name);
	}

	public TissueType withSublayers(String... names) {
		return (TissueType) super.withSublayers(names);
	}

	public TissueType withSublayers(Collection<String> names) {
		return (TissueType) super.withSublayers(names);
	}

	public TissueType withoutBundleNames(String... name) {
		return (TissueType) super.withoutBundleNames(name);
	}

	public TissueType withoutBundleNames(Collection<String> name) {
		return (TissueType) super.withoutBundleNames(name);
	}

	public TissueType withBundleNames(String... names) {
		return (TissueType) super.withBundleNames(names);
	}

	public TissueType withBundleNames(Collection<String> names) {
		return (TissueType) super.withBundleNames(names);
	}

	public TissueType renamedto(String newname) {
		return (TissueType) super.renamedto(newname);
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

	public TissueType atLayer(int lowerLayer) {
		return (TissueType) super.atLayer(lowerLayer);
	}

	public TissueType aboveNothing() {
		return (TissueType) super.aboveNothing();
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

	private TissueType setIsBlood(boolean is) {
		this.isBlood = is;
		return this;
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

	protected TissueType copy() {
		return (TissueType) super.copy();
	}

	@Override
	public boolean isLifeEssence() {
		return isBlood;
	}

	@Override
	public String toString() {
		return "tissue_" + this.getName();
	}

	@Override
	protected TissueType setBundleNames(Set<String> names) {
		// method stub
		return (TissueType) super.setBundleNames(names);
	}

	@Override
	protected TissueType setBundleNames(String... names) {
		// method stub
		return (TissueType) super.setBundleNames(names);
	}

	@Override
	protected TissueType setInitialState(PhysicalState initialState) {
		// method stub
		return (TissueType) super.setInitialState(initialState);
	}

	@Override
	protected TissueType setLayer(int other) {
		// method stub
		return (TissueType) super.setLayer(other);
	}

	@Override
	protected TissueType setName(String name) {
		// method stub
		return (TissueType) super.setName(name);
	}

	@Override
	protected TissueType setSensableProperties(Map<SenseProperty<?>, ? extends ISensableTrait> map) {
		// method stub
		return (TissueType) super.setSensableProperties(map);
	}

	@Override
	protected TissueType setSublayers(Set<String> names) {
		// method stub
		return (TissueType) super.setSublayers(names);
	}

	@Override
	protected TissueType setSublayers(String... names) {
		return (TissueType) super.setSublayers(names);
	}

	@Override
	protected TissueType addSensableProperties(Map<SenseProperty<?>, ? extends ISensableTrait> map) {
		return (TissueType) super.addSensableProperties(map);
	}

}
