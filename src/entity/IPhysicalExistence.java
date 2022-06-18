package entity;

/**
 * Things which have a physical position
 * 
 * @author borah
 *
 */
public interface IPhysicalExistence extends ILocatable {

	public void setX(int x);

	public void setY(int y);

	public void move(int xplus, int yplus);

}
