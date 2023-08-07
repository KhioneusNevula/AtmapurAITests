package mind.memory;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Multimap;

import mind.action.IActionType;
import mind.concepts.PropertyController;
import mind.concepts.type.ILocationMeme;
import mind.concepts.type.IMeme;
import mind.concepts.type.IProfile;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.feeling.IFeeling;
import mind.goals.IGoal;
import mind.goals.ITaskHint;
import mind.linguistics.Language;
import mind.memory.events.Consequence;
import mind.memory.events.EventDescription;
import mind.need.INeed;
import mind.need.INeed.INeedType;
import sim.interfaces.IUnique;

public interface IKnowledgeBase extends IRecordable {

	/**
	 * Gets a remembered profile for a unique entity; allows for searching through
	 * background memories as well (e.g. one's own cultural backgrounds)
	 * 
	 * @param entity
	 * @return
	 */
	public Profile getProfileFor(UUID entity);

	/**
	 * {@link #getProfileFor(UUID)}
	 * 
	 * @param entity
	 * @return
	 */
	default Profile getProfileFor(IUnique entity) {
		return getProfileFor(entity.getUUID());
	}

	/**
	 * returns the profile representing the self
	 * 
	 * @return
	 */
	public Profile getSelfProfile();

	/**
	 * If this given profile has the given property
	 * 
	 * @param prof
	 * @return
	 */
	public boolean hasProperty(Profile prof, Property cat);

	/**
	 * Whether it is known if the given profile has the given property
	 * 
	 * @param prof
	 * @param cat
	 * @return
	 */
	public boolean isPropertyKnown(Profile prof, Property cat);

	/**
	 * Gets the property data of a given property of a profile; note that for
	 * individuals this returns LOCAL, not CULTURAL info
	 * 
	 * @param prof
	 * @param cat
	 * @return
	 */
	public IPropertyData getProperties(Profile prof, Property cat);

	/**
	 * Whether a given concept is known or not by this memory; allows for checking
	 * through (for example) one's cultural background or whatever. Should NOT be
	 * used for profiles.
	 * 
	 * @param concept
	 * @return
	 */
	public boolean isKnown(IMeme concept);

	/**
	 * Whether this unique entity is recognized in this memory with a profile
	 * 
	 * @param unique
	 * @return
	 */
	public boolean isKnown(UUID unique);

	/**
	 * {@link IKnowledgeBase#isKnown(UUID)}
	 * 
	 * @param unique
	 * @return
	 */
	default boolean isKnown(IUnique unique) {
		return isKnown(unique.getUUID());
	}

	boolean isKnown(IProfile profile);

	/**
	 * The feeling that someone has for this concept
	 * 
	 * @param concept
	 * @return
	 */
	public IFeeling getAssociatedFeeling(IMeme concept);

	/**
	 * Consequences and their associated chance
	 * 
	 * @param event
	 * @return
	 */
	public Collection<? extends Map.Entry<Consequence, Float>> getAssociatedConsequence(EventDescription event);

	/**
	 * Returns the locally known location of this profile or null if unknown; note
	 * that for individuals this returns LOCAL not CULTURAL info
	 */
	public ILocationMeme getLocation(Profile prof);

	/**
	 * Whether the location of this profile is known
	 */
	public boolean knowsLocation(Profile prof);

	/**
	 * Returns the set of profiles that are known to this memory, EXCLUDING the
	 * Self.
	 * 
	 * @return
	 */
	public Iterable<Profile> getKnownProfiles();

	public Collection<Profile> getProfilesWithProperty(Property prop);

	/**
	 * Different kinds of propreties known to a individual/culture/etc
	 * 
	 * @return
	 */
	public Collection<Property> getRecognizedProperties();

	/**
	 * Gets the associations that this property is recognized to have; null if the
	 * property is unknown
	 * 
	 * @return
	 */
	public PropertyController getPropertyAssociations(Property prop);

	/**
	 * Gets goals remembered in this memory; TODO maybe add some feature of ranking
	 * to rank goals
	 * 
	 * @return
	 */
	public Collection<IGoal> getGoals();

	/**
	 * Gets the needs remembered in this memory
	 * 
	 * @return
	 */
	public Multimap<INeedType, INeed> getNeeds();

	/**
	 * Get the actions this entity or a member of it knows for the possible task
	 * hint TODO maybe sort by preference or somethin
	 * 
	 * @param forHint
	 * @return
	 */
	public Collection<IActionType<?>> getPossibleActions(ITaskHint forHint);

	/**
	 * Does a set number of passes on each section of this memory where it judges
	 * one memory from each section, either removing it or transfering it or doing
	 * some other important thing to it
	 */
	public void prune(int passes);

	/**
	 * Whether the given concept is significant enoguh to stay remembered for a
	 * short time
	 * 
	 * @param concept
	 * @return
	 */
	public boolean isSignificant(IMeme concept);

	/**
	 * Whether the given concept is significant enough to stay remembered over time
	 * 
	 * @param concept
	 * @return
	 */
	public boolean isLongTerm(IMeme concept);

	void addNeed(INeed goal);

	public Collection<Language> getLanguages();

	/**
	 * The language this uses typically
	 * 
	 * @Override
	 * @return
	 */
	public Language getMajorLanguage();

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

	/**
	 * Inits a properties instance for this profile and its properties; returns the
	 * instance for editing -- OR returns an existing instance, if present
	 * 
	 * @param forProfile
	 * @param prop
	 * @return
	 */
	public IPropertyData applyProperty(Profile forProfile, Property prop);

	/**
	 * Sets the property data for this property
	 * 
	 * @param forProfile
	 * @param prop
	 * @param rp
	 */
	public void setProperty(Profile forProfile, Property prop, IPropertyData rp);

}
