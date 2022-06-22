package sociology.sociocon;

import sim.IHasProfile;
import sociology.Profile;

public abstract class Construct implements IHasProfile {

	private Profile profile;

	@Override
	public Profile getProfile() {
		return profile;
	}

}
