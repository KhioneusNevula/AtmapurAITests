package mind.goals.taskgoals;

import mind.concepts.type.IProfile;
import mind.concepts.type.Profile;
import mind.goals.IGoal;
import mind.goals.ITaskGoal;
import mind.goals.ITaskHint;
import mind.goals.TaskHint;
import mind.memory.IHasKnowledge;

public class SocializeTaskGoal implements ITaskGoal {

	private Profile with;
	private Priority priority = Priority.NORMAL;

	/**
	 * 'with' may be null if the need to socialize is just general
	 * 
	 * @param with
	 */
	public SocializeTaskGoal(Profile with) {
		this.with = with;
	}

	@Override
	public ITaskHint getActionHint() {
		return TaskHint.SOCIALIZE;
	}

	@Override
	public boolean individualGoal() {
		return true;
	}

	@Override
	public boolean societalGoal() {
		return false;
	}

	public Profile getWith() {
		return with;
	}

	@Override
	public Profile socialTarget() {
		return with;
	}

	@Override
	public boolean isComplete(IHasKnowledge entity) {
		return entity.getMindMemory().socializedRecently();
	}

	@Override
	public Priority getPriority() {
		return priority;
	}

	public SocializeTaskGoal setPriority(Priority priority) {
		this.priority = priority;
		return this;
	}

	@Override
	public String getUniqueName() {
		return "goal_SOCIALIZE";
	}

	@Override
	public IProfile beneficiary() {
		return IProfile.SELF;
	}

	@Override
	public String toString() {
		return "SocialTG";
	}

	@Override
	public boolean isInvalid(IHasKnowledge knower) {
		return !knower.isMindMemory();
	}

	@Override
	public boolean equivalent(IGoal other) {
		return other instanceof SocializeTaskGoal && (((SocializeTaskGoal) other).socialTarget() == null
				|| ((SocializeTaskGoal) other).socialTarget().equals(this.socialTarget()));
	}

}
