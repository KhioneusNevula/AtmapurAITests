package mind.thought_exp.memory;

import main.Pair;
import mind.feeling.IFeeling;
import mind.thought_exp.IThought;
import mind.thought_exp.IUpgradedMind;

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
	public boolean apply(IUpgradedMind toMind) {
		return true;
	}

	@Override
	public Type getType() {
		return Type.RECENT_THOUGHT;
	}

}
