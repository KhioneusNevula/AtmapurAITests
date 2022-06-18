package psych.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import psych.action.goal.CompleteTasksGoal;
import psych.action.goal.EmptyGoal;
import psych.action.goal.Goal;
import psych.action.goal.RequirementGoal;
import psych.action.goal.RequirementWrapper;
import psych.actionstates.states.State;
import psych.mind.Mind;

public class Task {

	public static class ActionWrapper {
		Action action;
		Goal requirement;

		public ActionWrapper(Action act, Goal req) {
			this.action = act;
			this.requirement = req;
		}

		public ActionWrapper(Action action, State requirement) {
			this(action, new RequirementGoal(requirement));
		}

		public Action getAction() {
			return action;
		}

		public Goal getRequirement() {
			return requirement;
		}

	}

	public static class PossibleActionWrapper {

		Action action;
		RequirementWrapper reqs;

		public PossibleActionWrapper(Action action, RequirementWrapper reqs) {
			this.action = action;
			this.reqs = reqs;
		}

		public Action getAction() {
			return action;
		}

		public RequirementWrapper getReqs() {
			return reqs;
		}

		/**
		 * TODO factor in tiredness -- comparator based on efficiency. negative = less,
		 * positive = greater
		 */
		public int compareEfficiency(PossibleActionWrapper other) {
			int consthis = this.reqs.numConditions();
			int consother = other.reqs.numConditions();

			return consthis - consother;
		}

	}

	private Goal goal;
	public static final int UNIVERSAL_MAX_ACTIONS = 20;
	private int maxActions = UNIVERSAL_MAX_ACTIONS; // TODO make this more entity-specific lol

	private Task deeperTask = null;
	private Stack<ActionWrapper> actions = new Stack<>();
	private ActionWrapper currentlyExecuting = null;

	/**
	 * "Deeper task" may be null
	 * 
	 * @param goal
	 * @param deeperTask
	 */
	public Task(Goal goal, Task deeperTask, int maxActions) {
		if (goal.isEnd())
			throw new IllegalArgumentException("Goal {" + goal + "} \ncannot be an end-type goal");
		this.goal = goal;
		this.deeperTask = deeperTask;
		this.maxActions = maxActions;
	}

	public Task(Goal goal, int maxActions) {
		this(goal, null, maxActions);
	}

	public Task(Goal goal, Task deeperTask) {
		this(goal, deeperTask, UNIVERSAL_MAX_ACTIONS);
	}

	public Task(Goal goal) {
		this(goal, UNIVERSAL_MAX_ACTIONS);
	}

	public Goal getGoal() {
		return goal;
	}

	public Iterable<ActionWrapper> getActions() {
		return actions;
	}

	/**
	 * Return the action to be completed first, i.e. the one on top of the stack
	 * (last index)
	 * 
	 * @return
	 */
	public ActionWrapper getFirstAction() {
		return actions.peek();
	}

	/**
	 * Return action to be completed last, i.e. the one on the bottom of the stack
	 * (first index)
	 * 
	 * @return
	 */
	public ActionWrapper getLastAction() {
		return actions.get(0);
	}

	protected void addAction(ActionWrapper act) {
		this.actions.push(act);
	}

	public ActionWrapper getCurrentlyExecuting() {
		return currentlyExecuting;
	}

	/**
	 * If there are maximum/too many actions on the queue
	 * 
	 * @return
	 */
	public boolean isMaxed() {
		return actions.size() >= maxActions;
	}

	/**
	 * Whether this action queue's last goal is an end goal (e.g. a
	 * completetasksgoal or emptygoal)
	 * 
	 * @return
	 */
	public boolean isLastGoalEnd() {
		return actions.peek().requirement.isEnd();
	}

	/**
	 * Whether any more actions can be added to this queue
	 * 
	 * @return
	 */
	public boolean canAddNewActions() {
		return !(isMaxed() || isLastGoalEnd() || willBeComplete());
	}

	/**
	 * Whether this task has no more conditions to fulfill
	 * 
	 * @return
	 */
	public boolean willBeComplete() {
		return this.actions.peek().requirement.isComplete();
	}

	/**
	 * Whether this queue is incomplete but too full of actions
	 * 
	 * @return
	 */
	public boolean isTooFull() {
		return this.isMaxed() && !(this.isLastGoalEnd() || this.willBeComplete());
	}

	/**
	 * Generates the full plan of action for this task. "Inefficiency" means how
	 * many less efficient actions this mind will randomly pick from. Returns an
	 * empty requirement wrapper if all actions can be generated, the last single
	 * requirement goal if the action generation could not be completed, and a multi
	 * requirement wrapper if any actions in the list generate multiple requirements
	 * 
	 * @return
	 */
	public RequirementWrapper generateActionPlan(Mind mind, int inefficiency) {
		Iterable<Action> possible = mind.getPossibleActions();
		RequirementWrapper reqs = chooseNextAction(mind, possible, inefficiency);
		if (reqs == null || reqs.isMulti() || reqs.isEmpty())
			return reqs;
		ActionWrapper prev = this.actions.peek();
		while (canAddNewActions()) {
			reqs = chooseNextAction(mind, possible, inefficiency);
			if (reqs == null || reqs.isMulti() || reqs.isEmpty() || isTooFull()) {
				return reqs;
			}
		}
		return reqs;
	}

	/**
	 * Choose the next action in this task and add it to the queue. Throw exception
	 * if this task is already essentially complete or no actions can be added to
	 * the queue. If no more actions are possible, return null. Return
	 * requirementwrappers corresponding to new goals that need to be added to the
	 * task queue; an empty wrapper if no new goals need to be added (the task is
	 * now effectively complete), and a single wrapper if the goal is a single goal
	 * (a single wrapper should not be turned into a new task for obvious reasons).
	 * 
	 * 
	 */
	public RequirementWrapper chooseNextAction(Mind mind, Iterable<Action> possible, int n) {
		if (isMaxed())
			throw new IllegalStateException(
					"Action queue is full! ActionCount: " + this.actions.size() + " / " + this.maxActions);
		if (isLastGoalEnd())
			throw new IllegalStateException(
					"Cannot choose a new action; the task is currently unchangeable because previous goal is an end goal: "
							+ this.actions.peek().getRequirement());
		if (willBeComplete())
			throw new IllegalCallerException("Cannot choose a new action; the task is already complete");
		PossibleActionWrapper nextAction = this.decideAction(this.getPossibleNextActions(mind, possible, n));
		if (nextAction == null) {
			return null;
		}
		Goal goal = null;
		if (nextAction.reqs.isMulti()) {
			goal = new CompleteTasksGoal(nextAction.reqs);
		} else if (nextAction.reqs.isSingle()) {
			goal = nextAction.reqs.getRequirement();
		} else {
			goal = EmptyGoal.GOAL;
		}
		ActionWrapper newWrapper = new ActionWrapper(nextAction.action, goal);
		this.addAction(newWrapper);
		return nextAction.reqs;
	}

	/**
	 * Decides which action to add from a set. currently random
	 * 
	 * @return
	 */
	public PossibleActionWrapper decideAction(List<PossibleActionWrapper> actions) {
		return actions.stream().findAny().orElse(null);
	}

	/**
	 * Gets up to n possible next actions based on efficiency. throw exception if
	 * the previous action has an empty requirement condition. Return an empty list
	 * if there are simply no possible actions that can follow the current action.
	 * 
	 * @param n
	 * @return
	 */
	public List<PossibleActionWrapper> getPossibleNextActions(Mind mind, Iterable<Action> possible, int n) {

		Goal goal = null;
		if (this.actions.isEmpty()) {
			goal = this.goal;
		} else {
			goal = actions.peek().requirement;
			if (goal.isComplete())
				throw new IllegalStateException("Previous action " + actions.peek().action + " has no requirements");

		}

		List<PossibleActionWrapper> viable = new ArrayList<>();
		List<PossibleActionWrapper> mostEfficient = new ArrayList<>();
		for (Action pos : possible) {
			RequirementWrapper reqs = pos.generateRequirements(mind, goal);
			if (reqs != null) {
				viable.add(new PossibleActionWrapper(pos, reqs));
			}
		}
		viable.sort((PossibleActionWrapper::compareEfficiency));
		Collections.reverse(viable);
		for (int i = 0; i < n; i++) {
			if (i >= viable.size())
				break;
			mostEfficient.add(viable.get(i));
		}
		return mostEfficient;
	}

	/**
	 * Gets the root task which this one extends from
	 */
	public Task getRootTask() {
		Task root = this;
		while (root.deeperTask != null) {
			root = root.deeperTask;
		}
		return root;
	}

	/**
	 * Gets the goal of the root linked task to this one
	 * 
	 * @return
	 */
	public Goal getRootGoal() {
		return getRootTask().goal;
	}

	/**
	 * The next task which, once this task is finished, will run using this task's
	 * information. E.g. if this task is the process of cooking, the deeper task may
	 * be the process of eating the cooked food.
	 * 
	 * @return
	 */
	public Task getDeeperTask() {
		return deeperTask;
	}
}
