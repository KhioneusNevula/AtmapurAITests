package mind.action.types;

import java.util.Collection;
import java.util.Set;

import actor.Actor;
import mind.ICanAct;
import mind.action.ActionType;
import mind.action.IAction;
import mind.action.IActionType;
import mind.goals.ITaskGoal;
import mind.goals.taskgoals.TravelTaskGoal;
import sim.Location;

public class WanderAction implements IAction {

	private Location randomLoc;
	private String failure = "n/a";

	public WanderAction(ITaskGoal goal) {
	}

	@Override
	public boolean canExecuteIndividual(ICanAct user, boolean pondering) {
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
	public void beginExecutingIndividual(ICanAct forUser) {
		if (forUser.getAsMind().rand().nextInt(10) < 4)
			forUser.getMindMemory().setFeelingCurious(false);

	}

	@Override
	public Collection<ITaskGoal> genConditionGoal(ICanAct user) {
		return Set.of(new TravelTaskGoal(randomLoc, true));
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
