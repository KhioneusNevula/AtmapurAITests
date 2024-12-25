package sim.interfaces;

import java.util.UUID;

/**
 * Represents something unique that exists, as a member of a category
 * 
 * @author borah
 *
 */
public interface IUniqueThing extends Comparable<IUniqueThing> {

	public UUID getUUID();

	@Override
	default int compareTo(IUniqueThing o) {
		return getUUID().compareTo(o.getUUID());
	}

	/**
	 * Gets the type/species of actor, phenomenon, etc that this is
	 * 
	 * @return
	 */
	public IObjectType getObjectType();

	/**
	 * How different this thing is from the 'prototypical' example of its template.
	 * 
	 * @return
	 */
	public default float uniqueness() {
		return this.getObjectType().averageUniqueness();
	}

	/**
	 * Unique name of a specific unique entity
	 * 
	 * @return
	 */
	public String getUniqueName();

	default String getSimpleName() {
		return this.toString();
	}

}
