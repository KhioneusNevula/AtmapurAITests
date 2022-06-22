package psych.action.goal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

import psych.action.goal.Goal.Priority;
import psych.action.goal.Task.ActionWrapper.PossibleActionWrapper;
import psych.action.types.Action;
import psych.actionstates.checks.IsKnownCheck;
import psych.actionstates.states.State;
import psych.actionstates.states.State.ProfileType;
import psych.mind.Mind;
import psych.mind.Will;
import sociology.Profile;
import sociology.ProfilePlaceholder;

public class Task {

	private Goal goal;
	public static final int UNIVERSAL_MAX_ACTIONS = 20;
	private int maxActions = UNIVERSAL_MAX_ACTIONS; // TODO make this more entity-specific lol
	private int maxAttempts; // TODO make this instance-specific

	private Task deeperTask = null;
	private Stack<ActionWrapper> actions = new Stack<>();

	private Map<Action, Object> executionInformation = new HashMap<>();
	private Map<Action, Integer> executionTicks = new HashMap<>();
	/**
	 * 
	 */
	private boolean wasRecentActionSuccessful = true;
	private int attempts;
	private ActionWrapper recentAction = null;

	/**
	 * "Deeper task" may be null
	 * 
	 * @param goal
	 * @param deeperTask
	 */
	public Task(Goal goal, Task deeperTask, int maxActions) {
		if (goal.isEnd() || goal.getPriority() == Priority.NO_PRIORITY)
			throw new IllegalArgumentException("Goal {" + goal + "} \ncannot be " + (goal.isEnd() ? "an end-type" : "")
					+ (goal.getPriority() == Priority.NO_PRIORITY ? " no-priority" : "") + " goal.");
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

	public void clearActions() {
		this.actions.clear();
		this.executionInformation.clear();
		this.executionTicks.clear();
	}

	public Goal getGoal() {
		return goal;
	}

	/**
	 * Returns the next goal that needs to be completed
	 * 
	 * @return
	 */
	public Goal getNextGoal() {
		if (this.actions.isEmpty())
			return this.goal;
		return actions.peek().requirement;
	}

	public Iterable<ActionWrapper> getActions() {
		return actions;
	}

	/**
	 * max possible attempts for this action
	 */
	public int getMaxAttempts() {
		return maxAttempts;
	}

	public void setMaxAttempts(int maxAttempts) {
		this.maxAttempts = maxAttempts;
	}

	/**
	 * Set whether the recent action was successful; should only be executed from
	 * Will
	 * 
	 * @param wasRecentActionSuccessful
	 */
	public void setWasRecentActionSuccessful(boolean wasRecentActionSuccessful) {
		this.wasRecentActionSuccessful = wasRecentActionSuccessful;
	}

	/**
	 * Returns true if the recent action which just completed was successful in
	 * completion; if false, assumably the tail end of the action plan must be
	 * regenerated
	 * 
	 * @return
	 */
	public boolean wasRecentActionSuccessful() {
		return wasRecentActionSuccessful;
	}

	/**
	 * How many attempts were made at this task (assumably all of the previous ones
	 * failed)
	 * 
	 * @return
	 */
	public int getAttempts() {
		return attempts;
	}

	public void oneMoreAttempt() {
		this.attempts++;
	}

	public void refreshAttempts() {
		this.attempts = 0;
	}

	/**
	 * The action that was most recently popped off the stack to be executed
	 * 
	 * @return
	 */
	public ActionWrapper getRecentAction() {
		return recentAction;
	}

	public String report() {
		StringBuilder str = new StringBuilder("{");
		str.append("\n\tgoal:{" + this.goal.toString() + "},");
		if (this.deeperTask != null)
			str.append(" link:{" + this.deeperTask.getGoal() + "},");
		str.append("\n\tactions:{\n");
		boolean did = false;
		boolean big = actions.size() > 3;
		for (ActionWrapper a : this.actions) {
			did = true;
			str.append(big ? "\n\t\t{" : "{").append(a.action).append(" >reqs> ").append(a.requirement.goalReport())
					.append("},\n");
		}
		if (did)
			str.deleteCharAt(str.length() - 1);
		str.append("}");
		str.append(",attempts:" + attempts);
		str.append("," + (wasRecentActionSuccessful ? "" : "un") + "successfulRecentAction:" + this.recentAction);
		return str.append("\n}").toString();
	}

	@Override
	public String toString() {
		return "Task{goal:" + goal + ",attempts:" + attempts + ",actionCount:" + this.actions.size()
				+ (wasRecentActionSuccessful ? "," : ",un") + "successfulRecentAction:" + this.recentAction + "}";
	}

	/**
	 * returns the goal which precedes the most recent action, i.e. the action's
	 * desired result
	 * 
	 * @return
	 */
	private Goal getPreviousGoal() {
		if (this.actions.isEmpty() || this.actions.size() == 1)
			return goal;
		return actions.get(actions.size() - 2).requirement;
	}

	/**
	 * Starts executing the next available action and pops it off the stack and
	 * returns it. Returns null if the action can't execute for whatever reason,
	 * e.g. conditions change TODO actually complete the action execution part of
	 * the mind
	 * 
	 * @param will
	 * @return
	 */
	public ActionWrapper startExecutingAction(Will will) {
		Goal result = getPreviousGoal();
		if (this.actions.peek().action._canExecute(will.getMind().getOwner(), will, this, actions.peek().requirement,
				result)) {
			ActionWrapper action = this.actions.pop();
			int ticksLeft = action.action._startExecution(will.getMind().getOwner(), will, this, action.requirement,
					result);
			this.recentAction = action;
			if (ticksLeft < 0) {
				// TODO something more interesting for the action failing
				String reason = action.action._complete(will.getMind().getOwner(), will, this, action.requirement,
						result, ticksLeft);
				if (reason == null)
					throw new IllegalStateException(action.action
							+ " returned a fail state from startExecution but a success state from complete");
				System.err.println(
						will.getMind() + " failed to execute action " + action.action + " because \"" + reason + "\"");
				this.wasRecentActionSuccessful = false;
				return null;
			}
			this.setExecutionTicks(action.action, ticksLeft);
			return action;
		}
		return null;
	}

	public void update(int ticks, Mind mind) {
		this.goal.goalUpdate(mind);
	}

	/**
	 * returns the combined action type of all the actions in this task
	 * 
	 * @return
	 */
	public int getCombinedActionType() {
		int flag = 0;
		for (ActionWrapper wrap : this.actions) {
			flag |= wrap.action.getActionType();
		}
		return flag;
	}

	public <T> T getExecutionInformation(Action act) {
		return (T) executionInformation.get(act);
	}

	public void storeExecutionInformation(Action act, Object info) {
		executionInformation.put(act, info);
	}

	public int getExecutionTicks(Action act) {
		return executionTicks.getOrDefault(act, 0);
	}

	public void setExecutionTicks(Action act, int integer) {
		executionTicks.put(act, integer);
	}

	public void changeExecutionTicks(Action act, int by) {
		setExecutionTicks(act, getExecutionTicks(act) + by);
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
		if (actions.isEmpty())
			return false;
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
		if (actions.isEmpty())
			return false;
		return this.actions.peek().requirement.isEmpty();
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
	 * requirement wrapper if any actions in the list generate multiple
	 * requirements. Returns null if too many attempts have been made for this task
	 * 
	 * @return
	 */
	public RequirementWrapper generateActionPlan(Mind mind, int inefficiency) {
		Iterable<Action> possible = mind.getPossibleActions();
		oneMoreAttempt();
		if (attempts > maxAttempts) {
			return null;
		}

		RequirementWrapper reqs = chooseNextAction(mind, possible, inefficiency);
		if (reqs == null)
			return reqs;
		tryResolveProfiles(reqs, mind);
		if (reqs.isMulti() || reqs.isEmpty())
			return reqs;
		while (canAddNewActions()) {
			reqs = chooseNextAction(mind, possible, inefficiency);
			if (reqs == null)
				return reqs;
			tryResolveProfiles(reqs, mind);
			if (reqs.isMulti() || reqs.isEmpty() || isTooFull()) {
				return reqs;
			}
		}
		return reqs;
	}

	public void tryResolveProfiles(RequirementWrapper reqs, Mind mind) {
		for (RequirementGoal req : reqs) {
			for (ProfileType ptype : ProfileType.values()) {

				if (req.getState().getProfile(ptype) == null)
					continue;
				if (IsKnownCheck.requiresKnown(req.getState().getFor(ptype)) == Boolean.FALSE) {
					continue;
				}
				if (ptype == ProfileType.USER) { // if user profile, only check if the actor themself satisfies it
					Profile user = mind.getOwner().getProfile();
					if (req.getState().getFor(ptype).conditionsUnfulfilledBy(user).isEmpty()) {
						req.getState().getProfile(ptype).resolve(user);
						req.getState().eliminateResolvedConditions(req.getState().getProfile(ptype));
					}

				} else {
					Profile res = null;

					if (req.getState().getProfile(ptype).isResolved()) {
						Profile already = req.getState().getProfile(ptype).getResolved();
						if (req.getState().getFor(ptype).conditionsUnfulfilledBy(already).isEmpty()) {
							res = already;
						}
					}
					if (res == null) {
						res = ProfilePlaceholder.tryFindResolution(req.getState().getFor(ptype),
								mind.getRememberedProfiles());
					}
					if (res != null) {
						req.getState().getProfile(ptype).resolve(res);
						req.getState().eliminateResolvedConditions(req.getState().getProfile(ptype));
					}

				}
			}
		}
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
		ActionWrapper newWrapper = new ActionWrapper(nextAction.action, goal, this);
		this.addAction(newWrapper);
		return nextAction.reqs;
	}

	/**
	 * Decides which action to add from a set. currently random
	 * 
	 * @return
	 */
	public PossibleActionWrapper decideAction(List<PossibleActionWrapper> actions) {
		return actions.isEmpty() ? null : actions.get(new Random().nextInt(actions.size()));
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
			goal.goalUpdate(mind);
			if (goal.isComplete())
				throw new IllegalStateException("Previous action " + actions.peek().action + " has no requirements");

		}

		List<PossibleActionWrapper> viable = new ArrayList<>();
		List<PossibleActionWrapper> mostEfficient = new ArrayList<>();
		for (Action pos : possible) {
			if (!this.wasRecentActionSuccessful && this.recentAction != null && pos == this.recentAction.action)
				continue;
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

	/**
	 * whether this task's goal is complete
	 * 
	 * @return
	 */
	public boolean isComplete() {
		return this.goal.isComplete();
	}

	public boolean isEmpty() {
		return this.actions.isEmpty();
	}

	/**
	 * Whether this task splits into other subtasks, i.e. whether this task ends
	 * with a CompleteTasksGoal
	 * 
	 * @return
	 */
	public boolean isRootTask() {
		if (this.isEmpty())
			return false;
		return this.actions.peek().requirement.isCompleteTasksGoal();
	}

	/**
	 * Returns the goals which branch from this task
	 */
	public RequirementWrapper getSubGoals() {
		if (!this.isRootTask())
			throw new IllegalStateException("Not a root task");
		return ((CompleteTasksGoal) this.actions.peek().requirement).getGoals();

	}

	public static class ActionWrapper {
		Action action;
		Goal requirement;
		Task task;

		public ActionWrapper(Action act, Goal req, Task task) {
			this.action = act;
			this.requirement = req;
			this.task = task;
		}

		public ActionWrapper(Action action, State requirement, Task task) {
			this(action, new RequirementGoal(requirement, Priority.NO_PRIORITY), task);
		}

		public Action getAction() {
			return action;
		}

		public Goal getRequirement() {
			return requirement;
		}

		public Task getTask() {
			return task;
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
	}
}
