package mind.need;

import mind.goals.IGoal;
import mind.thought_exp.culture.UpgradedGroup;

public class PowerNeed extends AbstractNeed {

	private UpgradedGroup group;

	/**
	 * Optional group to indicate within which group the power is desired
	 * 
	 * @param degree
	 * @param group
	 */
	public PowerNeed(Degree degree, UpgradedGroup group) {
		super(NeedType.POWER, degree);
		this.group = group;
	}

	public UpgradedGroup getGroup() {
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
