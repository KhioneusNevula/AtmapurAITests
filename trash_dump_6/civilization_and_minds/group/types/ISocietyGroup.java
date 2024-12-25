package civilization_and_minds.group.types;

import java.util.Collection;

import civilization_and_minds.group.IGroup;
import civilization_and_minds.group.roles.IRoleGroup;
import sim.Tile;

/**
 * Any kind of group which can centralize power, such as a civilization or
 * settlement
 * 
 * @author borah
 *
 */
public interface ISocietyGroup extends IGroup {

	/**
	 * If this society has a central state, this represents it
	 * 
	 * @return
	 */
	public IGroup getState();

	/**
	 * Whether this society has a central state
	 * 
	 * @return
	 */
	public boolean hasCentralState();

	/**
	 * Get all roles in this society
	 * 
	 * @return
	 */
	public Collection<IRoleGroup> getRoles();

	/**
	 * Gets all the territory that this entity covers
	 * 
	 * @return
	 */
	public Collection<Tile> getTerritory();

	/**
	 * If this entity only covers one territory, get that territory. If it covers
	 * multiple, this is the territory which is considered to be most influential,
	 * powerful, or original
	 * 
	 * @return
	 */
	public Tile getCentralTerritory();

	/**
	 * How many territories this entity covers
	 * 
	 * @return
	 */
	public int territoryCount();

}
