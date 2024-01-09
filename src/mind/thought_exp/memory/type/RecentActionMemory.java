package mind.thought_exp.memory.type;

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
	public MemoryCategory getType() {
		return MemoryCategory.RECENT_ACTION;
	}

}
