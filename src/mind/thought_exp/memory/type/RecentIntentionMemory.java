package mind.thought_exp.memory.type;

import mind.goals.ITaskGoal;
import mind.thought_exp.IUpgradedMind;
import mind.thought_exp.memory.IBrainMemory;

/**
 * Stores an intermediary goal used to complete another primary goal
 * 
 * @author borah
 *
 */
public class RecentIntentionMemory extends AbstractMemory {

	private ITaskGoal primaryGoal;
	private ITaskGoal intention;

	public RecentIntentionMemory(ITaskGoal intention, ITaskGoal primaryGoal) {
		this.primaryGoal = primaryGoal;
		this.intention = intention;
	}

	/**
	 * Returns the primary goal of this intention
	 * 
	 * @return
	 */
	public ITaskGoal getPrimaryGoal() {
		return primaryGoal;
	}

	/**
	 * Returns the intention stord in this memory, intended to satisfy the primary
	 * goal
	 * 
	 * @return
	 */
	public ITaskGoal getIntentionGoal() {
		return intention;
	}

	@Override
	public boolean applyMemoryEffects(IBrainMemory toMind) {
		toMind.learnGoal(getIntentionGoal());
		return true;
	}

	@Override
	public void forgetMemoryEffects(IUpgradedMind toMind) {
		toMind.getKnowledgeBase().forgetGoal(getIntentionGoal());

	}

	@Override
	public MemoryCategory getType() {
		return MemoryCategory.RECENT_INTENTION;
	}

	@Override
	public int hashCode() {
		return this.intention.hashCode() + this.primaryGoal.hashCode();
	}
}
