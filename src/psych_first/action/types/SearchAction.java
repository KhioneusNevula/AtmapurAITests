package psych_first.action.types;

import culture.CulturalContext;
import entity.IPhysicalExistence;
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
import sim.Location;
import sociology.ProfilePlaceholder;
import sociology.ProfileType;

public class SearchAction extends Action {

	public SearchAction() {
		super("search");
		this.setActionType(ActionType.MOTION);
	}

	private class SearchData {
		Location target;
		Location origin;
	}

	@Override
	protected String complete(IHasProfile actor, Will will, Task task, Goal reqs, Goal result, int ticksLeft) {
		if (ticksLeft == -1) {
			return actor + " is not a movable entity";
		}
		return null;
	}

	@Override
	protected boolean canContinueExecution(IHasProfile actor, Will will, Task task, Goal requirements, Goal result) {
		return super.canContinueExecution(actor, will, task, requirements, result)

				&& task.getExecutionInformation(this) instanceof SearchData;
	}

	@Override
	protected void update(IHasProfile actor, Will will, Task task, Goal requirements, Goal result) {

	}

	@Override
	protected int startExecution(IHasProfile actor, Will will, Task task, Goal requirements, Goal result) {
		int searchRadius = 10; // TODO make this more general;
		int maxSearch = 400; // TODO make this more general
		if (actor instanceof IPhysicalExistence entity) {
			Location or = entity.getLocation();
			int dx = (entity.getWorld().rand().nextBoolean() ? -1 : 1) * entity.getWorld().rand().nextInt(searchRadius);
			int dy = (entity.getWorld().rand().nextBoolean() ? -1 : 1) * entity.getWorld().rand().nextInt(searchRadius);
			dx = Math.min(or.getX() + maxSearch, Math.max(or.getX() - maxSearch, dx));
			dy = Math.min(or.getY() + maxSearch, Math.max(or.getY() - maxSearch, dy));
			Location loc = new Location(or.getX() + dx, or.getY() + dy, entity.getWorld());
			SearchData dat = new SearchData();
			dat.origin = or;
			dat.target = loc;
			task.storeExecutionInformation(this, dat);
			return 400; // TODO make this more general
		}

		return -1; // TODO make this appropriate length
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
						.removeAllConditions(atCheck.getProfilePlaceholder().getProfileType())
						.requireUnResolved(atCheck.getProfilePlaceholder().getProfileType()).buildWrapper(Priority.IMPORTANT);
			}
		}
		return null;
	}

	@Override
	public String interpretExecutionProblem(Goal requirements, Goal result, Object execInfo, CulturalContext ctxt) {
		if (result instanceof RequirementGoal goal) {
			AtCheck check = AtCheck.get(goal.getState().getFor(ProfileType.USER));
			if (check == null) {
				return "invalid goal---does not test for location";
			}
			if (check.isProfileKnown()) {
				return "search cannot be used for a known location: " + check.getProfile().getOwner();
			}

		}
		return super.interpretExecutionProblem(requirements, result, execInfo, ctxt);
	}
}
