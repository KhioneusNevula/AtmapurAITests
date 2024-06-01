package mind.concepts.relations;

import sim.relationalclasses.IInvertibleRelationType;

public interface IConceptRelationType extends IInvertibleRelationType<IConceptRelationType> {

	/**
	 * is this relation bidirectional
	 * 
	 * @return
	 */
	public boolean bidirectional();

	/**
	 * does this relation involve an action or event
	 * 
	 * @return
	 */
	public boolean transformation();

	/**
	 * whether this relation consumes instances of the concept it involves
	 */
	public boolean consumes();

	/**
	 * whether this relation creates instances of the concept it involves
	 * 
	 * @return
	 */
	public boolean creates();

	/**
	 * An id string for data structures that need to sort keys for some reason
	 * 
	 * @return
	 */
	public String idString();

	/**
	 * This is used to check if the left relationship is a subtype or supertype of
	 * the right relationship. Returns true if the relations are equal as well
	 * 
	 * @return
	 */
	default boolean matches(IConceptRelationType other) {
		return this.subtypeOf(other) || other.subtypeOf(this);
	}

	/**
	 * return true if the left relation is a subtype of or equivalent to the right
	 * relationship
	 * 
	 * @param other
	 * @return
	 */
	default boolean subtypeOf(IConceptRelationType other) {
		return this.equals(other);
	}

	@Override
	public default boolean isInverseType() {
		return this instanceof InverseType;
	}

	/** the direct reverse of this relationship */
	public IConceptRelationType inverse();

	public static class InverseType implements IConceptRelationType {
		private IConceptRelationType bearerType;

		private InverseType(IConceptRelationType bearerType) {
			this.bearerType = bearerType;
		}

		public static InverseType from(IConceptRelationType bearerType) {
			return new InverseType(bearerType);
		}

		@Override
		public boolean bidirectional() {
			return false;
		}

		@Override
		public boolean consumes() {
			return bearerType.consumes();
		}

		@Override
		public boolean creates() {
			return bearerType.creates();
		}

		@Override
		public boolean transformation() {
			return bearerType.transformation();
		}

		@Override
		public String idString() {
			return "inv_" + bearerType.idString();
		}

		@Override
		public String toString() {
			return idString();
		}

		@Override
		public IConceptRelationType inverse() {
			return bearerType;
		}

	}

}
