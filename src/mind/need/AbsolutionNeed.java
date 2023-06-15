package mind.need;

import mind.goals.IGoal;

public class AbsolutionNeed extends AbstractNeed {

	public AbsolutionNeed(Degree degree/* , Event deed */) {
		super(NeedType.ABSOLUTION, degree);
	}

	@Override
	public IGoal genIndividualGoal() {
		// TODO Absolution goals
		return null;
	}

	@Override
	public IGoal genSocietalGoal() {
		return null;
	}

}
