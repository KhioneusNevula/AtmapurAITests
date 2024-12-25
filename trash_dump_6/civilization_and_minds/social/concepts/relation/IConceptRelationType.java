package civilization_and_minds.social.concepts.relation;

import civilization_and_minds.social.concepts.IConcept;
import utilities.IInvertibleRelationType;

/**
 * Class that represents a kind of relationship between two concepts or entities
 * 
 * @author borah
 * @param <I> the inverse relation type, i.e. what kind of relation is returned
 *            by {@link #inverse()}
 *
 */
public interface IConceptRelationType<I extends IConceptRelationType<?>> extends IConcept, IInvertibleRelationType<I> {

	/**
	 * If true, then the LEFT party is the agent or initial stage of the relational
	 * action of this relation; whereas RIGHT is the object or result stage. Result
	 * is undefined if bidirectional.
	 * 
	 * @return
	 */
	boolean leftIsAgent();

	/**
	 * If true, then LEFT is the object or result stage of the relational action of
	 * this relation; whereas RIGHT is the agent or initial stage. Result is
	 * undefined if bidirectional.
	 * 
	 * @return
	 */
	boolean leftIsObject();

	/**
	 * Whether this relation is one where the agent creates the object
	 * 
	 * @return
	 */
	boolean creates();

	/**
	 * Whether this relation is one where the agent transfers something to the
	 * object
	 * 
	 * @return
	 */
	boolean transfers();

	/**
	 * Whether this relation is one where the agent transforms into the object or is
	 * crafted into the object.
	 * 
	 * @return
	 */
	boolean transforms();

	/**
	 * Whether this relation requires an argument, e.g. a transfer relation might
	 * have an argument of what is transferred.
	 * 
	 * @return
	 */
	boolean requiresArgument();

	/**
	 * Whether this relation requires an action to be enacted. E.g. a giving
	 * relation requires a giving action to take place
	 * 
	 * @return
	 */
	boolean requiresAction();

	/**
	 * Whether the agent of this relation consumes or destroys the object.
	 * 
	 * @return
	 */
	boolean consumes();

	/**
	 * Whether this constitutes a relation where the agent is at the location of the
	 * object
	 * 
	 * @return
	 */
	boolean atLocation();

	/**
	 * Whether this constitutes a relation where the agent is made of the object.
	 * E.g. a house is made of wood
	 * 
	 * @return
	 */
	boolean madeOf();

	/**
	 * Whether this constitutes a relation where some quantity of the agent can be
	 * traded for some quantity of the object.
	 * 
	 * @return
	 */
	boolean tradeWorth();

	/**
	 * whether this constitutes a relation where the agent socially or
	 * interpersonally dominates the object, e.g. a king dominates their subjects
	 * 
	 * @return
	 */
	boolean dominates();

	/**
	 * Whether this constitutes a relation where the agent has the object as a
	 * specific property of its being.
	 * 
	 * @return
	 */
	boolean isProperty();

	/**
	 * IF this relation represents something linguistic
	 * 
	 * @return
	 */
	boolean linguistic();

	/**
	 * Whether this relation involves the agent damaging the object.
	 * 
	 * @return
	 */
	boolean damages();

	/**
	 * Whether this relationship constitutes a social or personal relationship
	 * 
	 * @return
	 */
	boolean social();

	@Override
	default ConceptType getConceptType() {
		return ConceptType.RELATION;
	}

	/**
	 * Whether this relation indicates that the first thing is equivalent to or
	 * characterized by the second thing
	 * 
	 * @return
	 */
	boolean copular();

	/**
	 * Whether this relation indicates something that the active member has as a
	 * capability, in their mind, or is in possession of
	 * 
	 * @return
	 */
	boolean has();

}
