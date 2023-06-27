package mind.need;

import com.google.common.collect.ImmutableSet;

import mind.concepts.type.IMeme;
import mind.goals.IGoal;

public class SafetyNeed extends AbstractNeed {

	private ImmutableSet<IMeme> dangers;

	/**
	 * optional argument representing the dangerous thing to be defended against
	 * 
	 * @param deg
	 */
	public SafetyNeed(Degree deg, IMeme... dangers) {
		super(NeedType.CREATIVITY, deg);
		if (dangers.length > 0)
			this.dangers = ImmutableSet.copyOf(dangers);
		else
			this.dangers = ImmutableSet.of();
	}

	/**
	 * whether this need is in regard to specific dangerous ideas, or just
	 * generalized danger as a concept
	 */
	public boolean specificDangers() {
		return dangers.size() > 0;
	}

	public ImmutableSet<IMeme> getDangers() {
		return dangers;
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
