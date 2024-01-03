package mind.thought_exp.memory;

import java.util.Collection;
import java.util.Iterator;

import com.google.common.collect.Multimap;

import mind.concepts.relations.IConceptRelationType;
import mind.concepts.type.IMeme;
import mind.concepts.type.IMeme.IMemeType;
import mind.concepts.type.IProfile;
import mind.concepts.type.Property;
import mind.goals.IGoal;
import mind.memory.IPropertyData;
import mind.need.INeed;
import mind.need.INeed.INeedType;
import mind.relationships.RelationType;
import mind.relationships.Relationship;

public interface IUpgradedKnowledgeBase {

	public static enum Interest {
		/** indicate this memory/profile should never be forgotten by any means */
		CORE_MEMORY,
		/**
		 * indicate this memory/profile will be remembered for a while but can be
		 * forgotten
		 */
		REMEMBER,
		/**
		 * indicate this memory/profile will be remembered until the next sleep cycle
		 */
		SHORT_TERM,
		/** indicate this is to be forgotten in a few ticks(?) */
		FORGET
	}

	public boolean isKnown(IProfile IProfile);

	Collection<IProfile> getKnownProfiles();

	/**
	 * Marks IProfile as known, return true if already known
	 * 
	 * @param IProfile
	 */
	public boolean learnProfile(IProfile IProfile);

	/**
	 * Learns a specific concept; marking it as known
	 * 
	 * @param concept
	 * @return
	 */
	public boolean learnConcept(IMeme concept);

	/**
	 * Forgets IProfile; does not delete relations but does delete properties. Using
	 * this without deleting relations can cause interesting behavior(?). Return
	 * false if the IProfile was unknown
	 * 
	 * @param IProfile
	 */
	public boolean forgetProfile(IProfile IProfile);

	/**
	 * Forgets IProfile; deletes relations as well; return false if the IProfile was
	 * unknown
	 * 
	 * @param IProfile
	 */
	public boolean deepForgetProfile(IProfile IProfile);

	public boolean isKnown(IMeme concept);

	/**
	 * Forgets a concept; does not delete its relations. This can cause interesting
	 * behavior. Return false if concept was not known
	 * 
	 * @param concept
	 * @return
	 */
	public boolean forgetConcept(IMeme concept);

	/**
	 * Erases all of a specific concept's relations (for IProfiles, this erases
	 * political/social relations too), though not in property lists. Return false
	 * if nothing was deleted
	 */
	public boolean forgetConceptRelations(IMeme concept);

	/**
	 * Erases all property data for this property among all IProfiles; return false
	 * if nothing was deleted
	 * 
	 * @param property
	 */
	public boolean forgetAllPropertyData(Property property);

	public IPropertyData getPropertyData(IProfile prof, Property prop);

	/**
	 * Set the property data. ideally, if you're only changing one aspect of the
	 * property, just write that specific aspect using getPropertyData.
	 * 
	 * @param prof
	 * @param prop
	 */
	public IPropertyData learnPropertyData(IProfile prof, Property prop, IPropertyData dat);

	public void learnPropertyIdentifier(Property prop,I)

	/**
	 * Forgets this property data and returns its previous contents
	 * 
	 * @param prof
	 * @param prop
	 */
	public IPropertyData forgetPropertyData(IProfile prof, Property prop);

	public boolean hasProperty(IProfile prof, Property prop);

	/** should not be used for IProfiles */
	public <T extends IMeme> Collection<T> getKnownConceptsOfType(IMemeType type);

	/**
	 * Get all profiles with this property
	 * 
	 * @param prop
	 * @return
	 */
	public Iterator<IProfile> getProfilesWithProperty(Property prop);

	public IProfile getSelf();

	/**
	 * whether this concept has a non-political/social relation (of any direction)
	 * to the other thing
	 * 
	 * @param one
	 * @param other
	 * @return
	 */
	public boolean hasRelation(IMeme one, IMeme other, IConceptRelationType type);

	/**
	 * Whether this has any relation of the given kind with any other thing, in
	 * either direction.
	 * 
	 * @param one
	 * @param type
	 * @return
	 */
	public boolean hasAnyRelationOfType(IMeme one, IConceptRelationType type);

	/**
	 * Whether this has any relation of the given kind that goes from this to
	 * something else.
	 * 
	 * @param one
	 * @param type
	 * @return
	 */
	public boolean hasDirectionalRelationOfType(IMeme one, IConceptRelationType type);

	/**
	 * Gets all concepts with a relation of this kind from the given concept, in
	 * this direction.
	 * 
	 * @param <T>
	 * @param fromWhat
	 * @param type
	 * @return
	 */
	public <T extends IMeme> Collection<T> getConceptsWithRelation(IMeme fromWhat, IConceptRelationType type);

	/**
	 * Gets profiles with this relationship, and also gets the relationships
	 * themselves
	 * 
	 * @param <T>
	 * @param fromWhat
	 * @param type
	 * @return
	 */
	public <T extends IProfile> Multimap<T, Relationship> getProfilesWithRelationship(IProfile fromWhat,
			RelationType type);

	/**
	 * Adds a relation into the knowledge; if the relation is bidirectional, will
	 * automatically add it both ways. Last argument may be null
	 * 
	 * @param one
	 * @param other
	 * @param type
	 */
	public <T extends IMeme> void learnRelation(IMeme one, IMeme other, IConceptRelationType type, Iterable<T> args);

	/**
	 * Forgets the entire relation between the two given entities; return null if
	 * nothing was forgotten; return the concepts list otherwise
	 * 
	 * @param one
	 * @param other
	 * @param type
	 */
	public <T extends IMeme> Collection<T> forgetRelation(IMeme one, IMeme other, IConceptRelationType type);

	/**
	 * Forgets all relations between given entities; return false if nothing was
	 * forgotten
	 */
	public boolean forgetAllRelations(IMeme one, IMeme other);

	/**
	 * Whether this concept has a relation going from One to Other in that direction
	 * 
	 * @param one
	 * @param other
	 * @param type
	 * @return
	 */
	public boolean hasDirectionalRelation(IMeme one, IMeme other, IConceptRelationType type);

	/**
	 * Gets the different 'arguments' possible for a relation. For example, a Become
	 * relation between wood and furniture can involve the Carpentry action and
	 * Spell action as arguments. Only for the direction given, as the relation may
	 * have different arguments in the other dirction.
	 * 
	 * @param one
	 * @param other
	 * @param type
	 * @return
	 */
	public <T extends IMeme> Collection<T> relationArguments(IMeme one, IMeme other, IConceptRelationType type);

	/**
	 * Add new relation arguments to a relation; return false and do nothing if no
	 * relation exists
	 * 
	 * @param one
	 * @param other
	 * @param type
	 * @param arguments
	 */
	public <T extends IMeme> boolean learnRelationArguments(IMeme one, IMeme other, IConceptRelationType type,
			Iterable<T> arguments);

	/**
	 * Forgets the given relation arguments from the relation between these things;
	 * return false if nothing was forgotten
	 * 
	 * @param one
	 * @param other
	 * @param type
	 * @param arguments
	 */
	public <T extends IMeme> void forgetRelationArguments(IMeme one, IMeme other, IConceptRelationType type,
			Iterable<T> arguments);

	/**
	 * Gets known personal/political relationships between these two parties. Should
	 * not be used to remember relationships between oneself and others.
	 */
	public Collection<Relationship> getKnownRelationships(IProfile one, IProfile other, RelationType type);

	/**
	 * Should not be used to remember relationships between oneself and others.
	 * 
	 * @param one
	 * @param other
	 * @return
	 */
	public Collection<Relationship> getAllKnownRelationships(IProfile one, IProfile other);

	/**
	 * Learn about two entities' relationships. Automatically adds bidirectional
	 * relations both ways. Should not be used to remember relationships between
	 * oneself and others.
	 * 
	 * @param one
	 * @param other
	 * @param relationship
	 */
	public void learnOfRelationship(IProfile one, IProfile other, Relationship relationship);

	/**
	 * Forget about this political/social relationship between these entities, and
	 * return false if nothing was forgotten. Should not be used to forget
	 * relationships between oneself and others.
	 * 
	 * @param one
	 * @param other
	 * @param relationship
	 */
	public boolean forgetAboutRelationship(IProfile one, IProfile other, Relationship relationship);

	/**
	 * Forget all of this type of political/social relationship between these
	 * entities, and return false if nothing was forgotten. Should not be used to
	 * forget relationships between oneself and others.
	 * 
	 * @param one
	 * @param other
	 * @param relationship
	 */
	public boolean forgetAboutAllRelationshipsOfType(IProfile one, IProfile other, RelationType type);

	/**
	 * Forget about all political/social relationships between these entities, and
	 * return false if nothing was forgotten. Should not be used to forget
	 * relationships between oneself and others.
	 * 
	 * @param one
	 * @param other
	 * @param relationship
	 */
	public boolean forgetAboutAllRelationships(IProfile one, IProfile other);

	public void learnGoal(IGoal goal);

	public Collection<IGoal> getGoals(IGoal.Type type);

	public Collection<IGoal> getGoals();

	/**
	 * erases a goal of a specific type, return true if forgotten
	 * 
	 * @param goal
	 * @return
	 */
	public boolean forgetGoal(IGoal goal);

	/**
	 * Forgets goals of type, return true if anything was forgotten
	 * 
	 * @param type
	 * @return
	 */
	public boolean forgetGoals(IGoal.Type type);

	public void forgetNeed(INeed last);

	public void addNeed(INeed need);

	public Collection<INeed> getNeeds();

	public Collection<INeed> getNeeds(INeedType type);

	// TODO add other forms of knowledge

}
