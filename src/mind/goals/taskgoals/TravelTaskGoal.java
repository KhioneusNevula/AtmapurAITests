package mind.goals.taskgoals;

import actor.Actor;
import mind.concepts.type.ILocationMeme;
import mind.concepts.type.IProfile;
import mind.goals.IGoal;
import mind.goals.ITaskGoal;
import mind.goals.ITaskHint;
import mind.goals.TaskHint;
import mind.thought_exp.IUpgradedHasKnowledge;
import sim.interfaces.IPhysicalExistence;

public class TravelTaskGoal implements ITaskGoal {

	private IProfile target = IProfile.SELF;

	private ILocationMeme location;

	private boolean reachDistance;
	private Priority priority = Priority.NORMAL;

	/**
	 * 
	 * @param targetLoc
	 * @param reachDistance if the entity needs to precisely be at the location or
	 *                      only within reach distance
	 */
	public TravelTaskGoal(ILocationMeme targetLoc, boolean reachDistance) {
		this.location = targetLoc;
		this.reachDistance = reachDistance;
	}

	public Priority getPriority() {
		return priority;
	}

	public TravelTaskGoal setPriority(Priority priority) {
		this.priority = priority;
		return this;
	}

	@Override
	public ILocationMeme targetLocation() {
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
	public IProfile beneficiary() {
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
	public boolean isInvalid(IUpgradedHasKnowledge knower) {
		if (reachDistance && !(knower instanceof Actor))
			return true;
		if (knower instanceof IPhysicalExistence)
			return false;
		return true;
	}

	@Override
	public boolean isComplete(IUpgradedHasKnowledge entity) {
		return entity.getAsHasActor().getActor().distance(
				location.getGeneralLocation()) <= (reachDistance ? (entity.getAsHasActor().getActor()).getReach() : 0);
	}

	@Override
	public String toString() {
		return "TravelTG{" + this.location + "}";
	}

	@Override
	public String getUniqueName() {
		return "goal_task_travel_x" + this.location;
	}

	@Override
	public boolean equivalent(IGoal other) {
		return ITaskGoal.super.equivalent(other) && other instanceof TravelTaskGoal
				&& ((TravelTaskGoal) other).location.equals(this.location);
	}

}
