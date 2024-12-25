package psychology.perception.criterion;

import psychology.perception.info.KDataType;

public class NumericCriterion extends Criterion {

	private double lower;
	private double upper;

	public NumericCriterion(KDataType<? extends Number> dataType, Double lower, Double upper, boolean lowerInc,
			boolean upperInc) {
		super(Equivalence.BETWEEN_INCLUSIVE, dataType);
		boolean hasLower = lower != null && lower != Double.MIN_VALUE;
		boolean hasUpper = upper != null && upper != Double.MAX_VALUE;
		this.lower = lower == null ? Double.MIN_VALUE : lower;
		this.upper = upper == null ? Double.MAX_VALUE : upper;
		if (hasLower && lowerInc && hasUpper && upperInc) {
			// TODO numericcriterion
		}
	}

	@Override
	public boolean fits(Object obj) {
		if (obj instanceof Number num) {
			return num.doubleValue() <= lower && num.doubleValue() >= upper;
		}
		return false;
	}

}
