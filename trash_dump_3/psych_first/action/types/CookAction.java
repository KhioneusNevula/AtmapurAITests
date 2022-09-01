package psych_first.action.types;

import culture.CulturalContext;
import psych_first.action.ActionType;
import psych_first.action.goal.Goal;
import psych_first.action.goal.RequirementGoal;
import psych_first.action.goal.RequirementWrapper;
import psych_first.action.goal.Task;
import psych_first.actionstates.checks.Check;
import psych_first.actionstates.checks.SociopropProfileMatchingCheck;
import psych_first.actionstates.states.StateBuilder;
import psych_first.mind.Mind;
import psych_first.mind.Will;
import sim.IHasProfile;
import sociology.ProfileType;
import sociology.sociocon.Sociocat;
import sociology.sociocon.Socioprops;

public class CookAction extends Action {

	public CookAction() {
		super("cook");
		this.setActionType(ActionType.INTERACTION);
	}

	@Override
	protected String complete(IHasProfile actor, Will will, Task task, Goal reqs, Goal result, int ticksLeft) {
		// TODO Auto-generated method stub
		return "cooking is unimplemented";
	}

	@Override
	public String interpretExecutionProblem(Goal requirements, Goal result, Object execInfo, CulturalContext ctxt) {
		// TODO Auto-generated method stub

		return "cooking unimplemented"; // super.interpretExecutionProblem(requirements, result, execInfo);
	}

	@Override
	protected int startExecution(IHasProfile actor, Will will, Task task, Goal requirements, Goal result) {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public RequirementWrapper generateRequirements(Mind fromMind, Goal goal, StateBuilder builder) {

		if (goal instanceof RequirementGoal res) {
			SociopropProfileMatchingCheck check = (SociopropProfileMatchingCheck) res.getState()
					.getFor(ProfileType.USER).getCondition(Socioprops.ACTOR_HELD);

			if (check != null && check.isProfilePlaceholder() && !check.getProfilePlaceholder().isResolved()) {
				if (res.getState().getFor(check.getProfilePlaceholder().getProfileType())
						.hasCondition(Check.createSociocatCheck(Sociocat.FOOD, true))) {

					// TODO create proper conditions for cooking; for now, returns empty
					return RequirementWrapper.EMPTY;
				}
			}
		}
		return null;
	}

}
