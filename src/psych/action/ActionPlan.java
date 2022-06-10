package psych.action;

import java.util.Collection;
import java.util.Set;
import java.util.Stack;

import psych.action.goal.Goal;
import psych.actionstates.states.WorldState;

public class ActionPlan {

	/**
	 * Last action in the stack is the first to be executed
	 */
	private Stack<Action> actions = new Stack<>();
	private Goal goal;
	/**
	 * Represents the observedstate coming after the action in the queue
	 */
	private Stack<WorldState> intermediaryStates = new Stack<>();

	public ActionPlan(Goal goal) {
		this.goal = goal;
	}

	/**
	 * finds the n (or less) most efficient actions that could precede the given one
	 * 
	 * @param all
	 * @return
	 */
	public Set<Action> findPossibleActions(Collection<Action> all, int n) {

	}
}
