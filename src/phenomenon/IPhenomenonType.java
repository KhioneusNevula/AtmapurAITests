package phenomenon;

import actor.ITemplate;
import mind.memory.events.IEventType;

public interface IPhenomenonType extends ITemplate, IEventType {

	public String name();

	/**
	 * If this phenomenon affects something in the world, e.g. fires burn *on*
	 * objects
	 * 
	 * @return
	 */
	public boolean affectsObject();

	/**
	 * If this phenomenon results in the removal of what it affects from the world,
	 * e.g. burning up removes what is burned up
	 * 
	 * @return
	 */
	public boolean removesObject();

	/**
	 * If this phenomenon is a transformation of an object, e.g. death is a
	 * transformation from living to dead and melting is a transformation from
	 * liquid to solid; BUT, a push is not a transformation (it is a motion), for
	 * example
	 * 
	 * @return
	 */
	public boolean isTransformation();

	@Override
	default IMemeType getMemeType() {
		return MemeType.PHENOMENON_TYPE;
	}

}
