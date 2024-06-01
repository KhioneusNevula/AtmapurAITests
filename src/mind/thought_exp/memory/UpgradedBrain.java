package mind.thought_exp.memory;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.function.Function;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.MultimapBuilder.SortedSetMultimapBuilder;
import com.google.common.collect.SortedSetMultimap;

import mind.concepts.relations.IConceptRelationType;
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
import mind.thought_exp.memory.type.MemoryWrapper;
import sim.relationalclasses.AbstractRelationalGraph.IEdge;

public class UpgradedBrain extends UpgradedAbstractKnowledgeBase implements IBrainMemory {

	private Map<IProfile, UpgradedCulture> cultures = new TreeMap<>();
	private Map<IMeme, IFeeling> feelings;
	/**
	 * Note that the multimap stores using a tree-set that is in latest-to-oldest
	 * order
	 */
	private Map<IThoughtMemory.Interest, SortedSetMultimap<IThoughtMemory.MemoryCategory, MemoryWrapper>> memories = new EnumMap<>(
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
	public Collection<IUpgradedKnowledgeBase> mindAndCultures() {
		return mindAndCultures;
	}

	private MindAndCulturesCollection mindAndCultures = new MindAndCulturesCollection();

	private class MindAndCulturesCollection extends AbstractCollection<IUpgradedKnowledgeBase> {

		@Override
		public boolean contains(Object o) {
			return o instanceof UpgradedCulture ? cultures.containsKey(((UpgradedCulture) o).selfProfile)
					: (o instanceof IBrainMemory ? this == o : false);
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public Iterator<IUpgradedKnowledgeBase> iterator() {
			return new MACIterator();
		}

		@Override
		public int size() {
			return cultures.size() + 1;
		}

		private class MACIterator implements Iterator<IUpgradedKnowledgeBase> {

			private Iterator<UpgradedCulture> culIt = cultures.values().iterator();
			private boolean atHead = true;

			@Override
			public boolean hasNext() {
				return atHead || culIt.hasNext();
			}

			@Override
			public IUpgradedKnowledgeBase next() {
				if (atHead) {
					atHead = false;
					return UpgradedBrain.this;
				}
				return culIt.next();
			}

		}

	}

	@Override
	public <T> Collection<T> getCollectionFromCultures(
			Function<IUpgradedKnowledgeBase, ? extends Collection<? extends T>> function) {
		return new CulturesFunctionCollection<>(function, false);
	}

	@Override
	public <T> Collection<T> getCollectionFromMindAndCultures(
			Function<IUpgradedKnowledgeBase, ? extends Collection<? extends T>> function) {
		return new CulturesFunctionCollection<T>(function, true);
	}

	@Override
	public <T> Collection<T> getCollectionFromMindAndCultures(
			Function<IUpgradedKnowledgeBase, ? extends Collection<? extends T>> function, Random rand) {
		return new CulturesFunctionCollection<T>(function, true).rand(rand);
	}

	private class CulturesFunctionCollection<T> extends AbstractCollection<T> {
		Function<IUpgradedKnowledgeBase, ? extends Collection<? extends T>> function;
		LinkedList<Collection<? extends T>> collections = new LinkedList<>();
		boolean includeMind;
		int size = -1;
		Random rand;

		public CulturesFunctionCollection(Function<IUpgradedKnowledgeBase, ? extends Collection<? extends T>> function,
				boolean includeMind) {
			this.function = function;
			this.includeMind = includeMind;
		}

		private CulturesFunctionCollection<T> rand(Random rand) {
			this.rand = rand;
			return this;
		}

		private void initAll() {
			if (collections.isEmpty()) {
				if (includeMind)
					collections.add(function.apply(UpgradedBrain.this));
				for (UpgradedCulture c : cultures.values()) {
					collections.add(function.apply(c));
				}
			}
		}

		@Override
		public Iterator<T> iterator() {
			return new CFCIterator();
		}

		@Override
		public int size() {
			if (size <= -1) {
				initAll();
				size = 0;
				for (Collection<? extends T> col : this.collections) {
					size += col.size();
				}
			}
			return size;
		}

		private class CFCIterator implements Iterator<T> {
			Iterator<? extends T> curIter;
			Iterator<Collection<? extends T>> colIter;
			T skip;

			public CFCIterator() {
				initAll();
				colIter = rand != null
						? (rand.nextBoolean() ? collections.iterator() : collections.descendingIterator())
						: collections.iterator();
				while (curIter == null && colIter.hasNext()) {
					Collection<? extends T> col = colIter.next();
					if (rand != null && col instanceof Deque<? extends T>ls) {
						curIter = rand.nextBoolean() ? ls.descendingIterator() : ls.iterator();
					} else {
						curIter = col.iterator();
					}
					if (!curIter.hasNext())
						curIter = null;
				}
			}

			@Override
			public boolean hasNext() {
				if (skip != null)
					return true;
				if (curIter == null)
					return false;
				if (curIter.hasNext())
					return true;
				return false;
			}

			@Override
			public T next() {
				T next = null;
				if (skip != null) {
					next = skip;
					skip = null;
					if (curIter == null)
						return next;
				} else {
					if (curIter == null)
						throw new NoSuchElementException();
				}
				if (next == null) {
					next = curIter.next();
					if (rand != null && rand.nextBoolean() && curIter.hasNext()) {
						skip = next;
						next = curIter.next();
					}
				}
				if (!curIter.hasNext()) {
					curIter = null;
					while (curIter == null && colIter.hasNext()) {
						Collection<? extends T> nextColIter = colIter.next();
						if (rand != null && nextColIter instanceof Deque<? extends T>ls) {
							curIter = rand.nextBoolean() ? ls.descendingIterator() : ls.iterator();
						} else {
							curIter = nextColIter.iterator();
						}
						if (!curIter.hasNext())
							curIter = null;
					}
				}

				return next;
			}
		}
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

	/**
	 * If the memory multimap is not present, generate a new one
	 * 
	 * @param memorySection
	 * @return
	 */
	private Multimap<IThoughtMemory.MemoryCategory, MemoryWrapper> makeMemoryMultimapFor(Interest memorySection) {
		if (memorySection == Interest.FORGET)
			throw new IllegalArgumentException();
		return memories.computeIfAbsent(memorySection, (a) -> SortedSetMultimapBuilder
				.enumKeys(IThoughtMemory.MemoryCategory.class).<MemoryWrapper>treeSetValues((ag, bg) -> {
					int diff = (int) (bg.getTime() - ag.getTime());
					if (diff != 0)
						return diff;
					else
						return ag.getMemory().hashCode() - bg.getMemory().hashCode();
				}).build());
	}

	@Override
	public Collection<MemoryWrapper> getShortTermMemories() {
		return this.memories.get(Interest.SHORT_TERM) == null ? Collections.emptySet()
				: this.memories.get(Interest.SHORT_TERM).values();
	}

	@Override
	public Collection<MemoryWrapper> getShortTermMemoriesOfType(IThoughtMemory.MemoryCategory type) {

		return this.memories.get(Interest.SHORT_TERM) == null ? Collections.emptySet()
				: this.memories.get(Interest.SHORT_TERM).get(type);
	}

	@Override
	public boolean rememberShortTerm(IThoughtMemory memory, long time) {

		return makeMemoryMultimapFor(Interest.SHORT_TERM).put(memory.getType(), new MemoryWrapper(memory, time));
	}

	@Override
	public boolean remember(Interest memoryType, IThoughtMemory memory, long time) {
		return makeMemoryMultimapFor(memoryType).put(memory.getType(), new MemoryWrapper(memory, time));
	}

	@Override
	public <T extends IMeme> void learnRelationAndAddSource(IMeme one, IMeme other, IConceptRelationType type,
			IThoughtMemory source) {
		IEdge<IConceptRelationType, Collection<IMeme>, IMeme> edge = this.relations.addSourceToEdge(one, other, type,
				source, new TreeSet<>((a, b) -> a.getUniqueName().compareTo(b.getUniqueName())));
	}

	@Override
	public <T extends IMeme> Collection<T> tryForgetRelationUsingSource(IMeme one, IMeme other,
			IConceptRelationType type, IThoughtMemory source) {
		IEdge<IConceptRelationType, Collection<IMeme>, IMeme> edge = relations.attemptRemoveEdge(one, other, type,
				source);
		if (edge == null)
			return null;
		return (Collection<T>) edge.getArgs();
	}

	@Override
	public boolean forgetMemory(Interest section, IThoughtMemory memory) {
		if (this.memories.get(section) == null)
			return false;
		return this.memories.get(section).remove(memory.getType(), memory);
	}

	@Override
	public Collection<MemoryWrapper> getMemories(Interest section) {
		if (this.memories.get(section) == null)
			return Collections.emptySet();
		return this.memories.get(section).values();
	}

	@Override
	public Collection<MemoryWrapper> getMemoriesOfType(Interest section, MemoryCategory type) {

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
		if (!relations.isEmpty()) {
			builder.append("\trelations: V=" + this.relations.getAllNodes() + "\n");
			builder.append("\t\t: E=" + this.relations.getAllEdges() + "\n");
		}
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
