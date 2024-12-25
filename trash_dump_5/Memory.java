package mind.memory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;

import main.ImmutableCollection;
import mind.Culture;
import mind.Group;
import mind.action.IActionType;
import mind.concepts.PropertyController;
import mind.concepts.type.ILocationMeme;
import mind.concepts.type.IMeme;
import mind.concepts.type.IProfile;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.feeling.Feeling;
import mind.feeling.IFeeling;
import mind.goals.ITaskHint;
import mind.relationships.IParty;
import mind.relationships.RelationType;
import mind.relationships.Relationship;

public class Memory extends AbstractKnowledgeEntity implements IMindMemory {

	// some way of forming associations and whatever among properties
	private Map<IProfile, Culture> cultures = new TreeMap<>();
	/**
	 * TODO maximum sensable things
	 */
	private SenseMemory senses = new SenseMemory(100, true);
	private SenseMemory recognition = new SenseMemory(Integer.MAX_VALUE, false);
	private MemoryEmotions emotions = new MemoryEmotions();
	private Map<IMeme, IFeeling> feelings;
	private SetMultimap<IProfile, Relationship> agreements;
	private TreeMap<UUID, Relationship> agreementsById;
	private Map<IProfile, Float> trust;
	private HashMap<IProfile, Integer> partOfGroups;
	private boolean feelingCurious = true;
	private boolean socializedRecently;

	public Memory(UUID selfID, String type) {
		super(selfID, type);
	}

	public Memory addCulture(Culture culture) {

		cultures.put(culture.getSelfProfile(), culture);
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
		this.emotions.clear();
		this.feelings = null;
	}

	public MemoryEmotions getEmotions() {
		return emotions;
	}

	@Override
	public IFeeling getAssociatedFeeling(IMeme concept) {
		return this.feelings == null ? null : feelings.get(concept);
	}

	/**
	 * Associates a feeling with this concept
	 * 
	 * @param concept
	 * @param feeling
	 */
	public void associateFeeling(IMeme concept, IFeeling feeling) {
		if (this.feelings == null)
			feelings = new HashMap<>();
		feelings.put(concept, feeling);
	}

	/**
	 * Initializes a feeling to associate with a concept, and return it; or return
	 * the existing feeling there
	 * 
	 * @param concept
	 * @return
	 */
	public IFeeling initializeFeeling(IMeme concept) {
		if (feelings != null && feelings.containsKey(concept))
			return feelings.get(concept);
		IFeeling feelin = new Feeling();
		this.associateFeeling(concept, feelin);
		return feelin;
	}

	@Override
	public Profile getProfileFor(UUID entity) {
		Profile prof = super.getProfileFor(entity);
		if (prof == null) {
			for (Culture cul : cultures.values()) {
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
			for (Culture cul : cultures.values()) {
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
			for (Culture cul : this.cultures.values()) {
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
	@Override
	public Map<Culture, IPropertyData> getPropertiesFromCulture(Profile prof, Property cat) {

		Map<Culture, IPropertyData> set = Map.of();
		for (Culture cult : cultures.values()) {
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

	@Override
	public Map<Culture, PropertyController> getPropertyAssociationsFromCulture(Property prop) {
		Map<Culture, PropertyController> set = Map.of();
		for (Culture cult : cultures.values()) {
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

	@Override
	public Multimap<Culture, IActionType<?>> getPossibleActionsFromCulture(ITaskHint hint) {
		Multimap<Culture, IActionType<?>> map = MultimapBuilder.treeKeys().hashSetValues().build();
		for (Culture cult : cultures.values()) {
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
			for (Culture c : cultures.values())
				if (c.isPropertyKnown(prof, cat))
					return true;
			return false;
		}
		return true;
	}

	@Override
	public ILocationMeme getLocation(Profile prof) {
		ILocationMeme loc = this.senses.getSensedLocation(prof);
		if (loc == null)
			loc = this.locationKnowledge != null ? this.locationKnowledge.get(prof) : null;
		return loc;
	}

	@Override
	public Map<Culture, ILocationMeme> getLocationsFromCulture(Profile prof) {
		Map<Culture, ILocationMeme> set = Map.of();
		for (Culture c : cultures.values()) {
			ILocationMeme a = c.getLocation(prof);
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
			for (Culture c : cultures.values()) {
				ILocationMeme a = c.getLocation(prof);
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
	public SenseMemory getSenses() {
		return senses;
	}

	/**
	 * The memory that handles recognition of things and association with physical
	 * (spatial, etc) traits
	 * 
	 * @return
	 */
	public SenseMemory getRecognition() {
		return recognition;
	}

	@Override
	public Multimap<Culture, Profile> getProfilesWithPropertyFromCulture(Property prop) {
		Multimap<Culture, Profile> profs = null;
		for (Culture cult : this.cultures.values()) {
			Collection<Profile> r = cult.getProfilesWithProperty(prop);
			if (!r.isEmpty()) {
				(profs == null ? profs = MultimapBuilder.treeKeys().treeSetValues().build() : profs).putAll(cult, r);
			}
		}
		return profs == null ? ImmutableMultimap.of() : profs;
	}

	private SetMultimap<IProfile, Relationship> findAgreements() {
		return agreements == null ? agreements = MultimapBuilder.treeKeys().treeSetValues().build() : agreements;
	}

	public Collection<Relationship> getRelationshipsWith(IProfile other) {
		return agreements == null ? Set.of() : agreements.get(other);
	}

	public Relationship getRelationship(IProfile with, RelationType type) {
		if (agreements != null) {
			return agreements.get(with).stream().filter((a) -> a.getType() == type).findAny().orElse(null);
		}
		return null;
	}

	public boolean isPartOf(IProfile group) {
		return partOfGroups == null ? null : partOfGroups.getOrDefault(group, 0) > 0;
	}

	public Relationship getRelationshipByID(UUID agreementID) {
		return agreementsById == null ? null : agreementsById.get(agreementID);
	}

	public Collection<IProfile> getAllPartiesWithRelationships() {
		return agreements == null ? Set.of() : agreements.keySet();
	}

	public Collection<Relationship> getAllRelationships() {
		return agreements == null ? Set.of() : agreements.values();
	}

	public float getTrust(IProfile with) {
		return trust == null ? 0 : trust.getOrDefault(with, 0f);
	}

	private void changeTrust(IProfile with, float trust) {
		if (this.trust == null)
			this.trust = new HashMap<>();
		this.trust.put(with, this.trust.getOrDefault(with, 0f) + trust);
	}

	public void establishRelationship(IParty with, Relationship agreement) {
		IProfile profile = this.recognizeProfile(with.getUUID(), with.toString());
		if (agreement.getType().isSingular() && agreements != null) {
			agreements.get(profile).forEach((a) -> {
				if (a.getType() == agreement.getType())
					agreementsById.remove(a.getAgreementID());
			});
			agreements.get(profile).removeIf((a) -> a.getType() == agreement.getType());
		}
		this.findAgreements().put(profile, agreement);

		(this.agreementsById == null ? this.agreementsById = new TreeMap<>() : agreementsById)
				.put(agreement.getAgreementID(), agreement);
		if (agreement.getType().isMembership()) {
			(partOfGroups == null ? partOfGroups = new HashMap<>() : partOfGroups).put(profile,
					partOfGroups.getOrDefault(with, 0) + 1);
			if (with instanceof Group)
				this.addCulture(((Group) with).getCulture());
		} else if (agreement.getType() == RelationType.FEEL) {
			this.changeTrust(profile, agreement.getGoal().asPersonalRelationship().trust());
		}
	}

	public void dissolveRelationship(IProfile with, Relationship agreement) {
		if (this.agreements != null && this.agreements.remove(with, agreement)) {
			if (this.agreementsById != null)
				this.agreementsById.remove(agreement.getAgreementID());
			if (agreement.getType().isMembership() && partOfGroups != null) {
				int a = partOfGroups.getOrDefault(with, 0);
				if (a == 1) {
					partOfGroups.remove(with);
					this.cultures.remove(with);
				} else if (a <= 0)
					throw new IllegalStateException("Group membership broken? with " + with);
				else
					partOfGroups.put(with, a - 1);
			} else if (agreement.getType() == RelationType.FEEL) {
				this.changeTrust(with, -agreement.getGoal().asPersonalRelationship().trust());
			}
		}
	}

	public boolean hasRelationshipsWith(IProfile other) {
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

	@Override
	public Collection<Culture> cultures() {
		return new ImmutableCollection<>(this.cultures.values());
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

	@Override
	public String toString() {
		return "memory_" + this.getSelfProfile();
	}

}
