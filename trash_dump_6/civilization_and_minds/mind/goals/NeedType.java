package civilization_and_minds.mind.goals;

import civilization_and_minds.mind.goals.INeed.INeedType;

/**
 * A type of need
 * 
 * @author borah
 *
 */
public enum NeedType implements INeedType {
	/** need for food */
	HUNGER(true),
	/** need for water */
	THIRST(true),
	/** need to breathe air */
	BREATH(true),
	/** need to heal wounds and sickness */
	HEALTH(true),
	/** need to be away from danger */
	SAFETY(true),
	/** need to sleep */
	REST,
	/** need to avoid the environment, e.g. rain, darkness, cold */
	SHELTER,
	/** need to talk to others */
	COMMUNITY,
	/** need to release emotions of some kind */
	CATHARSIS,
	/** need for ability to perform actions */
	FREEDOM,
	/** need to not be physically uncomfortable */
	COMFORT,
	/** need to feel happiness */
	HAPPINESS,
	/** need to know information */
	KNOWLEDGE,
	/** need for sxual activity */
	DESIRE,
	/** need for security of resources to fulfill other needs */
	RESOURCES,
	/** need of longer life */
	LONGEVITY,
	/** need to produce a family or whatever */
	CONTINUITY,
	/** need for great capability */
	POWER;

	private boolean mortal;

	private NeedType(boolean mortal) {
		this.mortal = mortal;
	}

	private NeedType() {
	}

	@Override
	public String getUniqueName() {
		return "need_" + this.name().toLowerCase();
	}

	@Override
	public ConceptType getConceptType() {
		return ConceptType.NEED_CATEGORY;
	}

	@Override
	public boolean isMortal() {
		return mortal;
	}

}
