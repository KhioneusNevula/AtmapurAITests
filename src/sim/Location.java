package sim;

import sim.interfaces.ILocatable;

public class Location implements ILocatable {

	private int x;
	private int y;
	private World world;
	public static final String TYPE_STRING = "_location";

	public Location(int x, int y, World world) {
		this.x = x;
		this.y = y;
		this.world = world;
	}

	public Location(ILocatable l, World world) {
		this(l.getX(), l.getY(), world);
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
		return this;
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public String toString() {
		return "L(" + x + "," + y + ")";
	}

}
