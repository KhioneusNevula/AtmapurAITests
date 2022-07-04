package psych_first.action.types;

import culture.CulturalContext;
import culture.Culture;
import psych_first.action.ActionType;
import psych_first.action.goal.Goal;
import psych_first.action.goal.RequirementGoal;
import psych_first.action.goal.RequirementWrapper;
import psych_first.action.goal.Task;
import psych_first.action.goal.Goal.Priority;
import psych_first.actionstates.checks.Check;
import psych_first.actionstates.checks.SociopropProfileMatchingCheck;
import psych_first.actionstates.states.StateBuilder;
import psych_first.mind.Mind;
import psych_first.mind.Will;
import sim.IHasProfile;
import sociology.ProfileType;
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
	protected int startExecution(IHasProfile actor, Will will, Task task, Goal requirements, Goal result) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RequirementWrapper generateRequirements(Mind fromMind, Goal goal, StateBuilder builder) {
		if (!fromMind.getCulture().containsInHierarchy(Culture.EMBODIED))
			return null;

		if (goal instanceof RequirementGoal res) {
			SociopropProfileMatchingCheck check = (SociopropProfileMatchingCheck) res.getState()
					.getFor(ProfileType.USER).getCondition(Socioprops.ACTOR_HELD);

			if (check != null && check.isProfilePlaceholder()) {
				return builder.removeConditionForChecker(ProfileType.USER, Socioprops.ACTOR_HELD)
						.addLocationCondition(ProfileType.USER, check.getProfilePlaceholder().getType())
						.requireResolved(check.getProfilePlaceholder().getType()).buildWrapper(Priority.IMPORTANT);
			}
		}
		return null;
	}

	@Override
	public String interpretExecutionProblem(Goal requirements, Goal result, Object execInfo, CulturalContext ctxt) {
		if (!doesPlaceholderSatisfy(requirements, ProfileType.USER, Check.Fundamental.AT, ctxt)) {
			return "not at desired location";
		}
		return super.interpretExecutionProblem(requirements, result, execInfo, ctxt);
	}

}
