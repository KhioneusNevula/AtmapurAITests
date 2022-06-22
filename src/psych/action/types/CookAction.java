package psych.action.types;

import psych.action.ActionType;
import psych.action.goal.Goal;
import psych.action.goal.RequirementGoal;
import psych.action.goal.RequirementWrapper;
import psych.action.goal.Task;
import psych.actionstates.checks.Check;
import psych.actionstates.checks.SociopropProfileMatchingCheck;
import psych.actionstates.states.State.ProfileType;
import psych.actionstates.states.StateBuilder;
import psych.mind.Mind;
import psych.mind.Will;
import sim.IHasProfile;
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
	protected int startExecution(IHasProfile actor, Will will, Goal requirements, Goal result) {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public RequirementWrapper generateRequirements(Mind fromMind, Goal goal, StateBuilder builder) {
		if (goal instanceof RequirementGoal res) {
			SociopropProfileMatchingCheck check = (SociopropProfileMatchingCheck) res.getState()
					.getFor(ProfileType.USER).getCondition(Socioprops.ACTOR_HELD);

			if (check != null && check.isProfilePlaceholder() && !check.getProfilePlaceholder().isResolved()) {
				if (res.getState().getFor(check.getProfilePlaceholder().getType())
						.hasCondition(Check.createSociocatCheck(Sociocat.FOOD, true))) {

					// TODO create proper conditions for cooking; for now, returns empty
					return RequirementWrapper.EMPTY;
				}
			}
		}
		return null;
	}

}
