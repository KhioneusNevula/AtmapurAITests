package mind.need;

import mind.concepts.type.Profile;
import mind.goals.IGoal;
import mind.goals.IGoal.Priority;
import mind.goals.taskgoals.SocializeTaskGoal;

public class CommunityNeed extends AbstractNeed {

	private Profile with;

	/**
	 * "With" may be null; this is just if the person needs to spend time with a
	 * specific individual or group such as a friend
	 * 
	 * @param degree
	 * @param with
	 */
	public CommunityNeed(Degree degree, Profile with) {
		super(NeedType.COMMUNITY, degree);
		this.with = with;
	}

	@Override
	public IGoal genIndividualGoal() {
		switch (this.getDegree()) {
		case BEYOND:
			return new SocializeTaskGoal(with).setPriority(Priority.OBSESSION);
		case MILD:
			return new SocializeTaskGoal(with).setPriority(Priority.TRIVIAL);
		case MODERATE:
			return new SocializeTaskGoal(with).setPriority(Priority.NORMAL);
		case SEVERE:
			return new SocializeTaskGoal(with).setPriority(Priority.SERIOUS);
		}
		return null;
	}

	@Override
	public IGoal genSocietalGoal() {
		return null;
	}
}
