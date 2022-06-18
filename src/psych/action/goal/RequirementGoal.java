package psych.action.goal;

import psych.action.Action;
import psych.actionstates.states.State;
import psych.mind.Mind;

public class RequirementGoal extends Goal {

	private State state;

	public RequirementGoal(State state) {
		this.state = state;
	}

	@Override
	protected double checkCompletion(Mind mind) {
		return state.numConditions() == 0 ? 100
				: 100 * state.conditionsUnfulfilledBy(mind.getOwner().getProfile()).numConditions()
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
		return super.toString() + " for state " + this.state;
	}

}
