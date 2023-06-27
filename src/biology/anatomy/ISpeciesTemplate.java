package biology.anatomy;

import java.util.Map;

import actor.IBlueprintTemplate;
import mind.Culture;

public interface ISpeciesTemplate extends IBlueprintTemplate {

	/**
	 * More name-specific implementation of
	 * {@link IBlueprintTemplate#materialLayerTypes()}
	 * 
	 * @return
	 */
	public Map<String, ITissueLayerType> tissueTypes();

	@Override
	default Map<String, ITissueLayerType> materialLayerTypes() {
		return tissueTypes();
	}

	@Override
	public Map<String, IBodyPartType> partTypes();

	/**
	 * Generates a default, static culture that all members of this species
	 * subscribe to, e.g. to define their actions and whatnot. may be null for
	 * nonsentient species
	 * 
	 * @return
	 */
	public Culture genDefaultCulture();

}
