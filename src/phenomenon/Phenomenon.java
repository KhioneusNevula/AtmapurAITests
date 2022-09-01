package phenomenon;

import sim.World;
import sim.interfaces.IExistsInWorld;
import sim.interfaces.IHasProfile;
import sim.interfaces.ISensable;

/**
 * TODO someday do phenomenon
 * 
 * @author borah
 *
 */
public abstract class Phenomenon implements IHasProfile, ISensable, IExistsInWorld {

	private World world;

	@Override
	public World getWorld() {
		return world;
	}
}
