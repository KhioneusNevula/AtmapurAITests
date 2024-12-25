package civilization_and_minds.mind.goals;

import java.util.stream.Stream;

import civilization_and_minds.IIntelligent;
import civilization_and_minds.social.concepts.IConcept;

/**
 * A Need is something which a civilization or individual requires for some
 * reason or other, e.g. food
 * 
 * @author borah
 *
 */
public interface INeed extends IConcept {

	/**
	 * The category of need
	 * 
	 * @author borah
	 *
	 */
	public static interface INeedType extends IConcept {
		/**
		 * If this need is a life-or-death need, e.g. hunger, breath, healing.
		 * 
		 * @return
		 */
		public boolean isMortal();
	}

	/**
	 * How severe this need is
	 * 
	 * @return
	 */
	public Necessity getSeverity();

	/**
	 * Generate goals to satisfy this need
	 * 
	 * @param forAgent
	 * @return
	 */
	public Stream<IGoal> getGoals(IIntelligent forAgent);

	/**
	 * The category of the need
	 * 
	 * @return
	 */
	public INeedType getNeedType();

	@Override
	default ConceptType getConceptType() {
		return ConceptType.NEED;
	}
}
