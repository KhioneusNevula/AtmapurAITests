package phenomenon;

import entity.IPhysicalExistence;
import sim.IHasProfile;
import sim.Location;
import sim.World;
import sociology.Profile;

/**
 * TODO someday this will be done lol
 * 
 * @author borah
 *
 */
public abstract class Phenomenon implements IPhysicalExistence, IHasProfile {

	private Profile profile;
	private int x;
	private int y;
	private Location location;

	@Override
	public Profile getProfile() {
		return profile;
	}

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
		return null;
	}

	@Override
	public void setX(int x) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setY(int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public World getWorld() {
		// TODO Auto-generated method stub
		return null;
	}

}
