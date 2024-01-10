package mind.concepts.relations;

public enum ConceptRelationType implements IConceptRelationType {
	/**
	 * a relation where one category (this end) is contained inside of another
	 * category (the other end), i.e. a cat is contained within the category of
	 * mammal, or Wood is contained in the category of BurnsWithHeat. Properties
	 * have arguments: For example, Bluebird is a type-of HasColoredFeathers, with
	 * argument BLUE since the property has a Blue color argument. <br>
	 * Note: you can just call this 'type-of' if you want
	 */
	SUBSET_OF(false, false),
	/**
	 * a relation where a Profile (this end) has a property described by a property
	 * (the other end), i.e. PersonA has the Person property. <br>
	 * Too many of a non-Type Of kind of relationship between profiles and a
	 * specific Meme is likely to generate a new category to intercept it. <br>
	 * E,g, if Profile1, Profile2, and Profile3 all PRODUCE Apples, then a new
	 * AppleProducer meme can be generated, which PRODUCEs Apples and to which
	 * Profile1, Profile2, and Profile3 all have IN_SET relationships. <br>
	 * Notes: You can call this "IS-A" for simplicity.
	 */
	IN_SET(false, false),
	/**
	 * a relation where one thing (this end) produces the other (end) without using
	 * ingredients to build it, i.e. a tree produces fruit. This is usually
	 * coterminous with Found-In
	 */
	PRODUCES(false, true),
	/**
	 * a relation where one thing (this end) is constituted of another category (the
	 * other end), i.e. its physical properties are of another category. E.g. a
	 * house is made of wood.
	 */
	MADE_OF(false, false),

	/**
	 * A relation for structures in physical space; when items are put together not
	 * in the manner of crafting, but just into a structure. This has to do with
	 * 'ideas' of structures, e.g. four walls make a "house," where a house is
	 * built-from Walls. Usually associated with a Template. Not "transformational"
	 * because the things don't change, they are only regarded in a specific way.
	 */
	BUILT_FROM(false, false),

	/**
	 * a relation where one thing (this end) is found in a specific location or
	 * circumstance (the other end), e.g. trees are found in forests, or, for
	 * example, a specific item may be found in a specific stockpile.
	 */
	FOUND_AT(false, false),
	/**
	 * a relation where one thing (this end) becomes another thing (the other end)
	 * because of an event, i.e. thread becomes cloth because of Weaving. Can be
	 * used for a Recipe too
	 */
	BECOMES(false, true),

	/**
	 * Indicates that an action (this end) is used to complete the task-hint (the
	 * other end)
	 */
	USED_FOR(false, false),
	/**
	 * the most default form of relationship indicating someone (this end) utilizes
	 * something (the other end) for a general purpose defined by the action
	 * argument.
	 */
	USES(false, true),

	/**
	 * Default relationship to check if the left thing is in possession of the right
	 * thing
	 */
	POSSESSES(false, false),
	/**
	 * a form of relationship indicating that (this end) is a danger to (the other
	 * end)
	 */
	DANGER_TO(false, true),
	/**
	 * Indicates that something (this end) is used for an action relating to
	 * something else (the other end)
	 */
	/* USED_FOR(false, true), */

	/**
	 * whether the entities, groups, etc represented by the ends of this
	 * relationship can communicate in the same language. Takes language as an
	 * argument
	 */
	INTELLIGIBLE(true, false);

	/** if this relationship is undirected */
	final boolean bidirectional;
	/** if this relation is contingent on an event occurring */
	final boolean transformation;

	final InverseType inverse;

	/**
	 * If non-bidirectional, then an 'inverse' version of this concept will be
	 * created
	 * 
	 * @param bidirectional
	 * @param transformation
	 */
	private ConceptRelationType(boolean bidirectional, boolean transformation) {
		this.bidirectional = bidirectional;
		this.transformation = transformation;
		if (bidirectional)
			inverse = null;
		else
			inverse = InverseType.from(this);
	}

	@Override
	public boolean bidirectional() {
		return bidirectional;
	}

	@Override
	public boolean transformation() {
		return transformation;
	}

	@Override
	public boolean consumes() {
		return false;
	}

	@Override
	public boolean creates() {
		return this == PRODUCES;
	}

	@Override
	public String idString() {
		return "rel" + this.toString();
	}

	@Override
	public IConceptRelationType inverse() {
		if (bidirectional)
			return this;
		return inverse;
	}

}
