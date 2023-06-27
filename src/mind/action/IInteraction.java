package mind.action;

import mind.ICanAct;
import mind.speech.IUtterance;

public interface IInteraction extends IAction {

	public boolean isInitiator();

	/**
	 * Gets the interaction instance linked to this action
	 * 
	 * @return
	 */
	public IInteractionInstance getInteractionInstance();

	/**
	 * Whether the interaction has properly been initiated
	 * 
	 * @return
	 */
	public boolean isInteracting();

	/**
	 * Receive a communication from another individual in the interaction
	 * 
	 * @param listener
	 * @param speaker
	 * @param commu
	 */
	public void receiveCommunication(ICanAct listener, ICanAct speaker, IUtterance commu);

	/**
	 * The type of interaction this is; e.g. talking actions constitute a
	 * Conversation, for example
	 * 
	 * @return
	 */
	public IInteractionType getInteractionType();

	/**
	 * what to do when we end the interaction instance this interaction is
	 * associated with
	 * 
	 * @param ender
	 */
	public void endInteraction(ICanAct ender);

}
