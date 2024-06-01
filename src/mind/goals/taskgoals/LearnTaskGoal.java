package mind.goals.taskgoals;

import mind.concepts.type.IProfile;
import mind.goals.IGoal;
import mind.goals.ITaskGoal;
import mind.goals.ITaskHint;
import mind.goals.TaskHint;
import mind.goals.question.Question;
import mind.thought_exp.IThought;
import mind.thought_exp.IUpgradedHasKnowledge;

public class LearnTaskGoal implements ITaskGoal {

	private Question question;
	private Priority priority = Priority.NORMAL;
	private boolean needThoughtToAnswer = false;

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
	public boolean isComplete(IUpgradedHasKnowledge entity) {
		if (question == null) {
			return true; // TODO curiosity emotion
		}
		Boolean ans = question.isAnswered(entity);
		if (ans == null) {
			needThoughtToAnswer = true;
			return false;
		}
		return ans;
	}

	@Override
	public IThought checkCompletion(IUpgradedHasKnowledge mind) {
		needThoughtToAnswer = false;
		return question.getAnsweringThought(mind);
	}

	@Override
	public IThought checkCompletionAndRemember(IUpgradedHasKnowledge mind) {
		return question.getAnsweringThoughtAndRemember(mind);
	}

	@Override
	public boolean checkResult(IThought thought) {
		return question.isAnsweredAccordingToThought(thought);
	}

	@Override
	public boolean useThoughtToCheckCompletion(IUpgradedHasKnowledge mind) {
		if (needThoughtToAnswer)
			return needThoughtToAnswer;
		else {
			return question.isAnswered(mind) == null;
		}
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
	public Question learnInfo() {
		return this.question;
	}

	@Override
	public boolean isInvalid(IUpgradedHasKnowledge knower) {
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

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LearnTaskGoal ltg) {
			return this.question == ltg.question || this.question.equals(ltg.question);
		}
		return super.equals(obj);
	}

}
