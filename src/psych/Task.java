package psych;

import java.util.Stack;

public class Task {

	private Stack<Action> queue = new Stack<>();
	private Goal goal;
	private int maxActionMemory = 20;
	private Action currentAction;
	private ActionSearch search;
	private int maxStrikes;
	private int strikes;

	private boolean incomplete = false;
	private boolean planned = false;

	/**
	 * 
	 * @param goal       - the goal of the task
	 * @param maxStrikes - how many tries are performed before discarding the task
	 */
	public Task(Goal goal, int maxStrikes) {
		this.goal = goal;
		this.maxStrikes = maxStrikes;
	}

	public boolean done() {
		return this.queue.empty();
	}

	public boolean incomplete() {
		return incomplete;
	}

	/**
	 * Figure out what to do for this task using an ActionSearch
	 * 
	 * @return
	 */
	public boolean planTask() {
		if (toDiscard())
			throw new IllegalStateException("Too many strikes on this task: " + strikes + "/" + maxStrikes);
		this.search = new ActionSearch(this);
		planned = this.search.performSearch();
		return planned;
	}

	/**
	 * Whether this task has been figured out
	 * 
	 * @return
	 */
	public boolean wasPlanned() {
		return planned;
	}

	/**
	 * add a strike and return new value of strikes
	 * 
	 * @return
	 */
	public int addStrike() {
		return ++this.maxStrikes;
	}

	public boolean toDiscard() {
		return this.strikes >= maxStrikes;
	}

	public void perform() {

	}
}
