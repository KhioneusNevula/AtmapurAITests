package mind.goals.taskgoals;

import mind.concepts.type.IProfile;
import mind.goals.IGoal;
import mind.goals.ITaskGoal;
import mind.goals.ITaskHint;
import mind.goals.TaskHint;
import mind.memory.IHasKnowledge;

public enum RestTaskGoal implements ITaskGoal {

	INSTANCE;

	@Override
	public ITaskHint getActionHint() {
		return TaskHint.REST;
	}

	@Override
	public IProfile getTarget() {
		return IProfile.SELF;
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
	public boolean isComplete(IHasKnowledge entity) {
		return !entity.getAsMind().isConscious();
	}

	@Override
	public boolean isInvalid(IHasKnowledge knower) {
		return false;
	}

	@Override
	public String toString() {
		return "RestGoal";
	}

	@Override
	public String getUniqueName() {
		return "goal_task_rest";
	}

	@Override
	public boolean equivalent(IGoal other) {
		return other == this;
	}

}
