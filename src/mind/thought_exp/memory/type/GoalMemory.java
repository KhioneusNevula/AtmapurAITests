package mind.thought_exp.memory.type;

import mind.goals.IGoal;
import mind.thought_exp.IUpgradedMind;
import mind.thought_exp.memory.IBrainMemory;

/**
 * Used to store long-term goals
 * 
 * @author borah
 *
 */
public class GoalMemory extends AbstractMemory {

	public GoalMemory(IGoal goal) {
		this.setTopic(goal);
	}

	@Override
	public IGoal getTopic() {
		return (IGoal) super.getTopic();
	}

	public IGoal getGoal() {
		return getTopic();
	}

	@Override
	public boolean applyMemoryEffects(IBrainMemory toMind) {
		toMind.learnGoal(getGoal());
		return true;
	}

	@Override
	public void forgetMemoryEffects(IUpgradedMind toMind) {
		toMind.getKnowledgeBase().forgetGoal(getGoal());
	}

	@Override
	public MemoryCategory getType() {
		return MemoryCategory.GOAL;
	}

	@Override
	public int hashCode() {
		return this.getGoal().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GoalMemory gm)
			return this.getGoal().equals(gm.getGoal());
		return super.equals(obj);
	}

}
