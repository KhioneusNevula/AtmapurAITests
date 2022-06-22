package psych.actionstates.checks;

import sociology.IProfile;
import sociology.ProfilePlaceholder;

public interface IProfileDependentCheck {

	public IProfile getProfile();

	/**
	 * Whether the profile in this is a placeholder
	 * 
	 * @return
	 */
	public default boolean isProfilePlaceholder() {
		return getProfile() instanceof ProfilePlaceholder;
	}

	/**
	 * Whether the profile is either a resolved placeholder or a proper profile
	 * 
	 * @return
	 */
	public default boolean isProfileKnown() {
		return getProfile().getActualProfile() != null;
	}

	/**
	 * returns the contained profile as a placeholder
	 * 
	 * @return
	 */
	public default ProfilePlaceholder getProfilePlaceholder() {
		return (ProfilePlaceholder) getProfile();
	}

}
