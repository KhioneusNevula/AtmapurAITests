package psych_first.actionstates.checks;

import java.util.Objects;

import culture.CulturalContext;
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
public class SociopropProfileMatchingCheck extends SociopropCheck<Profile> implements IProfileDependentCheck {

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
	public Boolean satisfies(IProfile p, CulturalContext ctxt) {
		if (profile.getActualProfile() == null || p.getActualProfile() == null)
			return null;
		Profile v = getValue(p, ctxt);
		return Objects.equals(profile.getActualProfile(), v) || Objects.equals(v, profile.getActualProfile());
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
