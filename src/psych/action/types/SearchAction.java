package psych.action.types;

import psych.action.ActionType;
import psych.action.goal.Goal;
import psych.action.goal.Goal.Priority;
import psych.action.goal.RequirementGoal;
import psych.action.goal.RequirementWrapper;
import psych.action.goal.Task;
import psych.actionstates.checks.AtCheck;
import psych.actionstates.checks.Check;
import psych.actionstates.states.State.ProfileType;
import psych.actionstates.states.StateBuilder;
import psych.mind.Mind;
import psych.mind.Will;
import sim.IHasProfile;
import sociology.ProfilePlaceholder;

public class SearchAction extends Action {

	public SearchAction() {
		super("search");
		this.setActionType(ActionType.MENTAL);
	}

	@Override
	protected String complete(IHasProfile actor, Will will, Task task, Goal reqs, Goal result, int ticksLeft) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int startExecution(IHasProfile actor, Will will, Goal requirements, Goal result) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RequirementWrapper generateRequirements(Mind fromMind, Goal goal, StateBuilder builder) {
		if (goal instanceof RequirementGoal res) {
			AtCheck atCheck = (AtCheck) res.getState().getFor(ProfileType.USER).getCondition(Check.Fundamental.AT);
			if (atCheck == null)
				return null;
			ProfilePlaceholder pp = atCheck.getProfile() instanceof ProfilePlaceholder
					? (ProfilePlaceholder) atCheck.getProfile()
					: null;
			if (pp == null)
				return null;

			if (!pp.isResolved()) {
				return builder.removeConditionForChecker(ProfileType.USER, Check.Fundamental.AT)
						.requireUnResolved(atCheck.getProfilePlaceholder().getType()).buildWrapper(Priority.IMPORTANT);
			}
		}
		return null;
	}

}
