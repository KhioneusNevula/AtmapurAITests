package psych.action.goal;

import psych.action.types.Action;
import psych.actionstates.states.State;
import psych.mind.Mind;

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
		return Priority.NO_PRIORITY;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

}
