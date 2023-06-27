package mind;

import java.util.Collection;

import mind.action.IAction;
import mind.action.IInteraction;
import mind.action.IInteractionInstance;
import mind.goals.IGoal.Priority;

public interface IWill {

	/**
	 * How many actions is maximum for a queue of actions
	 * 
	 * @return
	 */
	int getMaxActions();

	Collection<IAction> getCurrentActions();

	int getTicksPassed(IAction action);

	/**
	 * Generates goals from active needs TODO better prioritizing
	 */
	void updateGoalsFromNeeds();

	/**
	 * Obtains goals from memory to register them for actions
	 */
	void updateGoals();

	/**
	 * return true if this entity accepts the interaction
	 * 
	 * @param offerer
	 * @param interaction
	 * @return
	 */
	boolean considerInteraction(ICanAct offerer, IInteractionInstance interaction, IInteraction action,
			Priority priority);

}
