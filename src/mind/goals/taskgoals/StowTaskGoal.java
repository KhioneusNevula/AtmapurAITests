package mind.goals.taskgoals;

import actor.Actor;
import mind.concepts.type.IProfile;
import mind.goals.IGoal;
import mind.goals.ITaskGoal;
import mind.goals.ITaskHint;
import mind.goals.TaskHint;
import mind.thought_exp.IUpgradedHasKnowledge;

public class StowTaskGoal implements ITaskGoal {

	private Priority priority = Priority.NORMAL;

	/**
	 * TODO allow specifying which body part holds the item, and where to stow
	 */
	public StowTaskGoal(/** IComponentPart holding, Location loc */
	) {

	}

	public Priority getPriority() {
		return priority;
	}

	public StowTaskGoal setPriority(Priority priority) {
		this.priority = priority;
		return this;
	}

	@Override
	public ITaskHint getActionHint() {
		return TaskHint.ACQUIRE;
	}

	@Override
	public boolean isComplete(IUpgradedHasKnowledge entity) {

		return entity.getAsHasActor().getActor().getHeld() == null;
	}

	@Override
	public IProfile beneficiary() {
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
	public boolean isInvalid(IUpgradedHasKnowledge knower) {

		return !(knower instanceof Actor) || knower.getAsHasActor().getActor().getHeld() == null;
	}

	@Override
	public String toString() {
		return "StowTG";
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
