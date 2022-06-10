package psych.action;

import psych.Mind;
import psych.action.types.ActionType;
import psych.actionstates.states.WorldState;
import sociology.sociocon.IPurposeElement;
import sociology.sociocon.IPurposeSource;

/**
 * Actions are remembered in the mind
 * 
 * @author borah
 *
 */
public abstract class Action implements IPurposeElement {

	private IPurposeSource origin = null;
	private String name;
	private WorldState requirements;
	private WorldState result;

	private ActionType type;

	// TODO completion state, required state

	public Action(String name, ActionType type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * TODO getCompletionState getRequiredState
	 */

	/**
	 * Whether this action can be executed for the given actor and the perceived
	 * state of the universe, whether as a result of previous actions or as it
	 * currently is
	 */
	public boolean canExecute(Mind actor, WorldState universe) {
		// TODO check conditions on profile
		return true;
	}

	/**
	 * Execute this action by this mind on this target. Return true if successful
	 * 
	 * @param actor
	 * @param forTarget
	 * @return
	 */
	public abstract boolean execute(Mind actor, WorldState universe);

	@Override
	public IPurposeSource getOrigin() {
		return origin;
	}

	public Action setOrigin(IPurposeSource origin) {
		this.origin = origin;
		return this;
	}

	public ActionType getType() {
		return type;
	}

	@Override
	public String getName() {
		return name;
	}

	public WorldState getRequirements(Mind forM) {
		return requirements;
	}

	public WorldState getResult(Mind forM) {
		return result;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " of type " + type + ": " + this.name + " becauseof: " + this.origin;
	}

}
