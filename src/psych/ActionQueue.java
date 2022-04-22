package psych;

import java.util.Stack;

public class ActionQueue {

	private int maxActionMemory = 30;
	private Stack<Task> queue = new Stack<Task>();
	private Task currentTask;

	private Brain brainRef;

	public ActionQueue(Brain brain) {
		this.brainRef = brain;
	}

	public Brain getBrainRef() {
		return brainRef;
	}

	public Stack<Task> getQueue() {
		return queue;
	}

	public void addTask(Goal goal, int maxStrikes) {
		this.addTask(new Task(goal, maxStrikes));
	}

	protected void addTask(Task a) {
		this.queue.push(a);
		if (this.queue.size() > this.maxActionMemory) {
			this.queue.remove(0);
		}
	}

	/**
	 * Figures out the plan for the next task
	 * 
	 * @return
	 */
	public boolean planNextTask() {
		return this.queue.peek().planTask();
	}

	/**
	 * True if the current task was begun, false otherwise
	 * 
	 * @return
	 */
	public boolean startNewTask() {
		this.currentTask = this.queue.pop();

		if (currentTask.wasPlanned() ? true : currentTask.planTask()) {
			currentTask.perform();

			return true;
		}
		currentTask.addStrike();
		if (!currentTask.toDiscard())
			this.queue.add(0, currentTask);
		currentTask = null;
		return false;
	}

}
