package psych.mind;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import psych.action.Task;

/**
 * This is the part of the mind where actions are taken
 * 
 * @author borah
 *
 */
public class Will {

	private Mind mind;
	private static final int UNIVERSAL_MAX_TASKS = 15;
	private int maxTasks = UNIVERSAL_MAX_TASKS;
	private Stack<Task> tasks = new Stack<>();
	private EnumMap<Need, Task> needTasks = new EnumMap<>(Need.class);

	public Will(Mind mind) {
		this.mind = mind;
	}

	public Mind getMind() {
		return mind;
	}

	public Stack<Task> getTasks() {
		return tasks;
	}

	/**
	 * Decides whether to add a set of new tasks; returns the tasks it can NOT add.
	 * Currently based on free space in task queue
	 * 
	 * @param tasks
	 * @return
	 */
	public Set<Task> newTasks(Task... tasks) {
		int freeSpace = maxTasks - this.tasks.size();
		Set<Task> leftover = new HashSet<>();
		for (int i = 0; i < freeSpace; i++) {
			if (i >= tasks.length) {
				break;
			}
			if (i >= freeSpace) {
				leftover.add(tasks[i]);
			} else {
				this.tasks.add(tasks[i]);
			}
		}
		return leftover;
	}
}
