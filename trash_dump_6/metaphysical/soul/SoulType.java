package metaphysical.soul;

import sim.interfaces.IObjectType;

public enum SoulType implements IObjectType {
	/** for animal and non-thinking souls */
	UNINTELLIGENT,
	/** for thinking and humane souls */
	SAPIENT,
	/** for souls which belong to higher-order entities */
	NONMORTAL,
	/** for souls belonging to godlike entities */
	DIVINE, OTHER;

	@Override
	public String getUniqueName() {
		return "soultype_" + this.name().toLowerCase();
	}

	@Override
	public ConceptType getConceptType() {
		return ConceptType.SOUL_TYPE;
	}

	@Override
	public float averageUniqueness() {
		return 1f;
	}

}
