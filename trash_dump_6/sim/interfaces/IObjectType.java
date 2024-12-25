package sim.interfaces;

import java.util.Collection;
import java.util.Collections;

import actor.construction.properties.ISensableTrait;
import actor.construction.properties.SenseProperty;
import civilization_and_minds.social.concepts.IConcept;

/**
 * A "type" of actor or phenomenon
 * 
 * @author borah
 *
 */
public interface IObjectType extends IConcept {

	public String name();

	/**
	 * The average unusualness of any distinctive example of this template
	 * 
	 * @return
	 */
	public float averageUniqueness();

	/**
	 * 
	 * @param <A>
	 * @param property
	 * @param ignoreType whether to only get traits specific to the body part and
	 *                   not the type as a whole
	 * @return
	 */
	public default <A extends ISensableTrait> A getDefaultSensableProperty(SenseProperty<A> property) {
		return null;
	}

	public default Collection<SenseProperty<?>> getDefaultSensableProperties() {
		return Collections.emptySet();
	}

}
