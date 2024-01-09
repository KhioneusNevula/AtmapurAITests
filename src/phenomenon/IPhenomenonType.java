package phenomenon;

import java.util.Collection;

import actor.ITemplate;
import mind.memory.events.IEventType;

public interface IPhenomenonType extends ITemplate, IEventType {

	public String name();

	public static enum Properties {
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

	@Override
	default IMemeType getMemeType() {
		return MemeType.PHENOMENON_TYPE;
	}

	Collection<Properties> getProperties();

}
