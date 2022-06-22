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
}
