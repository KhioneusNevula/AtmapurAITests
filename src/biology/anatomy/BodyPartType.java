package biology.anatomy;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import actor.IPartAbility;
import biology.systems.types.ISensor;
import mind.concepts.type.SenseProperty;
import mind.concepts.type.SenseProperty.BasicShape;
import mind.concepts.type.SenseProperty.BasicSound;
import mind.concepts.type.SenseProperty.BasicTexture;

public class BodyPartType implements IBodyPartType, Cloneable {
	public static final BodyPartType BODY = part("body", 0.4f).setRoot(true)
			.setTissueTags("muscle", "skin", "fat", "nerves").setSensableProperties(Map.of(SenseProperty.SHAPE,
					SenseProperty.BasicShape.BODY, SenseProperty.TEXTURE, BasicTexture.SMOOTH));
	public static final BodyPartType SPINE = part("spine", 0.4f).setParent(BODY).setSurrounding(BODY)
			/* .setHasBlood(false) */.setFace(Face.BACK).setTissueTags("bone").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.ROD, SenseProperty.TEXTURE, BasicTexture.BUMPY));
	public static final BodyPartType RIBCAGE = part("ribcage", 0.2f).setParent(SPINE).setSurrounding(BODY)
			/* .setHasBlood(false) */.setTissueTags("bone").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.RIBCAGE, SenseProperty.TEXTURE, BasicTexture.HARD));
	public static final BodyPartType CHEST = part("chest", 0.2f).setParent(BODY).setFace(Face.FRONT)
			.setTissueTags("muscle", "skin", "hair", "fat").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.PILLOW, SenseProperty.TEXTURE, BasicTexture.FIRM));
	public static final BodyPartType LEFT_BREAST = part("left_breast", 0.2f).setParent(CHEST).setFace(Face.FRONT)
			.setCategory("breast").setTissueTags("muscle", "skin", "fat").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.OVOID, SenseProperty.TEXTURE, BasicTexture.FIRM));
	public static final BodyPartType RIGHT_BREAST = LEFT_BREAST.thatIsOnRight().setName("right_breast");
	public static final BodyPartType COLLARBONE = part("collarbone", 0.09f).setParent(RIBCAGE).setSurrounding(BODY)
			/* .setHasBlood(false) */.setTissueTags("bone").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.PLANK, SenseProperty.TEXTURE, BasicTexture.HARD));
	public static final BodyPartType HEART = part("heart", 0.05f).setParent(BODY).setSurrounding(RIBCAGE)
			.setAbilities(Abilities.PUMP_LIFE_ESSENCE).setTissueTags("muscle")
			.setSensableProperties(Map.of(SenseProperty.SHAPE, BasicShape.HEART, SenseProperty.TEXTURE,
					BasicTexture.SQUISHY, SenseProperty.SOUND, BasicSound.HEARTBEAT));
	public static final BodyPartType LEFT_LUNG = part("left_lung", 0.15f).setParent(BODY).setSurrounding(RIBCAGE)
			.setSide(Side.LEFT).setCategory("lung").setTissueTags("muscle").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.PILLOW, SenseProperty.TEXTURE, BasicTexture.SQUISHY));
	public static final BodyPartType RIGHT_LUNG = LEFT_LUNG.thatIsOnRight().setName("right_lung");
	public static final BodyPartType STOMACH = part("stomach", 0.07f).setParent(BODY).setSurrounding(RIBCAGE)
			.setTissueTags("muscle").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.OVOID, SenseProperty.TEXTURE, BasicTexture.SQUISHY));
	public static final BodyPartType GUTS = part("guts", 0.25f).setParent(BODY).setSurrounding(BODY)
			.setTissueTags("muscle").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.STRINGY, SenseProperty.TEXTURE, BasicTexture.SQUISHY));
	public static final BodyPartType ABDOMEN = part("abdomen", 0.25f).setParent(BODY).setFace(Face.FRONT)
			.setTissueTags("muscle", "skin", "hair", "fat").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.PILLOW, SenseProperty.TEXTURE, BasicTexture.FIRM));
	public static final BodyPartType PELVIS = part("pelvis", 0.08f).setParent(SPINE).setSurrounding(BODY)
			/* .setHasBlood(false) */.setTissueTags("bone").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.PELVIS, SenseProperty.TEXTURE, BasicTexture.HARD));
	public static final BodyPartType JUNK = part("junk", 0.02f).setParent(BODY)
			.setAbilities(Abilities.FERTILIZE, Abilities.STORE_SEED).setFace(Face.FRONT)
			.setTissueTags("muscle", "skin", "fat").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.MINUS_EIGHT, SenseProperty.TEXTURE, BasicTexture.SQUISHY));
	public static final BodyPartType LEFT_BUTTOCK = part("left_buttock", 0.1f).setParent(BODY).setHeight(Height.BELOW)
			.setFace(Face.BACK).setTissueTags("muscle", "fat", "hair", "skin").setCategory("buttock")
			.setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.PILLOW, SenseProperty.TEXTURE, BasicTexture.SQUISHY));
	public static final BodyPartType RIGHT_BUTTOCK = LEFT_BUTTOCK.thatIsOnRight().setName("right_buttock");
	public static final BodyPartType LEFT_OVARY = part("left_ovary", 0.02f).setParent(BODY).setSurrounding(BODY)
			.setAbilities(Abilities.STORE_EGGS).setSide(Side.LEFT).setCategory("ovary").setTissueTags("muscle");
	public static final BodyPartType RIGHT_OVARY = LEFT_OVARY.thatIsOnRight().setName("right_ovary");
	public static final BodyPartType BIRTHING_CANAL = part("birthing_canal", 0.02f).setParent(BODY).setSurrounding(BODY)
			.setHeight(Height.BELOW).setHole(true).setAbilities(Abilities.GIVE_BIRTH).setTissueTags("muscle")
			.setSensableProperties(Map.of(SenseProperty.SHAPE, BasicShape.ORIFICE));
	public static final BodyPartType WOMB = part("womb", 0.2f).setParent(BODY).setSurrounding(BODY)
			.setAbilities(Abilities.GESTATE).setTissueTags("muscle").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.OVOID, SenseProperty.TEXTURE, BasicTexture.SQUISHY));
	// legs
	public static final BodyPartType LEFT_LEG = part("left_leg", 0.4f).setParent(BODY).setHeight(Height.BELOW)
			.setSide(Side.LEFT).setCategory("leg").setTissueTags("muscle", "skin", "hair", "fat")
			.setAbilities(Abilities.WALK).setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.LIMB, SenseProperty.TEXTURE, BasicTexture.SMOOTH));
	public static final BodyPartType LEFT_LEG_BONE = LEFT_LEG.makeBones(PELVIS);
	public static final BodyPartType RIGHT_LEG = LEFT_LEG.thatIsOnRight().setName("right_leg");
	public static final BodyPartType RIGHT_LEG_BONE = RIGHT_LEG.makeBones(PELVIS);
	public static final BodyPartType LEFT_FOOT = part("left_foot", 0.07f).setParent(LEFT_LEG).setHeight(Height.BELOW)
			.setSide(Side.LEFT).setCategory("foot").setTissueTags("muscle", "skin", "hair", "fat")
			.setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.FOOT, SenseProperty.TEXTURE, BasicTexture.SMOOTH));
	public static final BodyPartType RIGHT_FOOT = LEFT_FOOT.thatIsOnRight().setName("right_foot");
	public static final BodyPartType LEFT_FOOT_BONE = LEFT_FOOT.makeBones(LEFT_LEG_BONE);
	public static final BodyPartType RIGHT_FOOT_BONE = RIGHT_FOOT.makeBones(RIGHT_LEG_BONE);
	// arms
	public static final BodyPartType LEFT_ARM = part("left_arm", 0.4f).setParent(BODY).setSide(Side.LEFT)
			.setCategory("arm").setTissueTags("muscle", "skin", "hair", "fat").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.LIMB, SenseProperty.TEXTURE, BasicTexture.SMOOTH));
	public static final BodyPartType LEFT_ARM_BONE = LEFT_ARM.makeBones(RIBCAGE);
	public static final BodyPartType RIGHT_ARM = LEFT_ARM.thatIsOnRight().setName("right_arm");
	public static final BodyPartType RIGHT_ARM_BONE = RIGHT_ARM.makeBones(RIBCAGE);
	public static final BodyPartType LEFT_HAND = part("left_hand", 0.07f).setParent(LEFT_ARM).setHeight(Height.BELOW)
			.setSide(Side.LEFT).setCategory("hand").setTissueTags("muscle", "skin", "hair", "fat")
			.setAbilities(Abilities.GRASP).setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.HAND, SenseProperty.TEXTURE, BasicTexture.SMOOTH));
	public static final BodyPartType RIGHT_HAND = LEFT_HAND.thatIsOnRight().setName("right_hand");
	public static final BodyPartType LEFT_HAND_BONE = LEFT_HAND.makeBones(LEFT_ARM_BONE);
	public static final BodyPartType RIGHT_HAND_BONE = RIGHT_HAND.makeBones(RIGHT_ARM_BONE);
	// head
	public static final BodyPartType NECK = part("neck", 0.03f).setParent(BODY).setHeight(Height.ABOVE)
			.setTissueTags("muscle", "skin", "hair", "fat").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.SHORT_NECK, SenseProperty.TEXTURE, BasicTexture.FIRM));
	public static final BodyPartType THROAT = part("throat", 0.03f).setParent(BODY).setHeight(Height.ABOVE)
			.setSurrounding(NECK).setTissueTags("muscle", "fat").setSensableProperties(Map.of(SenseProperty.SHAPE,
					BasicShape.SHORT_NECK, SenseProperty.TEXTURE, BasicTexture.CARTILAGIOUS));
	public static final BodyPartType HEAD = part("head", 0.1f).setParent(NECK).setHeight(Height.ABOVE)
			.setTissueTags("muscle", "skin", "hair", "fat").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.HEAD, SenseProperty.TEXTURE, BasicTexture.SMOOTH));
	public static final BodyPartType SKULL = HEAD.makeBones(SPINE).setName("skull")
			.addSensableProperties(Map.of(SenseProperty.SHAPE, BasicShape.SKULL));
	public static final BodyPartType BRAIN = part("brain", 0.08f).setParent(HEAD).setSurrounding(SKULL)
			/* .setHasNerves(false) */.setAbilities(Abilities.THINK).setTissueTags("gray_matter", "white_matter")
			.setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.BRAIN, SenseProperty.TEXTURE, BasicTexture.SQUISHY));
	public static final BodyPartType SCALP = part("scalp", 0.04f).setParent(HEAD).setHeight(Height.ABOVE)
			.setTissueTags("muscle", "skin", "fat").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.BOWL, SenseProperty.TEXTURE, BasicTexture.SQUISHY));
	public static final BodyPartType HAIR = part("hair", 0.04f).setParent(SCALP).setHeight(Height.ABOVE)
			.setTissueTags("hair").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.HAIR_MASS, SenseProperty.TEXTURE, BasicTexture.SILKY));
	public static final BodyPartType FACE = part("face", 0.1f).setParent(HEAD).setFace(Face.FRONT)
			.setTissueTags("muscle", "skin", "fat").setSensableProperties(Map.of(SenseProperty.SHAPE, BasicShape.FACE,
					SenseProperty.TEXTURE, BasicTexture.SMOOTH, SenseProperty.SIGNATURE_SHAPE, UUID.randomUUID()));
	public static final BodyPartType LEFT_EYE = part("left_eye", 0.01f).setParent(FACE).setSide(Side.LEFT)
			.setFace(Face.FRONT).setCategory("eye").setTissueTags("eye_fluid", "eye_muscle").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.SPHERE, SenseProperty.TEXTURE, BasicTexture.SQUISHY));
	public static final BodyPartType RIGHT_EYE = LEFT_EYE.thatIsOnRight().setName("right_eye");
	public static final BodyPartType MOUTH = part("mouth", 0.04f).setParent(FACE).setFace(Face.FRONT).setHole(true)
			.setTissueTags("muscle").setSensableProperties(Map.of(SenseProperty.SHAPE, BasicShape.ORIFICE))
			.setAbilities(Abilities.EAT);
	public static final BodyPartType TOOTH = part("tooth", 0.005f).setParent(SKULL).setSurrounding(MOUTH)
			/* .setHasBlood(false) */.setCount(32).setTissueTags("bone").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.TOOTH, SenseProperty.TEXTURE, BasicTexture.HARD));
	public static final BodyPartType TONGUE = part("tongue", 0.03f).setParent(MOUTH).setSurrounding(MOUTH)
			.setTissueTags("muscle")
			.setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.BLOB, SenseProperty.TEXTURE, BasicTexture.SQUISHY))
			.setAbilities(Abilities.SPEAK);
	public static final BodyPartType JAWBONE = part("jawbone", 0.06f).setParent(SKULL).setSurrounding(HEAD)
			/* .setHasBlood(false) */.setTissueTags("bone").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.BRACKET, SenseProperty.TEXTURE, BasicTexture.HARD));
	public static final BodyPartType NOSE = part("nose", 0.02f).setParent(FACE).setFace(Face.FRONT)
			.setTissueTags("cartilage", "skin", "muscle", "fat").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.NOSE, SenseProperty.TEXTURE, BasicTexture.CARTILAGIOUS));
	public static final BodyPartType LEFT_NOSTRIL = part("left_nostril", 0.002f).setParent(NOSE).setSide(Side.LEFT)
			.setHole(true).setHeight(Height.BELOW).setCategory("nostril").setTissueTags("hair")
			.setSensableProperties(Map.of(SenseProperty.SHAPE, BasicShape.ORIFICE));
	public static final BodyPartType RIGHT_NOSTRIL = LEFT_NOSTRIL.thatIsOnRight().setName("right_nostril");
	public static final BodyPartType MOUSTACHE = part("moustache", 0.01f).setParent(FACE).setFace(Face.FRONT)
			.setTissueTags("hair").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.MOUSTACHE, SenseProperty.TEXTURE, BasicTexture.SILKY));
	public static final BodyPartType BEARD = part("beard", 0.01f).setParent(FACE).setFace(Face.FRONT)
			.setTissueTags("hair").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.BEARD, SenseProperty.TEXTURE, BasicTexture.SILKY));
	public static final BodyPartType LEFT_EAR = part("left_ear", 0.03f).setParent(HEAD).setSide(Side.LEFT)
			.setCategory("ear").setTissueTags("cartilage", "skin", "fat").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.EAR, SenseProperty.TEXTURE, BasicTexture.CARTILAGIOUS));
	public static final BodyPartType LEFT_EAR_CANAL = part("left_ear_canal", 0.005f).setParent(LEFT_EAR)
			.setSide(Side.LEFT).setHole(true).setCategory("ear_canal").setTissueTags("hair")
			.setSensableProperties(Map.of(SenseProperty.SHAPE, BasicShape.ORIFICE));
	public static final BodyPartType RIGHT_EAR = LEFT_EAR.thatIsOnRight().setName("right_ear");
	public static final BodyPartType RIGHT_EAR_CANAL = LEFT_EAR_CANAL.thatIsOnRight().setName("right_ear_canal")
			.setParent(RIGHT_EAR);
	/*
	 * some fun little nonhuman features
	 */
	public static final BodyPartType WHISKER = part("whisker", 0.0001f).setParent(FACE).setFace(Face.FRONT)
			.setTissueTags("whisker_hair").setCount(10).setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.THIN_STRAND, SenseProperty.TEXTURE, BasicTexture.SILKY));
	public static final BodyPartType TAIL = part("tail", 0.4f).setParent(BODY).setAbilities(Abilities.PREHENSILE)
			.setFace(Face.BACK).setTissueTags("muscle", "skin", "fat", "hair").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.TAIL, SenseProperty.TEXTURE, BasicTexture.SMOOTH));
	public static final BodyPartType TAILBONE = TAIL.makeBones(PELVIS).setName("tailbone")
			.setSensableProperties(Map.of(SenseProperty.SHAPE, BasicShape.TAIL));
	/**
	 * Give this a bat wing shape or a feathery wing shape, whatever
	 */
	public static final BodyPartType LEFT_WING = part("left_wing", 0.3f).setParent(BODY).setFace(Face.BACK)
			.setSide(Side.LEFT).setCategory("wing").setTissueTags("muscle", "skin", "fat", "hair")
			.setAbilities(Abilities.FLY);
	public static final BodyPartType RIGHT_WING = LEFT_WING.thatIsOnRight().setName("right_wing");
	public static final BodyPartType LEFT_WING_BONE = LEFT_WING.makeBones(RIBCAGE);
	public static final BodyPartType RIGHT_WING_BONE = RIGHT_WING.makeBones(RIBCAGE);
	/** set "Body's "surrounding" to this in order to use it! */
	public static final BodyPartType SHELL = part("shell", 0.5f).setParent(BODY)/* .setHasBlood(false) */
			.setTissueTags("shell").setSensableProperties(Map.of(SenseProperty.SHAPE, BasicShape.BOWL));
	public static final BodyPartType LEFT_HORN = part("left_horn", 0.1f).setParent(SKULL).setHeight(Height.ABOVE)
			.setUncovered(true)/* .setHasBlood(false) */.setUncovered(true).setCategory("horn").setTissueTags("bone");
	public static final BodyPartType RIGHT_HORN = LEFT_HORN.thatIsOnRight().setName("right_horn");
	public static final BodyPartType LEFT_ANTENNA = part("left_antenna", 0.1f).setAbilities(Abilities.PREHENSILE)
			.setSide(Side.LEFT).setCategory("antenna").setTissueTags("muscle", "fat", "skin")
			.setSensableProperties(Map.of(SenseProperty.SHAPE, BasicShape.THIN_STRAND));
	public static final BodyPartType RIGHT_ANTENNA = LEFT_ANTENNA.thatIsOnRight().setName("right_antenna");
	public static final BodyPartType TENTACLE = part("tentacle", 0.6f).setParent(BODY)
			.setAbilities(Abilities.PREHENSILE, Abilities.GRASP).setCount(8).setTissueTags("muscle", "fat", "skin")
			.setSensableProperties(Map.of(SenseProperty.SHAPE, BasicShape.TAPERING_ROD));
	public static final BodyPartType SUCKER = part("sucker", 0.004f).setParent(TENTACLE).setCount(20)
			.setTissueTags("muscle", "fat", "sucker")
			.setSensableProperties(Map.of(SenseProperty.SHAPE, BasicShape.BOWL));

	private String name;
	private String parent;
	private String surrounding;
	private boolean isHole = false;
	private boolean isRoot = false;
	private Set<String> tissueTags = Set.of();
	private Set<IPartAbility> abilities = Set.of();
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
	private Map<SenseProperty<?>, Object> sensables = Map.of();
	private Multimap<ISensor, SenseProperty<?>> potentialSenses = ImmutableMultimap.of();

	private static BodyPartType part(String name, float size) {
		return new BodyPartType(name, size);
	}

	private BodyPartType(String name, float size) {
		this.name = name;
	}

	/**
	 * makes a bone body part for the given basic body part, using the second
	 * argument as the parent (e.g. a leg bone is parented to a pelvis). <br>
	 * Named to: <part name>_bone <br>
	 * Categorized as: <category_name>_bone
	 * 
	 * @param other
	 * @param parent
	 * @return
	 */
	public BodyPartType makeBones(IBodyPartType parent) {
		BodyPartType bone = this.copy().setSurrounding(this).setParent(parent).renamedTo(this.name + "_bone")
				.setTissueTags("bone").addSensableProperties(Map.of(SenseProperty.TEXTURE, BasicTexture.HARD));
		if (this.category() != null)
			bone.setCategory(category + "_bone");
		return bone;
	}

	public BodyPartType withCategory(String cat) {
		return this.copy().setCategory(cat);
	}

	public BodyPartType butWithoutCategory() {
		return this.copy().setCategory(null);
	}

	public BodyPartType thatCan(IPartAbility... abs) {
		return thatCan(Set.of(abs));
	}

	public BodyPartType thatCan(Collection<IPartAbility> abs) {
		Set<IPartAbility> set = new HashSet<>(abilities);
		set.addAll(abs);
		return this.copy().setAbilities(set);
	}

	public BodyPartType thatCannot(Collection<IPartAbility> abs) {
		Set<IPartAbility> set = new HashSet<>(abilities);
		set.removeAll(Set.copyOf(abs));
		return this.copy().setAbilities(Set.copyOf(set));
	}

	public BodyPartType thatCannot(IPartAbility... abs) {
		Set<IPartAbility> set = new HashSet<>(abilities);
		set.removeAll(Set.of(abs));
		return this.copy().setAbilities(Set.copyOf(set));

	}

	public BodyPartType withoutSensableProperties(Collection<SenseProperty<?>> props) {
		Map<SenseProperty<?>, Object> sebs = new TreeMap<>(this.sensables);
		for (SenseProperty<?> prop : props) {
			sebs.remove(prop);
		}
		return this.copy().setSensableProperties(sebs);
	}

	public BodyPartType withSensableProperties(Map<SenseProperty<?>, Object> map) {
		Map<SenseProperty<?>, Object> mapa = new TreeMap<>(sensables);
		mapa.putAll(map);
		return this.copy().setSensableProperties(mapa);
	}

	public BodyPartType withTissueTags(Collection<String> tags) {
		Set<String> taga = new TreeSet<>(tissueTags);
		taga.addAll(tags);
		return this.copy().setTissueTags(taga);
	}

	public BodyPartType withTissueTags(String... tags) {

		return withTissueTags(Set.of(tags));
	}

	public BodyPartType withoutTissueTags(String... tags) {
		Set<String> set = new TreeSet<>(tissueTags);
		set.removeAll(Set.of(tags));
		return this.copy().setTissueTags(Set.copyOf(set));
	}

	public BodyPartType withoutTissueTags(Collection<String> tags) {
		Set<String> set = new TreeSet<>(tissueTags);
		set.removeAll(tags);
		return this.copy().setTissueTags(Set.copyOf(set));
	}

	public BodyPartType thatIsOnLeft() {
		return this.copy().setSide(Side.LEFT);
	}

	public BodyPartType thatIsOnRight() {
		return this.copy().setSide(Side.RIGHT);
	}

	public BodyPartType butNeitherLeftNorRight() {
		return this.copy().setSide(Side.MIDDLE);
	}

	public BodyPartType thatIsAbove() {
		return this.copy().setHeight(Height.ABOVE);
	}

	public BodyPartType thatIsBelow() {
		return this.copy().setHeight(Height.BELOW);
	}

	public BodyPartType butNeitherAboveNorBelow() {
		return this.copy().setHeight(Height.ALIGNED);
	}

	public BodyPartType thatIsInFront() {
		return this.copy().setFace(Face.FRONT);
	}

	public BodyPartType thatIsInBack() {
		return this.copy().setFace(Face.BACK);
	}

	public BodyPartType butNeitherFrontNorBack() {
		return this.copy().setFace(Face.MIDDLE);
	}

	public BodyPartType resizedTo(float newSize) {
		return this.copy().setSize(newSize);
	}

	public BodyPartType renamedTo(String newName) {
		return this.copy().setName(newName);
	}

	/**
	 * Makes this body part NOT a root
	 * 
	 * @return
	 */
	public BodyPartType butNotRoot() {
		return this.copy().setRoot(false);
	}

	/**
	 * Returns a clone of this body part with the given parent; also makes it no
	 * longer the root (if it previously was)
	 * 
	 * @param parent
	 * @return
	 */
	public BodyPartType thatHasParent(IBodyPartType parent) {
		return this.copy().setRoot(false).setParent(parent);
	}

	public BodyPartType thatHasParent(String parent) {
		return this.copy().setRoot(false).setParent(parent);
	}

	/**
	 * returns copy of this body part with no parent
	 * 
	 * @return
	 */
	public BodyPartType butWithoutParent() {
		return this.copy().setParent((String) null);
	}

	/**
	 * Returns copy of part with surrounding part removed
	 * 
	 * @return
	 */
	public BodyPartType butWithoutSurrounding() {
		return this.copy().setSurrounding((String) null);
	}

	/**
	 * returns copy of this body part as the root
	 * 
	 * @return
	 */
	public BodyPartType thatIsRoot() {
		if (this.parent != null)
			throw new IllegalStateException("Body part with parent cannot become root");
		return this.copy().setRoot(true);
	}

	/**
	 * Returns a clone of this body part with the given surrounding part
	 * 
	 * @param surroundings
	 * @return
	 */
	public BodyPartType thatHasSurrounding(IBodyPartType surroundings) {
		return this.copy().setSurrounding(surroundings);
	}

	public BodyPartType thatHasSurrounding(String surroundings) {
		return this.copy().setSurrounding(surroundings);
	}

	/**
	 * Return copy of body part as a hole instead of a protrusion
	 * 
	 * @return
	 */
	public BodyPartType thatIsHole() {
		return this.copy().setHole(true);
	}

	/**
	 * Return copy of body part as a non-hole
	 * 
	 * @return
	 */
	public BodyPartType butNotHole() {
		return this.copy().setHole(false);
	}

	public BodyPartType thatIsUncovered() {
		return this.copy().setUncovered(true);
	}

	public BodyPartType butNotUncovered() {
		return this.copy().setUncovered(false);
	}

	/**
	 * Sets the number of this body part
	 * 
	 * @param count
	 * @return
	 */
	public BodyPartType ofNumber(int count) {
		return this.copy().setCount(count);
	}

	/**
	 * Sets the number of this body part to 1
	 * 
	 * @return
	 */
	public BodyPartType thatIsSingular() {
		return this.copy().setCount(1);
	}
	/*
	 * private BasicBodyParts setEats(boolean eats) { this.eats = eats; return this;
	 * }
	 */

	private BodyPartType setCategory(String cat) {
		this.category = cat;
		return this;
	}

	private BodyPartType setSize(float size) {
		this.size = size;
		return this;
	}

	private BodyPartType setName(String name) {
		this.name = name;
		return this;
	}
	/*
	 * private BasicBodyPart setHasBlood(boolean hasBlood) { this.hasBlood =
	 * hasBlood; return this; }
	 */

	/*
	 * private BasicBodyPart setHasNerves(boolean hasNerves) { this.hasNerves =
	 * hasNerves; return this; }
	 */

	private BodyPartType setHole(boolean isHole) {
		this.isHole = isHole;
		return this;
	}

	private BodyPartType setParent(IBodyPartType parent) {
		this.parent = parent.getName();
		return this;
	}

	private BodyPartType setParent(String parent) {
		this.parent = parent;
		return this;
	}

	private BodyPartType setSurrounding(IBodyPartType surrounding) {
		this.surrounding = surrounding.getName();
		return this;
	}

	private BodyPartType setSurrounding(String surrounding) {
		this.surrounding = surrounding;
		return this;
	}

	private BodyPartType setRoot(boolean isRoot) {
		this.isRoot = isRoot;
		return this;
	}
	/*
	 * private BodyPartType setPumpsBlood(boolean pumpsBlood) { this.pumpsBlood =
	 * pumpsBlood; return this; } private BodyPartType setThinks(boolean thinks) {
	 * this.thinks = thinks; return this; }
	 * 
	 * 
	 * private BodyPartType setFertilizes(boolean a) { this.fertilizes = a; return
	 * this; }
	 * 
	 * private BodyPartType setStoresSeed(boolean a) { this.storesSeed = a; return
	 * this; }
	 * 
	 * private BodyPartType setStoresEggs(boolean a) { this.storesEggs = a; return
	 * this; }
	 * 
	 * private BodyPartType setGestates(boolean a) { this.gestates = a; return this;
	 * } private BodyPartType setGivesBirth(boolean a) { this.givesBirth = a; return
	 * this; }
	 * 
	 * private BodyPartType setPrehensile(boolean a) { this.prehensile = a; return
	 * this; }
	 */

	private BodyPartType setUncovered(boolean a) {
		this.uncovered = a;
		return this;
	}

	/*
	 * private BasicBodyParts setDigests(boolean a) { this.digests = a; return this;
	 * }
	 */

	private BodyPartType setFace(Face f) {
		this.face = f;
		return this;
	}

	private BodyPartType setSide(Side s) {
		this.side = s;
		return this;
	}

	private BodyPartType setHeight(Height h) {
		this.height = h;
		return this;
	}

	private BodyPartType setCount(int count) {
		if (count <= 0)
			throw new IllegalArgumentException("invalid count: " + count);
		this.count = count;
		return this;
	}

	private BodyPartType setTissueTags(String... tags) {
		this.tissueTags = Set.of(tags);
		return this;
	}

	/**
	 * Obviously, should be an immutable set
	 * 
	 * @param tags
	 * @return
	 */
	private BodyPartType setTissueTags(Set<String> tags) {
		this.tissueTags = ImmutableSet.copyOf(tags);
		return this;
	}

	private BodyPartType setAbilities(IPartAbility... abs) {
		return setAbilities(Set.of(abs));
	}

	private BodyPartType setAbilities(Set<IPartAbility> abs) {
		this.abilities = ImmutableSet.copyOf(abs);
		return this;
	}

	private BodyPartType addSensableProperties(Map<SenseProperty<?>, Object> map) {
		map = new TreeMap<>(sensables);
		map.putAll(map);
		return this.setSensableProperties(map);
	}

	private BodyPartType setSensableProperties(Map<SenseProperty<?>, Object> map) {
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
	public String getName() {
		return name;
	}

	@Override
	public String getParent() {
		return parent;
	}

	@Override
	public String getSurroundingPart() {
		return surrounding;
	}

	@Override
	public boolean isRoot() {
		return isRoot;
	}

	/*
	 * @Override public boolean eats() { return eats; }
	 */
	/*
	 * @Override public boolean hasLifeEssence() { return hasBlood; }
	 */

	/*
	 * @Override public boolean hasNerves() { return hasNerves; }
	 */

	@Override
	public boolean isHole() {
		return isHole;
	}

	@Override
	public boolean pumpsLifeEssence() {
		return this.hasAbility(Abilities.PUMP_LIFE_ESSENCE);
	}

	@Override
	public boolean thinks() {
		return this.hasAbility(Abilities.THINK);
	}

	@Override
	public boolean fertilizes() {
		return this.hasAbility(Abilities.FERTILIZE);
	}

	@Override
	public boolean gestates() {
		return this.hasAbility(Abilities.GESTATE);
	}

	@Override
	public boolean givesBirth() {
		return this.hasAbility(Abilities.GIVE_BIRTH);
	}

	@Override
	public boolean storesEggs() {
		return this.hasAbility(Abilities.STORE_EGGS);
	}

	@Override
	public boolean storesSeed() {
		return this.hasAbility(Abilities.STORE_SEED);
	}

	public boolean prehensile() {
		return this.hasAbility(Abilities.PREHENSILE);
	}

	/*
	 * @Override public boolean digests() { return digests; }
	 */
	protected BodyPartType copy() {
		BodyPartType clone;
		try {
			clone = (BodyPartType) this.clone();
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
	public Collection<String> tissueTags() {
		return tissueTags;
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
	public Collection<SenseProperty<?>> getSensableProperties(ISensor sensor) {
		return this.potentialSenses.get(sensor);
	}

	@Override
	public <T> T getTrait(SenseProperty<T> prop) {
		return (T) sensables.get(prop);
	}

	@Override
	public Collection<SenseProperty<?>> getSensableProperties() {
		return potentialSenses.values();
	}

	@Override
	public String toString() {
		return "bp_" + this.name;
	}

}
