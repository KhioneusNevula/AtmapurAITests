package psych_first.actionstates.states;

import java.util.Collection;
import java.util.Map;

import culture.CulturalContext;
import psych_first.actionstates.ConditionSet;
import psych_first.actionstates.checks.Check;
import sociology.Profile;
import sociology.ProfilePlaceholder;
import sociology.ProfileType;

public interface State extends Cloneable {

	/**
	 * Gets the placeholder for this profile; null if this state doesn't use that
	 * specific profile
	 * 
	 * @param key
	 * @return
	 */
	public ProfilePlaceholder getProfile(ProfileType key);

	/**
	 * Get the associated conditions for the given profile; may be null if the
	 * profile is not relevant
	 * 
	 * @param key
	 * @return
	 */
	public ConditionSet getFor(ProfileType key);

	public Collection<ConditionSet> getAllConditions();

	/**
	 * Whether this state has no actual conditions
	 * 
	 * @return
	 */
	public default boolean isEmpty() {
		for (ConditionSet con : getAllConditions()) {
			if (!con.isEmpty())
				return false;
		}
		return true;
	}

	public default int numConditions() {
		return (int) getAllConditions().stream().flatMap((a) -> a.getAllConditions().stream()).count();
	}

	/**
	 * Returns the State representing all conditions that remain unfulfilled given
	 * the State parameter. Assumes that "this" object is the requirements, and the
	 * parameter is the result
	 * 
	 * @param result
	 * @return
	 */

	public default State conditionsUnfulfilledBy(State result) {
		ActionState state = new ActionState();
		for (ProfileType type : ProfileType.values()) {
			ConditionSet otherCons = result.getFor(type);
			ConditionSet thisCons = this.getFor(type);
			if (thisCons == null)
				continue;

			for (Object checker : thisCons.getCheckers()) {
				Check<?> thisT = thisCons.getCondition(checker);
				Check<?> otherT = otherCons.getCondition(checker);
				if (otherT == null || !otherT.satisfies(thisT)) {
					state.getFor(type).addConditions(thisT);
				}
			}

		}
		return state;
	}

	/**
	 * Profile is the profile of the user
	 * 
	 * @param param
	 * @return
	 */
	public default State conditionsUnfulfilledBy(Profile param, CulturalContext ctxt) {
		ActionState state = new ActionState();
		for (ProfileType type : ProfileType.values()) {
			ConditionSet thisCons = this.getFor(type);

			ProfilePlaceholder pp = this.getProfile(type);
			if (pp == null)
				continue;
			state.getFor(type).addConditions(thisCons.conditionsUnfulfilledBy(pp, ctxt));

		}
		return state;

	}

	/**
	 * elimintates conditions resolved by the given RESOLVED profile placeholder
	 */
	public void eliminateResolvedConditions(ProfilePlaceholder pp, CulturalContext ctxt);

	public String conditionsString();

	ActionState putProfilePlaceholder(ProfileType type, ProfilePlaceholder pp);

	ActionState setProfilePlaceholders(Map<ProfileType, ProfilePlaceholder> values);

	public State clone() throws CloneNotSupportedException;

}
