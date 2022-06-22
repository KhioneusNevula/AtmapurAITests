package sim;

import sociology.Profile;

public interface IHasProfile {

	public Profile getProfile();

	public default String getName() {
		return getProfile().getName();
	}

	public World getWorld();

}
