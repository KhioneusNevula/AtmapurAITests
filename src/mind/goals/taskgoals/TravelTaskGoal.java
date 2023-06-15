package mind.goals.taskgoals;

import actor.Actor;
import mind.concepts.type.IProfile;
import mind.goals.IGoal;
import mind.goals.ITaskGoal;
import mind.goals.ITaskHint;
import mind.goals.TaskHint;
import mind.memory.IHasKnowledge;
import sim.Location;
import sim.interfaces.IPhysicalExistence;

public class TravelTaskGoal implements ITaskGoal {

	private IProfile target = IProfile.SELF;

	private Location location;

	private boolean reachDistance;

	/**
	 * 
	 * @param targetLoc
	 * @param reachDistance if the entity needs to precisely be at the location or
	 *                      only within reach distance
	 */
	public TravelTaskGoal(Location targetLoc, boolean reachDistance) {
		this.location = targetLoc;
		this.reachDistance = reachDistance;
	}

	@Override
	public Location getTargetLocation() {
		return location;
	}

	/**
	 * If the completer of this goal only needs to be within reach distance of the
	 * location
	 */
	public boolean onlyReachDistance() {
		return reachDistance;
	}

	@Override
	public ITaskHint getActionHint() {
		return TaskHint.TRAVEL;
	}

	@Override
	public IProfile getTarget() {
		return target;
	}

	@Override
	public boolean individualGoal() {
		return true;
	}

	@Override
	public boolean societalGoal() {
		return false;
	}

	@Override
	public boolean isInvalid(IHasKnowledge knower) {
		if (reachDistance && !(knower instanceof Actor))
			return true;
		if (knower instanceof IPhysicalExistence)
			return false;
		return true;
	}

	@Override
	public boolean isComplete(IHasKnowledge entity) {
		return entity.getAsHasActor().getActor()
				.distance(location) <= (reachDistance ? (entity.getAsHasActor().getActor()).REACH : 0);
	}

	@Override
	public String toString() {
		return "TravelGoal{" + this.location + "}";
	}

	@Override
	public String getUniqueName() {
		return "goal_task_travel_x" + this.location.getX() + "_y" + this.location.getY();
	}

	@Override
	public boolean equivalent(IGoal other) {
		return ITaskGoal.super.equivalent(other) && other instanceof TravelTaskGoal
				&& ((TravelTaskGoal) other).location.equals(this.location);
	}

}
