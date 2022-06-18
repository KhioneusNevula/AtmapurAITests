package psych.actionstates.traits.numeric;

import psych.actionstates.traits.ICheckable;
import psych.actionstates.traits.TraitState;
import sociology.Profile;

/**
 * For matching socioprops with non-numeric values
 * 
 * @author borah
 *
 * @param <T>
 */
public abstract class NumericCheck<M extends ICheckable<T>, T extends Number> extends TraitState<M> {

	private T upper = getMaxValue();
	private T lower = getMinValue();

	public NumericCheck(M checker, T value) {
		this(checker, value, value);
	}

	/**
	 * Taken on faith that lower <= upper
	 */
	public NumericCheck(M checker, T lower, T upper) {
		super(checker);
		if (lower != null && upper != null && lower.doubleValue() > upper.doubleValue()) {
			throw new IllegalArgumentException("Upper bound is less than lower bound: " + lower + "," + upper);
		}

		this.setValues(lower, upper);
	}

	protected void setValues(T lower, T upper) {
		this.upper = upper == null ? getMaxValue() : upper;
		this.lower = lower == null ? getMinValue() : lower;
		if (this.upper == this.lower) {
			this.setType(ConditionType.EQUAL);
		} else if (upper == null && upper == lower) {
			throw new IllegalStateException("Upper and lower bounds cannot be [" + lower + "," + upper + "]");
		} else if (upper == null) {
			this.setType(ConditionType.GREATER);
		} else if (lower == null) {
			this.setType(ConditionType.LESS);
		} else if (upper != null && lower != null) {
			this.setType(ConditionType.BETWEEN);
		} else {
			throw new IllegalStateException("[" + lower + "," + upper + "] ???");
		}
	}

	public T getLower() {
		return lower;
	}

	public T getUpper() {
		return upper;
	}

	protected abstract T getMaxValue();

	protected abstract T getMinValue();

	@Override
	public Boolean satisfies(Profile p) {
		double value = getChecker().getValue(p).doubleValue();
		/*
		 * switch (this.getType()) { case BETWEEN: return value <=
		 * this.upper.doubleValue() && value >= this.lower.doubleValue(); case GREATER:
		 * return value >= this.lower.doubleValue(); case LESS: return value <=
		 * this.upper.doubleValue(); case EQUAL: return value ==
		 * this.upper.doubleValue(); default: return false; }
		 */

		return value <= this.upper.doubleValue() && value >= this.lower.doubleValue();
	}

	// satisfies if the two ranges have a space of intersection

	public boolean satisfies(TraitState<?> other2) {
		if (!(other2 instanceof NumericCheck))
			return false;
		NumericCheck<M, ?> other = (NumericCheck<M, ?>) other2;

		/*
		 * if (other.getType() == ConditionType.EQUAL) { if (this.getType() ==
		 * ConditionType.EQUAL) { return this.lower.doubleValue() ==
		 * other.lower.doubleValue(); } else { return (this.lower != null ?
		 * other.getLower().doubleValue() >= this.lower.doubleValue() : true) &&
		 * (this.upper != null ? other.getUpper().doubleValue() <=
		 * this.upper.doubleValue() : true); } } else if (other.getType() ==
		 * ConditionType.BETWEEN) { if (this.getType() == ConditionType.EQUAL) { return
		 * this.lower.doubleValue() <= other.upper.doubleValue() &&
		 * this.upper.doubleValue() >= other.lower.doubleValue(); } } else {
		 * 
		 * }
		 */

		return Math.max(this.lower.doubleValue(), other.lower.doubleValue()) <= Math.min(this.upper.doubleValue(),
				other.upper.doubleValue());
	}

	@Override
	public void updateToMatch(Profile p) {
		T vala = getChecker().getValue(p);
		this.setValues(vala, vala);
	}

}
