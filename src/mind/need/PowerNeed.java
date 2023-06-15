package mind.need;

import mind.Group;
import mind.goals.IGoal;

public class PowerNeed extends AbstractNeed {

	private Group group;

	/**
	 * Optional group to indicate within which group the power is desired
	 * 
	 * @param degree
	 * @param group
	 */
	public PowerNeed(Degree degree, Group group) {
		super(NeedType.POWER, degree);
		this.group = group;
	}

	public Group getGroup() {
		return group;
	}

	@Override
	public IGoal genIndividualGoal() {
		// TODO power need
		return null;
	}

	@Override
	public IGoal genSocietalGoal() {
		return null;
	}
}
