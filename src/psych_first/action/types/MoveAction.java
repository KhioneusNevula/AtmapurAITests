package psych_first.action.types;

import culture.CulturalContext;
import psych_first.action.ActionType;
import psych_first.action.goal.Goal;
import psych_first.action.goal.Goal.Priority;
import psych_first.action.goal.RequirementGoal;
import psych_first.action.goal.RequirementWrapper;
import psych_first.action.goal.Task;
import psych_first.actionstates.checks.AtCheck;
import psych_first.actionstates.checks.Check;
import psych_first.actionstates.states.StateBuilder;
import psych_first.mind.Mind;
import psych_first.mind.Will;
import sim.IHasProfile;
import sociology.ProfilePlaceholder;
import sociology.ProfileType;

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
	protected int startExecution(IHasProfile actor, Will will, Task task, Goal requirements, Goal result) {
		// TODO make an actual algorithm lol idk

		return 0;
	}

	@Override
	public RequirementWrapper generateRequirements(Mind fromMind, Goal goal, StateBuilder builder) {

		if (goal instanceof RequirementGoal req) {
			if (req.getState().getFor(ProfileType.USER).hasConditionFor(Check.Fundamental.AT)) {
				return builder.removeConditionForChecker(ProfileType.USER, Check.Fundamental.AT)
						.requireResolved(
								AtCheck.get(req.getState().getFor(ProfileType.USER)).getProfilePlaceholder().getProfileType())
						.buildWrapper(Priority.IMPORTANT);
			}
		}

		return null;
	}

	@Override
	public String interpretExecutionProblem(Goal requirements, Goal result, Object execInfo, CulturalContext ctxt) {

		if (result instanceof RequirementGoal goal) {
			ProfilePlaceholder pp = AtCheck.get(goal.getState().getFor(ProfileType.USER)).getProfilePlaceholder();
			if (!pp.isResolved())
				return "location unresolved";

		}

		return super.interpretExecutionProblem(requirements, result, execInfo, ctxt);
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
