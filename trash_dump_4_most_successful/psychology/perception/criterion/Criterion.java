package psychology.perception.criterion;

import psychology.perception.info.KDataType;

public abstract class Criterion {

	public static final Criterion PRESENCE = new Criterion(Equivalence.EQUALS, KDataType.BOOLEAN) {
		@Override
		public boolean fits(Object obj) {
			if (obj == Boolean.TRUE)
				return true;
			return false;
		}
	};

	public static enum Equivalence {
		EQUALS, NOT_EQUALS, GREATER, LESS, GREATER_OR_EQUALS, LESS_OR_EQUALS, CONTAINS, IS_CONTAINED_IN,
		BETWEEN_EXCLUSIVE, BETWEEN_INCLUSIVE, BETWEEN_INCLUDE_LOWER, BETWEEN_INCLUDE_UPPER
	}

	protected Equivalence equivalence;
	private KDataType<?> dataType;

	protected Criterion(Equivalence equivalence, KDataType<?> type) {
		this.equivalence = equivalence;
		this.dataType = type;
	}

	public Equivalence getEquivalence() {
		return equivalence;
	}

	public KDataType<?> getDataType() {
		return dataType;
	}

	public abstract boolean fits(Object obj);

	@Override
	public int hashCode() {
		return super.hashCode() + this.equivalence.hashCode() * this.dataType.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Criterion crit) {
			if (crit.equivalence.equals(this.equivalence) && crit.dataType.equals(this.dataType)) {
				return true;
			}
		}
		return super.equals(obj);
	}

}
