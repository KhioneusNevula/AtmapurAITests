package mind.thought_exp.memory;

import mind.thought_exp.actions.IActionThought;

public class RecentActionMemory extends RecentThoughtMemory {

	public RecentActionMemory(IActionThought thought) {
		super(thought);
	}

	@Override
	public IActionThought getTopic() {
		return (IActionThought) super.getTopic();
	}

	@Override
	public Type getType() {
		return Type.RECENT_ACTION;
	}

}
