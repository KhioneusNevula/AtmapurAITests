package mind;

import java.util.Collection;

import mind.Will.ActionQueue;
import mind.action.IAction;
import mind.action.IActionType;
import mind.action.IInteraction;
import mind.action.IInteractionInstance;
import mind.action.WillingnessMatrix;
import mind.feeling.IFeeling.Axis;
import mind.goals.IGoal;
import mind.goals.IGoal.Priority;
import mind.relationships.IParty;

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
	boolean considerInteraction(IEntity offerer, IInteractionInstance interaction, IInteraction action,
			Priority priority);

	/**
	 * Calculate willingness to perform an action based on a mix of relationships
	 * (e.g. trust) and emotions. If a random float between 0 and 1 is less than
	 * this value, the action should be done. If willingness is negative, the action
	 * will not be done.
	 * 
	 * @param actionType    the type of action considered
	 * @param action        the action itself (may be null if this is an early stage
	 *                      of action selection)
	 * @param goal          the goal for the action (may be null)
	 * @param mainCompany   the direct target of this action; can be null
	 * @param offerer       the offerer or mandator of this action
	 * @param importantAxes the important axes of emotion to consider
	 * @return
	 */
	public WillingnessMatrix willingness(IActionType<?> actionType, IAction action, IGoal goal, IParty mainCompany,
			IParty offerer, Collection<Axis> importantAxes);

	public IIndividualMind getOwner();

	String report();

	ActionQueue getQueueForGoal(IGoal goal);

}
