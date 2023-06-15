package mind.need;

import mind.goals.IGoal;

public class JusticeNeed extends AbstractNeed {

	public JusticeNeed(Degree degree/* , Event deed */) {
		super(NeedType.JUSTICE, degree);
	}

	@Override
	public IGoal genIndividualGoal() {
		// TODO individual+ social justice goals
		return null;
	}

	@Override
	public IGoal genSocietalGoal() {
		return null;
	}

}
