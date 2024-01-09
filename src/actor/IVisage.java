package actor;

import java.util.Collection;

import biology.systems.types.ISensor;
import mind.concepts.type.SenseProperty;

public interface IVisage {

	/**
	 * Traits that can be sensed for this visage as a whole by this sensor type
	 * 
	 * @param <T>
	 * @param property
	 * @return
	 */
	public Collection<SenseProperty<?>> getSensableTraits(ISensor sensor);

	/**
	 * specific trait for this sense property, if present; null if otherwise
	 * 
	 * @param <T>
	 * @param property
	 * @return
	 */
	public <T> T getTrait(SenseProperty<T> property);

	/**
	 * gets the "species" of this visage, the type or whatever
	 * 
	 * @return
	 */
	public ITemplate getSpecies();

	/**
	 * If this visage is a multipart entity
	 * 
	 * @return
	 */
	default boolean isMultipart() {
		return this instanceof IMultipart;
	}

	/**
	 * Returns this as a multipart entity
	 * 
	 * @param <A>
	 * @param <B>
	 * @param <C>
	 * @return
	 */
	default <A extends IComponentType, B extends IMaterialLayerType, C extends IComponentPart> IMultipart getAsMultipart() {
		return (IMultipart) this;
	}

	/**
	 * Whether this visage is invisible for whatever reason
	 * 
	 * @return
	 */
	public boolean isInvisible();

	/**
	 * Returns the owner of this visage
	 * 
	 * @return
	 */
	public IUniqueExistence getOwner();

}
