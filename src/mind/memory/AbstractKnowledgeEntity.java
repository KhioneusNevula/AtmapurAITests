package mind.memory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

import mind.action.IActionType;
import mind.concepts.PropertyController;
import mind.concepts.type.IConcept;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.goals.IGoal;
import mind.goals.ITaskHint;
import mind.goals.TaskHint;
import mind.need.INeed;
import mind.need.INeed.INeedType;

public abstract class AbstractKnowledgeEntity implements IKnowledgeBase {
	protected Profile self;
	protected Table<Profile, Property, IPropertyData> infoTable;
	// some way of forming associations and whatever among properties
	protected Map<Property, PropertyController> propertyConcepts;
	protected Map<UUID, Profile> profiles;
	protected Set<IGoal> goals;
	protected Multimap<INeedType, INeed> needs;
	protected Multimap<ITaskHint, IActionType<?>> doableActions;

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

	/**
	 * Inits a properties instance for this profile and its properties; returns the
	 * instance for editing -- OR returns an existing instance, if present
	 * 
	 * @param forProfile
	 * @param prop
	 * @return
	 */
	public IPropertyData applyProperty(Profile forProfile, Property prop) {
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
			prof = profiles.get(entity);

		return prof;
	}

	@Override
	public boolean isKnown(IConcept concept) {

		// TODO different kinds of concepts that may be known
		if (concept instanceof Profile) {
			return this.isKnown(((Profile) concept).getUUID());
		} else if (concept instanceof Property) {
			return (this.propertyConcepts == null ? false : propertyConcepts.containsKey(concept));
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
		if (profiles.containsKey(unique)) {
			return true;
		}
		return false;
	}

	@Override
	public IPropertyData getProperties(Profile prof, Property cat) {
		if (infoTable != null)
			return infoTable.get(prof, cat);
		return null;
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

	@Override
	public boolean hasProperty(Profile prof, Property cat) {
		if (this.infoTable == null)
			return false;
		if (this.infoTable.contains(prof, cat))
			return true;
		return false;
	}

	@Override
	public Collection<Profile> getKnownProfiles() {
		return this.profiles == null ? Set.of() : profiles.values();
	}

	public void recognizeProfile(Profile prof) {
		(this.profiles == null ? profiles = new TreeMap<>() : profiles).put(prof.getUUID(), prof);
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
		(this.profiles == null ? profiles = new TreeMap<>() : profiles).put(forEntity, prof);
		return prof;
	}

	@Override
	public Collection<Profile> getProfilesWithProperty(Property prop) {
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
		(this.goals == null ? goals = new HashSet<>() : goals).add(goal);
	}

	public void forgetGoal(IGoal goal) {
		if (this.goals != null) {
			goals.remove(goal);
			if (goals.isEmpty())
				goals = null;
		}
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
	public boolean isSignificant(IConcept concept) {
		// TODO make an algorithm of significance
		return true;
	}

	@Override
	public boolean isLongTerm(IConcept concept) {
		return true;
	}

}
