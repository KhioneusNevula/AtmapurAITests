package psych.mind;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import psych.action.ActionType;
import psych.action.goal.Goal;
import psych.action.goal.NeedGoal;
import psych.action.goal.RequirementWrapper;
import psych.action.goal.Task;
import psych.action.goal.Task.ActionWrapper;
import psych.action.types.Action;
import sim.IHasProfile;

/**
 * This is the part of the mind where actions are taken
 * 
 * @author borah
 *
 */
public class Will implements IMindPart {

	private Mind mind;
	// TODO make all these numbers more individual
	private static final int UNIVERSAL_MAX_TASKS = 25;
	private int maxTasks = UNIVERSAL_MAX_TASKS;
	private static final int UNIVERSAL_MAX_FOCUS = 3;
	private int maxFocus = 3;
	private List<Task> tasks = new ArrayList<>();
	private Map<ActionType, ActionWrapper> executingActions = new EnumMap<>(ActionType.class);
	private Stack<Task> focusedTasks = new Stack<>();

	public Will(Mind mind) {
		this.mind = mind;
	}

	@Override
	public String report() {
		return "{taskSet:" + tasks + ",\n\tfocusedTasks:" + focusedTasks + ",\n\tactionsExecuting:" + executingActions
				+ ",\n\tmaxTasks:" + maxTasks + "}";

	}

	public Mind getMind() {
		return mind;
	}

	public int getMaxFocus() {
		return maxFocus;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public Stack<Task> getFocusedTasks() {
		return focusedTasks;
	}

	public void update(int ticks) {
		// TODO update will; make a proper way to focus on goals
		updateExecutingActions(ticks);
		updateTaskGoals(ticks);
		chooseNewActions(ticks);
	}

	public void updateTaskGoals(int ticks) {
		for (Task task : new ArrayList<>(this.tasks)) {
			if (task.isComplete()) {
				this.removeTask(task);
			}
		}
	}

	/**
	 * return if successfully focused on the given task
	 * 
	 * @param on
	 * @return
	 */
	public boolean focus(Task on) {
		if (focusedTasks.size() < maxFocus) {
			this.focusedTasks.push(on);
			return true;
		}
		return false;
	}

	protected void chooseNewActions(int ticks) {
		for (Task task : this.focusedTasks) {
			if (executingActions.containsValue(task.getRecentAction()))
				continue;
			if (task.isEmpty())
				throw new IllegalStateException("Empty task " + task + " present in focus queue");

			ActionWrapper fa = task.getFirstAction();
			Set<ActionType> type = ActionType.taskTypes(fa.getAction().getActionType());
			for (ActionType t : type) {
				if (executingActions.get(t) != null) {
					System.out.println("Action " + fa.getAction() + " not added to " + this.mind
							+ " because actiontype slot " + t + " was full"); // TODO remove this print
					continue;
				}
			}
			fa = task.startExecutingAction(this);
			if (fa == null) {
				System.out.println("Unable to get next action from task " + task);
				continue;
			}

			for (ActionType t : type) {
				executingActions.put(t, fa);
			}
			System.out.println(mind + " chose next action " + fa.getAction() + " for " + task.getGoal());
		}
	}

	/**
	 * Updates currently executing actions; does not choose new ones
	 * 
	 * @param ticks
	 */
	protected void updateExecutingActions(int ticks) {
		Set<ActionWrapper> already = new HashSet<>();
		for (ActionType type : ActionType.values()) {
			ActionWrapper wrap = this.executingActions.get(type);
			if (wrap == null) {

				continue;
			}
			if (already.contains(wrap))
				continue;
			Action action = wrap.getAction();
			Goal result = wrap.getTask().getNextGoal();
			Goal req = wrap.getRequirement();
			IHasProfile actor = this.mind.getOwner();
			Task task = wrap.getTask();
			if (action._canContinueExecution(actor, this, task, req, result)) {
				action._update(actor, this, task, req, result);
				task.changeExecutionTicks(action, -1);
			} else {

				String reason = action._complete(actor, this, task, req, result, task.getExecutionTicks(action));
				if (reason != null) {
					task.setWasRecentActionSuccessful(false);
					System.err.println(this.mind + " failed to execute " + action + " because of \"" + reason + "\"");
					// TODO try regen action plan if action failed
				}
				for (ActionType type2 : EnumSet.copyOf(this.executingActions.keySet())) {
					executingActions.remove(type2, wrap);
				}
			}
			already.add(wrap);

		}
	}

	public void debugExecuteActionPlans() {

		if (focusedTasks.isEmpty()) {

			if (tasks.isEmpty())
				return;
			focus(tasks.get(0));
		}

	}

	/**
	 * Generates an action plan for a random task
	 */
	public void debugGenerateActionPlan(int eff) {
		if (tasks.isEmpty())
			return;
		Task forTask = new ArrayList<>(tasks).get(mind.rand().nextInt(tasks.size()));
		forTask.clearActions();
		long time = System.currentTimeMillis();
		RequirementWrapper re = forTask.generateActionPlan(this.mind, eff);
		if (re == null)
			System.out.println("Too many attempts for task");
		long overallTime = System.currentTimeMillis() - time;

		if (overallTime > 2) {
			System.out.println(mind + " action plan took " + (overallTime) + " milliseconds to generate");
			System.out.println(forTask);
		}
	}

	/**
	 * Gets all currently executing actions
	 * 
	 * @return
	 */
	public Collection<ActionWrapper> getCurrentActions() {
		return executingActions.values();

	}

	/**
	 * Gets the executing action for the given {@link psych.action.ActionType
	 * ActionType}
	 * 
	 * @param type
	 * @return
	 */
	public ActionWrapper getExecutingActionFor(ActionType type) {
		return executingActions.get(type);
	}

	/**
	 * Force option is there in case of idk mind control; adds a goal to this entity
	 * 
	 * @param task
	 * @param force
	 */
	public void addTask(Task task, boolean force) {
		// TODO better priority selection
		if (!force && tasks.size() >= maxTasks)
			throw new UnsupportedOperationException("Cannot add non-forced task to a full queue");
		if (force && tasks.size() >= maxTasks) {

			tasks.remove(tasks.size() - 1);
			if (!tasks.contains(task))
				tasks.add(task);
			tasks.sort((t1, t2) -> t1.getGoal().getPriority().ordinal() - t2.getGoal().getPriority().ordinal());

		} else if (tasks.size() < maxTasks) {
			tasks.add(task);
		}
	}

	public void removeTask(Task task) {
		this.tasks.remove(task);
		this.focusedTasks.remove(task);
		for (ActionType t : EnumSet.copyOf(this.executingActions.keySet())) {
			if (executingActions.get(t).getTask() == task)
				executingActions.remove(t);
		}
		// TODO remove print statement
		System.out.println(this.mind + " removed " + task.toString());
	}

	/**
	 * Adds the goal to the queue as a task
	 * 
	 * @param goal
	 * @return
	 */
	public Task chooseGoal(Goal goal, boolean force) {
		Task task = new Task(goal);
		/*
		 * System.out.println(mind.toString() + (force ? " was forced to choose task " :
		 * " chose task ") + task); // TODO remove this print
		 */
		this.addTask(task, force);
		return task;
	}

	/**
	 * whether the will is already handling a need
	 * 
	 * @param need
	 * @return
	 */
	public boolean hasTaskFor(Need need) {
		for (Task task : this.tasks) {
			if (task.getRootGoal() instanceof NeedGoal n) {
				if (n.getFocus() == need)
					return true;
			}
		}
		return false;
	}

}
