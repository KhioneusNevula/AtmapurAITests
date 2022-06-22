package psych.action.goal;

import psych.action.types.Action;
import psych.actionstates.states.State;
import psych.mind.Mind;

public class RequirementGoal extends Goal {

	private State state;
	private Priority priority;

	public RequirementGoal(State state, Priority priority) {
		this.state = state;
		this.priority = priority;
	}

	@Override
	public Priority getPriority() {
		return priority;
	}

	@Override
	protected double checkCompletion(Mind mind) {
		return state.numConditions() == 0 ? 100
				: 100 * (state.numConditions()
						- state.conditionsUnfulfilledBy(mind.getOwner().getProfile()).numConditions())
						/ (double) state.numConditions();
	}

	@Override
	public double contributionFactor(Action act, State result, Mind mind) {

		return state.numConditions() == 0 ? 100
				: 100 * state.conditionsUnfulfilledBy(result).numConditions() / (double) state.numConditions();
	}

	public State getState() {
		return state;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	@Override
	public boolean isEmpty() {
		return state.isEmpty();
	}

	@Override
	public String goalReport() {
		return super.goalReport() + " state " + this.state.conditionsString();
	}

}
