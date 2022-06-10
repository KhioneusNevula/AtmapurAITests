package entity;

/**
 * Things which have a physical position
 * 
 * @author borah
 *
 */
public interface IPhysicalExistence {
	public int getX();

	public int getY();

	public void setX(int x);

	public void setY(int y);

	public void move(int xplus, int yplus);

	public default double distance(IPhysicalExistence other) {
		return distance(other.getX(), other.getY());
	}

	public default double distance(int x, int y) {
		return Math.sqrt(Math.pow((x - getX()), 2) + Math.pow((y - getY()), 2));
	}
}
