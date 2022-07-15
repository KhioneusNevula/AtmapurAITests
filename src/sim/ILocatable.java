package sim;

public interface ILocatable {
	public int getX();

	public int getY();

	public Location getLocation();

	public default double distance(ILocatable other) {
		return distance(other.getX(), other.getY());
	}

	public default double distance(int x, int y) {
		return Math.sqrt(Math.pow((x - getX()), 2) + Math.pow((y - getY()), 2));
	}

	public default boolean at(int x, int y) {
		return this.getX() == x && this.getY() == y;
	}

	public default boolean at(ILocatable other) {
		return this.at(other.getX(), other.getY());
	}

	public default boolean adjacentTo(int x, int y) {
		if (at(x, y))
			return false;
		return this.distance(x, y) <= 1;
	}

	public default boolean adjacentTo(ILocatable other) {
		return adjacentTo(other.getX(), other.getY());
	}

	public World getWorld();

}
