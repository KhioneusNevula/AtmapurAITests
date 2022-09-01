package psych_first.perception.senses;

public enum Certainty {
	/**
	 * not certain at all
	 */
	FALSE,
	/** possibly true */
	DUBIOUS,
	/** likely to be true */
	LIKELY,
	/**
	 * completely certain
	 */
	COMPLETELY_CERTAIN;

	public boolean moreRecognizableThan(Certainty other) {
		return this.ordinal() > other.ordinal();
	}

	public boolean lessRecognizableThan(Certainty other) {
		return this.ordinal() < other.ordinal();
	}
}