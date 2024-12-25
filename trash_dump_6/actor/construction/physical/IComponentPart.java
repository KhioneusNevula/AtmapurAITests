package actor.construction.physical;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Multimap;

import actor.construction.properties.IAbilityStat;
import actor.construction.properties.ISensableTrait;
import actor.construction.properties.SenseProperty;
import biology.sensing.ISense;

/**
 * Component parts should sort their layers (if applicable) with the highest
 * layer coming first in sequence <br>
 * TODO add custom sizes? idk
 * 
 * @author borah
 *
 */
public interface IComponentPart extends Comparable<IComponentPart> {

	/**
	 * If this part has been destroyed to thorough nonexistence
	 * 
	 * @return
	 */
	public boolean isGone();

	public UUID getId();

	/**
	 * Whether this part is notable for being unusual to its Template (I.e. the
	 * IPartType). For example a damaged part is in an unusual state, or a part
	 * which is the wrong color, etc.
	 * 
	 * @return
	 */
	public boolean isUnusual();

	@Override
	default int compareTo(IComponentPart o) {
		return this.getId().compareTo(o.getId());
	}

	/**
	 * Update the state of this tracker, and whether it is "usual" or not
	 * 
	 * @return
	 */
	boolean checkIfUsual();

	public IComponentType getType();

	/**
	 * gets the nutrition content of this body part
	 * 
	 * @return
	 */
	public float getNutrition();

	/**
	 * types of nutrition this part gives
	 * 
	 * @return
	 */
	public int nutritionTypes();

	/**
	 * Gets parts that are subparts of this one
	 * 
	 * @return
	 */
	Multimap<String, ? extends IComponentPart> getChildParts();

	/**
	 * Get the parent part to this one
	 * 
	 * @return
	 */
	IComponentPart getParent();

	/**
	 * 
	 * @param <A>
	 * @param property
	 * @param ignoreType whether to only get traits specific to the body part and
	 *                   not the type as a whole
	 * @return
	 */
	public <A extends ISensableTrait> A getProperty(SenseProperty<A> property, boolean ignoreType);

	public Collection<SenseProperty<?>> getSensableProperties();

	/**
	 * Gets ability statistics for a component part which can use abilities. Results
	 * can be defaulted to type class
	 * 
	 * @param <T>
	 * @param type
	 * @return
	 */
	public <T> T getAbilityStat(IPartAbility forab, IAbilityStat<T> type);

	/**
	 * Return all senses of this component
	 * 
	 * @return
	 */
	public Collection<ISense> getSenses();

	/**
	 * Gets materials constituting this part. Expected to be in order of outermost
	 * at first position and innermost at last
	 * 
	 * @return
	 */
	Map<? extends IMaterialLayerType, ? extends IMaterialLayer> getMaterials();

	/**
	 * If this part only has one material layer
	 * 
	 * @return
	 */
	public boolean hasOneMaterial();

	/**
	 * Return the main material of this part if it has one material
	 * 
	 * @return
	 */
	public IMaterialLayer getMainMaterial();

	Multimap<String, ? extends IComponentPart> getSurroundeds();

	IComponentPart getSurrounding();

	public String report();

	public <T extends ISensableTrait> void changeProperty(SenseProperty<T> property, T value);

	/**
	 * Return all types of ability stats this part uses
	 * 
	 * @return
	 */
	Collection<IAbilityStat<?>> getAbilityStatTypes(IPartAbility forPart);

}
