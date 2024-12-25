package phenomenon;

import java.util.Collection;

import sim.interfaces.IObjectType;

public interface IPhenomenonType extends IObjectType {

	public String name();

	public static enum Properties {

		/**
		 * If this is a world phenomenon, i.e. present in the environment, not
		 * physically in the gameplay arena
		 */
		WORLD_PHENOMENON,
		/**
		 * If this phenomenon makes ambient darkness
		 */
		AMBIENT_DARK,
		/**
		 * If this phenomenon makes ambient light
		 */
		AMBIENT_LIGHT,
		/**
		 * If this phenomenon is visible in the sky
		 */
		IN_SKY,
		/**
		 * if this phenomenon always has a direct, clear source entity that generated
		 * it. This does not apply to things which have a more indirect source.
		 */
		HAS_SOURCE,
		/**
		 * If this phenomenon always affects something in the world, e.g. fires burn
		 * *on* objects
		 */
		AFFECTS_OBJECT,
		/**
		 * If this phenomenon results in the removal of what it affects from the world,
		 * e.g. burning up removes what is burned up
		 */
		REMOVES_OBJECT,
		/**
		 * * If this phenomenon is a transformation of an object, e.g. death is a
		 * transformation from living to dead and melting is a transformation from
		 * liquid to solid; BUT, a push is not a transformation (it is a motion), for
		 * example
		 */
		TRANSFORMS_OBJECT,
		/**
		 * If this phenomenon produces a new object, e.g. a tree growing a fruit
		 * produces a new fruit
		 * 
		 */
		CREATES_OBJECT,
		/** If this phenomenon removes a fluid, e.g. drying removes a fluid */
		REMOVES_FLUID,
		/** If this phenomenon creates fluid, e.g. rain creates water */
		CREATES_FLUID
	}

	public boolean hasProperty(Properties property);

	/**
	 * Whether this phenomenon type is a specific event, rather than a passively
	 * existing phenomenon
	 * 
	 * @return
	 */
	public boolean isEvent();

	Collection<Properties> getProperties();

	@Override
	default ConceptType getConceptType() {
		return ConceptType.PHENOMENON_TYPE;
	}

}
