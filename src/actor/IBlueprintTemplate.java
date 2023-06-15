package actor;

import java.util.Map;

/**
 * Indicates a "type" of object, i.e. a blueprint
 * 
 * @author borah
 *
 */
public interface IBlueprintTemplate extends ITemplate {

	/**
	 * The different part types in this thing
	 * 
	 * @return
	 */
	public Map<String, ? extends IComponentType> partTypes();

	/**
	 * The types of material layers in this thing
	 * 
	 * @return
	 */
	public Map<String, ? extends IMaterialLayerType> materialLayerTypes();

	/**
	 * The name of this thing
	 * 
	 * @return
	 */
	public String name();
}
