package biology.anatomy;

import java.util.Map;

import actor.IBlueprintTemplate;

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

}
