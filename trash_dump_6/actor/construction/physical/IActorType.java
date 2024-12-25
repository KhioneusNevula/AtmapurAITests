package actor.construction.physical;

import java.util.Map;

import actor.Actor;
import metaphysical.soul.ISoul;
import sim.interfaces.IObjectType;

/**
 * Indicates a "type" of Actor
 * 
 * @author borah
 *
 */
public interface IActorType extends IObjectType {

	/**
	 * The different part types in this thing
	 * 
	 * @return
	 */
	public Map<String, ? extends IComponentType> partTypes();

	/**
	 * If this template consists only of one main type of part. For example, a rock,
	 * or something made of pieces of only one ttype
	 * 
	 * @return
	 */
	public boolean hasSinglePartType();

	/**
	 * If this template contains not only one single part type, but a part type with
	 * just one unit
	 * 
	 * @return
	 */
	public default boolean hasOnlyOnePart() {
		return this.hasSinglePartType() && this.mainComponent().count() == 1;
	}

	/**
	 * For single-part actors, the component encompassing its entire entity. Throw
	 * exception if not single-part
	 * 
	 * @return
	 */
	public IComponentType mainComponent();

	/**
	 * The name of this thing
	 * 
	 * @return
	 */
	public String name();

	/**
	 * Whether this kind of being should be given a soul when spawned
	 * 
	 * @return
	 */
	public boolean mustBeGivenSoul();

	/**
	 * Generates a soul for given things based on relevant properties. Return null
	 * if no soul needed; throw exception if this template is not supposed to
	 * generate souls
	 * 
	 * @param a
	 * @param phys
	 * @param gen
	 * @return
	 */
	public ISoul generateSoul(Actor a, IComponentPart forPart);

	@Override
	default ConceptType getConceptType() {
		return ConceptType.ACTOR_TYPE;
	}

}
