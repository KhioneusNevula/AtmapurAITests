package actor.construction.simple;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Table;

import actor.construction.physical.IComponentType;
import actor.construction.physical.IPartAbility;
import actor.construction.properties.IAbilityStat;
import actor.construction.properties.ISensableTrait;
import actor.construction.properties.SenseProperty;
import biology.sensing.ISense;

/**
 * A simple part type for a simple part
 * 
 * @author borah
 *
 */
public class SimplePartType implements IComponentType {

	private String name;
	private String parent;
	private String surrounding;
	private boolean isHole = false;
	private boolean isRoot = false;
	private Set<IPartAbility> abilities = Set.of();
	private Set<ISense> senses = Set.of();
	private Table<IPartAbility, IAbilityStat<?>, Object> senseStats = HashBasedTable.create();
	/*
	 * // private boolean eats = false; private boolean thinks = false; private
	 * boolean pumpsBlood = false; private boolean fertilizes = false; private
	 * boolean storesSeed = false; private boolean gestates = false; private boolean
	 * storesEggs = false; // private boolean digests = false; private boolean
	 * givesBirth = false; private boolean prehensile = false;
	 */
	private boolean uncovered = false;
	private String category;
	private float size;
	private int count = 1;
	private Side side = Side.MIDDLE;
	private Height height = Height.ALIGNED;
	private Face face = Face.MIDDLE;
	private float nutritionValue = 0;
	private Map<SenseProperty<?>, Object> sensables = Map.of();

	protected static SimplePartType part(String name, float size) {
		return new SimplePartType(name, size);
	}

	public static PartTypeBuilder builder(String name, float size) {
		return new PartTypeBuilder(name, size);
	}

	protected SimplePartType(String name, float size) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return this.getName().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SimplePartType spt) {
			return this.name.equals(spt.name)
					&& (this.parent == null ? spt.parent == null : spt.parent != null && this.parent.equals(spt.parent))
					&& (this.surrounding == null ? spt.surrounding == null
							: spt.surrounding != null && this.surrounding.equals(spt.surrounding))
					&& this.isHole == spt.isHole && this.isRoot == spt.isRoot && this.abilities.equals(spt.abilities)
					&& this.uncovered == spt.uncovered
					&& (this.category == null ? spt.category == null
							: spt.category != null && this.category.equals(spt.category))
					&& this.size == spt.size && this.count == spt.count && this.side == spt.side
					&& this.height == spt.height && this.face == spt.face && this.nutritionValue == spt.nutritionValue
					&& this.sensables.equals(spt.sensables);
		}
		return super.equals(obj);
	}

	public SimplePartType withCategory(String cat) {
		return this.copy().setCategory(cat);
	}

	public SimplePartType butWithoutCategory() {
		return this.copy().setCategory(null);
	}

	public SimplePartType thatCan(IPartAbility... abs) {
		return thatCan(Set.of(abs));
	}

	public SimplePartType thatCan(Collection<IPartAbility> abs) {
		Set<IPartAbility> set = new HashSet<>(abilities);
		set.addAll(abs);
		return this.copy().setAbilities(set);
	}

	public SimplePartType thatCannot(Collection<IPartAbility> abs) {
		Set<IPartAbility> set = new HashSet<>(abilities);
		set.removeAll(Set.copyOf(abs));
		return this.copy().setAbilities(Set.copyOf(set));
	}

	public SimplePartType thatCannot(IPartAbility... abs) {
		Set<IPartAbility> set = new HashSet<>(abilities);
		set.removeAll(Set.of(abs));
		return this.copy().setAbilities(Set.copyOf(set));

	}

	public <T> SimplePartType withDefaultStat(IPartAbility abil, IAbilityStat<T> stat, T value) {
		return this.copy().setAbilityStat(abil, stat, value);
	}

	public <T> SimplePartType withoutDefaultStat(IPartAbility abil, IAbilityStat<T> stat) {
		return this.copy().removeAbilityStat(abil, stat);
	}

	public SimplePartType withoutSensableProperties(Collection<SenseProperty<?>> props) {
		Map<SenseProperty<?>, Object> sebs = new TreeMap<>(this.sensables);
		for (SenseProperty<?> prop : props) {
			sebs.remove(prop);
		}
		return this.copy().setSensableProperties(sebs);
	}

	public SimplePartType withSensableProperties(Map<SenseProperty<?>, Object> map) {
		Map<SenseProperty<?>, Object> mapa = new TreeMap<>(sensables);
		mapa.putAll(map);
		return this.copy().setSensableProperties(mapa);
	}

	public SimplePartType thatIsOnLeft() {
		return this.copy().setSide(Side.LEFT);
	}

	public SimplePartType thatIsOnRight() {
		return this.copy().setSide(Side.RIGHT);
	}

	public SimplePartType butNeitherLeftNorRight() {
		return this.copy().setSide(Side.MIDDLE);
	}

	public SimplePartType thatIsAbove() {
		return this.copy().setHeight(Height.ABOVE);
	}

	public SimplePartType thatIsBelow() {
		return this.copy().setHeight(Height.BELOW);
	}

	public SimplePartType butNeitherAboveNorBelow() {
		return this.copy().setHeight(Height.ALIGNED);
	}

	public SimplePartType thatIsInFront() {
		return this.copy().setFace(Face.FRONT);
	}

	public SimplePartType thatIsInBack() {
		return this.copy().setFace(Face.BACK);
	}

	public SimplePartType butNeitherFrontNorBack() {
		return this.copy().setFace(Face.MIDDLE);
	}

	public SimplePartType resizedTo(float newSize) {
		return this.copy().setSize(newSize);
	}

	public SimplePartType renamedTo(String newName) {
		return this.copy().setName(newName);
	}

	/**
	 * Makes this body part NOT a root
	 * 
	 * @return
	 */
	public SimplePartType butNotRoot() {
		return this.copy().setRoot(false);
	}

	/**
	 * Returns a clone of this body part with the given parent; also makes it no
	 * longer the root (if it previously was)
	 * 
	 * @param parent
	 * @return
	 */
	public SimplePartType thatHasParent(IComponentType parent) {
		return this.copy().setRoot(false).setParent(parent);
	}

	public SimplePartType thatHasParent(String parent) {
		return this.copy().setRoot(false).setParent(parent);
	}

	/**
	 * returns copy of this body part with no parent
	 * 
	 * @return
	 */
	public SimplePartType butWithoutParent() {
		return this.copy().setParent((String) null);
	}

	/**
	 * Returns copy of part with surrounding part removed
	 * 
	 * @return
	 */
	public SimplePartType butWithoutSurrounding() {
		return this.copy().setSurrounding((String) null);
	}

	/**
	 * returns copy of this body part as the root
	 * 
	 * @return
	 */
	public SimplePartType thatIsRoot() {
		if (this.parent != null)
			throw new IllegalStateException("part with parent cannot become root");
		return this.copy().setRoot(true);
	}

	/**
	 * Returns a clone of this body part with the given surrounding part
	 * 
	 * @param surroundings
	 * @return
	 */
	public SimplePartType thatHasSurrounding(IComponentType surroundings) {
		return this.copy().setSurrounding(surroundings);
	}

	public SimplePartType thatHasSurrounding(String surroundings) {
		return this.copy().setSurrounding(surroundings);
	}

	/**
	 * Return copy of body part as a hole instead of a protrusion
	 * 
	 * @return
	 */
	public SimplePartType thatIsHole() {
		return this.copy().setHole(true);
	}

	/**
	 * Return copy of body part as a non-hole
	 * 
	 * @return
	 */
	public SimplePartType butNotHole() {
		return this.copy().setHole(false);
	}

	public SimplePartType thatIsUncovered() {
		return this.copy().setUncovered(true);
	}

	public SimplePartType butNotUncovered() {
		return this.copy().setUncovered(false);
	}

	/**
	 * Sets the number of this body part
	 * 
	 * @param count
	 * @return
	 */
	public SimplePartType ofNumber(int count) {
		return this.copy().setCount(count);
	}

	/**
	 * Sets the number of this body part to 1
	 * 
	 * @return
	 */
	public SimplePartType thatIsSingular() {
		return this.copy().setCount(1);
	}

	public SimplePartType withDefaultNutritionValue(float val) {
		return this.copy().setNutritionValue(val);
	}

	protected SimplePartType setNutritionValue(float nutritionValue) {
		this.nutritionValue = nutritionValue;
		return this;
	}

	protected SimplePartType setCategory(String cat) {
		this.category = cat;
		return this;
	}

	protected SimplePartType setSize(float size) {
		this.size = size;
		return this;
	}

	protected SimplePartType setName(String name) {
		this.name = name;
		return this;
	}

	protected SimplePartType setHole(boolean isHole) {
		this.isHole = isHole;
		return this;
	}

	protected SimplePartType setParent(IComponentType parent) {
		this.parent = parent.getName();
		return this;
	}

	protected SimplePartType setParent(String parent) {
		this.parent = parent;
		return this;
	}

	protected SimplePartType setSurrounding(IComponentType surrounding) {
		this.surrounding = surrounding.getName();
		return this;
	}

	protected SimplePartType setSurrounding(String surrounding) {
		this.surrounding = surrounding;
		return this;
	}

	protected SimplePartType setRoot(boolean isRoot) {
		this.isRoot = isRoot;
		return this;
	}

	protected SimplePartType setUncovered(boolean a) {
		this.uncovered = a;
		return this;
	}

	/*
	 * protected BasicSimpleParts setDigests(boolean a) { this.digests = a; return
	 * this; }
	 */

	protected SimplePartType setFace(Face f) {
		this.face = f;
		return this;
	}

	protected SimplePartType setSide(Side s) {
		this.side = s;
		return this;
	}

	protected SimplePartType setHeight(Height h) {
		this.height = h;
		return this;
	}

	protected SimplePartType setCount(int count) {
		if (count <= 0)
			throw new IllegalArgumentException("invalid count: " + count);
		this.count = count;
		return this;
	}

	protected SimplePartType setAbilities(IPartAbility... abs) {
		return setAbilities(Set.of(abs));
	}

	protected SimplePartType setAbilities(Set<IPartAbility> abs) {
		this.abilities = ImmutableSet.copyOf(abs);
		this.senses = ImmutableSet.<ISense>builder()
				.addAll(abs.stream().filter((a) -> a.isSensingAbility()).<ISense>map((a) -> (ISense) a).iterator())
				.build();
		return this;
	}

	protected <T> SimplePartType setAbilityStat(IPartAbility abiliy, IAbilityStat<T> stat, T value) {
		senseStats.put(abiliy, stat, value);
		return this;
	}

	protected <T> SimplePartType removeAbilityStat(IPartAbility ability, IAbilityStat<T> stat) {
		senseStats.remove(ability, stat);
		return this;
	}

	protected SimplePartType addSensableProperties(Map<SenseProperty<?>, Object> map) {
		map = new TreeMap<>(sensables);
		map.putAll(map);
		return this.setSensableProperties(map);
	}

	protected SimplePartType setSensableProperties(Map<SenseProperty<?>, Object> map) {
		sensables = new TreeMap<>();
		for (Map.Entry<SenseProperty<?>, Object> entry : map.entrySet()) {
			if (!entry.getKey().getType().isAssignableFrom(entry.getValue().getClass())) {
				throw new IllegalArgumentException("Illegal sense property added " + entry.getKey().getName()
						+ " for value " + entry.getValue() + " when building " + this.getName());
			}
			sensables.put(entry.getKey(), entry.getValue());
		}
		sensables = ImmutableMap.copyOf(sensables);
		return this;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getParent() {
		return parent;
	}

	@Override
	public float defaultNutritionContent() {
		return nutritionValue;
	}

	@Override
	public String getSurroundingPart() {
		return surrounding;
	}

	@Override
	public boolean isRoot() {
		return isRoot;
	}

	@Override
	public boolean isHole() {
		return isHole;
	}

	/*
	 * @Override public boolean digests() { return digests; }
	 */
	protected SimplePartType copy() {
		SimplePartType clone;
		try {
			clone = (SimplePartType) this.clone();
		} catch (Exception e) {
			return null;
		}
		return clone;
	}

	@Override
	public float size() {
		return size;
	}

	@Override
	public Face getFace() {
		return face;
	}

	@Override
	public Height getHeight() {
		return height;
	}

	@Override
	public Side getSide() {
		return side;
	}

	@Override
	public String category() {
		return category;
	}

	@Override
	public int count() {
		return count;
	}

	@Override
	public boolean uncovered() {
		return uncovered;
	}

	@Override
	public boolean hasAbility(IPartAbility ability) {
		return this.abilities.contains(ability);
	}

	@Override
	public Collection<IPartAbility> abilities() {
		return this.abilities;
	}

	@Override
	public Collection<ISense> getDefaultSenses() {
		return this.senses;
	}

	@Override
	public Collection<IAbilityStat<?>> getDefaultAbilityStats(IPartAbility forab) {
		return this.senseStats.row(forab).keySet();
	}

	@Override
	public <T> T getDefaultAbilityStat(IPartAbility forab, IAbilityStat<T> stat) {
		T o = (T) this.senseStats.get(forab, stat);
		if (o == null)
			return stat.defaultValue();
		return o;
	}

	@Override
	public Collection<SenseProperty<?>> getSensableProperties() {
		return sensables.keySet();
	}

	@Override
	public <T extends ISensableTrait> T getTrait(SenseProperty<T> prop) {
		return (T) sensables.get(prop);
	}

	@Override
	public String toString() {
		return "simple_part_" + this.name;
	}

	protected <T extends SimplePartType> T cast() {
		return (T) this;
	}

	public static class PartTypeBuilder {

		private SimplePartType at;

		private PartTypeBuilder(String name, float size) {
			at = new SimplePartType(name, size);
		}

		public SimplePartType build() {
			return at;
		}

		public PartTypeBuilder setCategory(String cat) {
			at.setCategory(cat);
			return this;
		}

		public PartTypeBuilder setSize(float size) {
			at.setSize(size);
			return this;
		}

		public PartTypeBuilder setName(String name) {
			at.setName(name);
			return this;
		}

		public PartTypeBuilder setHole(boolean isHole) {
			at.setHole(isHole);
			return this;
		}

		public PartTypeBuilder setParent(IComponentType parent) {
			at.setParent(parent);
			return this;
		}

		public PartTypeBuilder setParent(String parent) {
			at.setParent(parent);
			return this;
		}

		public PartTypeBuilder setSurrounding(IComponentType surrounding) {
			at.setSurrounding(surrounding);
			return this;
		}

		public PartTypeBuilder setSurrounding(String surrounding) {
			at.setSurrounding(surrounding);
			return this;
		}

		public PartTypeBuilder setRoot(boolean isRoot) {
			at.setRoot(isRoot);
			return this;
		}

		public PartTypeBuilder setUncovered(boolean a) {
			at.setUncovered(a);
			return this;
		}

		public PartTypeBuilder setFace(Face f) {
			at.setFace(f);
			return this;
		}

		public PartTypeBuilder setSide(Side s) {
			at.setSide(s);
			return this;
		}

		public PartTypeBuilder setHeight(Height h) {
			at.setHeight(h);
			return this;
		}

		public PartTypeBuilder setCount(int count) {
			at.setCount(count);
			return this;
		}

		public PartTypeBuilder setAbilities(IPartAbility... abs) {
			at.setAbilities(abs);
			return this;
		}

		public PartTypeBuilder setAbilities(Set<IPartAbility> abs) {
			at.setAbilities(abs);
			return this;
		}

		public PartTypeBuilder addSensableProperties(Map<SenseProperty<?>, Object> map) {
			at.addSensableProperties(map);
			return this;
		}

		public PartTypeBuilder setSensableProperties(Map<SenseProperty<?>, Object> map) {
			at.setSensableProperties(map);
			return this;
		}

		public PartTypeBuilder setDefaultNutrition(float def) {
			at.setNutritionValue(def);
			return this;
		}

		public <T> PartTypeBuilder setAbilityStat(IPartAbility abil, IAbilityStat<T> stat, T value) {
			at.setAbilityStat(abil, stat, value);
			return this;
		}

	}
}
