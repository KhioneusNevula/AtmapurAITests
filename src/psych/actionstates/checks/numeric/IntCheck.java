package psych.actionstates.checks.numeric;

import psych.actionstates.checks.ICheckable;

public class IntCheck<M extends ICheckable<Integer>> extends NumericCheck<M, Integer> {

	public IntCheck(M checker, Integer lower, Integer upper) {
		super(checker, lower, upper);
	}

	public IntCheck(M checker, Integer both) {
		super(checker, both);
	}

	@Override
	protected Integer getMaxValue() {
		return Integer.MAX_VALUE;
	}

	@Override
	protected Integer getMinValue() {
		return Integer.MIN_VALUE;
	}

	@Override
	protected String idString() {
		return "integer." + this.getChecker().getName();
	}

}
