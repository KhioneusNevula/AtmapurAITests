package psych.actionstates.checks;

import sociology.sociocon.Socioprop;

public abstract class SociopropCheck<T> extends Check<Socioprop<T>> {

	public SociopropCheck(Socioprop<T> checker) {
		super(checker);
	}

	@Override
	protected String idString() {
		return this.getChecker().getOrigin() + "." + this.getChecker().getName();
	}

}
