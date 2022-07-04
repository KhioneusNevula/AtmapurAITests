package psych_first.actionstates.checks.numeric;

import psych_first.actionstates.checks.ICheckable;

public class DoubleCheck<M extends ICheckable<Double>> extends NumericCheck<M, Double> {

	public DoubleCheck(M checker, Double lower, Double upper) {
		super(checker, lower, upper);
	}

	public DoubleCheck(M checker, Double both) {
		super(checker, both);
	}

	@Override
	protected Double getMaxValue() {
		return Double.MAX_VALUE;
	}

	@Override
	protected Double getMinValue() {
		return Double.MIN_VALUE;
	}

	@Override
	protected String idString() {
		return "double." + this.getChecker().getName();
	}

}
