package psych_first.perception.senses;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import culture.CulturalContext;
import psych_first.perception.knowledge.IKnowledgeType;
import psych_first.perception.senses.types.ContactSense;
import psych_first.perception.senses.types.RadialSense;
import psych_first.perception.senses.types.RaySense;
import sociology.ITypeClass;
import sociology.TypeProfile;

public abstract class Sense implements Comparable<Sense>, ITypeClass {

	public static final RadialSense SOUND = new RadialSense("sound", 3);
	public static final RadialSense SMELL = new RadialSense("smell", 3);
	public static final RaySense SIGHT = new RaySense("sight", 3);
	public static final ContactSense TOUCH = new ContactSense("touch", 3);

	private String id;
	private int standardLevel;
	private UUID uuid = UUID.randomUUID();
	private TypeProfile type;

	public Sense(String id, int standardLevel) {
		this.id = id;
		this.standardLevel = standardLevel;
	}

	/**
	 * TODO add this to the world
	 */
	@Override
	public ITypeClass setProfile(TypeProfile or) {
		this.type = or;
		return this;
	}

	public int getStandardLevel() {
		return standardLevel;
	}

	public String getId() {
		return id;
	}

	/**
	 * return whether this sensor system can sense the given output using this
	 * sense, i.e. whether it is in field of view or whatever
	 * 
	 * @param sensor
	 * @param output
	 * @return
	 */
	public abstract boolean canSense(SensorSystem sensor, SensoryOutput output);

	/**
	 * checks and returns all traits of the output that could be sensed; this is
	 * irrespective of sense level
	 * 
	 * @param sensor
	 * @param sensed
	 * @return
	 */
	public abstract Collection<SensoryAttribute<?>> senses(SensorSystem sensor, SensoryInput input);

	/**
	 * returns a set of properties needed for this sense to be present
	 * 
	 * @return
	 */
	public Collection<IKnowledgeType<?>> necessaryProperties() {
		return Collections.emptySet();
	}

	@Override
	public int compareTo(Sense o) {
		return id.compareTo(o.id);
	}

	@Override
	public <T> T getInfo(IKnowledgeType<T> type, CulturalContext ctxt) {
		return this.getProfile().getInfo(type, ctxt);
	}

	@Override
	public UUID getUuid() {

		return this.uuid;
	}

	@Override
	public boolean hasInfo(IKnowledgeType<?> type, CulturalContext ctxt) {
		return this.getProfile().hasInfo(type, ctxt);
	}

	@Override
	public TypeProfile getProfile() {
		return this.type;
	}

}
