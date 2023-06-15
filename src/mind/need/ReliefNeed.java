package mind.need;

import mind.goals.IGoal;

public class ReliefNeed extends AbstractNeed {

	public ReliefNeed(Degree degree/* , Feeling pain */) {
		super(NeedType.RELIEF, degree);
	}

	@Override
	public IGoal genIndividualGoal() {
		// TODO gen relief goals
		return null;
	}

	@Override
	public IGoal genSocietalGoal() {
		throw new UnsupportedOperationException("Relief goals are individual only");
	}

}
