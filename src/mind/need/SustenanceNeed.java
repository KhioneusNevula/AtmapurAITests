package mind.need;

import mind.concepts.type.IMeme;
import mind.concepts.type.IProfile;
import mind.goals.IGoal;
import mind.goals.IGoal.Priority;
import mind.goals.RoleGoal;
import mind.goals.taskgoals.AcquireTaskGoal;
import mind.goals.taskgoals.EatTaskGoal;

public class SustenanceNeed extends AbstractNeed {

	private IMeme type;

	/**
	 * What type of food is needed (i.e. some civilization needs both food and
	 * water, or both food and blood, or whatever
	 * 
	 * @param degree
	 * @param type
	 */
	public SustenanceNeed(Degree degree, IMeme type) {
		super(NeedType.SUSTENANCE, degree);
		this.type = type;
	}

	public IMeme getFoodType() {
		return type;
	}

	@Override
	public IGoal genIndividualGoal() {
		switch (this.getDegree()) {
		case BEYOND:
			return null; // TODO beyond eating goal
		case MILD:
			return new EatTaskGoal(type, 0.9f).setPriority(Priority.TRIVIAL);
		case MODERATE:
			return new EatTaskGoal(type, EatTaskGoal.DEFAULT_PERCENT).setPriority(Priority.NORMAL); // TODO check if the
																									// food is edible or
																									// drinkable
		case SEVERE:
			return new EatTaskGoal(type, EatTaskGoal.DEFAULT_PERCENT).setPriority(Priority.VITAL);

		}
		return null;
	}

	@Override
	public IGoal genSocietalGoal() {
		switch (this.getDegree()) {
		case BEYOND:
			return null; // TODO beyond eating societal goal
		default:
			return new RoleGoal(new AcquireTaskGoal(type, IProfile.RELATIONSHIP_PARTY));
		}
	}

}
