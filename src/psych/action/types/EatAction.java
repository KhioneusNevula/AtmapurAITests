package psych.action.types;

import psych.action.ActionType;
import psych.action.goal.Goal;
import psych.action.goal.Goal.Priority;
import psych.action.goal.NeedGoal;
import psych.action.goal.RequirementGoal;
import psych.action.goal.RequirementWrapper;
import psych.action.goal.Task;
import psych.actionstates.checks.Check;
import psych.actionstates.states.State.ProfileType;
import psych.actionstates.states.StateBuilder;
import psych.mind.Mind;
import psych.mind.Need;
import psych.mind.Will;
import sim.IHasProfile;
import sociology.sociocon.Sociocat;
import sociology.sociocon.Socioprops;

public class EatAction extends Action {

	public EatAction() {
		super("eat");
		setActionType(ActionType.MOUTH, ActionType.INTERACTION);
	}

	@Override
	protected String complete(IHasProfile actor, Will will, Task task, Goal reqs, Goal result, int ticksLeft) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int startExecution(IHasProfile actor, Will will, Goal requirements, Goal result) {

		return 0;
	}

	@Override
	public RequirementWrapper generateRequirements(Mind fromMind, Goal goal, StateBuilder builder) {
		if (goal instanceof NeedGoal && ((NeedGoal) goal).getFocus() == Need.SATIATION) {

			RequirementGoal foodGoal = builder
					.addProfileMatchingCondition(ProfileType.USER, Socioprops.ACTOR_HELD, ProfileType.TARGET)
					.addConditions(ProfileType.TARGET, Check.createSociocatCheck(Sociocat.FOOD, true),
							Check.checkIfKnown())

					.buildGoal(Priority.NECESSITY);

			RequirementWrapper reqs = RequirementWrapper.create(foodGoal);
			return reqs;
		}
		return null;
	}

}
