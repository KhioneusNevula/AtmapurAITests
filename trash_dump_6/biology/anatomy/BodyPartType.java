package biology.anatomy;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.collect.ImmutableSet;

import actor.construction.physical.IComponentType;
import actor.construction.physical.IPartAbility;
import actor.construction.properties.SenseProperty;
import actor.construction.properties.SenseProperty.BasicShape;
import actor.construction.properties.SenseProperty.BasicSound;
import actor.construction.properties.SenseProperty.BasicTexture;
import actor.construction.simple.SimplePartType;
import biology.sensing.BasicSenses;

public class BodyPartType extends SimplePartType implements IBodyPartType, Cloneable {
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

	public static final BodyPartType COLLARBONE = part("collarbone", 0.09f).setParent(RIBCAGE).setSurrounding(BODY)
			/* .setHasBlood(false) */.setTissueTags("bone").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.PLANK, SenseProperty.TEXTURE, BasicTexture.HARD));
	public static final BodyPartType HEART = part("heart", 0.05f).setParent(BODY).setSurrounding(RIBCAGE)
			.setAbilities(BodyAbility.PUMP_LIFE_ESSENCE).setTissueTags("muscle")
			.setSensableProperties(Map.of(SenseProperty.SHAPE, BasicShape.HEART, SenseProperty.TEXTURE,
					BasicTexture.SQUISHY, SenseProperty.SOUND, BasicSound.HEARTBEAT));
	public static final BodyPartType LEFT_LUNG = part("left_lung", 0.15f).setParent(BODY).setSurrounding(RIBCAGE)
			.setSide(Side.LEFT).setCategory("lung").setTissueTags("muscle").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.PILLOW, SenseProperty.TEXTURE, BasicTexture.SQUISHY));
	public static final BodyPartType RIGHT_LUNG = LEFT_LUNG.thatIsOnRight().setName("right_lung");
	public static final BodyPartType STOMACH = part("stomach", 0.07f).setParent(BODY).setSurrounding(RIBCAGE)
			.setTissueTags("muscle").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.OVOID, SenseProperty.TEXTURE, BasicTexture.SQUISHY));
	public static final BodyPartType ABDOMEN = part("abdomen", 0.25f).setParent(BODY).setFace(Face.FRONT)
			.setTissueTags("muscle", "skin", "hair", "fat").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.PILLOW, SenseProperty.TEXTURE, BasicTexture.FIRM));
	public static final BodyPartType GUTS = part("guts", 0.25f).setParent(BODY).setSurrounding(ABDOMEN)
			.setTissueTags("muscle").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.STRINGY, SenseProperty.TEXTURE, BasicTexture.SQUISHY));
	public static final BodyPartType PELVIS = part("pelvis", 0.08f).setParent(SPINE).setSurrounding(BODY)
			/* .setHasBlood(false) */.setTissueTags("bone").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.PELVIS, SenseProperty.TEXTURE, BasicTexture.HARD));
	public static final BodyPartType JUNK = part("junk", 0.02f).setParent(BODY)
			.setAbilities(BodyAbility.FERTILIZE, BodyAbility.STORE_SEED).setFace(Face.FRONT)
			.setTissueTags("muscle", "skin", "fat").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.MINUS_EIGHT, SenseProperty.TEXTURE, BasicTexture.SQUISHY));
	public static final BodyPartType LEFT_BUTTOCK = part("left_buttock", 0.1f).setParent(BODY).setHeight(Height.BELOW)
			.setFace(Face.BACK).setTissueTags("muscle", "fat", "hair", "skin").setCategory("buttock")
			.setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.PILLOW, SenseProperty.TEXTURE, BasicTexture.SQUISHY));
	public static final BodyPartType RIGHT_BUTTOCK = LEFT_BUTTOCK.thatIsOnRight().setName("right_buttock");
	public static final BodyPartType LEFT_OVARY = part("left_ovary", 0.02f).setParent(BODY).setSurrounding(BODY)
			.setAbilities(BodyAbility.STORE_EGGS).setSide(Side.LEFT).setCategory("ovary").setTissueTags("muscle");
	public static final BodyPartType RIGHT_OVARY = LEFT_OVARY.thatIsOnRight().setName("right_ovary");
	public static final BodyPartType BIRTHING = part("birthing", 0.02f).setParent(BODY).setSurrounding(BODY)
			.setHeight(Height.BELOW).setHole(true).setAbilities(BodyAbility.GIVE_BIRTH).setTissueTags("muscle")
			.setSensableProperties(Map.of(SenseProperty.SHAPE, BasicShape.ORIFICE));
	public static final BodyPartType WOMB = part("womb", 0.2f).setParent(BODY).setSurrounding(ABDOMEN)
			.setAbilities(BodyAbility.GESTATE).setTissueTags("muscle").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.OVOID, SenseProperty.TEXTURE, BasicTexture.SQUISHY));
	// legs
	public static final BodyPartType LEFT_LEG = part("left_leg", 0.4f).setParent(BODY).setHeight(Height.BELOW)
			.setSide(Side.LEFT).setCategory("leg").setTissueTags("muscle", "skin", "hair", "fat")
			.setAbilities(BodyAbility.WALK).setSensableProperties(
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
			.setAbilities(BodyAbility.GRASP).setSensableProperties(
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
			/* .setHasNerves(false) */.setAbilities(BodyAbility.HAVE_SOUL).setTissueTags("gray_matter", "white_matter")
			.setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.BRAIN, SenseProperty.TEXTURE, BasicTexture.SQUISHY));
	public static final BodyPartType SCALP = part("scalp", 0.04f).setParent(HEAD).setHeight(Height.ABOVE)
			.setTissueTags("muscle", "skin", "fat").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.BOWL, SenseProperty.TEXTURE, BasicTexture.SQUISHY));
	public static final BodyPartType HAIR = part("hair", 0.04f).setParent(SCALP).setHeight(Height.ABOVE)
			.setTissueTags("hair").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.HAIR_MASS, SenseProperty.TEXTURE, BasicTexture.SILKY));
	public static final BodyPartType FACE = part("face", 0.1f).setParent(HEAD).setFace(Face.FRONT)
			.setTissueTags("muscle", "skin", "fat").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.FACE, SenseProperty.TEXTURE, BasicTexture.SMOOTH));
	public static final BodyPartType LEFT_EYE = part("left_eye", 0.01f).setParent(FACE).setSide(Side.LEFT)
			.setFace(Face.FRONT).setCategory("eye").setTissueTags("eye_fluid", "eye_muscle")
			.setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.SPHERE, SenseProperty.TEXTURE, BasicTexture.SQUISHY))
			.setAbilities(BasicSenses.SIGHT);
	public static final BodyPartType RIGHT_EYE = LEFT_EYE.thatIsOnRight().setName("right_eye");
	public static final BodyPartType MOUTH = part("mouth", 0.04f).setParent(FACE).setFace(Face.FRONT).setHole(true)
			.setTissueTags("muscle").setSensableProperties(Map.of(SenseProperty.SHAPE, BasicShape.ORIFICE))
			.setAbilities(BodyAbility.EAT);
	public static final BodyPartType TOOTH = part("tooth", 0.005f).setParent(SKULL).setSurrounding(MOUTH)
			/* .setHasBlood(false) */.setCount(32).setTissueTags("bone").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.TOOTH, SenseProperty.TEXTURE, BasicTexture.HARD));
	public static final BodyPartType TONGUE = part("tongue", 0.03f).setParent(MOUTH).setSurrounding(MOUTH)
			.setTissueTags("muscle")
			.setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.BLOB, SenseProperty.TEXTURE, BasicTexture.SQUISHY))
			.setAbilities(BodyAbility.SPEAK, BasicSenses.TASTE);
	public static final BodyPartType JAWBONE = part("jawbone", 0.06f).setParent(SKULL).setSurrounding(HEAD)
			/* .setHasBlood(false) */.setTissueTags("bone").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.BRACKET, SenseProperty.TEXTURE, BasicTexture.HARD));
	public static final BodyPartType NOSE = part("nose", 0.02f).setParent(FACE).setFace(Face.FRONT)
			.setTissueTags("cartilage", "skin", "muscle", "fat").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.NOSE, SenseProperty.TEXTURE, BasicTexture.CARTILAGIOUS));
	public static final BodyPartType LEFT_NOSTRIL = part("left_nostril", 0.002f).setParent(NOSE).setSide(Side.LEFT)
			.setHole(true).setHeight(Height.BELOW).setCategory("nostril").setTissueTags("hair")
			.setSensableProperties(Map.of(SenseProperty.SHAPE, BasicShape.ORIFICE)).setAbilities(BasicSenses.SMELL);
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
			.setSensableProperties(Map.of(SenseProperty.SHAPE, BasicShape.ORIFICE)).setAbilities(BasicSenses.HEARING);
	public static final BodyPartType RIGHT_EAR = LEFT_EAR.thatIsOnRight().setName("right_ear");
	public static final BodyPartType RIGHT_EAR_CANAL = LEFT_EAR_CANAL.thatIsOnRight().setName("right_ear_canal")
			.setParent(RIGHT_EAR);
	/*
	 * some fun little nonhuman features
	 */
	public static final BodyPartType WHISKER = part("whisker", 0.0001f).setParent(FACE).setFace(Face.FRONT)
			.setTissueTags("whisker_hair").setCount(10).setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.THIN_STRAND, SenseProperty.TEXTURE, BasicTexture.SILKY));
	public static final BodyPartType TAIL = part("tail", 0.4f).setParent(BODY).setAbilities(BodyAbility.PREHENSILE)
			.setFace(Face.BACK).setTissueTags("muscle", "skin", "fat", "hair").setSensableProperties(
					Map.of(SenseProperty.SHAPE, BasicShape.TAIL, SenseProperty.TEXTURE, BasicTexture.SMOOTH));
	public static final BodyPartType TAILBONE = TAIL.makeBones(PELVIS).setName("tailbone")
			.setSensableProperties(Map.of(SenseProperty.SHAPE, BasicShape.TAIL));
	/**
	 * Give this a bat wing shape or a feathery wing shape, whatever
	 */
	public static final BodyPartType LEFT_WING = part("left_wing", 0.3f).setParent(BODY).setFace(Face.BACK)
			.setSide(Side.LEFT).setCategory("wing").setTissueTags("muscle", "skin", "fat", "hair")
			.setAbilities(BodyAbility.FLY);
	public static final BodyPartType RIGHT_WING = LEFT_WING.thatIsOnRight().setName("right_wing");
	public static final BodyPartType LEFT_WING_BONE = LEFT_WING.makeBones(RIBCAGE);
	public static final BodyPartType RIGHT_WING_BONE = RIGHT_WING.makeBones(RIBCAGE);
	/** set "Body's "surrounding" to this in order to use it! */
	public static final BodyPartType SHELL = part("shell", 0.5f).setParent(BODY)/* .setHasBlood(false) */
			.setTissueTags("shell").setSensableProperties(Map.of(SenseProperty.SHAPE, BasicShape.BOWL));
	public static final BodyPartType LEFT_HORN = part("left_horn", 0.1f).setParent(SKULL).setHeight(Height.ABOVE)
			.setUncovered(true)/* .setHasBlood(false) */.setUncovered(true).setCategory("horn").setTissueTags("bone");
	public static final BodyPartType RIGHT_HORN = LEFT_HORN.thatIsOnRight().setName("right_horn");
	public static final BodyPartType LEFT_ANTENNA = part("left_antenna", 0.1f).setAbilities(BodyAbility.PREHENSILE)
			.setSide(Side.LEFT).setCategory("antenna").setTissueTags("muscle", "fat", "skin")
			.setSensableProperties(Map.of(SenseProperty.SHAPE, BasicShape.THIN_STRAND));
	public static final BodyPartType RIGHT_ANTENNA = LEFT_ANTENNA.thatIsOnRight().setName("right_antenna");
	public static final BodyPartType TENTACLE = part("tentacle", 0.6f).setParent(BODY)
			.setAbilities(BodyAbility.PREHENSILE, BodyAbility.GRASP).setCount(8).setTissueTags("muscle", "fat", "skin")
			.setSensableProperties(Map.of(SenseProperty.SHAPE, BasicShape.TAPERING_ROD));
	public static final BodyPartType SUCKER = part("sucker", 0.004f).setParent(TENTACLE).setCount(20)
			.setTissueTags("muscle", "fat", "sucker")
			.setSensableProperties(Map.of(SenseProperty.SHAPE, BasicShape.BOWL));

	private Set<String> tissueTags = Set.of();

	protected static BodyPartType part(String name, float size) {
		return new BodyPartType(name, size);
	}

	private BodyPartType(String name, float size) {
		super(name, size);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BodyPartType tp) {
			return super.equals(obj) && this.tissueTags.equals(tp.tissueTags);
		}
		return false;
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
		BodyPartType bone = this.copy().setSurrounding(this).setParent(parent).renamedTo(this.getName() + "_bone")
				.setTissueTags("bone").addSensableProperties(Map.of(SenseProperty.TEXTURE, BasicTexture.HARD));
		if (this.category() != null)
			bone.setCategory(category() + "_bone");
		return bone;
	}

	public BodyPartType withCategory(String cat) {
		return (BodyPartType) super.withCategory(cat);
	}

	public BodyPartType butWithoutCategory() {
		return (BodyPartType) super.butWithoutCategory();
	}

	public BodyPartType thatCan(IPartAbility... abs) {
		return (BodyPartType) super.thatCan(abs);
	}

	public BodyPartType thatCan(Collection<IPartAbility> abs) {
		return (BodyPartType) super.thatCan(abs);
	}

	public BodyPartType thatCannot(Collection<IPartAbility> abs) {
		return (BodyPartType) super.thatCannot(abs);
	}

	public BodyPartType thatCannot(IPartAbility... abs) {
		return (BodyPartType) super.thatCannot(abs);

	}

	public BodyPartType withoutSensableProperties(Collection<SenseProperty<?>> props) {
		return (BodyPartType) super.withoutSensableProperties(props);
	}

	public BodyPartType withSensableProperties(Map<SenseProperty<?>, Object> map) {
		return (BodyPartType) super.withSensableProperties(map);
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
		return (BodyPartType) super.thatIsOnLeft();
	}

	public BodyPartType thatIsOnRight() {
		return (BodyPartType) super.thatIsOnRight();
	}

	public BodyPartType butNeitherLeftNorRight() {
		return (BodyPartType) super.butNeitherLeftNorRight();
	}

	public BodyPartType thatIsAbove() {
		return (BodyPartType) super.thatIsAbove();
	}

	public BodyPartType thatIsBelow() {
		return (BodyPartType) super.thatIsBelow();
	}

	public BodyPartType butNeitherAboveNorBelow() {
		return (BodyPartType) super.butNeitherAboveNorBelow();
	}

	public BodyPartType thatIsInFront() {
		return (BodyPartType) super.thatIsInFront();
	}

	public BodyPartType thatIsInBack() {
		return (BodyPartType) super.thatIsInBack();
	}

	public BodyPartType butNeitherFrontNorBack() {
		return (BodyPartType) super.butNeitherFrontNorBack();
	}

	public BodyPartType resizedTo(float newSize) {
		return (BodyPartType) super.resizedTo(newSize);
	}

	public BodyPartType renamedTo(String newName) {
		return (BodyPartType) super.renamedTo(newName);
	}

	/**
	 * Makes this body part NOT a root
	 * 
	 * @return
	 */
	public BodyPartType butNotRoot() {
		return (BodyPartType) super.butNotRoot();
	}

	/**
	 * Returns a clone of this body part with the given parent; also makes it no
	 * longer the root (if it previously was)
	 * 
	 * @param parent
	 * @return
	 */
	public BodyPartType thatHasParent(IBodyPartType parent) {
		return (BodyPartType) super.thatHasParent(parent);
	}

	public BodyPartType thatHasParent(String parent) {
		return (BodyPartType) super.thatHasParent(parent);
	}

	/**
	 * returns copy of this body part with no parent
	 * 
	 * @return
	 */
	public BodyPartType butWithoutParent() {
		return (BodyPartType) super.butWithoutParent();
	}

	/**
	 * Returns copy of part with surrounding part removed
	 * 
	 * @return
	 */
	public BodyPartType butWithoutSurrounding() {
		return (BodyPartType) super.butWithoutSurrounding();
	}

	/**
	 * returns copy of this body part as the root
	 * 
	 * @return
	 */
	public BodyPartType thatIsRoot() {
		return (BodyPartType) super.thatIsRoot();
	}

	/**
	 * Returns a clone of this body part with the given surrounding part
	 * 
	 * @param surroundings
	 * @return
	 */
	public BodyPartType thatHasSurrounding(IBodyPartType surroundings) {
		return (BodyPartType) super.thatHasSurrounding(surroundings);
	}

	public BodyPartType thatHasSurrounding(String surroundings) {
		return (BodyPartType) super.thatHasSurrounding(surroundings);
	}

	/**
	 * Return copy of body part as a hole instead of a protrusion
	 * 
	 * @return
	 */
	public BodyPartType thatIsHole() {
		return (BodyPartType) super.thatIsHole();
	}

	/**
	 * Return copy of body part as a non-hole
	 * 
	 * @return
	 */
	public BodyPartType butNotHole() {
		return (BodyPartType) super.butNotHole();
	}

	@Override
	public BodyPartType thatIsUncovered() {
		return (BodyPartType) super.thatIsUncovered();
	}

	@Override
	public BodyPartType butNotUncovered() {
		return (BodyPartType) super.butNotUncovered();
	}

	@Override
	public BodyPartType ofNumber(int count) {
		return (BodyPartType) super.ofNumber(count);
	}

	@Override
	public BodyPartType thatIsSingular() {
		return (BodyPartType) super.thatIsSingular();
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

	@Override
	public boolean pumpsLifeEssence() {
		return this.hasAbility(BodyAbility.PUMP_LIFE_ESSENCE);
	}

	@Override
	public boolean thinks() {
		return this.hasAbility(BodyAbility.HAVE_SOUL);
	}

	@Override
	public boolean fertilizes() {
		return this.hasAbility(BodyAbility.FERTILIZE);
	}

	@Override
	public boolean gestates() {
		return this.hasAbility(BodyAbility.GESTATE);
	}

	@Override
	public boolean givesBirth() {
		return this.hasAbility(BodyAbility.GIVE_BIRTH);
	}

	@Override
	public boolean storesEggs() {
		return this.hasAbility(BodyAbility.STORE_EGGS);
	}

	@Override
	public boolean storesSeed() {
		return this.hasAbility(BodyAbility.STORE_SEED);
	}

	public boolean prehensile() {
		return this.hasAbility(BodyAbility.PREHENSILE);
	}

	@Override
	public Collection<String> tissueTags() {
		return tissueTags;
	}

	@Override
	public String toString() {
		return "bp_" + this.getName();
	}

	@Override
	protected BodyPartType setAbilities(IPartAbility... abs) {

		return (BodyPartType) super.setAbilities(abs);
	}

	@Override
	protected BodyPartType setAbilities(Set<IPartAbility> abs) {

		return (BodyPartType) super.setAbilities(abs);
	}

	@Override
	protected BodyPartType setCategory(String cat) {

		return (BodyPartType) super.setCategory(cat);
	}

	@Override
	protected BodyPartType setCount(int count) {

		return (BodyPartType) super.setCount(count);
	}

	@Override
	protected BodyPartType setFace(Face f) {

		return (BodyPartType) super.setFace(f);
	}

	@Override
	protected BodyPartType setHeight(Height h) {

		return (BodyPartType) super.setHeight(h);
	}

	@Override
	protected BodyPartType setHole(boolean isHole) {

		return (BodyPartType) super.setHole(isHole);
	}

	@Override
	protected BodyPartType setName(String name) {

		return (BodyPartType) super.setName(name);
	}

	@Override
	protected BodyPartType setParent(IComponentType parent) {

		return (BodyPartType) super.setParent(parent);
	}

	@Override
	protected BodyPartType setParent(String parent) {

		return (BodyPartType) super.setParent(parent);
	}

	@Override
	protected BodyPartType setRoot(boolean isRoot) {

		return (BodyPartType) super.setRoot(isRoot);
	}

	@Override
	protected BodyPartType setSensableProperties(Map<SenseProperty<?>, Object> map) {

		return (BodyPartType) super.setSensableProperties(map);
	}

	@Override
	protected BodyPartType setSide(Side s) {

		return (BodyPartType) super.setSide(s);
	}

	@Override
	protected BodyPartType setSize(float size) {

		return (BodyPartType) super.setSize(size);
	}

	@Override
	protected BodyPartType setSurrounding(IComponentType surrounding) {

		return (BodyPartType) super.setSurrounding(surrounding);
	}

	@Override
	protected BodyPartType setSurrounding(String surrounding) {

		return (BodyPartType) super.setSurrounding(surrounding);
	}

	@Override
	protected BodyPartType setUncovered(boolean a) {

		return (BodyPartType) super.setUncovered(a);
	}

	@Override
	protected BodyPartType copy() {
		return (BodyPartType) super.copy();
	}

	@Override
	protected BodyPartType addSensableProperties(Map<SenseProperty<?>, Object> map) {

		return (BodyPartType) super.addSensableProperties(map);
	}

}
