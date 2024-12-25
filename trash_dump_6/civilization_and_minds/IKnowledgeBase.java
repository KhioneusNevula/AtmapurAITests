package civilization_and_minds;

import java.util.stream.Stream;

import civilization_and_minds.social.concepts.IConcept;
import civilization_and_minds.social.concepts.profile.Profile;
import civilization_and_minds.social.concepts.relation.IConceptRelationType;

/**
 * A repository of knowledge of some kind.
 * 
 * @author borah
 *
 */
public interface IKnowledgeBase {

	/**
	 * Whether this knowledgebase is unchanging
	 * 
	 * @return
	 */
	public boolean isStatic();

	/**
	 * Return the self profile of this knowledg base
	 * 
	 * @return
	 */
	public Profile getSelfProfile();

	/**
	 * The parent knwoledge base of this one, i.e. a Mind has a culture as its
	 * parent.
	 * 
	 * @return
	 */
	public IKnowledgeBase getParent();

	/**
	 * Whether this kind of relation exists between the given two concepts
	 * 
	 * @param first
	 * @param relation
	 * @param second
	 * @param checkParent whether to check the parent knowledge base if this
	 *                    knowledge base lacks the knowledge
	 * @return
	 */
	public boolean relationExists(IConcept first, IConceptRelationType<?> relation, IConcept second,
			boolean checkParent);

	/**
	 * Get the argument of this specific relation.
	 * 
	 * @param <T>
	 * @param first
	 * @param relation
	 * @param second
	 * @param checkParent whether to check the parent knowledgebase if this
	 *                    knowledge base has no such relation or no such data
	 * @return
	 */
	public <T extends IConcept> T getRelationArgument(IConcept first, IConceptRelationType<?> relation, IConcept second,
			boolean checkParent);

	/**
	 * If this concept is known to this knowledge base
	 * 
	 * @param concept
	 * @param checkParent whether to check the parent knowledge base when
	 *                    determining this, i.e. determining if a mind knows a
	 *                    concept and checking cultural knowledge as well
	 * @return
	 */
	public boolean knows(IConcept concept, boolean checkParent);

	/**
	 * Return how many concepts are known in this knowledge base
	 * 
	 * @param checkParent whether to check the parent knowledge base when
	 *                    determining this, i.e. determining if a mind knows a
	 *                    concept and also checking cultural knowledge
	 * @return
	 */
	public int countConcepts(boolean checkParent);

	/**
	 * Returns a stream of all concepts known to this entity. May repeat concepts if
	 * #checkParent is true.
	 * 
	 * @param checkParent whether to check the parent knowledge base when iterating,
	 *                    i.e. iterating through both locally known concepts and
	 *                    parent-known concepts.
	 * @return
	 */
	public Stream<IConcept> getAllConcepts(boolean checkParent);

	/**
	 * Returns stream of all concepts this entity knows which have connections to
	 * the given concept. May repeat concepts if #checkParent is true.
	 * 
	 * @param toConcept
	 * @param checkParents whether to check the parent knowledge base when
	 *                     iterating, i.e. iterating through both locally known
	 *                     concepts and parent-known concepts.
	 * @return
	 */
	public Stream<IConcept> getConnectedConcepts(IConcept toConcept, boolean checkParents);

	/**
	 * Returns stream of all concepts this entity knows which have connections to
	 * the given concept of the given type. May repeat concepts if #checkParent is
	 * true.
	 * 
	 * @param toConcept
	 * @param relation
	 * @param checkParents whether to check the parent knowledge base when
	 *                     iterating, i.e. iterating through both locally known
	 *                     concepts and parent-known concepts.
	 * @return
	 */
	public Stream<IConcept> getConnectedConcepts(IConcept toConcept, IConceptRelationType<?> relation,
			boolean checkParents);

	/**
	 * Get all types of relations between these concepts
	 * 
	 * @param conceptOne
	 * @param conceptTwo
	 * @param checkParents
	 * @return
	 */
	public Stream<IConceptRelationType<?>> getRelationTypes(IConcept conceptOne, IConcept conceptTwo,
			boolean checkParents);

	/**
	 * Count the number of concepts connected to this one
	 * 
	 * @param toConcept
	 * @param checkParents whether to check the parent knowledge base when
	 *                     iterating, i.e. iterating through both locally known
	 *                     concepts and parent-known concepts.
	 * @return
	 */
	public int countConnectedConcepts(IConcept toConcept, boolean checkParent);

	/**
	 * Count the number of concepts connected to this one of a given type
	 * 
	 * @param toConcept
	 * @param checkParents whether to check the parent knowledge base when
	 *                     iterating, i.e. iterating through both locally known
	 *                     concepts and parent-known concepts.
	 * @return
	 */
	public int countConnectedConcepts(IConcept toConcept, IConceptRelationType<?> type, boolean checkParent);

	/**
	 * Establish a new relationship between concepts. Return false if relationship
	 * exists already (or if static), in which case the relationship will remain
	 * unchanged (including not applying the given argument).
	 * 
	 * @param first
	 * @param type
	 * @param second
	 * @param argument can be null
	 */
	public boolean learnNewRelationship(IConcept first, IConceptRelationType<?> type, IConcept second,
			IConcept argument);

	/**
	 * Establish a new relationship between concepts. Same as
	 * {@link #learnNewRelationship(IConcept, IConceptRelationType, IConcept, IConcept)}
	 * but without optional argument
	 * 
	 * @param first
	 * @param type
	 * @param second
	 */
	public boolean learnNewRelationship(IConcept first, IConceptRelationType<?> type, IConcept second);

	/**
	 * Change the argument of this relationship. Return false if relationship does
	 * not exist (or if culture is static), in which case nothing will be changed.
	 * 
	 * @param first
	 * @param type
	 * @param second
	 * @param argument
	 */
	public boolean changeArgument(IConcept first, IConceptRelationType<?> type, IConcept second, IConcept argument);

	/**
	 * Delete the argument of this relationship. Return false if the relationship
	 * does not exist (or culture is static).
	 * 
	 * @param first
	 * @param type
	 * @param second
	 * @return
	 */
	public boolean deleteArgument(IConcept first, IConceptRelationType<?> type, IConcept second);

	/**
	 * Remove this relationship and return its argument. Return value may be null if
	 * there was no argument or no such relationship existed (or culture is static)
	 * 
	 * @param first
	 * @param type
	 * @param second
	 * @return
	 */
	public IConcept forgetRelationship(IConcept first, IConceptRelationType<?> type, IConcept second);

	String report();

}
