package entity;

import sim.ILocatable;

/**
 * Things which have a physical position
 * 
 * @author borah
 *
 */
public interface IPhysicalExistence extends ILocatable {

	public void setX(int x);

	public void setY(int y);

	public default void move(int xplus, int yplus) {

		setX(getX() + xplus);
		setY(getY() + yplus);
	}

}
