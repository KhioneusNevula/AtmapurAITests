package mind.need;

import mind.goals.IGoal;
import mind.goals.IGoal.Priority;
import mind.goals.question.Question;
import mind.goals.taskgoals.LearnTaskGoal;

public class KnowledgeNeed extends AbstractNeed {

	private Question topic;

	public KnowledgeNeed(Degree degree, Question topic) {
		super(NeedType.KNOWLEDGE, degree);
		this.topic = topic;
	}

	public Question getTopic() {
		return topic;
	}

	@Override
	public IGoal genIndividualGoal() {
		// TODO knowledge need goals
		switch (this.getDegree()) {
		case BEYOND:
			return null;
		case MILD:
			return new LearnTaskGoal(topic).setPriority(Priority.TRIVIAL);
		default:
			return null;
		}
	}

	@Override
	public IGoal genSocietalGoal() {
		return null;
	}

}
