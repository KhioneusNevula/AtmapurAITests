package mind.need;

import java.util.Set;

import mind.concepts.type.IMeme;
import mind.goals.IGoal;

public interface INeed extends IMeme {
	public static enum Individuality {
		INDIVIDUAL(true, false), INDIVIDUAL_OR_SOCIETAL(true, true), SOCIETAL(false, true);

		public final boolean individual;
		public final boolean societal;

		private Individuality(boolean ind, boolean soc) {
			individual = ind;
			societal = soc;
		}
	}

	public static enum Degree {
		/**
		 * examples: a mild need for food means that we will keep surviving without
		 * food, but we still want to obtain food just for security. A mild need for
		 * structure may be that positional selection works, but there is a better way
		 * to do it.
		 */
		MILD,
		/**
		 * The generic amount of need. <br>
		 * example: a moderate need for food means we will likely need it soon, if not
		 * now. a moderate need for ACcessibility means that tasks can be completed, but
		 * it's happening slowly.
		 */
		MODERATE,
		/**
		 * example: a severe need for food means people are dying and food is needed to
		 * solve hunger. a severe need for structure means there is serious conflict
		 * bringing down the system.
		 */
		SEVERE,
		/**
		 * Beyond need is the insane need beyond what is actively necessary, changing
		 * the baseline of necessity essentially, and causes the Need to be sought out
		 * beyond what is acceptable. A Beyond need for Structure creates an autocratic
		 * dictatorship. A Beyond need for Accessibility might result in the pursuit of
		 * time travel to make all tasks easy. A Beyond need for longevity is a search
		 * for immortality. A Beyond need for Happiness is a search for a beatific drug.
		 */
		BEYOND;
	}

	public static interface INeedType {
		public Individuality individuality();

		public String uniqueName();
	}

	/**
	 * what type of need it is
	 * 
	 * @return
	 */
	public INeedType getType();

	/**
	 * how intense the need is
	 * 
	 * @return
	 */
	public Degree getDegree();

	/**
	 * create an appropriate goal for an individual to desire to fulfill this need
	 * (or throw exception if this need is non-individual and return null if
	 * multiple goals)
	 */
	public IGoal genIndividualGoal();

	/**
	 * {@link INeed#genIndividualGoal} but if having multiple goals; return a set
	 * containing the genIndividualGoal result if only one goal
	 * 
	 * @return
	 */
	default Iterable<IGoal> genIndividualGoals() {
		return Set.of(genIndividualGoal());
	}

	/**
	 * create an appropriate goal for a society to complete to fulfill this need (or
	 * throw an exception if this need is non-societal, return null if multiple
	 * goals)
	 */
	public IGoal genSocietalGoal();

	/**
	 * {@link INeed#genSocietalGoal()} but for multiple goals; return set containing
	 * the genSocietalGoal result if only one goal
	 * 
	 * @return
	 */
	default Iterable<IGoal> genSocietalGoals() {
		return Set.of(genSocietalGoal());
	}

}
