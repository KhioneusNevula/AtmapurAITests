package biology.anatomy;

import java.util.Map;

import actor.construction.physical.IActorType;

/**
 * A template type for a living entity, typically iwth many physical parts
 * 
 * @author borah
 *
 */
public interface ISpecies extends IActorType {

	/**
	 * More name-specific implementation of
	 * {@link IActorType#materialLayerTypes()}
	 * 
	 * @return
	 */
	public Map<String, ITissueLayerType> tissueTypes();

	@Override
	public Map<String, IBodyPartType> partTypes();

	/**
	 * TODO Generates a default, static culture that all members of this species
	 * subscribe to, e.g. to define their actions and whatnot. may be null for
	 * nonsentient species
	 * 
	 * @return
	 */

}
