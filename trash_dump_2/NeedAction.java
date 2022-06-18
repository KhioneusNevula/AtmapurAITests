package psych.action.types;

import psych.Mind;
import psych.Need;
import psych.action.Action;
import sociology.Profile;

public class NeedAction extends Action {

	private Need focus;

	public NeedAction(String name, Need fulfill) {
		super(name, ActionType.EAT);
		this.focus = fulfill;
	}

	public int getContribution(Profile from) {
		return focus.getFulfillmentValue(from);
	}

	@Override
	public boolean execute(Mind actor) {
		Integer h = getContribution(forTarget);
		focus.changeNeedValue(actor, h);
		return h > 0;
	}

	@Override
	public String toString() {
		return super.toString() + " for need: " + focus;
	}

}
