package mind.action.types;

import actor.Actor;
import mind.action.ActionType;
import mind.action.IAction;
import mind.action.IActionType;
import mind.goals.ITaskGoal;
import mind.goals.taskgoals.TravelTaskGoal;
import mind.memory.IHasKnowledge;
import sim.Location;

public class WanderAction implements IAction {

	private Location randomLoc;
	private String failure = "n/a";

	public WanderAction(ITaskGoal goal) {
	}

	@Override
	public boolean canExecuteIndividual(IHasKnowledge user, boolean pondering) {
		Actor actor = user.getAsHasActor().getActor();
		if (randomLoc != null && actor.distance(randomLoc) < actor.getReach())
			return true;
		if (pondering) {
			int x = Math.max(0, Math.min(actor.getWorld().getWidth(), actor.getX()
					+ (actor.rand().nextBoolean() ? 1 : -1) * actor.rand().nextInt(actor.getWorld().getWidth() / 4)));
			int y = Math.max(0, Math.min(actor.getWorld().getHeight(), actor.getY()
					+ (actor.rand().nextBoolean() ? 1 : -1) * actor.rand().nextInt(actor.getWorld().getHeight() / 4)));
			randomLoc = new Location(x, y);
			failure = "finding location for travel at " + randomLoc;
		}
		return false;
	}

	@Override
	public void beginExecutingIndividual(IHasKnowledge forUser) {
		if (forUser.getAsMind().rand().nextInt(10) < 4)
			forUser.getMindMemory().setFeelingCurious(false);

	}

	@Override
	public ITaskGoal genConditionGoal(IHasKnowledge user) {
		return new TravelTaskGoal(randomLoc, true);
	}

	@Override
	public IActionType<?> getType() {
		return ActionType.WANDER;
	}

	@Override
	public String toString() {
		return "WanderA{" + (this.randomLoc == null ? "" : this.randomLoc.toString()) + "}";
	}

	@Override
	public int executionAttempts() {
		return 10;
	}

	@Override
	public String reasonForLastFailure() {
		return failure;
	}

}
