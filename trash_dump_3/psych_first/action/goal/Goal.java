package psych_first.action.goal;

import psych_first.action.types.Action;
import psych_first.actionstates.states.State;
import psych_first.mind.Mind;

public abstract class Goal {

	public static enum Priority {
		/** a goal which is fundamental to survival **/
		NECESSITY,
		/** a goal which isn't fundamental but helps with fundamentals **/
		IMPORTANT,
		/** a goal which is a quality of life goal **/
		UNESSENTIAL,
		/** Goals with no meaningful priority because they're not used to form tasks **/
		X_PRIORITY
	}

	/**
	 * When this is >= 99.99, the goal is complete
	 */
	protected float completeness;

	public Goal() {
	}

	/**
	 * Checks completion of this goal; use goalUpdate to update the completeness
	 * 
	 * @return
	 */
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

	public abstract Priority getPriority();

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

	/**
	 * update the goal's state; return if complete
	 * 
	 * @param mind
	 * @return
	 */
	public boolean goalUpdate(Mind mind) {
		this.completeness = Math.min(100, Math.max(0, (float) checkCompletion(mind)));
		return this.isComplete();
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

	/**
	 * Returns whether this goal is a complete tasks goal, i.e. whether it branches
	 * into other tasks
	 * 
	 * @return
	 */
	public boolean isCompleteTasksGoal() {
		return false;
	}

	/**
	 * Whether this goal is empty of conditions or whatever
	 * 
	 * @return
	 */
	public abstract boolean isEmpty();

	@Override
	public String toString() {
		return this.getPriority().name().charAt(0) + "_" + this.getClass().getSimpleName() + ":" + this.completeness
				+ "%";
	}

	public String goalReport() {
		return this.toString();
	}

	/**
	 * returns the contents of this goal as a wrapper of RequirementGoals
	 * 
	 * @return
	 */
	public abstract RequirementWrapper getRequirementWrapper();

}
