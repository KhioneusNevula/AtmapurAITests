package psych.actionstates.states;

import java.util.Collection;

import psych.actionstates.ConditionSet;
import psych.actionstates.traits.TraitState;
import sociology.Profile;
import sociology.ProfilePlaceholder;

public interface State {

	public static enum ProfileType {
		USER, TARGET
	}

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
				TraitState<?> thisT = thisCons.getCondition(checker);
				TraitState<?> otherT = otherCons.getCondition(checker);
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
	public default State conditionsUnfulfilledBy(Profile param) {
		ActionState state = new ActionState();
		for (ProfileType type : ProfileType.values()) {
			ConditionSet thisCons = this.getFor(type);
			if (thisCons == null)
				continue;

			for (Object checker : thisCons.getCheckers()) {
				TraitState<?> thisT = thisCons.getCondition(checker);
				if (thisT.satisfies(param) != true) {
					state.getFor(type).addConditions(thisT);
				}
			}

		}
		return state;

	}

	public abstract String conditionsString();

}
