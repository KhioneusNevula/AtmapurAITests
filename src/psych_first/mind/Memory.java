package psych_first.mind;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import psych_first.action.types.Action;
import psych_first.mind.Memory.MemoryType.MapMemoryType;
import psych_first.perception.knowledge.Identity;
import sociology.Profile;

/**
 * This system is so that memories can be associated with emotions and therefore
 * sorted by importance so that they may be forgotten. Every MemoryNode has a
 * UUID so it is fully unique; every *Type* of memory has a static MemoryType
 * for quick access; every MemoryType has a MemorySection in the brain of an
 * individual, and every MemorySection exists in a Memory
 * 
 * @author borah
 */
public class Memory implements IMindPart {

	private Map<MemoryType<?>, MemorySection<?>> memories = new TreeMap<>();

	private Mind mind;

	public String report() {
		StringBuilder str = new StringBuilder("{");
		boolean did1 = false;
		for (MemoryType<?> type : memories.keySet()) {
			did1 = true;
			str.append(type.id + ":{");
			boolean did = false;
			for (Object memory : memories.get(type)) {

				str.append("<" + memory + ">~" + memories.get(type).getRefreshesFor(memory) + ",");
				did = true;
			}
			if (did)
				str.deleteCharAt(str.length() - 1);
			str.append("},");
		}
		if (did1)
			str.deleteCharAt(str.length() - 1);
		str.append("}");
		return str.toString();
	}

	public Memory(Mind mind) {
		this.mind = mind;
	}

	public void update(int ticks) {
		// TODO make a forgetting system and whatever
		for (MemorySection<?> sec : memories.values()) {
			sec.update(ticks);
		}
	}

	public Mind getMind() {
		return mind;
	}

	/**
	 * Adds memory sections of the given types to the mind but does not add ones for
	 * which there already exists a section; returns true if any new sections were
	 * added
	 * 
	 * @param units
	 * @return
	 */
	public boolean initializeSections(MemoryType<?>... types) {
		boolean b = false;
		for (MemoryType<?> type : types) {
			if (!this.hasMemories(type)) {
				memories.put(type, new MemorySection<>(type, this));
				b = true;
			}
		}
		return b;
	}

	/**
	 * Whether this has memories of the given type
	 * 
	 * @param type
	 * @return
	 */
	public boolean hasMemories(MemoryType<?> type) {
		return memories.containsKey(type);
	}

	/**
	 * Return the memory section for the memories of this type
	 * 
	 * @param <T>
	 * @param type
	 * @return
	 */
	public <T> MemorySection<T> getMemories(MemoryType<T> type) {
		return (MemorySection<T>) memories.get(type);
	}

	public static class MapMemorySection<K, L> extends MemorySection<L> {

		private Map<K, UUID> keyMap = new HashMap<>();

		public MapMemorySection(MapMemoryType<K, L> type, Memory owner) {
			super(type, owner);
		}

		public L getMemory(K key) {
			return getMemory(keyMap.get(key));
		}

		public boolean contains(Object o) {
			return super.contains(o) || this.keyMap.containsKey(o);
		}

	}

	/**
	 * A section of memories in the brain, i.e. a way to encapsulate a collection of
	 * memories of a specific type
	 * 
	 * @author borah
	 *
	 * @param <L>
	 */
	public static class MemorySection<L> implements Iterable<L> {
		private MemoryType<L> memoryType;
		private Map<UUID, Node> memories = new HashMap<>();
		private Map<L, UUID> memoriesInverse = new HashMap<>();
		private final Node EMPTY_NODE = new Node(null);
		private Memory owner;
		// private Map<UUID, Emotion> memoryEmotions = new TreeMap<>(); // TODO emotions

		private class Node {
			private L memory;
			private int refreshes;

			// private Emotion emotion; // TODO emotions
			Node(L memory) {
				this.memory = memory;
				this.refreshes = 1;
			}

			@Override
			public String toString() {
				return memoryType.toString() + memory + "; refreshes: " + refreshes;
			}
		}

		public MemorySection(MemoryType<L> memoryType, Memory owner) {
			this.memoryType = memoryType;
			this.owner = owner;

		}

		public Memory getOwner() {
			return owner;
		}

		/**
		 * TODO make a more emotion-based (or data driven?) forgetting system
		 */
		public void update(int ticks) {
			if (ticks % 40 == 0) {
				for (Node node : new TreeMap<>(memories).values()) {
					if (this.forget(node)) {
						/*
						 * TODO remove this print statement
						 */
						/*
						 * System.out.println(this.owner.getMind().getOwner().getName() + " forgot " +
						 * node);
						 */
					}
				}
			}
		}

		/**
		 * how many times this memory has been refreshed (refresh counts decrease every
		 * time the memory is attempted to be forgotten)
		 * 
		 * @param memoryID
		 * @return
		 */
		public int getRefreshes(UUID memoryID) {
			return memories.getOrDefault(memoryID, EMPTY_NODE).refreshes;
		}

		/**
		 * Returns 0 if the memory does not exist
		 * 
		 * @param memory
		 * @return
		 */
		public int getRefreshesFor(Object memory) {
			if (!memoriesInverse.containsKey(memory))
				return 0;
			return getRefreshes(memoriesInverse.get(memory));
		}

		public void setRefreshes(UUID id, int r) {
			if (memories.containsKey(id)) {
				memories.get(id).refreshes = r;
			}
		}

		public void setRefreshesFor(Object memory, int r) {
			if (!memoriesInverse.containsKey(memory)) {
				this.add(memory);
			}
			this.makeEternal(memoriesInverse.get(memory));
		}

		/**
		 * makes the memory harder to forget;
		 */
		public int refresh(UUID memoryId) {
			if (!memories.containsKey(memoryId))
				return -1;
			return ++memories.get(memoryId).refreshes;
		}

		/**
		 * makes the given memory eternal by setting its refreshes to -1.
		 * 
		 * @param memory
		 */
		public void makeEternal(UUID id) {
			if (memories.containsKey(id)) {
				memories.get(id).refreshes = -1;
			}
		}

		/**
		 * makes the given memory eternal by setting its refreshes to -1
		 * 
		 * @param memory
		 */
		public void makeEternalFor(Object memory) {
			if (!memoriesInverse.containsKey(memory)) {
				this.add(memory);
			}
			this.makeEternal(memoriesInverse.get(memory));
		}

		/**
		 * makes memory harder to forget; adds memory if not present
		 * 
		 * @param memory
		 * @return
		 */
		public int refreshFor(Object memory) {
			if (!memoriesInverse.containsKey(memory)) {
				this.add(memory);
			}
			return this.refresh(memoriesInverse.get(memory));

		}

		public L getMemory(UUID id) {
			return memories.getOrDefault(id, EMPTY_NODE).memory;
		}

		public UUID getUUID(Object memory) {
			return memoriesInverse.get(memory);
		}

		public Collection<L> getAllMemories() {
			return memoriesInverse.keySet();
		}

		public MemoryType<L> getMemoryType() {
			return memoryType;
		}

		public int size() {
			return memories.size();
		}

		public boolean isEmpty() {
			return memories.isEmpty();
		}

		public boolean contains(Object o) {
			return memories.containsValue(o) ? true
					: (memoriesInverse.containsKey(o) ? true : (o instanceof UUID ? memories.containsKey(o) : false));
		}

		public Iterator<L> iterator() {
			return getAllMemories().iterator();
		}

		public UUID add(Object e) {
			UUID r = UUID.randomUUID();
			memories.put(r, new Node((L) e)); // TODO emotion functionality
			memoriesInverse.put((L) e, r);
			return r;
		}

		public boolean addAll(Collection<?> additions) {
			boolean b = false;
			for (Object o : additions) {
				if (this.memoryType.memoryClass.isInstance(o)) {
					this.add(o);
					b = true;
				}
			}
			return b;
		}

		private UUID remove(Node node) {
			UUID id = memoriesInverse.remove(node.memory);
			if (id != null) {
				memories.remove(id);
			}
			return id;
		}

		/**
		 * removes uuid and returns the memory
		 * 
		 * @param id
		 * @return
		 */
		public L remove(UUID id) {
			Node m = memories.remove(id);
			if (m != null) {
				memoriesInverse.remove(id, m.memory);
			}
			return m.memory;
		}

		/**
		 * removes memory and returns id
		 * 
		 * @param memory
		 * @return
		 */
		public UUID removeMem(Object memory) {
			UUID m = memoriesInverse.remove(memory);
			if (m != null) {
				memories.remove(m);
			}
			return m;
		}

		public boolean remove(UUID id, L memory) {
			Node n = memories.remove(id);
			boolean w = memoriesInverse.remove(n.memory, id);
			return w;
		}

		public void clear() {
			memories.clear();
			memoriesInverse.clear();
		}

		/**
		 * return true if the memory has been removed
		 * 
		 * @param memory
		 * @return
		 */
		private boolean forget(Node memory) {
			memory.refreshes--;
			if (memory.refreshes == 0) {
				this.remove(memory);
				return true;
			}
			return false;
		}

		/**
		 * attempts to forget the memory; if the memory is fully forgotten, return true.
		 * force will automatically remove the memory
		 * 
		 * @param memory
		 * @param force
		 */
		public boolean forget(L memory, boolean force) {
			if (force) {
				return this.removeMem(memory) != null;
			}
			UUID id = memoriesInverse.get(memory);
			if (id == null)
				return false;
			return this.forget(memories.get(id));
		}

		/**
		 * force-forgets all memories
		 */
		public void forgetAll() {
			clear();
		}

	}

	/**
	 * A type of memory, e.g. profile memories or whatever
	 * 
	 * @author borah
	 *
	 * @param <L>
	 */
	public static class MemoryType<L> implements Comparable<MemoryType<?>> {

		public static final MemoryType<Profile> PROFILE = type("profile", Profile.class);
		public static final MemoryType<Action> POSSIBLE_ACTIONS = type("possible_actions", Action.class);
		public static final MemoryType<Identity> IDENTITIES = type("identity", Identity.class);

		private Class<L> memoryClass;
		private String id;

		public static <T> MemoryType<T> type(String id, Class<T> memoryClass) {
			return new MemoryType<>(id, memoryClass);
		}

		public static <K, L> MapMemoryType<K, L> type(String id, Class<K> keyClass, Class<L> memoryClass) {
			return new MapMemoryType<>(id, keyClass, memoryClass);
		}

		// private TODO something about instantiating memories and whatnot idk
		protected MemoryType(String id, Class<L> memoryClass) {
			this.id = id;
			this.memoryClass = memoryClass;
		}

		public String getId() {
			return id;
		}

		public Class<L> getMemoryClass() {
			return memoryClass;
		}

		@Override
		public int compareTo(MemoryType<?> o) {
			return this.id.compareTo(o.id);
		}

		@Override
		public String toString() {
			return this.id + " memory ";
		}

		public static class MapMemoryType<K, L> extends MemoryType<L> {
			private Class<K> keyClass;

			protected MapMemoryType(String id, Class<K> keyClass, Class<L> memoryClass) {
				super(id, memoryClass);
				this.keyClass = keyClass;
			}

			public Class<K> getKeyClass() {
				return keyClass;
			}
		}
	}

}
