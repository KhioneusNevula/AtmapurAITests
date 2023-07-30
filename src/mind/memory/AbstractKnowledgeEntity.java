package mind.memory;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

import main.MathHelp;
import main.Pair;
import mind.action.IActionType;
import mind.concepts.PropertyController;
import mind.concepts.type.ILocationMeme;
import mind.concepts.type.IMeme;
import mind.concepts.type.IMeme.MemeType;
import mind.concepts.type.IProfile;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.goals.IGoal;
import mind.goals.ITaskHint;
import mind.goals.TaskHint;
import mind.linguistics.Language;
import mind.memory.events.Consequence;
import mind.memory.events.EventDescription;
import mind.need.INeed;
import mind.need.INeed.INeedType;
import sim.interfaces.IUnique;

public abstract class AbstractKnowledgeEntity implements IKnowledgeBase {
	protected Profile self;
	protected Table<Profile, Property, IPropertyData> infoTable;
	protected Map<Property, PropertyController> propertyConcepts;

	protected Map<Profile, Interest> profiles;
	protected Set<IGoal> goals;
	protected Multimap<INeedType, INeed> needs;
	protected Multimap<ITaskHint, IActionType<?>> doableActions;
	protected Language mainLanguage;
	protected Set<Language> languages = new TreeSet<>(Comparator.naturalOrder());
	protected EventAssociationsMemory events = new EventAssociationsMemory();
	protected Map<Profile, ILocationMeme> locationKnowledge;

	public AbstractKnowledgeEntity(UUID selfID, String type) {
		this.self = new Profile(selfID, type);
	}

	/**
	 * Clear the memory of this entity
	 */
	public void clearMemory() {
		this.infoTable = null;
		this.profiles = null;
		this.goals = null;
		this.propertyConcepts = null;
		this.needs = null;
		this.doableActions = null;
	}

	@Override
	public Profile getSelfProfile() {
		return self;
	}

	public Collection<Pair<Consequence, Float>> getAssociatedConsequence(EventDescription event) {
		return events.getConsequences(event);
	}

	@Override
	public void setProperty(Profile forProfile, Property prop, IPropertyData rp) {
		if (!this.isKnown(forProfile.getUUID()))
			this.recognizeProfile(forProfile);
		if (prop.isOnlyPresence() && !rp.onlyMarksPresence())
			throw new IllegalArgumentException(forProfile + " " + prop + " " + rp);
		this.findInfoTable().put(forProfile, prop, rp);
	}

	@Override
	public IPropertyData applyProperty(Profile forProfile, Property prop) {
		if (!this.isKnown(forProfile.getUUID()))
			this.recognizeProfile(forProfile);
		IPropertyData ret = findInfoTable().get(forProfile, prop);
		if (ret != null)
			return ret;
		if (prop.isOnlyPresence()) {
			findInfoTable().put(forProfile, prop, IPropertyData.PRESENCE);
			return IPropertyData.PRESENCE;
		}
		RememberedProperties props = new RememberedProperties(prop);
		findInfoTable().put(forProfile, prop, props);
		return props;
	}

	/**
	 * Removes a property from a profile and returns whatever data it was stored
	 * with; null if not present
	 * 
	 * @param forP
	 * @param prop
	 * @return
	 */
	public IPropertyData removeProperty(Profile forP, Property prop) {
		if (infoTable == null)
			return null;
		IPropertyData dat = infoTable.remove(forP, prop);
		if (infoTable.isEmpty())
			infoTable = null;
		return dat;
	}

	/**
	 * Obtains or initializes the associations controller for the given property
	 * 
	 * @param forProp
	 * @return
	 */
	public PropertyController findPropertyAssociations(Property forProp) {
		return (propertyConcepts == null ? propertyConcepts = new TreeMap<>() : propertyConcepts)
				.computeIfAbsent(forProp, PropertyController::new);
	}

	/**
	 * Forget the associations of this property
	 * 
	 * @param forProp
	 * @return
	 */
	public PropertyController forgetAssociations(Property forProp) {
		PropertyController con = (propertyConcepts == null ? null : propertyConcepts.remove(forProp));
		if (propertyConcepts.isEmpty())
			propertyConcepts = null;
		return con;
	}

	protected Table<Profile, Property, IPropertyData> findInfoTable() {
		if (this.infoTable == null)
			this.infoTable = TreeBasedTable.create();
		return infoTable;
	}

	@Override
	public Profile getProfileFor(UUID entity) {
		Profile prof = null;
		if (entity.equals(self.getUUID()))
			return self;
		if (profiles != null)
			prof = profiles.keySet().stream().filter((a) -> a.getUUID().equals(entity)).findAny().orElse(null);

		return prof;
	}

	@Override
	public boolean isKnown(IMeme concept) {

		// TODO different kinds of concepts that may be known
		if (concept.getMemeType() instanceof MemeType) {
			switch ((MemeType) concept.getMemeType()) {
			case PROFILE:
				return this.isKnown((IProfile) concept);
			case PROPERTY:
				if (concept == Property.ANY)
					return true;
				return (this.propertyConcepts == null ? false : propertyConcepts.containsKey(concept));
			case ACTION_TYPE:
				return (this.doableActions == null ? false : this.doableActions.containsValue(concept));
			case LANGUAGE:
				return (this.languages == null ? false : this.languages.contains(concept));

			default:
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean isKnown(IProfile unique) {
		if (unique.equals(self)) {
			return true;
		}
		if (profiles == null)
			return false;
		if (profiles.containsKey(unique)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isKnown(UUID unique) {
		if (unique.equals(self.getUUID())) {
			return true;
		}
		if (profiles == null)
			return false;
		if (profiles.containsKey(new Profile(unique))) {
			return true;
		}
		return false;
	}

	@Override
	public IPropertyData getProperties(Profile prof, Property cat) {
		if (cat == Property.ANY)
			return IPropertyData.PRESENCE;
		if (infoTable != null)
			return infoTable.contains(prof, cat) ? infoTable.get(prof, cat) : IPropertyData.UNKNOWN;
		return IPropertyData.UNKNOWN;
	}

	/**
	 * Add a property to the known list, plus associations
	 * 
	 * @param cat
	 * @param associations
	 */
	public void learnProperty(Property cat, PropertyController associations) {
		(this.propertyConcepts == null ? propertyConcepts = new TreeMap<>() : propertyConcepts).put(cat, associations);
	}

	public void learnLocation(Profile profile, ILocationMeme loc) {
		if (!this.isKnown(profile.getUUID())) {
			this.recognizeProfile(profile);
		}
		(this.locationKnowledge == null ? locationKnowledge = new TreeMap<>() : locationKnowledge).put(profile, loc);
	}

	@Override
	public boolean hasProperty(Profile prof, Property cat) {
		if (cat == Property.ANY)
			return true;
		if (this.getProperties(prof, cat).isPresent())
			return true;
		return false;
	}

	@Override
	public boolean isPropertyKnown(Profile prof, Property cat) {
		return !this.getProperties(prof, cat).isUnknown();
	}

	@Override
	public Collection<Profile> getKnownProfiles() {
		return this.profiles == null ? Set.of() : profiles.keySet();
	}

	public void recognizeProfile(Profile prof) {
		(this.profiles == null ? profiles = new TreeMap<>() : profiles).put(prof, Interest.SHORT_TERM);
	}

	/**
	 * recognize a profile by this memory entity
	 * 
	 * @param forEntity
	 * @param type
	 * @return
	 */
	public Profile recognizeProfile(UUID forEntity, String type) {
		Profile prof = new Profile(forEntity, type);
		(this.profiles == null ? profiles = new TreeMap<>() : profiles).put(prof, Interest.SHORT_TERM);
		return prof;
	}

	public Profile recognizeProfile(IUnique forEntity) {
		Profile prof = new Profile(forEntity);
		this.recognizeProfile(prof);
		return prof;
	}

	@Override
	public Collection<Profile> getProfilesWithProperty(Property prop) {
		if (prop == Property.ANY) {
			return infoTable == null ? Set.of() : infoTable.rowKeySet();
		}
		return this.infoTable == null ? Set.of() : this.infoTable.column(prop).keySet();
	}

	@Override
	public PropertyController getPropertyAssociations(Property prop) {
		return propertyConcepts == null ? null : propertyConcepts.get(prop);
	}

	@Override
	public Collection<Property> getRecognizedProperties() {
		return this.propertyConcepts == null ? Set.of() : propertyConcepts.keySet();
	}

	@Override
	public Collection<IGoal> getGoals() {
		return this.goals == null ? Set.of() : goals;
	}

	public void addGoal(IGoal goal) {
		if (this.goals != null) {
			Optional<IGoal> ge = this.goals.stream().filter((a) -> goal.equivalent(a)).findAny();
			if (ge.isPresent()) {
				this.forgetGoal(ge.get());
			}
		}
		(this.goals == null ? goals = new TreeSet<>((a, b) -> 2 * a.getPriority().compareTo(b.getPriority())
				+ MathHelp.clamp(-1, 1, a.getUniqueName().compareTo(b.getUniqueName()))) : goals).add(goal);
	}

	public void forgetGoal(IGoal goal) {
		if (this.goals != null) {
			goals.remove(goal);
			if (goals.isEmpty())
				goals = null;
		}
	}

	@Override
	public ILocationMeme getLocation(Profile prof) {
		if (this.locationKnowledge == null)
			return null;
		return locationKnowledge.get(prof);
	}

	@Override
	public boolean knowsLocation(Profile prof) {
		return this.getLocation(prof) != null;
	}

	@Override
	public Multimap<INeedType, INeed> getNeeds() {
		return this.needs == null ? ImmutableMultimap.of() : needs;
	}

	@Override
	public void addNeed(INeed goal) {
		(this.needs == null ? needs = MultimapBuilder.hashKeys().hashSetValues().build() : needs).put(goal.getType(),
				goal);
	}

	public void forgetNeed(INeed goal) {
		if (this.needs != null) {
			needs.remove(goal.getType(), goal);
			if (needs.isEmpty())
				needs = null;
		}
	}

	public void addDoableAction(IActionType<?> type) {
		if (this.doableActions == null) {
			this.doableActions = MultimapBuilder.hashKeys().hashSetValues().build();
		}
		if (type.getUsage().contains(TaskHint.ALL)) {
			doableActions.put(TaskHint.ALL, type);
		} else {
			for (ITaskHint hint : type.getUsage()) {
				doableActions.put(hint, type);
			}
		}

	}

	public void forgetDoableAction(IActionType<?> type) {
		if (this.doableActions != null) {
			for (ITaskHint hint : type.getUsage()) {
				doableActions.remove(hint, type);
			}
		}
	}

	@Override
	public Collection<IActionType<?>> getPossibleActions(ITaskHint forHint) {
		return this.doableActions == null ? Set.of() : doableActions.get(forHint);
	}

	public String report() {
		StringBuilder builder = new StringBuilder(this.getClass().getSimpleName() + "->");

		return builder.toString();
	}

	@Override
	public boolean isSignificant(IMeme concept) {
		if (concept.equals(this.self))
			return true;
		// TODO make an algorithm of significance
		return true;
	}

	@Override
	public boolean isLongTerm(IMeme concept) {
		return true;
	}

	@Override
	public Collection<Language> getLanguages() {
		return this.languages;
	}

	@Override
	public Language getMajorLanguage() {
		return this.mainLanguage;
	}

}
