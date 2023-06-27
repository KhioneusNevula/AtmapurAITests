package mind.need;

import mind.concepts.type.IMeme;
import mind.goals.IGoal;

public class SelfPreservationNeed extends AbstractNeed {

	private IMeme danger;

	public SelfPreservationNeed(Degree degree, IMeme protectFrom) {
		super(NeedType.SELF_PRESERVATION, degree);
		this.danger = protectFrom;
	}

	public IMeme getDanger() {
		return danger;
	}

	@Override
	public IGoal genIndividualGoal() {
		// TODO generate self preservation goal
		return null;
	}

	@Override
	public IGoal genSocietalGoal() {
		throw new UnsupportedOperationException();
	}

}
