package psych.actionstates.checks;

import sociology.Profile;
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
	public Boolean satisfies(Profile p) {
		return (this.getType() == ConditionType.EQUAL) == p.hasSociocat(getChecker());
	}

	@Override
	public void updateToMatch(Profile p) {
		if (p.hasSociocat(getChecker())) {
			this.setType(ConditionType.EQUAL);
		} else {
			this.setType(ConditionType.NOT_EQUAL);
		}
	}

	@Override
	public String report() {
		return (checksIfPresent() ? "has" : "does not have") + " sociocat " + this.getChecker();
	}

}
