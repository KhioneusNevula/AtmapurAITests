package biology;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiFunction;

import com.google.common.collect.ImmutableMap;

import actor.Actor;
import actor.construction.physical.IComponentPart;
import actor.construction.physical.IComponentType;
import biology.anatomy.BodyPartType;
import biology.anatomy.IBodyPartType;
import biology.anatomy.ISpecies;
import biology.anatomy.ITissueLayerType;
import biology.anatomy.TissueType;
import civilization_and_minds.mind.mechanics.PersonAgent;
import civilization_and_minds.mind.type.HumanMind;
import civilization_and_minds.social.concepts.profile.Profile;
import civilization_and_minds.social.concepts.profile.ProfileType;
import metaphysical.soul.HumanSoul;
import metaphysical.soul.ISoul;

public class Species implements ISpecies {
	public static final Species FLESHBALL = new Species("Fleshball", TissueType.MUSCLE, BodyPartType.BODY, 0f);

	private static final HumanSoul genSoulHuman(Actor act, IComponentPart part) {
		PersonAgent ag = new PersonAgent(act.getUUID(), act.getObjectType(), act.getSimpleName());
		Profile prof = new Profile(act.getUUID(), ProfileType.INDIVIDUAL);
		HumanMind mind = new HumanMind(prof, ag);
		HumanSoul soul = new HumanSoul(act.getUUID(), mind);
		soul.tetherSpirit(act, Collections.singleton(part));
		return soul;
	}

	public static final Species HUMAN = new Species("Human",
			Set.of(TissueType.BONE, TissueType.BLOOD, TissueType.MUSCLE, TissueType.SKIN, TissueType.FAT,
					TissueType.HAIR, TissueType.NERVES, TissueType.GRAY_MATTER, TissueType.WHITE_MATTER,
					TissueType.EYE_FLUID, TissueType.EYE_MUSCLE, TissueType.CARTILAGE),
			Set.of(BodyPartType.BODY, BodyPartType.BRAIN, BodyPartType.SPINE, BodyPartType.RIBCAGE, BodyPartType.CHEST,
					BodyPartType.COLLARBONE, BodyPartType.HEART, BodyPartType.LEFT_LUNG, BodyPartType.RIGHT_LUNG,
					BodyPartType.STOMACH, BodyPartType.GUTS, BodyPartType.ABDOMEN, BodyPartType.PELVIS,
					BodyPartType.JUNK, BodyPartType.LEFT_BUTTOCK, BodyPartType.RIGHT_BUTTOCK, BodyPartType.LEFT_OVARY,
					BodyPartType.RIGHT_OVARY, BodyPartType.BIRTHING, BodyPartType.WOMB, BodyPartType.LEFT_LEG,
					BodyPartType.LEFT_LEG_BONE, BodyPartType.RIGHT_LEG, BodyPartType.RIGHT_LEG_BONE,
					BodyPartType.LEFT_FOOT, BodyPartType.LEFT_FOOT_BONE, BodyPartType.RIGHT_FOOT,
					BodyPartType.RIGHT_FOOT_BONE, BodyPartType.LEFT_ARM, BodyPartType.LEFT_ARM_BONE,
					BodyPartType.RIGHT_ARM, BodyPartType.RIGHT_ARM_BONE, BodyPartType.LEFT_HAND,
					BodyPartType.LEFT_HAND_BONE, BodyPartType.RIGHT_HAND, BodyPartType.RIGHT_HAND_BONE,
					BodyPartType.NECK, BodyPartType.THROAT, BodyPartType.HEAD, BodyPartType.SKULL, BodyPartType.SCALP,
					BodyPartType.HAIR, BodyPartType.FACE, BodyPartType.LEFT_EYE, BodyPartType.RIGHT_EYE,
					BodyPartType.MOUTH, BodyPartType.TOOTH, BodyPartType.TONGUE, BodyPartType.JAWBONE,
					BodyPartType.NOSE, BodyPartType.LEFT_NOSTRIL, BodyPartType.RIGHT_NOSTRIL, BodyPartType.MOUSTACHE,
					BodyPartType.BEARD, BodyPartType.LEFT_EAR, BodyPartType.LEFT_EAR_CANAL, BodyPartType.RIGHT_EAR,
					BodyPartType.RIGHT_EAR_CANAL),
			0.5f).addSoulGen(Species::genSoulHuman);

	public static final Species ELF = new Species(HUMAN, "Elf", Set.of(), Set.of(), Set.of(),
			Set.of("moustache", "beard"), 0.5f);
	public static final Species FAIRY = new Species(HUMAN, "Fairy", Set.of(TissueType.ESSENCE),
			Set.of(BodyPartType.LEFT_WING, BodyPartType.RIGHT_WING, BodyPartType.LEFT_WING_BONE,
					BodyPartType.RIGHT_WING_BONE, BodyPartType.TAIL, BodyPartType.TAILBONE),
			Set.of("blood"), Set.of(), 0.8f);
	public static final Species IMP = new Species(FAIRY, "Imp", Set.of(),
			Set.of(BodyPartType.LEFT_HORN, BodyPartType.RIGHT_HORN, BodyPartType.TAIL.withoutTissueTags("hair")),
			Set.of(), Set.of("hair", "beard", "moustache"), 0.8f);

	private Map<String, ITissueLayerType> tissue = ImmutableMap.of();
	private Map<String, IBodyPartType> parts = ImmutableMap.of();
	private float averageUniqueness;
	private IBodyPartType singlePart;
	private String name;
	private BiFunction<Actor, IComponentPart, ? extends ISoul> soulgen;

	/**
	 * For single-part entities. e.g. a sentient ball of fur
	 * 
	 * @param tissue
	 * @param part
	 * @param averageUniqueness
	 */
	private Species(String name, ITissueLayerType tissue, IBodyPartType part, float averageUniqueness) {
		this(name, Collections.singleton(tissue), Collections.singleton(part), averageUniqueness);
		this.singlePart = part;
	}

	/**
	 * Copy a previous template and either replace existing bodyparts/tissuelayers
	 * or add new ones
	 * 
	 * @param other
	 * @param tissue
	 * @param parts
	 */
	private Species(Species other, String name, Collection<ITissueLayerType> tissue, Collection<IBodyPartType> parts,
			Collection<String> deleteTissue, Collection<String> deleteParts, float averageUniqueness) {
		this.name = name;
		Map<String, ITissueLayerType> tiss = new TreeMap<>(other.tissueTypes());
		Map<String, IBodyPartType> par = new TreeMap<>(other.partTypes());
		for (String del : deleteTissue)
			tiss.remove(del);
		for (String del : deleteParts)
			par.remove(del);
		Map<String, ITissueLayerType> b = new TreeMap<>(tiss);
		Map<String, IBodyPartType> b2 = new TreeMap<>(par);
		for (ITissueLayerType tl : tissue) {
			if (!deleteTissue.contains(tl.getName())) {

				b.put(tl.getName(), tl);
			}
		}
		for (IBodyPartType pl : parts)
			if (!deleteParts.contains(pl.getName())) {

				b2.put(pl.getName(), pl);
			}

		this.tissue = ImmutableMap.copyOf(b);
		this.parts = ImmutableMap.copyOf(b2);
		this.averageUniqueness = averageUniqueness;
		this.soulgen = other.soulgen;
	}

	private Species(String name, Collection<ITissueLayerType> tissue, Collection<IBodyPartType> parts,
			float averageUniqueness) {
		ImmutableMap.Builder<String, ITissueLayerType> tissueL = ImmutableMap.builder();
		ImmutableMap.Builder<String, IBodyPartType> partL = ImmutableMap.builder();
		this.name = name;
		for (ITissueLayerType tl : tissue)
			tissueL.put(tl.getName(), tl);
		for (IBodyPartType pp : parts)
			partL.put(pp.getName(), pp);
		this.tissue = tissueL.build();
		this.parts = partL.build();
		this.averageUniqueness = averageUniqueness;
	}

	private Species addSoulGen(BiFunction<Actor, IComponentPart, ? extends ISoul> gener) {
		this.soulgen = gener;
		return this;
	}

	@Override
	public float averageUniqueness() {
		return averageUniqueness;
	}

	@Override
	public Map<String, ITissueLayerType> tissueTypes() {
		return tissue;
	}

	@Override
	public Map<String, IBodyPartType> partTypes() {
		return parts;
	}

	@Override
	public String toString() {
		return "species_" + this.name();
	}

	@Override
	public String getUniqueName() {
		return "species_" + this.name();
	}

	@Override
	public int hashCode() {
		return this.getUniqueName().hashCode();
	}

	@Override
	public boolean hasSinglePartType() {
		return this.singlePart != null;
	}

	@Override
	public IComponentType mainComponent() {
		if (!hasSinglePartType())
			throw new UnsupportedOperationException();
		return this.singlePart;
	}

	@Override
	public ISoul generateSoul(Actor a, IComponentPart forPart) {
		return this.soulgen.apply(a, forPart);
	}

	@Override
	public boolean mustBeGivenSoul() {
		return soulgen != null;
	}

	@Override
	public String name() {
		return this.name;
	}

}
