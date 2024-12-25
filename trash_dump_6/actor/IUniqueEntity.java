package actor;

import java.util.Random;

import actor.construction.physical.IVisage;
import phenomenon.IPhenomenon;
import sim.interfaces.IExistsInWorld;
import sim.interfaces.IPhysicalEntity;
import sim.interfaces.IUniqueThing;

/**
 * Thing which can be perceived and assigned properties
 * 
 * @author borah
 *
 */
public interface IUniqueEntity extends IUniqueThing, IExistsInWorld {

	/**
	 * Return the primary way of sensing this thing. <br>
	 * TODO Later, change this to also depend on the sense? maybe
	 * 
	 * @return
	 */
	public IVisage getVisage();

	/**
	 * Whether this entity has a physical position, i.e. is instanceof
	 * {@link IPhysicalEntity} and not a world phenomenon
	 * 
	 * @return
	 */
	public default boolean isPhysical() {
		return this instanceof IPhysicalEntity;
	}

	public default IPhysicalEntity getAsPhysical() {
		return (IPhysicalEntity) this;
	}

	public default boolean isActor() {
		return this instanceof Actor;
	}

	public default Actor getAsActor() {
		return (Actor) this;
	}

	public default boolean isPhenomenon() {
		return this instanceof IPhenomenon;
	}

	public default IPhenomenon getAsPhenomenon() {
		return (IPhenomenon) this;
	}

	public Random rand();

	/**
	 * Get the name used for display purposes in testing this from other similar
	 * entities
	 * 
	 * @return
	 */
	String getSimpleName();

}
