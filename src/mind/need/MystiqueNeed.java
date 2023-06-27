package mind.need;

import com.google.common.collect.ImmutableSet;

import mind.concepts.type.IMeme;
import mind.goals.IGoal;

public class MystiqueNeed extends AbstractNeed {

	private ImmutableSet<IMeme> concepts;

	/**
	 * need to make mystical stories about different concepts
	 */
	public MystiqueNeed(Degree degree, Iterable<IMeme> concepts) {
		super(NeedType.MYSTIQUE, degree);
		this.concepts = ImmutableSet.copyOf(concepts);
	}

	/**
	 * Need to make mystical stories about different concepts
	 * 
	 * @param degree
	 */
	public MystiqueNeed(Degree degree, IMeme... concepts) {
		super(NeedType.MYSTIQUE, degree);
		this.concepts = ImmutableSet.copyOf(concepts);
	}

	@Override
	public IGoal genIndividualGoal() {
		// TODO mystique need
		return null;
	}

	@Override
	public IGoal genSocietalGoal() {
		return null;
	}

}
