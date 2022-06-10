package psych.actionstates.traits;

import sociology.Profile;
import sociology.sociocon.Sociocat;

public class SociocatTrait extends TraitState<Sociocat> {

	private boolean ifPresent;

	public SociocatTrait(Sociocat checker, boolean ifPresent) {
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
		return ifPresent == p.hasSociocat(getChecker());
	}

	@Override
	public void updateToMatch(Profile p) {
		this.ifPresent = p.hasSociocat(getChecker());
	}

}
