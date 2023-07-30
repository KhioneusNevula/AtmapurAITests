package mind.concepts.type;

import java.awt.Color;
import java.util.Collection;
import java.util.UUID;

import com.google.common.collect.ImmutableSet;

import biology.systems.types.ISensor;

public class SenseProperty<T> implements Comparable<SenseProperty<?>>, IProperty {

	public static final SenseProperty<IColor> COLOR = new SenseProperty<>("color", IColor.class, ISensor.SIGHT);
	public static final SenseProperty<IShape> SHAPE = new SenseProperty<>("shape", IShape.class, ISensor.SIGHT);
	public static final SenseProperty<ITexture> TEXTURE = new SenseProperty<>("texture", ITexture.class);
	public static final SenseProperty<ISmell> SMELL = new SenseProperty<>("smell", ISmell.class);
	public static final SenseProperty<ISound> SOUND = new SenseProperty<>("sound", ISound.class);
	public static final SenseProperty<Boolean> TRANSLUCENCE = new SenseProperty<>("translucence", Boolean.class,
			ISensor.SIGHT);
	public static final UniqueProperty SIGNATURE_SHAPE = new UniqueProperty("signature_shape", ISensor.SIGHT);

	private Class<T> type;

	private String name;

	private Collection<ISensor> sensors;

	public SenseProperty(String name, Class<T> type, ISensor... sensor) {
		this.type = type;
		this.name = name;
		this.sensors = ImmutableSet.copyOf(sensor);

	}

	public Class<T> getType() {
		return type;
	}

	public boolean isUnique() {
		return this instanceof UniqueProperty;
	}

	public String getName() {
		return name;
	}

	@Override
	public String getUniqueName() {
		return "senseproperty_" + name;
	}

	public Collection<ISensor> getSensors() {
		return sensors;
	}

	@Override
	public int compareTo(SenseProperty<?> o) {
		return this.name.compareTo(o.name);
	}

	@Override
	public String toString() {
		return this.getUniqueName() + this.sensors;
	}

	public static class UniqueProperty extends SenseProperty<UUID> {
		public UniqueProperty(String name, ISensor... sensor) {
			super(name, UUID.class, sensor);
		}
	}

	public static interface ISound {
		public String getName();

		public boolean repetitive();
	}

	public static enum BasicSound implements ISound {
		HEARTBEAT(true), SMACK, CHEW, CRACK, CRACKLING(true), ROAR;

		private boolean repetitive;

		private BasicSound() {
		}

		private BasicSound(boolean repetitive) {
			this.repetitive = repetitive;
		}

		@Override
		public String getName() {
			return name();
		}

		@Override
		public boolean repetitive() {
			return repetitive;
		}
	}

	public static interface ISmell {
		public String getName();

		public boolean isAcrid();

		public boolean isSweet();
	}

	public static enum BasicSmell implements ISmell {
		WATERY, SWEATY, EARTHY, FLOWER(false, true), BAKED(false, true), TRASH(true, false), EXCREMENT(true, false),
		SICKLY_SWEET(true, true);

		boolean acrid;
		boolean sweet;

		private BasicSmell() {
		}

		@Override
		public String getName() {
			return name();
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
	}

	public static interface ITexture {
		public String getName();
	}

	public static enum BasicTexture implements ITexture {
		SMOOTH, STICKY, LEATHERY, SOFT, SPIKY, BUMPY, POWDERY, SILKY, FIRM, SQUISHY, COARSE, ROUGH, HARD, CARTILAGIOUS;

		@Override
		public String getName() {
			return name();
		}
	}

	public static interface IShape {
		public String getName();
	}

	public static enum BasicShape implements IShape {
		CUBE, SPHERE, OVOID, ROD, WHEEL, PLANE, STAR, NOSE, CLOTH, BOARD, PLANK, HEAD, HORN, ANTLER, BODY, RIBCAGE,
		LIMB, HAND, ORIFICE, FOOT, PAW, HOOF, TAIL, FACE, TOOTH, FANG, EAR, MOUSTACHE, BEARD, BOWL, BRACKET, POINTY_EAR,
		ANIMAL_EAR, ELEPHANT_EAR, FLOWER, BRANCH, LEAF, TRUNK, ROCKY, MOUNTAIN, CRYSTALLINE, POWDER, BLOB, PASTE,
		TAPERING_ROD, HAIR_MASS, THIN_STRAND, PILLOW, MINUS_EIGHT, PELVIS, BRAIN, HEART, SKULL, SHORT_NECK, STRINGY,
		LIQUID, FORMLESS;

		@Override
		public String getName() {
			return name();
		}
	}

	public static interface IColor {
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
		LIGHT_TURQUOISE(0x40E0D0), TEAL(0, 128, 128), INDIGO(0x3f0fb7), COLORLESS(null), COSMIC(null), MYSTIC(null),
		UNNATURAL(null), ABNORMAL(null), ELDRITCH(null), UNEARTHLY(null), OTHERWORLDLY(null), INEFFABLE(null),
		CELESTIAL(null);

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
			return name();
		}
	}

	@Override
	public IMemeType getMemeType() {
		return MemeType.SENSE_PROPERTY;
	}

}
