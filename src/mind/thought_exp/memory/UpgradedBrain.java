package mind.thought_exp.memory;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import mind.concepts.relations.RelationsGraph;
import mind.concepts.type.IMeme;
import mind.concepts.type.IProfile;
import mind.concepts.type.Profile;
import mind.feeling.IFeeling;
import mind.relationships.IParty;
import mind.relationships.RelationType;
import mind.relationships.Relationship;
import mind.thought_exp.IThoughtMemory;
import mind.thought_exp.IThoughtMemory.Interest;
import mind.thought_exp.IThoughtMemory.MemoryCategory;
import mind.thought_exp.culture.UpgradedCulture;
import mind.thought_exp.culture.UpgradedGroup;

public class UpgradedBrain extends UpgradedAbstractKnowledgeBase implements IBrainMemory {

	private Map<IProfile, UpgradedCulture> cultures = new TreeMap<>();
	private Map<IMeme, IFeeling> feelings;
	private Map<IThoughtMemory.Interest, Multimap<IThoughtMemory.MemoryCategory, IThoughtMemory>> memories = new EnumMap<>(
			IThoughtMemory.Interest.class);

	private Multimap<IProfile, Relationship> agreements = MultimapBuilder.treeKeys().linkedListValues().build();
	private Map<IProfile, Float> trust = new TreeMap<>();
	private Map<IProfile, Integer> partOfGroups = new TreeMap<>();
	// TODO short term memory maximum and all that

	public UpgradedBrain(UUID selfID, String type) {
		super(new Profile(selfID, type));
	}

	@Override
	public IFeeling getAssociatedFeeling(IMeme concept) {
		return feelings == null ? null : feelings.get(concept);
	}

	public void prune(int passes) {
		// TODO prune memory

	}

	@Override
	public Collection<UpgradedCulture> cultures() {
		return cultures.values();
	}

	@Override
	public void addCulture(UpgradedCulture a) {
		this.cultures.put(a.getSelf(), a);
	}

	@Override
	public void forgetCulture(UpgradedCulture toForget) {
		this.cultures.remove(toForget.getSelf(), toForget);
	}

	@Override
	public void forgetCulture(IProfile toForget) {
		this.cultures.remove(toForget);
	}

	@Override
	public boolean forgetConcept(IMeme concept) {
		return super.forgetConcept(concept);
	}

	@Override
	public boolean learnConcept(IMeme concept) {
		return super.learnConcept(concept);
	}

	private Multimap<IThoughtMemory.MemoryCategory, IThoughtMemory> makeMemoryMultimapFor(Interest memorySection) {
		if (memorySection == Interest.FORGET)
			throw new IllegalArgumentException();
		return memories.computeIfAbsent(memorySection,
				(a) -> MultimapBuilder.enumKeys(IThoughtMemory.MemoryCategory.class).linkedListValues().build());
	}

	@Override
	public Collection<IThoughtMemory> getShortTermMemories() {
		return this.memories.get(Interest.SHORT_TERM) == null ? Collections.emptySet()
				: this.memories.get(Interest.SHORT_TERM).values();
	}

	@Override
	public Collection<IThoughtMemory> getShortTermMemoriesOfType(IThoughtMemory.MemoryCategory type) {

		return this.memories.get(Interest.SHORT_TERM) == null ? Collections.emptySet()
				: this.memories.get(Interest.SHORT_TERM).get(type);
	}

	@Override
	public boolean rememberShortTerm(IThoughtMemory memory) {

		return makeMemoryMultimapFor(Interest.SHORT_TERM).put(memory.getType(), memory);
	}

	@Override
	public boolean remember(Interest memoryType, IThoughtMemory memory) {
		return makeMemoryMultimapFor(memoryType).put(memory.getType(), memory);
	}

	@Override
	public boolean forgetMemory(Interest section, IThoughtMemory memory) {
		if (this.memories.get(section) == null)
			return false;
		return this.memories.get(section).remove(memory.getType(), memory);
	}

	@Override
	public Collection<IThoughtMemory> getMemories(Interest section) {
		if (this.memories.get(section) == null)
			return Collections.emptySet();
		return this.memories.get(section).values();
	}

	@Override
	public Collection<IThoughtMemory> getMemoriesOfType(Interest section, MemoryCategory type) {

		return this.memories.get(section) == null ? Collections.emptySet() : this.memories.get(section).get(type);
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
		IProfile profile;
		this.learnProfile(profile = new Profile(with));
		if (agreement.getType().isSingular() && agreements != null) {
			agreements.get(profile).removeIf((a) -> a.getType() == agreement.getType());
		}
		this.agreements.put(profile, agreement);

		if (agreement.getType().isMembership()) {
			(partOfGroups == null ? partOfGroups = new HashMap<>() : partOfGroups).put(profile,
					partOfGroups.getOrDefault(with, 0) + 1);
			if (with instanceof UpgradedGroup)
				this.addCulture(((UpgradedGroup) with).getCulture());
		} else if (agreement.getType() == RelationType.FEEL) {
			this.changeTrust(profile, agreement.getGoal().asPersonalRelationship().trust());
		}
	}

	public void dissolveRelationship(IProfile with, Relationship agreement) {
		if (this.agreements != null && this.agreements.remove(with, agreement)) {
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
	public RelationsGraph getRelationsGraph() {
		return this.relations;
	}

	public String report() {
		StringBuilder builder = new StringBuilder();
		builder.append("UpgradedBrain: {\n");
		builder.append("\tselfProfile: " + this.selfProfile + ", ");
		builder.append("cultures: " + this.cultures + "\n");
		if (!knownConcepts.isEmpty())
			builder.append("\tknownConcepts: " + this.knownConcepts.values() + "\n");
		if (!relations.isEmpty())
			builder.append("\trelations: " + this.relations + "\n");
		if (!relationships.isEmpty())
			builder.append("\tknownRelationships: " + this.relationships + "\n");
		if (!identifiers.isEmpty())
			builder.append("\tpropertyIdentifiers: " + this.identifiers + "\n");
		if (!profileProperties.isEmpty())
			builder.append("\tprofileProperties: " + this.profileProperties + "\n");
		if (!needs.isEmpty())
			builder.append("\tneeds: " + this.needs.values() + "\n");
		if (!goals.isEmpty())
			builder.append("\tgoals: " + this.goals.values() + "\n");
		if (!agreements.isEmpty())
			builder.append("\tagreements: " + this.agreements + "\n");
		if (!memories.isEmpty())
			builder.append("\tshortTermMemories: " + this.memories.values() + "\n");
		builder.append("}");
		return builder.toString();
	}

	@Override
	public String toString() {
		return "Brain(" + this.selfProfile + ")";
	}

}
