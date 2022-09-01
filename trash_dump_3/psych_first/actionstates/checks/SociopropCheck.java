package psych_first.actionstates.checks;

import culture.CulturalContext;
import sociology.IProfile;
import sociology.sociocon.Socioprop;

public abstract class SociopropCheck<T> extends Check<Socioprop<T>> {

	public SociopropCheck(Socioprop<T> checker) {
		super(checker);
	}

	@Override
	protected String idString() {
		return this.getChecker().getOrigin() + "." + this.getChecker().getName();
	}

	protected T getValue(IProfile from, CulturalContext ctxt) {
		return from.hasValue(getChecker(), ctxt) ? from.getValue(getChecker(), ctxt)
				: (!from.isTypeProfile() && from.getTypeProfile().hasValue(getChecker(), ctxt)
						? from.getTypeProfile().getValue(getChecker(), ctxt)
						: null);
	}

}
