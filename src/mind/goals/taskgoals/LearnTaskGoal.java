package mind.goals.taskgoals;

import mind.concepts.type.IProfile;
import mind.goals.IGoal;
import mind.goals.ITaskGoal;
import mind.goals.ITaskHint;
import mind.goals.TaskHint;
import mind.goals.question.Question;
import mind.memory.IHasKnowledge;

public class LearnTaskGoal implements ITaskGoal {

	private Question question;
	private Priority priority = Priority.NORMAL;

	public LearnTaskGoal() {

	}

	public Priority getPriority() {
		return priority;
	}

	public LearnTaskGoal setPriority(Priority priority) {
		this.priority = priority;
		return this;
	}

	/**
	 * The target of learning, and the property to learn about (can be null if the
	 * information to learn is variable)
	 * 
	 * @param learnAbout
	 * @param learnProperty
	 */
	public LearnTaskGoal(Question question) {
		this.question = question;
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
		return question != null ? question.isAnswered(entity) : !entity.getMindMemory().isFeelingCurious();
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
	public IProfile beneficiary() {
		return IProfile.SELF;
	}

	/**
	 * May be null if the learning is just general curiosity
	 */
	@Override
	public Question learnTarget() {
		return this.question;
	}

	@Override
	public boolean isInvalid(IHasKnowledge knower) {
		return !knower.isMindMemory();
	}

	@Override
	public String toString() {
		return "LearnTG" + (this.question != null ? "{" + question + "}" : "");
	}

	@Override
	public boolean equivalent(IGoal other) {
		return other instanceof LearnTaskGoal && (((LearnTaskGoal) other).question == null
				|| ((LearnTaskGoal) other).question.getTopic().equals(this.question.getTopic()));
	}

}
