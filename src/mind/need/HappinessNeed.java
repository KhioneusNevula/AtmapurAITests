package mind.need;

import mind.goals.IGoal;

public class HappinessNeed extends AbstractNeed {

	public HappinessNeed(Degree degree) {
		super(NeedType.HAPPINESS, degree);
	}

	@Override
	public IGoal genIndividualGoal() {
		// TODO happiness goals
		return null;
	}

	@Override
	public IGoal genSocietalGoal() {
		return null;
	}

}
