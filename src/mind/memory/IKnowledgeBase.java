package mind.memory;

import java.util.Collection;
import java.util.UUID;

import com.google.common.collect.Multimap;

import mind.action.IActionType;
import mind.concepts.PropertyController;
import mind.concepts.type.IMeme;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.goals.IGoal;
import mind.goals.ITaskHint;
import mind.linguistics.Language;
import mind.need.INeed;
import mind.need.INeed.INeedType;
import sim.Location;
import sim.interfaces.IUnique;

public interface IKnowledgeBase {

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

	/**
	 * Returns the locally known location of this profile or null if unknown; note
	 * that for individuals this returns LOCAL not CULTURAL info
	 */
	public Location getLocation(Profile prof);

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
	public Collection<Profile> getKnownProfiles();

	public Collection<Profile> getProfilesWithProperty(Property prop);

	/**
	 * Different kinds of propreties known to a individual/culture/etc
	 * 
	 * @return
	 */
	public Collection<Property> getRecognizedProperties();

	/**
	 * Gets the associations that this property is recognized to have
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
	 * Get the actions this entity knows for the possible task hint
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
	 * @return
	 */
	public Language getMajorLanguage();

}
