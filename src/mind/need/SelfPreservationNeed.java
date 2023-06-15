package mind.need;

import mind.concepts.type.IConcept;
import mind.goals.IGoal;

public class SelfPreservationNeed extends AbstractNeed {

	private IConcept danger;

	public SelfPreservationNeed(Degree degree, IConcept protectFrom) {
		super(NeedType.SELF_PRESERVATION, degree);
		this.danger = protectFrom;
	}

	public IConcept getDanger() {
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
