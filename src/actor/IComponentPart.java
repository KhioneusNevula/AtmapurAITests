package actor;

import java.util.Collection;
import java.util.UUID;

import com.google.common.collect.Multimap;

import biology.systems.types.ISensor;
import mind.concepts.type.SenseProperty;

public interface IComponentPart extends Comparable<IComponentPart> {

	/**
	 * 
	 * @param <A>
	 * @param property
	 * @param ignoreType whether to only get traits specific to the body part and
	 *                   not the type as a whole
	 * @return
	 */
	public <A> A getProperty(SenseProperty<A> property, boolean ignoreType);

	public Collection<SenseProperty<?>> getSensableProperties(ISensor sensor);

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

}
