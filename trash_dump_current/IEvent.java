package mind.memory.events;

import java.util.Collection;

import mind.action.IActionType;
import mind.action.IInteractionInstance;
import mind.concepts.type.IMeme;
import phenomenon.IPhenomenonType;
import sim.Location;

public interface IEvent extends IMeme {

	/**
	 * The entity(ies) which underwent the occurrence, represented as profile
	 * 
	 * @return
	 */
	public Collection<? extends IMeme> object();

	/**
	 * If the event has a cause, the entity(ies) which caused the event
	 * 
	 * @return
	 */
	public Collection<? extends IMeme> cause();

	/**
	 * returns profile(s)/other meme types fitting in the specific event role
	 * 
	 * @param role
	 * @return
	 */
	public <T extends IMeme> Collection<T> getForRole(EventRole role);

	/**
	 * Where the event occurred
	 * 
	 * @return
	 */
	public Location location();

	/**
	 * If this event has a location
	 * 
	 * @return
	 */
	public boolean hasLocation();

	/**
	 * If the event was the result of an action, what the action itself was
	 * 
	 * @return
	 */
	public IActionType<?> action();

	/**
	 * If the event encompasses an action
	 * 
	 * @return
	 */
	public boolean isAction();

	/**
	 * The time the action occurred
	 * 
	 * @return
	 */
	public long time();

	/**
	 * If the event is a phenomenon, what the phenomenon was
	 * 
	 * @return
	 */
	public IPhenomenonType phenomenon();

	/**
	 * Properties of the phenomenon resulting from this event
	 * 
	 * @return
	 */
	public Collection<IMeme> phenomenonProperties();

	/**
	 * If the event encompasses a phenomenon
	 * 
	 * @return
	 */
	public boolean isPhenomenon();

	/**
	 * The interaction event
	 * 
	 * @return
	 */
	public IInteractionInstance interaction();

	/**
	 * If the event encompasses an action involving multiple participants
	 * 
	 * @return
	 */
	public boolean isInteraction();

	@Override
	default IMemeType getMemeType() {
		return MemeType.EVENT;
	}

	public static enum EventRole implements IMeme {
		CAUSER, OBJECT, USED, LOCATION;

		@Override
		public String getUniqueName() {
			return "eventrole_" + name();
		}

		@Override
		public IMemeType getMemeType() {
			return MemeType.EVENT_ROLE;
		}
	}

}
