package psych_first.action.goal;

import psych_first.action.types.Action;
import psych_first.actionstates.states.State;
import psych_first.mind.Mind;

/**
 * This goal is already complete and serves only as an endpoint in a task
 * 
 * @author borah
 *
 */
public class EmptyGoal extends Goal {

	public static final EmptyGoal GOAL = new EmptyGoal();

	public static EmptyGoal get() {
		return GOAL;
	}

	private EmptyGoal() {
	}

	@Override
	protected double checkCompletion(Mind mind) {
		return 100;
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
		return "EMPTY GOAL";
	}

	@Override
	public Priority getPriority() {
		return Priority.X_PRIORITY;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public RequirementWrapper getRequirementWrapper() {
		return RequirementWrapper.EMPTY;
	}

}
