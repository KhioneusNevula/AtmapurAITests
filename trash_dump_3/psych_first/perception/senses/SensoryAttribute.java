package psych_first.perception.senses;

import java.util.Set;

import psych_first.perception.knowledge.IKnowledgeType;
import sim.IHasProfile;
import sim.World;

public abstract class SensoryAttribute<T> implements IKnowledgeType<T> {

	private Set<Sense> senses;
	private String id;
	private Class<T> infoClass;
	private Certainty recognition = Certainty.FALSE;

	public SensoryAttribute(String id, Class<T> infoClass, Certainty rec, Sense... senses) {
		this.id = id;
		this.infoClass = infoClass;
		this.senses = Set.of(senses);
		this.recognition = rec;
	}

	/**
	 * return whether this attribute can be used to identify the entity;
	 * 
	 * @return
	 */
	public Certainty getRecognizability() {
		return recognition;
	}

	/**
	 * whether this attribute can genuinely be rendered
	 * 
	 * @param world
	 * @return
	 */
	public boolean canRender(World world) {
		return false;
	}

	/**
	 * render this attribute in the world
	 * 
	 * @param forEntity
	 */
	public void render(IHasProfile forEntity, World drawing) {

	}

	/**
	 * the class of the data in question
	 * 
	 * @return
	 */
	public Class<T> getInfoClass() {
		return infoClass;
	}

	public String getId() {
		return id;
	}

	/**
	 * the senses needed to find out this piece of information, e.g. voice can only
	 * be sensed via sound but shape can be sensed via sight and touch
	 * 
	 * @return
	 */
	public Set<Sense> getSenses() {
		return senses;
	}

	public static class AttributeHolder<T> {
		private SensoryAttribute<T> type;
		private T value;
		private int senseLevel;

		public AttributeHolder(SensoryAttribute<T> type, T value, int senseLevel) {
			this(type, value, senseLevel, senseLevel);

		}

		public AttributeHolder(SensoryAttribute<T> type, T value, int senseLevel, int idLevel) {
			this.type = type;
			this.value = value;
			this.senseLevel = senseLevel;
		}

		public SensoryAttribute<T> getType() {
			return type;
		}

		/**
		 * the level of sensing needed to glean this attribute's information
		 * 
		 * @return
		 */
		public int getSenseLevel() {
			return senseLevel;
		}

		public T getValue() {
			return value;
		}
	}

	@Override
	public String getName() {
		return this.id;
	}

	@Override
	public Class<T> getValueClass() {
		return this.infoClass;
	}

	@Override
	public boolean isSocialKnowledge() {
		return false;
	}

}
