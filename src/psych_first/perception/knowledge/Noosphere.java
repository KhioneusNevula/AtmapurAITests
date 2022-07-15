package psych_first.perception.knowledge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import psych_first.perception.knowledge.facts.PieceOfInformation;
import sim.World;

public class Noosphere {

	public static class Node<T> {
		IKnowledgeType<T> type;
		T value;

		public Node(IKnowledgeType<T> type, Object value) {
			this.type = type;
			this.value = (T) value;

		}

		public IKnowledgeType<T> getType() {
			return type;
		}

		public T getValue() {
			return value;
		}

		@Override
		public boolean equals(Object obj) {
			return obj instanceof Node<?>n && this.type.equals(n.type) && this.value.equals(n.value);
		}

	}

	private Map<UUID, Node<?>> info = new HashMap<>();
	private List<PieceOfInformation> events = new ArrayList<>();
	private World world;
	private int dx;

	public Noosphere(World world) {
		this.world = world;
	}

	public static Noosphere get(World from) {
		return from.getNoosphere();
	}

	public World getWorld() {
		return world;
	}

	/**
	 * return the id of the knowledge as stored
	 * 
	 * @param type
	 * @param val
	 * @param x
	 * @param y
	 */
	public UUID put(IKnowledgeType<?> type, Object val) {
		UUID id = UUID.randomUUID();
		this.info.put(id, new Node<>(type, val));
		return id;
	}

	/**
	 * removes this piece of knowledge from this noosphere
	 * 
	 * @param <T>
	 * @param x
	 * @param y
	 * @return
	 */
	public <T> Node<T> clear(UUID id) {
		return (Node<T>) this.info.remove(id);
	}

	/**
	 * returns the value in the given slot only if it matches the given type
	 * 
	 * @param <T>
	 * @param type
	 * @param x
	 * @param y
	 * @return
	 */
	public <T> T getInfo(UUID id, IKnowledgeType<T> type) {
		Node<T> g = getNode(id);
		if (g == null)
			return null;
		if (g.getType() != type)
			return null;
		return g.getValue();
	}

	public <T> Node<T> getNode(UUID id) {
		return (Node<T>) this.info.get(id);
	}

}
