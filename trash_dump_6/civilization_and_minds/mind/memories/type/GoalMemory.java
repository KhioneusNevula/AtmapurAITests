package civilization_and_minds.mind.memories.type;

import civilization_and_minds.mind.goals.IGoal;
import civilization_and_minds.mind.mechanics.IMemoryKnowledge;
import civilization_and_minds.mind.memories.IMemoryItem;
import civilization_and_minds.social.concepts.IConcept;

public class GoalMemory implements IMemoryItem {

	/**
	 * To indicate the completion state of this goal
	 * 
	 * @author borah
	 *
	 */
	public static enum CompletionState {
		COMPLETE, INCOMPLETE, IMPOSSIBLE;

		public boolean isComplete() {
			return this == COMPLETE;
		}
	}

	private IGoal goal;
	private CompletionState completion;

	public GoalMemory(IGoal goal, CompletionState completion) {
		this.goal = goal;
		this.completion = completion;
	}

	@Override
	public IConcept getMainInfo() {
		return goal;
	}

	@Override
	public MemoryType getMemoryType() {
		return MemoryType.GOAL;
	}

	/**
	 * Whether the goal contained in this memory is complete
	 * 
	 * @return
	 */
	public boolean isGoalComplete() {
		return completion.isComplete();
	}

	public CompletionState getCompletion() {
		return completion;
	}

	/**
	 * If this goal was impossible, e.g. lacking the physical capabilities to
	 * complete it
	 * 
	 * @return
	 */
	public boolean wasImpossible() {
		return completion == CompletionState.IMPOSSIBLE;
	}

	@Override
	public boolean applyEffects(IMemoryKnowledge mems) {
		return true;
	}

	@Override
	public boolean removeEffects(IMemoryKnowledge mem) {
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GoalMemory mem) {
			return this.goal.equals(mem.goal) && this.completion == mem.completion;
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return this.completion.hashCode() + goal.hashCode();
	}

}
