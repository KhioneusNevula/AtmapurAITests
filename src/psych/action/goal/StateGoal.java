package psych.action.goal;

import psych.Mind;
import psych.action.Action;
import psych.actionstates.states.WorldState;
import sociology.Profile;

public class StateGoal extends Goal {

	private WorldState target;

	public StateGoal(Mind owner, WorldState target) {
		super(owner);
		this.target = target;
	}

	@Override
	protected boolean checkCompletion() {
		return target.isAccurate(getMind());
	}

	@Override
	public int contributionFactor(Action act, Profile from) {
		int reqs = (int) target.getConstituents().stream().flatMap((a) -> a.getConditions().keySet().stream()).count();
		return reqs - ((int) act.getResult(from).satisfiesRequirement(target).entrySet().stream()
				.flatMap((e) -> e.getValue().stream()).count());
	}

}
