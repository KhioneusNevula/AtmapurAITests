package mind.concepts.relations;

public interface IConceptRelationType {

	/**
	 * is this relation bidirectional
	 * 
	 * @return
	 */
	public boolean bidirectional();

	/**
	 * does this relation require an action or event
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
	 * Whether this relationship is not "really" a relationship, but rather just
	 * intended to be the backwards counterpart of a relationship
	 * 
	 * @return
	 */
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
			return bearerType.idString() + "_inverse";
		}

		@Override
		public IConceptRelationType inverse() {
			return bearerType;
		}

	}

}
