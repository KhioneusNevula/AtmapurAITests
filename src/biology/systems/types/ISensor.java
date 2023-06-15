package biology.systems.types;

import java.util.Collection;

import actor.Actor;
import actor.IComponentPart;
import actor.IComponentType;
import actor.MultipartActor;
import biology.systems.senses.SightSense;
import mind.concepts.type.SenseProperty;
import sim.World;

public interface ISensor {

	public static final SightSense SIGHT = new SightSense();

	/**
	 * Sense things in the world that are not directly connected to profiles by
	 * means of the specific property; return null if this property cannot be sensed
	 * in that way
	 * 
	 * @param inWorld
	 * @param bySystem
	 * @param byEntity
	 * @return
	 */
	public <T> Collection<Object> getSensed(World inWorld, SenseProperty<T> property, SenseSystem bySystem,
			Actor byEntity);

	/**
	 * Gets a general property of the whole actor that can be sensed. Return null if
	 * t (e.g. if the actor is out of sensory range)
	 */
	public <T> Object getGeneralTrait(SenseProperty<T> property, Actor target, World inWorld, SenseSystem bySystem,
			Actor byEntity);

	/**
	 * Gets the sensed trait about this entity under the specific property, about
	 * the type of part
	 * 
	 * @param target
	 * @param inWorld
	 * @param bySystem
	 * @param byEntity
	 * @return
	 */
	public <T> Object getSensedTrait(MultipartActor target, SenseProperty<T> property, IComponentType component,
			World inWorld, SenseSystem bySystem, Actor byEntity);

	/**
	 * Gets the part-specific sensed traits about this entity -- parts that don't
	 * "fit the usual traits of the part's template"
	 * 
	 * @param target
	 * @param inWorld
	 * @param bySystem
	 * @param byEntity
	 * @return
	 */
	public <T> Object getSpecificSensedTrait(MultipartActor target, SenseProperty<T> property,
			IComponentPart componentPart, World inWorld, SenseSystem bySystem, Actor byEntity);

}
