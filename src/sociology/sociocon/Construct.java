package sociology.sociocon;

import sociology.Profile;

public abstract class Construct implements IHasProfile {

	private Profile profile;

	@Override
	public Profile getProfile() {
		return profile;
	}

}
