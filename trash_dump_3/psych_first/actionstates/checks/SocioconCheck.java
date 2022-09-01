package psych_first.actionstates.checks;

import culture.CulturalContext;
import sociology.IProfile;
import sociology.sociocon.Sociocon;

public class SocioconCheck extends Check<Sociocon> {

	SocioconCheck(Sociocon checker, boolean ifPresent) {
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
				: (this.getType() == ConditionType.EQUAL) == (p.hasSociocon(getChecker())
						|| (!p.isTypeProfile() && p.getTypeProfile().hasSociocon(getChecker())));
	}

	@Override
	public String report() {
		return (checksIfPresent() ? "has" : "does not have") + " sociocon " + this.getChecker();
	}

}
