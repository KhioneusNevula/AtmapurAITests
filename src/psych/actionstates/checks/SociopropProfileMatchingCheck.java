package psych.actionstates.checks;

import java.util.Objects;

import sociology.IProfile;
import sociology.Profile;
import sociology.ProfilePlaceholder;
import sociology.sociocon.Socioprop;

/**
 * For matching socioprops with non-numeric values
 * 
 * @author borah
 *
 * @param <T>
 */
public class SociopropProfileMatchingCheck extends Check<Socioprop<Profile>> implements IProfileDependentCheck {

	private IProfile profile;

	/**
	 * The int is the index of the profile/placeholder being checked
	 * 
	 * @param checker
	 * @param value
	 */
	SociopropProfileMatchingCheck(Socioprop<Profile> checker, IProfile profile) {
		super(checker);
		this.profile = profile;
	}

	/**
	 * The value being checked against
	 * 
	 * @return
	 */
	public IProfile getValue() {
		return profile;
	}

	public IProfile getProfile() {
		return profile;
	}

	/**
	 * Whether the profile in this is a placeholder
	 * 
	 * @return
	 */
	public boolean isProfilePlaceholder() {
		return profile instanceof ProfilePlaceholder;
	}

	/**
	 * returns the contained profile as a placeholder
	 * 
	 * @return
	 */
	public ProfilePlaceholder getProfilePlaceholder() {
		return (ProfilePlaceholder) profile;
	}

	/**
	 * The actual profile to be checked
	 * 
	 * @return
	 */
	public Profile getActualProfile() {
		return profile.getActualProfile();
	}

	@Override
	public Boolean satisfies(Profile p) {
		if (profile.getActualProfile() == null)
			return null;
		return Objects.equals(profile.getActualProfile(), p.getValue(getChecker()))
				|| Objects.equals(p.getValue(getChecker()), profile.getActualProfile());
	}

	/*
	 * @Override public boolean satisfies(TraitState<?> other) { return
	 * super.satisfies(other) && this.getType() == other.getType(); }
	 */

	@Override
	public void updateToMatch(Profile p) {
		this.profile = p.getValue(getChecker());
	}

	@Override
	protected String idString() {
		return "profilematch." + this.getChecker().getOrigin() + "." + this.getChecker().getName();
	}

	@Override
	public String report() {
		return this.getChecker().toString() + " matches " + this.profile;
	}

}
