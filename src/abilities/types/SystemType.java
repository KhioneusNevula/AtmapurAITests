package abilities.types;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiFunction;

import abilities.EntitySystem;
import abilities.ISystemHolder;

public final class SystemType<T extends EntitySystem> implements Comparable<SystemType<?>> {
	private static final Map<String, SystemType<?>> allTypes = new TreeMap<>();

	/**
	 * parameter 1 (int) = maxHealth <br>
	 * parameter 2 (int) = number where health is severely bad<br>
	 * parameter 3 (double) = chance out of 20 to heal one point every tick; may
	 * change
	 */
	public static final SystemType<LifeSystem> LIFE = new SystemType<>("life", LifeSystem.class, LifeSystem::new);
	/**
	 * parameter 1 (int) = maxHunger <br>
	 * parameter 2 (double) = chance out of 20 to lose one point hunger every tick
	 */
	public static final SystemType<HungerSystem> HUNGER = new SystemType<>("hunger", HungerSystem.class,
			HungerSystem::new, LIFE);

	String id;
	private Class<T> type;
	private BiFunction<ISystemHolder, Object[], T> instantiate;
	Set<SystemType<?>> requiredSystems;

	private SystemType(String id, Class<T> type, BiFunction<ISystemHolder, Object[], T> instantiate,
			SystemType<?>... required) {
		this.id = id;
		this.type = type;
		this.requiredSystems = Set.of(required);

		this.instantiate = instantiate;
		allTypes.put(id, this);
	}

	public T instantiate(ISystemHolder for_, Object... args) {
		return this.instantiate.apply(for_, args);
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

	public static <T extends EntitySystem> SystemType<T> get(String id) {
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