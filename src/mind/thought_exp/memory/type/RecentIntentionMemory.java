package mind.thought_exp.memory.type;

import java.util.Stack;

import main.Pair;
import mind.action.IActionType;
import mind.goals.ITaskGoal;
import mind.thought_exp.IUpgradedMind;
import mind.thought_exp.memory.IBrainMemory;

public class RecentIntentionMemory extends AbstractMemory {

	private ITaskGoal primaryGoal;
	private Stack<Pair<IActionType<?>, ITaskGoal>> goals;
	/** amount of actions this goal has undergone */
	private int times;

	public RecentIntentionMemory(ITaskGoal primaryGoal, Stack<Pair<IActionType<?>, ITaskGoal>> goals, int times) {
		this.primaryGoal = primaryGoal;
		this.goals = goals;
		this.times = times;
	}

	/**
	 * if the goal has remaining actions
	 * 
	 * @return
	 */
	public boolean hasRemainingActions() {
		return !goals.isEmpty();
	}

	/**
	 * amount of actions this goal has undergone
	 * 
	 * @return
	 */
	public int getTimes() {
		return times;
	}

	/**
	 * Returns the initial goal of this memory
	 * 
	 * @return
	 */
	public ITaskGoal getPrimaryGoal() {
		return primaryGoal;
	}

	/**
	 * amount of actions being considered
	 * 
	 * @return
	 */
	public int getActionCount() {
		return this.goals.size();
	}

	/**
	 * Removes the last action-goal pair
	 */
	public Pair<IActionType<?>, ITaskGoal> popLast() {
		return this.goals.pop();
	}

	/**
	 * Gets the most recent goal for this memory, (primary goal if there are no
	 * actions)
	 */
	public ITaskGoal getLastGoal() {
		return this.goals.empty() ? this.primaryGoal : this.goals.peek().getSecond();
	}

	/**
	 * get the goal stack of this memory
	 * 
	 * @return
	 */
	public Stack<Pair<IActionType<?>, ITaskGoal>> goalStack() {
		return this.goals;
	}

	@Override
	public boolean applyMemoryEffects(IBrainMemory toMind) {
		return true;
	}

	@Override
	public void forgetMemoryEffects(IUpgradedMind toMind) {

	}

	@Override
	public MemoryCategory getType() {
		return MemoryCategory.RECENT_INTENTION;
	}

	@Override
	public int hashCode() {
		return this.goals.hashCode() + this.primaryGoal.hashCode();
	}
}
