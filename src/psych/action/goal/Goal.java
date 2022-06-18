package psych.action.goal;

import psych.action.Action;
import psych.actionstates.states.State;
import psych.mind.Mind;

public abstract class Goal {

	/**
	 * When this is >= 99.99, the goal is complete
	 */
	protected float completeness;

	public Goal() {
	}

	public boolean isComplete() {
		return completeness >= 99.99;
	}

	public double getCompleteness() {
		return completeness;
	}

	/**
	 * Uses the mind's state to check completeness level between 0 and 100
	 */
	protected abstract double checkCompletion(Mind mind);

	/**
	 * Whether the given action would contribute to completing this goal
	 * 
	 * @param act
	 * @return
	 */
	public boolean canContributeToGoal(Action act, State from, Mind mind) {
		return contributionFactor(act, from, mind) > 0;
	}

	/**
	 * Returns whatever percent this action would contribute to completing this goal
	 * 
	 * @param act
	 * @return
	 */
	public abstract double contributionFactor(Action act, State result, Mind mind);

	public void goalUpdate(Mind mind) {
		this.completeness = Math.min(100, Math.max(0, (float) checkCompletion(mind)));
	}

	/**
	 * Whether this goal indicates no more actions may be added to this task (i.e.
	 * whether this is a completetasksgoal)
	 * 
	 * @return
	 */
	public boolean isEnd() {
		return false;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " : " + this.completeness + "%";
	}

}
