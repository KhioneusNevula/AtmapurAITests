package mind.thought_exp.memory.type;

import main.Pair;
import mind.feeling.IFeeling;
import mind.thought_exp.IThought;
import mind.thought_exp.IUpgradedMind;
import mind.thought_exp.memory.IBrainMemory;

public class RecentThoughtMemory extends AbstractMemory {

	public RecentThoughtMemory(IThought thought) {
		this.setTopic(thought);
		Pair<IFeeling, Integer> fF = thought.finalFeeling();
		this.setFeeling(fF.getFirst());
		this.setFeelingDuration(fF.getSecond());
	}

	@Override
	public IThought getTopic() {
		return (IThought) super.getTopic();
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
		return MemoryCategory.RECENT_THOUGHT;
	}

	@Override
	public int hashCode() {
		return this.getTopic().hashCode();
	}

}
