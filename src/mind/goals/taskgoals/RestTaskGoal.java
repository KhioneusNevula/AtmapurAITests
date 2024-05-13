package mind.goals.taskgoals;

import mind.concepts.type.IProfile;
import mind.goals.IGoal;
import mind.goals.ITaskGoal;
import mind.goals.ITaskHint;
import mind.goals.TaskHint;
import mind.thought_exp.IThought;
import mind.thought_exp.IUpgradedHasKnowledge;

public enum RestTaskGoal implements ITaskGoal {

	OBSESSION(Priority.OBSESSION), VITAL(Priority.VITAL), SERIOUS(Priority.SERIOUS), NORMAL(Priority.NORMAL),
	TRIVIAL(Priority.TRIVIAL);

	private Priority priority;

	private RestTaskGoal(Priority vital2) {
		this.priority = vital2;
	}

	@Override
	public Priority getPriority() {
		return priority;
	}

	@Override
	public ITaskHint getActionHint() {
		return TaskHint.REST;
	}

	@Override
	public IProfile beneficiary() {
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
	public boolean isComplete(IUpgradedHasKnowledge entity) {
		return entity.getAsMind().ticksSinceFallingAsleep() < 10;
	}

	@Override
	public IThought checkCompletion(IUpgradedHasKnowledge mind) {
		return null;
	}

	@Override
	public boolean useThoughtToCheckCompletion(IUpgradedHasKnowledge mind) {
		return false;
	}

	@Override
	public boolean isInvalid(IUpgradedHasKnowledge knower) {
		return false;
	}

	@Override
	public String toString() {
		return "RestTG";
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
