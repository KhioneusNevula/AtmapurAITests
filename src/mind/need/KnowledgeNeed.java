package mind.need;

import mind.concepts.type.IConcept;
import mind.goals.IGoal;
import mind.goals.taskgoals.LearnTaskGoal;

public class KnowledgeNeed extends AbstractNeed {

	private IConcept topic;

	public KnowledgeNeed(Degree degree, IConcept topic) {
		super(NeedType.KNOWLEDGE, degree);
		this.topic = topic;
	}

	public IConcept getTopic() {
		return topic;
	}

	@Override
	public IGoal genIndividualGoal() {
		// TODO knowledge need goals
		switch (this.getDegree()) {
		case BEYOND:
			return null;
		case MILD:
			return new LearnTaskGoal(topic);
		default:
			return null;
		}
	}

	@Override
	public IGoal genSocietalGoal() {
		return null;
	}

}
