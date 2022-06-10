package psych.action.goal;

import psych.Mind;
import psych.Need;
import psych.action.Action;
import psych.action.types.NeedAction;
import sociology.Profile;

public class NeedGoal extends Goal {

	private Need focus;
	private int level;

	public NeedGoal(Mind owner, Need need, int level) {
		super(owner);
		this.focus = need;
		this.level = level;
	}

	@Override
	protected boolean checkCompletion() {
		return this.getMind().getNeed(focus) >= level;
	}

	@Override
	public int contributionFactor(Action act, Profile from) {
		return act instanceof NeedAction ? ((NeedAction) act).getContribution(from) : 0;
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

}
