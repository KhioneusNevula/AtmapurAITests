package phenomenon;

import sim.Location;
import sim.interfaces.IPhysicalExistence;

/**
 * TODO someday do worldphenomenon
 * 
 * @author borah
 *
 */
public abstract class WorldPhenomenon extends Phenomenon implements IPhysicalExistence {

	public WorldPhenomenon(IPhenomenonType type) {
		super(type);
	}

	private int x;
	private int y;
	private Location location;

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public Location getLocation() {
		return location;
	}
}
