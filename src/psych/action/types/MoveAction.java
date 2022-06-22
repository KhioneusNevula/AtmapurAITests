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

public class MoveAction extends Action {

	public MoveAction() {
		super("move");
		this.setActionType(ActionType.MOTION);
	}

	@Override
	protected String complete(IHasProfile actor, Will will, Task task, Goal reqs, Goal result, int ticksLeft) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int startExecution(IHasProfile actor, Will will, Goal requirements, Goal result) {
		// TODO make an actual algorithm lol idk

		return 0;
	}

	@Override
	public RequirementWrapper generateRequirements(Mind fromMind, Goal goal, StateBuilder builder) {
		if (goal instanceof RequirementGoal req) {
			if (req.getState().getFor(ProfileType.USER).hasConditionFor(Check.Fundamental.AT)) {
				return builder.removeConditionForChecker(ProfileType.USER, Check.Fundamental.AT)
						.requireResolved(
								AtCheck.get(req.getState().getFor(ProfileType.USER)).getProfilePlaceholder().getType())
						.buildWrapper(Priority.IMPORTANT);
			}
		}

		return null;
	}

	@Override
	protected boolean canContinueExecution(IHasProfile actor, Will will, Task task, Goal requirements, Goal result) {
		// TODO continue execution move action
		return super.canContinueExecution(actor, will, task, requirements, result);
	}

	@Override
	protected boolean canExecute(IHasProfile actor, Will will, Task task, Goal requirements, Goal result) {
		// TODO can execute move action
		return super.canExecute(actor, will, task, requirements, result);
	}

}
