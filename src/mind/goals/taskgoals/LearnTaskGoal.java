package mind.goals.taskgoals;

import mind.concepts.type.IConcept;
import mind.concepts.type.IProfile;
import mind.goals.ITaskGoal;
import mind.goals.ITaskHint;
import mind.goals.TaskHint;
import mind.memory.IHasKnowledge;

public class LearnTaskGoal implements ITaskGoal {

	private IConcept learnAbout;

	public LearnTaskGoal() {

	}

	public LearnTaskGoal(IConcept learnAbout) {
		this.learnAbout = learnAbout;
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
		return learnAbout != null ? entity.getKnowledgeBase().isKnown(learnAbout)
				: !entity.getMindMemory().isFeelingCurious();
	}

	@Override
	public String getUniqueName() {
		return "goal_LEARN";
	}

	@Override
	public ITaskHint getActionHint() {
		return TaskHint.LEARN;
	}

	@Override
	public IProfile getTarget() {
		return IProfile.SELF;
	}

	/**
	 * May be null if the learning is just general curiosity
	 */
	@Override
	public IConcept getLearnTarget() {
		return this.learnAbout;
	}

	@Override
	public boolean isInvalid(IHasKnowledge knower) {
		return !knower.isMindMemory();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + (this.learnAbout != null ? "{" + learnAbout + "}" : "");
	}

}
