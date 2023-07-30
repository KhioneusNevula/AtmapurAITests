package mind.action;

import java.util.Collection;

import mind.IEntity;
import mind.speech.IUtterance;
import sim.Location;

public interface IInteractionInstance {

	/**
	 * Adds an utterance to the shared communication of the interaction
	 * 
	 * @param source
	 * @param utterance
	 */
	public void addCommunication(IEntity source, IUtterance utterance);

	/**
	 * If all individuals have left the interaction
	 * 
	 * @return
	 */
	public default boolean allLeft() {
		return this.participants().isEmpty();
	}

	/**
	 * What the interaction does if this individual leaves the interaction instance.
	 * For example, it may have to be finished.
	 * 
	 * @param ender
	 */
	public void leave(IEntity ender);

	/**
	 * End the interaction instance
	 * 
	 * @param ender
	 */
	public void end(IEntity ender);

	/**
	 * The type of the interaction
	 * 
	 * @return
	 */
	public IInteractionType getType();

	/**
	 * The location of the interaction
	 * 
	 * @return
	 */
	public Location getLocation();

	/**
	 * The action that initiated this interaction event
	 * 
	 * @return
	 */
	public IInteraction initiatingAction();

	/**
	 * The actions comprising this interaction event
	 * 
	 * @return
	 */
	public Collection<IInteraction> actions();

	/**
	 * Gets an associated interaction that the participant is performing
	 * 
	 * @param participant
	 * @return
	 */
	public IInteraction getAssociatedActionForParticipant(IEntity participant);

	/**
	 * The entity which initiated this event
	 * 
	 * @return
	 */
	public IEntity initiator();

	/**
	 * Those involved in this interaction event
	 * 
	 * @return
	 */
	public Collection<IEntity> participants();

	/**
	 * Adds a participant and their associated interaction action to this instance
	 * 
	 * @param participants
	 */
	public void addParticipant(IEntity participant, IInteraction action);
}
