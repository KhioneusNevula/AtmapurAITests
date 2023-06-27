package sim;

import mind.concepts.type.IMeme;
import mind.concepts.type.Profile;
import sim.interfaces.ILocatable;

public class Location implements ILocatable, IMeme {

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

}
