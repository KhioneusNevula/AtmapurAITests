package psych.actionstates.traits;

import sociology.Profile;
import sociology.sociocon.Sociocon;

public class SocioconTrait extends TraitState<Sociocon> {

	private boolean ifPresent;

	public SocioconTrait(Sociocon checker, boolean ifPresent) {
		super(checker);
	}

	public boolean checksIfPresent() {
		return ifPresent;
	}

	@Override
	protected String idString() {
		return this.getChecker().getName();
	}

	@Override
	public boolean satisfies(Profile p) {
		return ifPresent == p.hasSociocon(getChecker());
	}

	@Override
	public void updateToMatch(Profile p) {
		ifPresent = p.hasSociocon(getChecker());
	}

}
