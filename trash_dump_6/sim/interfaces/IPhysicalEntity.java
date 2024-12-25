package sim.interfaces;

import sim.physicality.IInteractability;

/**
 * Things which have a physical existence, being movable, etc
 * 
 * @author borah
 *
 */
public interface IPhysicalEntity extends ILocatable, IExistsInWorld {

	/**
	 * Which plane(s) of interaction this entity can be interacted on. See
	 * {@link IInteractability}
	 * 
	 * @return
	 */
	public int physicality();

	public void setX(int x);

	public void setY(int y);

	public default void move(int xplus, int yplus) {

		setX(getX() + xplus);
		setY(getY() + yplus);
	}

	public default void moveToward(int x, int y, double step) {
		double dist = this.distance(x, y);
		double stepDist = step;
		if ((dist) < step) {
			this.setX(x);
			this.setY(y);
			return;
		}
		int vecX = x - this.getX();
		int vecY = y - this.getY();
		double vX = vecX / stepDist;
		double vY = vecY / stepDist;
		this.move((int) vX, (int) vY);
	}

}
