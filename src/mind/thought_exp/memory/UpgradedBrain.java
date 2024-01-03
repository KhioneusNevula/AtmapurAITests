package mind.thought_exp.memory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import mind.concepts.type.IMeme;
import mind.concepts.type.IProfile;
import mind.concepts.type.Profile;
import mind.feeling.IFeeling;
import mind.relationships.IParty;
import mind.relationships.RelationType;
import mind.relationships.Relationship;
import mind.thought_exp.IThoughtMemory;
import mind.thought_exp.culture.UpgradedCulture;
import mind.thought_exp.culture.UpgradedGroup;

public class UpgradedBrain extends UpgradedAbstractKnowledgeBase implements IBrainMemory {

	private Map<IProfile, UpgradedCulture> cultures = new TreeMap<>();
	private Map<IMeme, IFeeling> feelings;
	private Multimap<IThoughtMemory.Type, IThoughtMemory> shortTermMemories = MultimapBuilder
			.enumKeys(IThoughtMemory.Type.class).linkedListValues().build();
	private Multimap<IProfile, Relationship> agreements = MultimapBuilder.treeKeys().linkedListValues().build();
	private Map<IProfile, Float> trust = new TreeMap<>();
	private Map<IProfile, Integer> partOfGroups = new TreeMap<>();

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

	@Override
	public Collection<IThoughtMemory> getShortTermMemories() {
		return this.shortTermMemories.values();
	}

	@Override
	public Collection<IThoughtMemory> getShortTermMemoriesOfType(IThoughtMemory.Type type) {

		return this.shortTermMemories.get(type);
	}

	@Override
	public boolean rememberShortTerm(IThoughtMemory memory) {

		return this.shortTermMemories.put(memory.getType(), memory);
	}

	@Override
	public boolean forgetShortTerm(IThoughtMemory memory) {

		return this.shortTermMemories.remove(memory.getType(), memory);
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

	public String report() {
		return "UpgradedBrain:\n";
	}

}
