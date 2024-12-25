package actor.construction.properties;

import java.awt.Color;
import java.util.Collection;
import java.util.UUID;

import com.google.common.collect.ImmutableSet;

import actor.construction.properties.SenseProperty.WrapperSensableTrait.FloatTrait;
import biology.sensing.ISense;
import civilization_and_minds.social.concepts.IConcept;

/**
 * Sense properties are properties that can be sensed about an object.
 * 
 * @author borah
 *
 * @param <T>
 */
public class SenseProperty<T extends ISensableTrait> implements Comparable<SenseProperty<?>>, IConcept {

	public static final SenseProperty<IColor> COLOR = new SenseProperty<>("color", IColor.class, BasicColor.COLORLESS);
	public static final SenseProperty<IShape> SHAPE = new SenseProperty<>("shape", IShape.class, BasicShape.FORMLESS);
	public static final SenseProperty<ITexture> TEXTURE = new SenseProperty<>("texture", ITexture.class,
			BasicTexture.IMMATERIAL);
	public static final SenseProperty<ISmell> SMELL = new SenseProperty<>("smell", ISmell.class, BasicSmell.NONE);
	public static final SenseProperty<ISound> SOUND = new SenseProperty<>("sound", ISound.class, BasicSound.NONE);
	/**
	 * 1.0f is opaque, 0.0f is transparent
	 */
	public static final SenseProperty<FloatTrait> OPACITY = new SenseProperty<>("opacity", FloatTrait.class,
			FloatTrait.ONE);

	private Class<T> type;

	private String name;
	private Collection<ISense> expressedToSenses;
	private T defaultValue;

	public SenseProperty(String name, Class<T> type, T defaultValue, ISense... toSenses) {
		this.type = type;
		this.name = name;
		this.expressedToSenses = ImmutableSet.copyOf(toSenses);
		this.defaultValue = defaultValue;

	}

	/**
	 * The default value of this property, if not set in an entity's visage
	 * 
	 * @return
	 */
	public T getDefaultValue() {
		return defaultValue;
	}

	/**
	 * What senses this property is expressed to
	 * 
	 * @return
	 */
	public Collection<ISense> getSensesExpressedTo() {
		return expressedToSenses;
	}

	public Class<T> getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getUniqueName() {
		return "senseproperty_" + name;
	}

	@Override
	public int compareTo(SenseProperty<?> o) {
		return this.name.compareTo(o.name);
	}

	@Override
	public int hashCode() {
		return this.getUniqueName().hashCode();
	}

	@Override
	public String toString() {
		return this.getUniqueName();
	}

	@Override
	public ConceptType getConceptType() {
		return ConceptType.SENSABLE_PROPERTY;
	}

	public static abstract class WrapperSensableTrait<T> implements ISensableTrait {

		private T wrapped;

		public WrapperSensableTrait(T wrapped) {
			this.wrapped = wrapped;
		}

		public T getValue() {
			return wrapped;
		}

		@Override
		public boolean isUnique() {
			return false;
		}

		@Override
		public ConceptType getConceptType() {
			return ConceptType.SENSABLE_TRAIT;
		}

		@Override
		public String getUniqueName() {
			return "wrapped_" + this.getClass().getSimpleName().toLowerCase() + "_" + this.getValue();
		}

		public static class FloatTrait extends WrapperSensableTrait<Float> {

			public static final FloatTrait ONE = new FloatTrait(1f);
			public static final FloatTrait ZERO = new FloatTrait(0f);

			public FloatTrait(Float wrapped) {
				super(wrapped);
			}
		}

		public static class IntegerTrait extends WrapperSensableTrait<Integer> {
			public static final IntegerTrait ZERO = new IntegerTrait(1);

			public IntegerTrait(Integer wrapped) {
				super(wrapped);
			}
		}

		public static class BooleanTrait extends WrapperSensableTrait<Boolean> {
			public static final BooleanTrait FALSE = new BooleanTrait(false);
			public static final BooleanTrait TRUE = new BooleanTrait(true);

			private BooleanTrait(Boolean wrapped) {
				super(wrapped);
			}
		}
	}

	public static interface ISound extends ISensableTrait {
		public String getName();

		public boolean repetitive();

	}

	public static class UniqueSound implements ISound {
		private UUID id;
		private boolean repetitive;

		public UniqueSound(UUID id, boolean repetitive) {
			this.id = id;
			this.repetitive = repetitive;
		}

		@Override
		public String getName() {
			return "usound" + this.id;
		}

		@Override
		public String getUniqueName() {
			return this.getName();
		}

		@Override
		public ConceptType getConceptType() {
			return ConceptType.SENSABLE_TRAIT;
		}

		@Override
		public boolean isUnique() {
			return true;
		}

		@Override
		public boolean repetitive() {
			return repetitive;
		}
	}

	public static enum BasicSound implements ISound {
		HEARTBEAT(true), SMACK, CHEW, CRACK, CRACKLING(true), BOOM, ROAR, RAIN(true),
		/** lack of sound */
		NONE;

		private boolean repetitive;

		private BasicSound() {
		}

		private BasicSound(boolean repetitive) {
			this.repetitive = repetitive;
		}

		@Override
		public String getName() {
			return "sound_" + name();
		}

		@Override
		public String getUniqueName() {
			return this.getName();
		}

		@Override
		public ConceptType getConceptType() {
			return ConceptType.SENSABLE_TRAIT;
		}

		@Override
		public String toString() {
			return getName();
		}

		@Override
		public boolean repetitive() {
			return repetitive;
		}

		@Override
		public boolean canBeSensed() {
			return this != NONE;
		}

		@Override
		public boolean isUnique() {
			return false;
		}
	}

	public static interface ISmell extends ISensableTrait {
		public String getName();

		public boolean isAcrid();

		public boolean isSweet();
	}

	public static class UniqueSmell implements ISmell {
		private UUID id;
		private boolean sweet;
		private boolean acrid;

		public UniqueSmell(UUID id, boolean sweet, boolean acrid) {
			this.id = id;
			this.sweet = sweet;
			this.acrid = acrid;
		}

		@Override
		public String getName() {
			return "usmell" + this.id;
		}

		@Override
		public ConceptType getConceptType() {
			return ConceptType.SENSABLE_TRAIT;
		}

		@Override
		public String getUniqueName() {
			return this.getName();
		}

		@Override
		public boolean isAcrid() {
			return acrid;
		}

		@Override
		public boolean isSweet() {
			return sweet;
		}

		@Override
		public boolean isUnique() {
			return true;
		}
	}

	public static enum BasicSmell implements ISmell {
		FOODY, WATERY, SWEATY, EARTHY, FLOWER(false, true), BAKED(false, true), TRASH(true, false),
		EXCREMENT(true, false), SICKLY_SWEET(true, true), BURNING(true, false),
		/** lack of smell */
		NONE;

		boolean acrid;
		boolean sweet;

		private BasicSmell() {
		}

		@Override
		public String getName() {
			return "smell_" + name();
		}

		@Override
		public String getUniqueName() {
			return this.getName();
		}

		@Override
		public ConceptType getConceptType() {
			return ConceptType.SENSABLE_TRAIT;
		}

		@Override
		public String toString() {
			return getName();
		}

		private BasicSmell(boolean acrid, boolean sweet) {
			this.acrid = acrid;
			this.sweet = sweet;
		}

		@Override
		public boolean isAcrid() {
			return acrid;
		}

		@Override
		public boolean isSweet() {
			return sweet;
		}

		@Override
		public boolean isUnique() {
			return false;
		}

		@Override
		public boolean canBeSensed() {
			return this != NONE;
		}
	}

	public static interface ITexture extends ISensableTrait {
		public String getName();
	}

	public static enum BasicTexture implements ITexture {
		/** for things that are not physical and have no texture */
		IMMATERIAL, SMOOTH, WET, SMOKY, STICKY, LEATHERY, SOFT, SPIKY, BUMPY, POWDERY, SILKY, FIRM, SQUISHY, COARSE,
		ROUGH, HARD, CARTILAGIOUS;

		@Override
		public String getName() {
			return "texture_" + name();
		}

		@Override
		public ConceptType getConceptType() {
			return ConceptType.SENSABLE_TRAIT;
		}

		@Override
		public String getUniqueName() {
			return this.getName();
		}

		@Override
		public String toString() {
			return getName();
		}

		@Override
		public boolean isUnique() {
			return false;
		}

		@Override
		public boolean canBeSensed() {
			return this != IMMATERIAL;
		}
	}

	public static interface IShape extends ISensableTrait {
		public String getName();

	}

	public static class UniqueShape implements IShape {
		private UUID id;
		private IShape alike;

		public UniqueShape(UUID id, IShape alikeTo) {
			this.id = id;
			this.alike = alikeTo;
		}

		@Override
		public ISensableTrait resembles() {
			return alike;
		}

		@Override
		public String getName() {
			return "ushape" + id;
		}

		@Override
		public String getUniqueName() {
			return getName();
		}

		@Override
		public ConceptType getConceptType() {
			return ConceptType.SENSABLE_TRAIT;
		}

		@Override
		public boolean isUnique() {
			return true;
		}
	}

	public static enum BasicShape implements IShape {
		CUBE, SPHERE, OVOID, ROD, WHEEL, PLANE, STAR, NOSE, CLOTH, BOARD, PLANK, HEAD, HORN, ANTLER, BODY, RIBCAGE,
		LIMB, HAND, ORIFICE, FOOT, PAW, HOOF, TAIL, FACE, TOOTH, FANG, EAR, MOUSTACHE, BEARD, BOWL, BRACKET, POINTY_EAR,
		ANIMAL_EAR, ELEPHANT_EAR, FLOWER, BRANCH, LEAF, TRUNK, ROCKY, MOUNTAIN, CRYSTALLINE, POWDER, BLOB, PASTE,
		TAPERING_ROD, HAIR_MASS, THIN_STRAND, PILLOW, MINUS_EIGHT, PELVIS, BRAIN, HEART, SKULL, SHORT_NECK, STRINGY,
		LIQUID, FORMLESS, CIRCLE;

		@Override
		public String getName() {
			return "shape_" + name();
		}

		@Override
		public String getUniqueName() {
			return getName();
		}

		@Override
		public ConceptType getConceptType() {
			return ConceptType.SENSABLE_TRAIT;
		}

		@Override
		public String toString() {
			return getName();
		}

		@Override
		public boolean isUnique() {
			return false;
		}
	}

	public static interface IColor extends ISensableTrait {
		/**
		 * may be null for colors beyond natural colors or no color
		 * 
		 * @return
		 */
		public Color getColor();

		public String getName();
	}

	public static enum BasicColor implements IColor {
		RED(Color.RED), BLUE(Color.BLUE), GREEN(Color.GREEN), YELLOW(Color.YELLOW), MAGENTA(Color.MAGENTA),
		BLACK(Color.BLACK), WHITE(Color.WHITE), GRAY(Color.GRAY), DARK_GRAY(Color.DARK_GRAY),
		LIGHT_GRAY(Color.LIGHT_GRAY), PINK(Color.PINK), ORANGE(Color.ORANGE), CYAN(Color.CYAN),
		LIGHT_BLUE(135, 206, 235), PURPLE(100, 0, 100), GOLDEN_YELLOW(249, 166, 2), BROWN(102, 51, 0),
		PEACH(0xFF, 229, 180), DARK_GREEN(3, 53, 0), DARK_BROWN(54, 34, 4), DARK_BLUE(0x000028),
		LIGHT_TURQUOISE(0x40E0D0), TEAL(0, 128, 128), INDIGO(0x3f0fb7), COLORLESS(Color.WHITE), COSMIC(0x610099),
		MYSTIC(0xdb02c9), UNNATURAL(0xec90f5), ABNORMAL(0x4bc6d6), ELDRITCH(0x1b5943), UNEARTHLY(0xedccff),
		OTHERWORLDLY(0x372c4a), INEFFABLE(0xf7b5d3), CELESTIAL(0xd9bb0f);

		private Color color;

		private BasicColor(int rgb) {
			this(new Color(rgb));
		}

		private BasicColor(int r, int g, int b) {
			this(new Color(r, g, b));
		}

		private BasicColor(Color color) {
			this.color = color;
		}

		public Color getColor() {
			return color;
		}

		public String getName() {
			return "color_" + name();
		}

		@Override
		public String getUniqueName() {
			return getName();
		}

		@Override
		public ConceptType getConceptType() {
			return ConceptType.SENSABLE_TRAIT;
		}

		@Override
		public boolean isUnique() {
			return false;
		}

		@Override
		public String toString() {
			return getName();
		}
	}

}
