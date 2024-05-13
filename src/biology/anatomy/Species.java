package biology.anatomy;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableMap;

import mind.action.ActionType;
import mind.action.IActionType;
import mind.concepts.type.Profile;
import mind.thought_exp.culture.UpgradedCulture;
import sim.World;

public enum Species implements ISpeciesTemplate {
	HUMAN(Set.of(TissueType.BONE, TissueType.BLOOD, TissueType.MUSCLE, TissueType.SKIN, TissueType.FAT, TissueType.HAIR,
			TissueType.NERVES, TissueType.GRAY_MATTER, TissueType.WHITE_MATTER, TissueType.EYE_FLUID,
			TissueType.EYE_MUSCLE, TissueType.CARTILAGE),
			Set.of(BodyPartType.BODY, BodyPartType.SPINE, BodyPartType.RIBCAGE, BodyPartType.CHEST,
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
			(c) -> {
				for (IActionType<?> type : Set.of(ActionType.EAT, ActionType.EAT2, ActionType.EAT3, ActionType.WALK,
						ActionType.SLEEP, ActionType.PICK_UP, ActionType.WANDER, ActionType.SEARCH)) {
					c.learnConcept(type);
				}
			}, 0.5f),
	ELF(HUMAN, Set.of(), Set.of(), Set.of(), Set.of("moustache", "beard"), c -> {
	}, 0.5f),
	FAIRY(HUMAN, Set.of(TissueType.ESSENCE),
			Set.of(BodyPartType.LEFT_WING, BodyPartType.RIGHT_WING, BodyPartType.LEFT_WING_BONE,
					BodyPartType.RIGHT_WING_BONE, BodyPartType.TAIL, BodyPartType.TAILBONE),
			Set.of("blood"), Set.of(), (c) -> {
			}, 0.8f),
	IMP(FAIRY, Set.of(),
			Set.of(BodyPartType.LEFT_HORN, BodyPartType.RIGHT_HORN, BodyPartType.TAIL.withoutTissueTags("hair")),
			Set.of(), Set.of("hair", "beard", "moustache"), c -> {
			}, 0.8f);

	private Map<String, ITissueLayerType> tissue = ImmutableMap.of();
	private Map<String, IBodyPartType> parts = ImmutableMap.of();
	private Consumer<UpgradedCulture> genCulture;
	private float averageUniqueness;

	/**
	 * Copy a previous template and either replace existing bodyparts/tissuelayers
	 * or add new ones
	 * 
	 * @param other
	 * @param tissue
	 * @param parts
	 */
	private Species(Species other, Collection<ITissueLayerType> tissue, Collection<IBodyPartType> parts,
			Collection<String> deleteTissue, Collection<String> deleteParts, Consumer<UpgradedCulture> gdc,
			float averageUniqueness) {
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
		this.genCulture = other.genCulture.andThen(gdc);
		this.averageUniqueness = averageUniqueness;
	}

	private Species(Collection<ITissueLayerType> tissue, Collection<IBodyPartType> parts, Consumer<UpgradedCulture> gdc,
			float averageUniqueness) {
		ImmutableMap.Builder<String, ITissueLayerType> tissueL = ImmutableMap.builder();
		ImmutableMap.Builder<String, IBodyPartType> partL = ImmutableMap.builder();
		for (ITissueLayerType tl : tissue)
			tissueL.put(tl.getName(), tl);
		for (IBodyPartType pp : parts)
			partL.put(pp.getName(), pp);
		this.tissue = tissueL.build();
		this.parts = partL.build();
		this.genCulture = gdc;
		this.averageUniqueness = averageUniqueness;
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
	public UpgradedCulture genDefaultCulture(World world) {
		UpgradedCulture newc = new UpgradedCulture(new Profile(UUID.randomUUID(), "species"),
				"bio_" + this.name().toLowerCase(), world.rand());
		this.genCulture.accept(newc);
		newc.usualInit();
		newc.makeStatic();
		return newc;
	}

}
