package mind.need;

import mind.concepts.type.IConcept;
import mind.concepts.type.IProfile;
import mind.goals.IGoal;
import mind.goals.RoleGoal;
import mind.goals.taskgoals.AcquireTaskGoal;
import mind.goals.taskgoals.EatTaskGoal;

public class SustenanceNeed extends AbstractNeed {

	private IConcept type;

	/**
	 * What type of food is needed (i.e. some civilization needs both food and
	 * water, or both food and blood, or whatever
	 * 
	 * @param degree
	 * @param type
	 */
	public SustenanceNeed(Degree degree, IConcept type) {
		super(NeedType.SUSTENANCE, degree);
		this.type = type;
	}

	public IConcept getFoodType() {
		return type;
	}

	@Override
	public IGoal genIndividualGoal() {
		switch (this.getDegree()) {
		case BEYOND:
			return null; // TODO beyond eating goal
		default:
			return new EatTaskGoal(type, EatTaskGoal.DEFAULT_PERCENT); // TODO check if the food is edible or drinkable
		}
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
