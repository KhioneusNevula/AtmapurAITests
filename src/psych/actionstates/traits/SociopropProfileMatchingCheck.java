package psych.actionstates.traits;

import java.util.Objects;

import sociology.IProfile;
import sociology.Profile;
import sociology.sociocon.Socioprop;

/**
 * For matching socioprops with non-numeric values
 * 
 * @author borah
 *
 * @param <T>
 */
public class SociopropProfileMatchingCheck extends TraitState<Socioprop<Profile>> {

	private IProfile profile;

	/**
	 * The int is the index of the profile/placeholder being checked
	 * 
	 * @param checker
	 * @param value
	 */
	public SociopropProfileMatchingCheck(Socioprop<Profile> checker, IProfile profile) {
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

	@Override
	public boolean satisfies(Profile p) {
		return Objects.equals(profile, p.getValue(getChecker())) || Objects.equals(p.getValue(getChecker()), profile);
	}

	@Override
	public boolean satisfies(TraitState<?> other) {
		return super.satisfies(other) && this.getType() == other.getType();
	}

	@Override
	public void updateToMatch(Profile p) {
		this.profile = p.getValue(getChecker());
	}

	@Override
	protected String idString() {
		return "profilematch." + this.getChecker().getOrigin() + "." + this.getChecker().getName();
	}

}
