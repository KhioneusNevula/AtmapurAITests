package phenomenon;

import java.util.Collection;

import actor.IUniqueExistence;

public interface IPhenomenon extends IUniqueExistence {

	/**
	 * Gets the SENTIENT cause(s) (if any) of this phenomenon. E.g. a fire
	 * phenomenon may have been lit by a specific sentient entity
	 * 
	 * @return
	 */
	public Collection<IUniqueExistence> cause();

	/**
	 * Gets the NON-SENTIENT cause/source(s) (if any) of this phenomenon. This does
	 * not include a sentient entity source. E.g. a Burn Up event may have a Source
	 * which is an Explosion or a Flame
	 * 
	 * @return
	 */
	public Collection<IUniqueExistence> source();

	/**
	 * Gets the undergoer(s) (if any) of this phenomenon
	 * 
	 * @return
	 */
	public Collection<IUniqueExistence> object();

	/**
	 * Gets the result of this phenomenon if it is
	 * transformative/destructive/creative
	 * 
	 * @return
	 */
	public Collection<IUniqueExistence> products();

	/**
	 * Gets the type of phenomenon this is
	 * 
	 * @return
	 */
	public IPhenomenonType type();
}
