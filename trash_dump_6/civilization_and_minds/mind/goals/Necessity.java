package civilization_and_minds.mind.goals;

/**
 * Level of severity of a need, urgency of a goal, etc
 * 
 * @author borah
 *
 */
public enum Necessity {
	/** barely important currently */
	TRIVIAL,
	/** can be handled within maybe a day or something */
	NORMAL,
	/** needs to be handled very soon */
	SERIOUS,
	/**
	 * most serious and needs to be handled right now; logically, there should not
	 * be many maximal needs at a given time
	 */
	MAXIMAL;

	/** {@link #TRIVIAL} */
	public boolean isTrivial() {
		return this == TRIVIAL;
	}

	/** {@link #NORMAL} */
	public boolean isNormal() {
		return this == NORMAL;
	}

	/** {@link #SERIOUS} */
	public boolean isSerious() {
		return this == SERIOUS;
	}

	/** {@link #MAXIMAL} */
	public boolean isMaximal() {
		return this == MAXIMAL;
	}

}