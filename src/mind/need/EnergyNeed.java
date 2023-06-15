package mind.need;

import mind.goals.IGoal;
import mind.goals.taskgoals.RestTaskGoal;

public class EnergyNeed extends AbstractNeed {

	public EnergyNeed(Degree degree) {
		super(NeedType.ENERGY, degree);
	}

	@Override
	public IGoal genIndividualGoal() {
		// TODO energy need
		switch (getDegree()) {
		case BEYOND:
			return null;
		default:
			return RestTaskGoal.INSTANCE;
		}
	}

	@Override
	public IGoal genSocietalGoal() {
		return null;
	}

}
