package psych.action.types;

import psych.action.ActionType;
import psych.action.goal.Goal;
import psych.action.goal.Goal.Priority;
import psych.action.goal.RequirementGoal;
import psych.action.goal.RequirementWrapper;
import psych.action.goal.Task;
import psych.actionstates.checks.SociopropProfileMatchingCheck;
import psych.actionstates.states.State.ProfileType;
import psych.actionstates.states.StateBuilder;
import psych.mind.Mind;
import psych.mind.Will;
import sim.IHasProfile;
import sociology.sociocon.Socioprops;

public class PickupAction extends Action {

	public PickupAction() {
		super("pickup");
		this.setActionType(ActionType.INTERACTION);
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
			SociopropProfileMatchingCheck check = (SociopropProfileMatchingCheck) res.getState()
					.getFor(ProfileType.USER).getCondition(Socioprops.ACTOR_HELD);

			if (check != null && check.isProfilePlaceholder()) {
				return builder.removeConditionForChecker(ProfileType.USER, Socioprops.ACTOR_HELD)
						.addLocationCondition(ProfileType.USER, ProfileType.TARGET).requireResolved(ProfileType.TARGET)
						.buildWrapper(Priority.IMPORTANT);
			}
		}
		return null;
	}

}
