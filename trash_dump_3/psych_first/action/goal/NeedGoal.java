package psych_first.action.goal;

import psych_first.action.types.Action;
import psych_first.actionstates.states.ActionState;
import psych_first.actionstates.states.State;
import psych_first.mind.Mind;
import psych_first.mind.Need;

public class NeedGoal extends Goal {

	private Need focus;
	private int level;

	public NeedGoal(Need need, int level) {
		this.focus = need;
		this.level = level;

	}

	@Override
	protected double checkCompletion(Mind for_) {
		return (for_.getNeeds().getNeed(focus) / (double) level) * 100;
	}

	@Override
	public String toString() {
		return super.toString() + ":" + focus + ":" + level;
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

	@Override
	public Priority getPriority() {
		return focus.getPriority();
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public RequirementWrapper getRequirementWrapper() {
		return RequirementWrapper.create(new RequirementGoal(new ActionState(this), this.getPriority()));
	}

}
