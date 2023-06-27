package mind.memory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;

import actor.Actor;
import mind.Culture;
import mind.Group;
import mind.IGroup;
import mind.action.IActionType;
import mind.concepts.PropertyController;
import mind.concepts.type.IMeme;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.goals.ITaskHint;
import mind.relationships.IParty;
import mind.relationships.Relationship;
import sim.Location;

public class Memory extends AbstractKnowledgeEntity {

	// some way of forming associations and whatever among properties
	private Set<Culture> cultures = new TreeSet<>();
	/**
	 * TODO maximum sensable things
	 */
	private SenseMemory<Actor> senses = new SenseMemory<>(100, true);
	private SenseMemory<Profile> recognition = new SenseMemory<>(Integer.MAX_VALUE, false);
	private SetMultimap<IParty, Relationship> agreements;
	private TreeMap<UUID, Relationship> agreementsById;
	private HashMap<IGroup, Integer> partOfGroups;
	private boolean feelingCurious = true;
	private boolean socializedRecently;

	public Memory(UUID selfID, String type) {
		super(selfID, type);
	}

	public Memory addCulture(Culture culture) {

		cultures.add(culture);
		if (this.mainLanguage == null)
			this.mainLanguage = culture.mainLanguage;
		this.languages.addAll(culture.getLanguages());
		return this;
	}

	/**
	 * Clear the memory of this entity
	 */
	public void clearMemory() {
		super.clearMemory();
		this.senses.clearAll();
		this.recognition.clearAll();
		this.cultures.clear();

		this.agreements = null;
		this.agreementsById = null;
		this.partOfGroups = null;
	}

	@Override
	public Profile getProfileFor(UUID entity) {
		Profile prof = super.getProfileFor(entity);
		if (prof == null) {
			for (Culture cul : cultures) {
				prof = cul.getProfileFor(entity);
				if (prof != null)
					return prof;
			}
		}
		return prof;
	}

	@Override
	public boolean isKnown(IMeme concept) {
		if (!super.isKnown(concept)) {
			for (Culture cul : cultures) {
				if (cul.isKnown(concept))
					return true;
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean isKnown(UUID unique) {
		if (!super.isKnown(unique)) {
			for (Culture cul : this.cultures) {
				if (cul.isKnown(unique))
					return true;
			}
			return false;
		}
		return true;
	}

	/**
	 * Returns the knowledge of the properties from the culture, since an individual
	 * may have conflicting cultural AND individual info
	 */
	public Map<Culture, IPropertyData> getPropertiesFromCulture(Profile prof, Property cat) {

		Map<Culture, IPropertyData> set = Map.of();
		for (Culture cult : cultures) {
			IPropertyData dat2 = cult.getProperties(prof, cat);
			if (dat2 != null && !dat2.isUnknown()) {
				if (set.isEmpty())
					set = Map.of(cult, dat2);
				else if (set.size() == 1) {
					set = new TreeMap<>(set);
					set.put(cult, dat2);
				}
			}
		}
		return set;
	}

	public Map<Culture, PropertyController> getPropertyAssociationsFromCulture(Property prop) {
		Map<Culture, PropertyController> set = Map.of();
		for (Culture cult : cultures) {
			PropertyController dat2 = cult.getPropertyAssociations(prop);
			if (dat2 != null) {
				if (set.isEmpty())
					set = Map.of(cult, dat2);
				else if (set.size() == 1) {
					set = new TreeMap<>(set);
					set.put(cult, dat2);
				}
			}
		}
		return set;
	}

	public Multimap<Culture, IActionType<?>> getPossibleActionsFromCulture(ITaskHint hint) {
		Multimap<Culture, IActionType<?>> map = MultimapBuilder.treeKeys().hashSetValues().build();
		for (Culture cult : cultures) {
			for (IActionType<?> atype : cult.getPossibleActions(hint)) {
				map.put(cult, atype);
			}
		}
		return map;
	}

	@Override
	public boolean hasProperty(Profile prof, Property cat) {
		if (super.hasProperty(prof, cat))
			return true;
		for (IPropertyData d : this.getPropertiesFromCulture(prof, cat).values()) {
			if (d.isPresent())
				return true;
		}
		return false;
	}

	@Override
	public boolean isPropertyKnown(Profile prof, Property cat) {
		if (!super.isPropertyKnown(prof, cat)) {
			for (Culture c : cultures)
				if (c.isPropertyKnown(prof, cat))
					return true;
			return false;
		}
		return true;
	}

	@Override
	public Location getLocation(Profile prof) {
		Location loc = this.senses.getSensedLocation(prof);
		if (loc == null)
			loc = this.recognition.getSensedLocation(prof);
		return loc;
	}

	public Map<Culture, Location> getLocationsFromCulture(Profile prof) {
		Map<Culture, Location> set = Map.of();
		for (Culture c : cultures) {
			Location a = c.getLocation(prof);
			if (a != null) {
				if (set.isEmpty())
					set = new TreeMap<>();
				set.put(c, a);
			}
		}
		return Map.copyOf(set);
	}

	@Override
	public boolean knowsLocation(Profile prof) {
		if (this.getLocation(prof) == null) {
			for (Culture c : cultures) {
				Location a = c.getLocation(prof);
				if (a != null) {
					return true;
				}
			}
		}
		return true;
	}

	/**
	 * What is actively sensed right now
	 * 
	 * @return
	 */
	public SenseMemory<Actor> getSenses() {
		return senses;
	}

	/**
	 * The memory that handles recognition of things and association with physical
	 * (spatial, etc) traits
	 * 
	 * @return
	 */
	public SenseMemory<Profile> getRecognition() {
		return recognition;
	}

	public Multimap<Culture, Profile> getProfilesWithPropertyFromCulture(Property prop) {
		Multimap<Culture, Profile> profs = null;
		for (Culture cult : this.cultures) {
			Collection<Profile> r = cult.getProfilesWithProperty(prop);
			if (!r.isEmpty()) {
				(profs == null ? profs = MultimapBuilder.treeKeys().treeSetValues().build() : profs).putAll(cult, r);
			}
		}
		return profs == null ? ImmutableMultimap.of() : profs;
	}

	private SetMultimap<IParty, Relationship> findAgreements() {
		return agreements == null ? agreements = MultimapBuilder.treeKeys().treeSetValues().build() : agreements;
	}

	public Collection<Relationship> getRelationshipsWith(IParty other) {
		return agreements == null ? Set.of() : agreements.get(other);
	}

	public boolean isPartOf(IGroup group) {
		return partOfGroups == null ? null : partOfGroups.getOrDefault(group, 0) > 0;
	}

	public Relationship getRelationshipByID(UUID agreementID) {
		return agreementsById == null ? null : agreementsById.get(agreementID);
	}

	public Collection<IParty> getAllPartiesWithRelationships() {
		return agreements == null ? Set.of() : agreements.keySet();
	}

	public Collection<Relationship> getAllRelationships() {
		return agreements == null ? Set.of() : agreements.values();
	}

	public void establishRelationship(IParty with, Relationship agreement) {
		this.findAgreements().put(with, agreement);
		(this.agreementsById == null ? this.agreementsById = new TreeMap<>() : agreementsById)
				.put(agreement.getAgreementID(), agreement);
		if (agreement.getType().isMembership()) {
			(partOfGroups == null ? partOfGroups = new HashMap<>() : partOfGroups).put((IGroup) with,
					partOfGroups.getOrDefault(with, 0) + 1);
			if (with instanceof Group)
				this.addCulture(((Group) with).getCulture());
		}
	}

	public void dissolveRelationship(IParty with, Relationship agreement) {
		if (this.agreements != null && this.agreements.remove(with, agreement)) {
			if (this.agreementsById != null)
				this.agreementsById.remove(agreement.getAgreementID());
			if (agreement.getType().isMembership() && partOfGroups != null) {
				int a = partOfGroups.getOrDefault(with, 0);
				if (a == 1) {
					partOfGroups.remove(with);
					if (with instanceof Group)
						this.cultures.remove(((Group) with).getCulture());
				} else if (a <= 0)
					throw new IllegalStateException("Group membership broken? with " + with);
				else
					partOfGroups.put((IGroup) with, a - 1);
			}
		}
	}

	public boolean hasRelationshipsWith(IParty other) {
		return agreements == null ? false : !this.agreements.get(other).isEmpty();
	}

	@Override
	public String report() {
		StringBuilder build = new StringBuilder("Memory-" + this.self + "{");
		build.append("\n\tcultures:" + this.cultures);
		build.append("\n\tsenses:" + this.senses.report());
		if (this.propertyConcepts != null)
			build.append("\n\tconcepts:" + this.propertyConcepts);
		build.append("\n\tneeds:" + (needs == null ? null : this.needs.values()));
		build.append("\n\tgoals:" + this.goals);
		if (this.doableActions != null)
			build.append("\n\tdoableactions:" + this.doableActions);
		if (this.infoTable != null)
			build.append("\n\tinfo:" + this.infoTable);
		// TODO add more relevant info
		build.append("\n\t}");
		return build.toString();
	}

	public Collection<Culture> cultures() {
		return Set.copyOf(this.cultures);
	}

	/**
	 * Resets the senses -- clears everything that was sensed in the day
	 */
	public void clearSenses() {
		this.senses.clearAll();
	}

	@Override
	public void prune(int passes) {
		// TODO pruning
		if (passes <= 0)
			throw new IllegalArgumentException("" + passes);
		recognition.processMemory(this, senses, passes);
		for (int i = 0; i < passes; i++) {

		}
	}

	public int size() {
		return this.doableActions.size() + this.infoTable.size() + this.profiles.size() + this.propertyConcepts.size();
	}

	public boolean isFeelingCurious() {
		return feelingCurious;
	}

	public void setFeelingCurious(boolean feelingCurious) {
		this.feelingCurious = feelingCurious;
	}

	public boolean socializedRecently() {
		return this.socializedRecently;
	}

	public void setSocializedRecently(boolean re) {
		this.socializedRecently = re;
	}

}
