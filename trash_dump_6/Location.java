package utilities;

public class Location {

	private int x;
	private int y;
	public static final String TYPE_STRING = "_location";

	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Location(Location other) {
		this(other.getX(), other.getY());
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Location getLocation() {
		return this;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	public String getUniqueName() {
		return "location_" + x + "_" + y;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Location loc) {
			return this.x == loc.x && this.y == loc.y;
		}
		return false;
	}

}
