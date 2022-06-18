package psych.action;

import psych.action.goal.Goal;
import psych.action.goal.RequirementWrapper;
import psych.actionstates.states.State;
import psych.mind.Mind;
import sociology.sociocon.IPurposeElement;
import sociology.sociocon.IPurposeSource;

/**
 * Actions are remembered in the mind and executed in stack form. Each action
 * has a user (performer) and possibly a target and a tool. However, any other
 * involved entities/profiles/etc need to be set up as a MentalConstruct
 * 
 * @author borah
 *
 */
public abstract class Action implements IPurposeElement {

	private IPurposeSource origin = null;
	private String name;

	// TODO completion state, required state

	public Action(String name) {
		this.name = name;
	}

	/**
	 * Whether this action can be executed for the given being
	 */
	public boolean canExecute(Mind actor, State prevResult) {
		return true;
	}

	/**
	 * Execute this action by this mind on this target; "prevResult" is the result
	 * of the previous action. Return the current state of all relevant parts of
	 * this action if successful
	 * 
	 * @param actor
	 * @param forTarget
	 * @return
	 */
	public abstract State execute(Mind actor, State prevResult);

	@Override
	public IPurposeSource getOrigin() {
		return origin;
	}

	public Action setOrigin(IPurposeSource origin) {
		this.origin = origin;
		return this;
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * Using the given desired goal, generate the goal(s) that need to be completed
	 * to complete this action. Return an empty wrapper or wrapper with one empty
	 * state if this action completes all conditions. Return null if the action is
	 * not compatible with the previous one.
	 * 
	 * @param fromMind
	 * @param usingRequirements
	 * @return
	 */
	public abstract RequirementWrapper generateRequirements(Mind fromMind, Goal goal);

	/**
	 * TODO tiredness calculation
	 */

	/****/
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": " + this.name + " becauseof: " + this.origin;
	}

}
