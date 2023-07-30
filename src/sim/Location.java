package sim;

import mind.concepts.type.ILocationMeme;
import mind.concepts.type.Profile;
import sim.interfaces.ILocatable;

public class Location implements ILocatable, ILocationMeme {

	private int x;
	private int y;
	private Profile world;
	public static final String TYPE_STRING = "_location";

	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Location(Location other) {
		this(other.getX(), other.getY());
		this.world = other.world;
	}

	public Location(ILocatable l) {
		this(l.getX(), l.getY());

	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	public Location getLocation() {
		return this;
	}

	public Profile getWorld() {
		return world;
	}

	public Location setWorld(Profile world) {
		Location loc = new Location(this);
		loc.world = world;
		return loc;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	@Override
	public String getUniqueName() {
		return "location_" + x + "_" + y;
	}

	@Override
	public IMemeType getMemeType() {
		return MemeType.LOCATION;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ILocationMeme loc) {
			return this.x == loc.getGeneralLocation().getX() && this.y == loc.getGeneralLocation().getY()
					&& (this.world != null
							? loc.getGeneralLocation().getLocation().world != null
									&& this.world.equals(loc.getGeneralLocation().getLocation().world)
							: loc.getGeneralLocation().getLocation().world == null);
		}
		return false;
	}

	@Override
	public ILocatable getGeneralLocation() {
		return this;
	}

}
