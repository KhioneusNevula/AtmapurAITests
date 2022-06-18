package psych.action.goal;

import psych.action.Action;
import psych.actionstates.states.State;
import psych.mind.Mind;

/**
 * The goal corresponding to completing all tasks relating to this action before
 * this action can be complete.
 * 
 * @author borah
 *
 */
public class CompleteTasksGoal extends Goal {

	private RequirementWrapper goals;

	public CompleteTasksGoal(RequirementWrapper goalsToComplete) {
		this.goals = goalsToComplete;
	}

	public RequirementWrapper getGoals() {
		return goals;
	}

	@Override
	protected double checkCompletion(Mind mind) {
		double g = goals.numStates();
		double c = 0;
		for (RequirementGoal goal : goals) {
			c += goal.completeness;
		}
		return c / g;
	}

	@Override
	public double contributionFactor(Action act, State result, Mind mind) {
		return 0;
	}

	@Override
	public boolean isEnd() {
		return true;
	}

	@Override
	public String toString() {
		return super.toString() + " for " + this.goals.numStates() + " other goals";
	}

}
