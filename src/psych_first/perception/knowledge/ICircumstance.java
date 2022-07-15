package psych_first.perception.knowledge;

import psych_first.perception.knowledge.facts.Circumstances;

public interface ICircumstance {

	public default boolean isOccurrence() {
		return this instanceof IOccurrence;
	}

	public default boolean isCircumstances() {
		return this instanceof Circumstances;
	}

	/**
	 * null if this is not an occurrence
	 * 
	 * @return
	 */
	public default IOccurrence asOccurrence() {
		return this.isOccurrence() ? (IOccurrence) this : null;
	}

	/**
	 * null if this is not circumstances
	 * 
	 * @return
	 */
	public default Circumstances asCircumstances() {
		return this.isCircumstances() ? (Circumstances) this : null;
	}

}
