package mind.need;

import mind.goals.IGoal;

public class ShelterNeed extends AbstractNeed {

	/**
	 * no arguments; shelter is a generalized concept
	 * 
	 * @param deg
	 */
	public ShelterNeed(Degree deg) {
		super(NeedType.CREATIVITY, deg);
	}

	@Override
	public IGoal genIndividualGoal() {
		return null;
	}

	@Override
	public IGoal genSocietalGoal() {
		return null;
	}

}
