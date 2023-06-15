package phenomenon;

import java.util.UUID;

import actor.IUniqueExistence;
import sim.World;

/**
 * TODO someday do phenomenon
 * 
 * @author borah
 *
 */
public abstract class Phenomenon implements IUniqueExistence {

	private World world;
	private UUID id = UUID.randomUUID();

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public UUID getUUID() {
		return id;
	}
}
