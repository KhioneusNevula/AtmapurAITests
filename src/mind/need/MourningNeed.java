package mind.need;

import mind.concepts.type.Profile;
import mind.goals.IGoal;

public class MourningNeed extends AbstractNeed {

	private Profile deceased;

	public MourningNeed(Degree grado, Profile deceased) {
		super(NeedType.MOURNING, grado);
		this.deceased = deceased;
	}

	public Profile getDeceased() {
		return deceased;
	}

	@Override
	public IGoal genIndividualGoal() {
		// TODO mourning goals
		return null;
	}

	@Override
	public IGoal genSocietalGoal() {
		return null;
	}

}
