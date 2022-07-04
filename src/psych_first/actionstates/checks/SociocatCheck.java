package psych_first.actionstates.checks;

import culture.CulturalContext;
import sociology.IProfile;
import sociology.sociocon.Sociocat;

public class SociocatCheck extends Check<Sociocat> {

	SociocatCheck(Sociocat checker, boolean ifPresent) {
		super(checker);
		if (ifPresent) {
			this.setType(ConditionType.EQUAL);
		} else {
			this.setType(ConditionType.NOT_EQUAL);
		}
	}

	public boolean checksIfPresent() {
		return this.getType() == ConditionType.EQUAL;
	}

	@Override
	protected String idString() {
		return this.getChecker().getName();
	}

	@Override
	public Boolean satisfies(IProfile p, CulturalContext ctxt) {
		return p.getActualProfile() == null ? null
				: (this.getType() == ConditionType.EQUAL) == (p.hasSociocat(getChecker(), ctxt)
						|| (!p.isTypeProfile() ? p.getTypeProfile().hasSociocat(getChecker(), ctxt) : false));
	}

	@Override
	public String report() {
		return (checksIfPresent() ? "has" : "does not have") + " sociocat " + this.getChecker();
	}

}
