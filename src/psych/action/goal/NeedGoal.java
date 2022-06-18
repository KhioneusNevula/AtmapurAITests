package psych.action.goal;

import psych.action.Action;
import psych.actionstates.states.State;
import psych.mind.Mind;
import psych.mind.Need;

public class NeedGoal extends Goal {

	private Need focus;
	private int level;

	public NeedGoal(Need need, int level) {
		this.focus = need;
		this.level = level;

	}

	@Override
	protected double checkCompletion(Mind for_) {
		return (for_.getNeed(focus) / (double) level) * 100;
	}

	@Override
	public String toString() {
		return super.toString() + " for need " + focus + " at level " + level;
	}

	public Need getFocus() {
		return focus;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public double contributionFactor(Action act, State result, Mind mind) {
		// TODO Auto-generated method stub
		return 0;
	}

}
