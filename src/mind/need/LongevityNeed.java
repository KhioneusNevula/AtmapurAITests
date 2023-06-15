package mind.need;

import mind.goals.IGoal;

public class LongevityNeed extends AbstractNeed {

	private boolean wantsChildren;

	/**
	 * If there is also a CommunityNeed for Family present, then the goal generated
	 * is more likely to be children
	 */
	public LongevityNeed(Degree degree, boolean wantsChildren) {
		super(NeedType.LONGEVITY, degree);
		this.wantsChildren = wantsChildren;
	}

	/**
	 * if the haver of this need wants children
	 */
	public boolean wantsChildren() {
		return wantsChildren;
	}

	@Override
	public IGoal genIndividualGoal() {
		// TODO longevity goals: children, going to the doctor, immortality
		return null;
	}

	@Override
	public IGoal genSocietalGoal() {
		return null;
	}

}
