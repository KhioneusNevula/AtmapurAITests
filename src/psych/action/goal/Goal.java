package psych.action.goal;

import psych.Mind;
import psych.action.Action;
import sociology.Profile;

public abstract class Goal {

	protected boolean complete;
	private Mind mind;

	public Goal(Mind owner) {
		this.mind = owner;
	}

	public boolean isComplete() {
		return complete;
	}

	public Mind getMind() {
		return mind;
	}

	/**
	 * Uses the mind's state to check whether complete or not; return true if
	 * complete
	 */
	protected abstract boolean checkCompletion();

	/**
	 * Whether the given action would contribute to completing this goal
	 * 
	 * @param act
	 * @return
	 */
	public boolean canContributeToGoal(Action act, Profile from) {
		return contributionFactor(act, from) > 0;
	}

	/**
	 * Returns to what numeric degree this action would contribute to completing
	 * this goal; positive for
	 * 
	 * @param act
	 * @return
	 */
	public abstract int contributionFactor(Action act, Profile from);

	public void goalUpdate() {
		this.complete = checkCompletion();
	}

	@Override
	public String toString() {
		return (complete ? "COMPLETE " : "INCOMPLETE ") + this.getClass().getSimpleName();
	}

}
