package psych_first.perception.knowledge;

import java.util.UUID;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import sim.World;

public class Noosphere {

	private BiMap<UUID, IInformation> info = HashBiMap.create();
	private World world;

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
	public UUID put(IInformation value) {
		if (this.info.inverse().containsKey(value)) {
			return this.getId(value);
		}
		UUID id = UUID.randomUUID();
		this.info.put(id, value);
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
	public <T extends IInformation> T clear(UUID id) {
		return (T) this.info.remove(id);
	}

	/**
	 * removes this piece of knowledge from this noosphere
	 * 
	 * @param <T>
	 * @param x
	 * @param y
	 * @return
	 */
	public UUID clear(IInformation t) {
		return this.info.inverse().remove(t);
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
	public <T extends IInformation> T getInfo(UUID id, Class<T> asClass) {
		return (T) this.info.get(id);
	}

	public <T extends IInformation> T getInfo(UUID id) {
		return (T) getInfo(id, IInformation.class);
	}

	public UUID getId(IInformation info) {
		return this.info.inverse().get(info);
	}

}
