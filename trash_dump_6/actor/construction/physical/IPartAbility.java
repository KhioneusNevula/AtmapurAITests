package actor.construction.physical;

import java.util.Collection;

import actor.construction.properties.IAbilityStat;
import biology.sensing.ISense;

/**
 * A possibly ability of a component part; a present/nonpresent ability
 * represented as a boolean
 */
public interface IPartAbility {

	public String getName();

	/**
	 * Whether this ability is used to sense
	 * 
	 * @return
	 */
	public default boolean isSensingAbility() {
		return this instanceof ISense;
	}

	/**
	 * Return this as a sense
	 * 
	 * @return
	 */
	public default ISense getAsSense() {
		return (ISense) this;
	}

	/**
	 * Gets the stats this part ability expects. Theoretically, the stat is not
	 * necessary to use the ability, but the ability has unpredictable behavior if
	 * the necessary stat is not present.
	 * 
	 * @return
	 */
	public Collection<IAbilityStat<?>> getExpectedStats();
}
