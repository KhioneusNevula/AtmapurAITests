package psych.actionstates.states;

import java.util.function.Predicate;

import psych.action.goal.Goal.Priority;
import psych.action.goal.RequirementGoal;
import psych.action.goal.RequirementWrapper;
import psych.actionstates.checks.Check;
import psych.actionstates.states.State.ProfileType;
import sociology.Profile;
import sociology.ProfilePlaceholder;
import sociology.sociocon.Socioprop;

public class StateBuilder {

	private State state;

	private StateBuilder() {
		state = new ActionState();
	}

	public static StateBuilder start() {
		return new StateBuilder();
	}

	public static StateBuilder start(State from) {
		StateBuilder builder = new StateBuilder();
		try {
			builder.state = from.clone();
		} catch (CloneNotSupportedException e) {
			throw new UnsupportedOperationException("Cannot clone state of type " + from.getClass().getName());
		}
		return builder;
	}

	public StateBuilder initProfile(ProfilePlaceholder placeholder) {
		state.putProfilePlaceholder(placeholder.getType(), placeholder);
		return this;
	}

	public StateBuilder removeConditions(ProfileType type, Check<?>... conditions) {
		this.state.getFor(type).removeConditions(conditions);
		return this;
	}

	public StateBuilder removeConditionForChecker(ProfileType pt, Object checker) {
		if (this.state.getFor(pt).hasConditionFor(checker)) {
			this.state.getFor(pt).removeConditions(this.state.getFor(pt).getCondition(checker));
		}
		return this;
	}

	public StateBuilder removeAllConditionsOfPredicate(ProfileType pt, Predicate<Check<?>> pred) {
		for (Check<?> con : this.state.getFor(pt)) {
			if (pred.test(con)) {
				this.state.getFor(pt).removeConditions(con);
			}
		}
		return this;
	}

	public StateBuilder addConditions(ProfileType type, Check<?>... conditions) {
		if (state.getProfile(type) == null) {
			state.putProfilePlaceholder(type, new ProfilePlaceholder(type));
		}
		state.getFor(type).addConditions(conditions);
		return this;
	}

	/**
	 * Adds a {@link psych.actionstates.checks.AtCheck} condition at the profile
	 * "targetLocation"
	 * 
	 * @param forProfile
	 * @param targetLocation
	 * @return
	 */
	public StateBuilder addLocationCondition(ProfileType forProfile, ProfileType targetLocation) {
		ProfilePlaceholder pp = state.getProfile(targetLocation);
		if (pp == null) {
			pp = new ProfilePlaceholder(targetLocation);
			state.putProfilePlaceholder(targetLocation, pp);
		}
		return this.addConditions(forProfile, Check.createLocationCheck(pp));
	}

	/**
	 * Adds a SociopropProfileMatching check referencing a preexisting profile in
	 * this state
	 * 
	 * @param forProfile the profile to check the property for
	 * @param property   the property itself
	 * @param matchwith  the profile to match the property with
	 * @return
	 */
	public StateBuilder addProfileMatchingCondition(ProfileType forProfile, Socioprop<Profile> property,
			ProfileType matchwith) {
		ProfilePlaceholder pp = state.getProfile(matchwith);
		if (pp == null) {
			pp = new ProfilePlaceholder(matchwith);
			state.putProfilePlaceholder(matchwith, pp);
		}
		return this.addConditions(forProfile, Check.createProfileMatchCheck(property, pp));
	}

	public StateBuilder addConditions(ProfilePlaceholder placeholder, Check<?>... conditions) {
		state.putProfilePlaceholder(placeholder.getType(), placeholder);
		state.getFor(placeholder.getType()).addConditions(conditions);
		return this;
	}

	public StateBuilder requireResolved(ProfileType type) {
		addConditions(type, Check.checkIfKnown());
		return this;
	}

	public StateBuilder requireUnResolved(ProfileType type) {
		addConditions(type, Check.checkIfUnknown());
		return this;
	}

	public StateBuilder checkIfResolved(ProfileType type, boolean is) {
		addConditions(type, Check.checkIfKnown(is));
		return this;
	}

	public State build() {
		return state;
	}

	public RequirementGoal buildGoal(Priority priority) {
		return new RequirementGoal(state, priority);
	}

	public RequirementWrapper buildWrapper(Priority goalPriority) {
		return RequirementWrapper.create(buildGoal(goalPriority));
	}

}
