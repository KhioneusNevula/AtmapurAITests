package actor;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import biology.systems.types.ISensor;
import mind.concepts.type.SenseProperty;

public interface IMultipart extends IVisage {
	/**
	 * Get parts that are outermost (and usually sense-able)
	 * 
	 * @return
	 */
	public Map<UUID, ? extends IComponentPart> getOutermostParts();

	/**
	 * Whether all the necessary steps have been performed to properly make this
	 * body
	 * 
	 * @return
	 */
	public boolean isBuilt();

	/**
	 * Get parts that do not move with another part
	 * 
	 * @return
	 */
	public Map<UUID, ? extends IComponentPart> getPartsWithoutParent();

	/**
	 * Gets the specific type of this thing
	 * 
	 * @return
	 */
	public IBlueprintTemplate getSpecies();

	/**
	 * Get all parts with this specific ability
	 * 
	 * @param ability
	 * @return
	 */
	public Collection<? extends IComponentPart> getPartsWithAbility(IPartAbility ability);

	/**
	 * returns traits sensable in a specific part by the given sensor
	 * 
	 * @param sensor
	 * @param partType
	 * @return
	 */
	public Collection<SenseProperty<?>> getSensableTraits(ISensor sensor, IComponentPart part);

	/**
	 * 
	 * @param <T>
	 * @param prop
	 * @param part
	 * @param ignoreType whether to ignore the "type" of this part's traits, i.e.
	 *                   only obtain traits stored specifically on the part, and not
	 *                   in the type
	 * @return
	 */
	public <T> T getTrait(SenseProperty<T> prop, IComponentPart part, boolean ignoreType);

	Map<String, ? extends IComponentType> getPartTypes();

	/**
	 * Return a list of body parts, tissues, and other information
	 * 
	 * @return
	 */
	public String report();

	public Collection<? extends IComponentPart> getParts();
}
