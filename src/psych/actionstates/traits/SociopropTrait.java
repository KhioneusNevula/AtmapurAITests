package psych.actionstates.traits;

import sociology.sociocon.Socioprop;

public abstract class SociopropTrait<T> extends TraitState<Socioprop<T>> {

	public SociopropTrait(Socioprop<T> checker) {
		super(checker);
	}

	@Override
	protected String idString() {
		return this.getChecker().getOrigin() + "." + this.getChecker().getName();
	}

}
