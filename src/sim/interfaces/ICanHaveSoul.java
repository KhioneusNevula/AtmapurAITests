package sim.interfaces;

import java.util.Collection;

import abilities.ISystemHolder;
import psychology.Soul;

public interface ICanHaveSoul extends IHasProfile, ISystemHolder {
	/**
	 * get all souls contained in here
	 * 
	 * @return
	 */
	public Collection<Soul> getContainedSouls();

	/**
	 * 
	 * max souls that can fit in here
	 * 
	 * @return
	 */
	public int getMaxSouls();

	/**
	 * gets the soul in primary control of the body
	 * 
	 * @return
	 */
	public Soul getSoul();

	/**
	 * gets the soul that is natural/indigenous to this body, if applicable
	 * 
	 * @return
	 */
	public Soul getNaturalSoul();

}
