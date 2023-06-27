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
		case MILD:
			return RestTaskGoal.TRIVIAL;
		case MODERATE:
			return RestTaskGoal.NORMAL;
		case SEVERE:
			return RestTaskGoal.SERIOUS;
		}
		return null;
	}

	@Override
	public IGoal genSocietalGoal() {
		return null;
	}

}
