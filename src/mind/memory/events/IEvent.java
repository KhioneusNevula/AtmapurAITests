package mind.memory.events;

import java.util.Collection;

import mind.action.IActionType;
import mind.action.IInteractionInstance;
import mind.concepts.type.IMeme;
import mind.concepts.type.Profile;
import phenomenon.IPhenomenonType;
import sim.Location;

public interface IEvent extends IMeme {

	/**
	 * The entity(ies) which underwent the occurrence, represented as profile
	 * 
	 * @return
	 */
	public Collection<Profile> object();

	/**
	 * If the event has a cause, the entity(ies) which caused the event
	 * 
	 * @return
	 */
	public Collection<Profile> cause();

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

}
