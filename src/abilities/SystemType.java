package abilities;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import abilities.types.BreathSystem;
import abilities.types.HungerSystem;
import abilities.types.LifeSystem;

public class SystemType<T extends ESystem> implements Comparable<SystemType<?>> {
	private static final Map<String, SystemType<?>> allTypes = new TreeMap<>();

	public static final SystemType<LifeSystem> LIFE = new SystemType<>("life", LifeSystem.class);

	public static final SystemType<HungerSystem> HUNGER = new SystemType<>("hunger", HungerSystem.class, LIFE);

	public static final SystemType<BreathSystem> BREATH = new SystemType<>("breath", BreathSystem.class, HUNGER);

	String id;
	private Class<T> type;
	Set<SystemType<?>> requiredSystems;

	protected SystemType(String id, Class<T> type, SystemType<?>... required) {
		this.id = id;
		this.type = type;
		this.requiredSystems = Set.of(required);

		allTypes.put(id, this);
	}

	public String getId() {
		return id;
	}

	public Class<T> getType() {
		return type;
	}

	@Override
	public String toString() {
		return "SYSTEM_" + id;
	}

	public Set<SystemType<?>> getRequiredSystems() {
		return requiredSystems;
	}

	public static <T extends ESystem> SystemType<T> get(String id) {
		return (SystemType<T>) allTypes.get(id);
	}

	public static Iterator<SystemType<?>> iterate() {
		return allTypes.values().iterator();
	}

	@Override
	public int compareTo(SystemType<?> o) {
		return o == null ? 1 : (this.id.compareTo(o.id));
	}

}