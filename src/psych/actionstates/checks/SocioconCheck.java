package psych.actionstates.checks;

import sociology.Profile;
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
	public Boolean satisfies(Profile p) {
		return (this.getType() == ConditionType.EQUAL) == p.hasSociocon(getChecker());
	}

	@Override
	public void updateToMatch(Profile p) {
		if (p.hasSociocon(getChecker())) {
			this.setType(ConditionType.EQUAL);
		} else {
			this.setType(ConditionType.NOT_EQUAL);
		}
	}

	@Override
	public String report() {
		return (checksIfPresent() ? "has" : "does not have") + " sociocon " + this.getChecker();
	}

}
