package phenomenon;

import sociology.Profile;
import sociology.sociocon.IHasProfile;

public class Phenomenon implements IHasProfile {

	private Profile profile;

	@Override
	public Profile getProfile() {
		return profile;
	}

}
