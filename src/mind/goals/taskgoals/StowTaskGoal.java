package mind.goals.taskgoals;

import actor.Actor;
import mind.concepts.type.IProfile;
import mind.goals.IGoal;
import mind.goals.ITaskGoal;
import mind.goals.ITaskHint;
import mind.goals.TaskHint;
import mind.memory.IHasKnowledge;

public class StowTaskGoal implements ITaskGoal {

	/**
	 * TODO allow specifying which body part holds the item, and where to stow
	 */
	public StowTaskGoal(/** IComponentPart holding, Location loc */
	) {

	}

	@Override
	public ITaskHint getActionHint() {
		return TaskHint.ACQUIRE;
	}

	@Override
	public boolean isComplete(IHasKnowledge entity) {

		return entity.getAsHasActor().getActor().getHeld() == null;
	}

	@Override
	public IProfile getTarget() {
		return null;
	}

	@Override
	public boolean societalGoal() {
		return false;
	}

	@Override
	public boolean individualGoal() {
		return true;
	}

	@Override
	public boolean isInvalid(IHasKnowledge knower) {

		return !(knower instanceof Actor) || knower.getAsHasActor().getActor().getHeld() == null;
	}

	@Override
	public String toString() {
		return "StowGoal";
	}

	@Override
	public String getUniqueName() {
		return "goal_task_stow";
	}

	@Override
	public boolean equivalent(IGoal other) {
		return ITaskGoal.super.equivalent(other) && other instanceof StowTaskGoal;
	}

}
