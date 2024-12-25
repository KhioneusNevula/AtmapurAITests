package actor.construction.properties;

/**
 * A statistic relating to abilities that body parts can have. For example, for
 * sight we have a SightLevel (or something similar)
 * 
 * @author borah <T> the type of value, assumed to be immutable and hashable,
 *         associated with this stat
 */
public interface IAbilityStat<T> {

	/**
	 * The class of the value of this stat
	 * 
	 * @return
	 */
	public Class<? super T> getValueClass();

	public String getUniqueName();

	/**
	 * Whatt value to return if the statistic is not present at all
	 * 
	 * @return
	 */
	public T defaultValue();
}
