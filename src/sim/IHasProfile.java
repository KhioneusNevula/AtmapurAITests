package sim;

import sociology.Profile;
import sociology.TypeProfile;

public interface IHasProfile {

	public Profile getProfile();

	public default String getName() {
		return getProfile().getName();
	}

	public TypeProfile getType();

	public World getWorld();

}
