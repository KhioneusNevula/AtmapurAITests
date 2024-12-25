package civilization_and_minds.social.concepts.profile;

import civilization_and_minds.social.concepts.IConcept.ConceptType;

public enum ProfileType {
	/**
	 * A profile of a (real or imagined) individual being or creature
	 */
	INDIVIDUAL(ConceptType.INDIVIDUAL_PROFILE),
	/** A profile of a (real or imagined) group of beings */
	GROUP(ConceptType.GROUP_PROFILE),
	/**
	 * A profile of an item, e.g. artifact, relic, etc, or an object such as a tree
	 * or rock
	 */
	ITEM(ConceptType.ITEM_PROFILE),
	/** A profile of a structure, e.g. a house, stockpile, etc */
	STRUCTURE(ConceptType.STRUCTURE_PROFILE),
	/** A profile of a place of some kind, e.g. a landform */
	PLACE(ConceptType.PLACE_PROFILE),
	/** A profile of a language */
	LANGUAGE(ConceptType.LANGUAGE_PROFILE),
	/**
	 * A profile of a tile on the map
	 */
	TILE(ConceptType.TILE_PROFILE),
	/**
	 * A profile for the world in its entirety
	 */
	WORLD(ConceptType.WORLD_PROFILE),
	/** a profile for a phenomenon */
	PHENOMENON(ConceptType.PHENOMENON_PROFILE), OTHER(ConceptType.OTHER_PROFILE);

	private ConceptType ctype;

	private ProfileType(ConceptType type) {
		this.ctype = type;
	}

	public ConceptType getConceptType() {
		return ctype;
	}

	public boolean individual() {
		return this == INDIVIDUAL;
	}

	public boolean group() {
		return this == GROUP;
	}

	public boolean item() {
		return this == ITEM;
	}

	public boolean structure() {
		return this == STRUCTURE;
	}

	public boolean place() {
		return this == PLACE;
	}

	/**
	 * whether this profile represents a decision-making agent
	 * 
	 * @return
	 */
	public boolean isAgent() {
		return this.individual() || this.group();
	}

	/**
	 * whether this profile represents a location
	 * 
	 * @return
	 */
	public boolean isLocational() {
		return this == STRUCTURE || this == PLACE || this == TILE || this == WORLD;
	}

	public boolean language() {
		return this == LANGUAGE;
	}

	/** if this profile does not fit into a standard category */
	public boolean other() {
		return this == OTHER;
	}

}