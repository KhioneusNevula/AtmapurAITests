package psych.action;

import java.util.Collection;
import java.util.Map;
import java.util.Stack;

import psych.action.goal.Goal;
import psych.actionstates.ProfilePlaceholder;
import psych.actionstates.ProfilePotential;
import psych.actionstates.states.TheoreticState;

public class ActionPlan {

	/**
	 * Last action in the stack is the first to be executed
	 */
	private Stack<Action> actions = new Stack<>();
	private Goal goal;

	/**
	 * Represents the observedstate coming after the action in the queue
	 */
	private Stack<TheoreticState> intermediaryStates = new Stack<>();

	public ActionPlan(Goal goal) {
		this.goal = goal;
	}

	/**
	 * Gets the theoretic state that comes before this action, so the state mapping
	 * to its requirements
	 * 
	 * @param a
	 * @return
	 */
	public TheoreticState getPreviousState(Action a) {
		int index = actions.indexOf(a);
		if (index < 0)
			return null;
		if (index == 0)
			return this.goal.getRequiredState();
		return this.intermediaryStates.get(index - 1);
	}

	/**
	 * For finding the next action assuming it is already decided
	 * 
	 * @param previousState
	 * @return
	 */
	public Action getNextAction(TheoreticState previousState) {
		int index = intermediaryStates.indexOf(previousState);
		int aIndex = index + 1;
		if (actions.size() <= aIndex) {
			return null;
		}
		return actions.get(aIndex);
	}

	public Action getPreviousAction(TheoreticState nextState) {
		if (intermediaryStates.indexOf(nextState) >= 0) {
			return actions.get(intermediaryStates.indexOf(nextState));
		}
		return null;
	}

	/**
	 * Gets the theoretic state that results after this action
	 * 
	 * @param a
	 * @return
	 */
	public TheoreticState getResultState(Action a) {
		if (actions.indexOf(a) >= 0)
			return intermediaryStates.get(actions.indexOf(a));
		return null;
	}

	/**
	 * finds the n (or less) most efficient actions that could precede the current
	 * action and maps of their profile-placeholder/profile-potential
	 * correspondences
	 * 
	 * @param all
	 * @return
	 */
	public Map<Action, Map<ProfilePlaceholder, ProfilePotential>> findPossibleActions(Collection<Action> allPossible,
			int n) {

	}
}
