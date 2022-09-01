package psych_first.action.goal;

import psych_first.action.types.Action;
import psych_first.actionstates.states.State;
import psych_first.mind.Mind;

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

	@Override
	public Priority getPriority() {
		return Priority.X_PRIORITY;
	}

	@Override
	public boolean isCompleteTasksGoal() {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return goals.isEmpty();
	}

	@Override
	public RequirementWrapper getRequirementWrapper() {
		return goals;
	}

}
