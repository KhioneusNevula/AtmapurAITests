package phenomenon;

import sim.interfaces.IPhysicalEntity;
import utilities.Location;

/**
 * TODO someday do worldphenomenon
 * 
 * @author borah
 *
 */
public abstract class WorldPhenomenon extends Phenomenon implements IPhysicalEntity {

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
