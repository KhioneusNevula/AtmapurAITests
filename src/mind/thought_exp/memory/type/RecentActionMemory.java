package mind.thought_exp.memory.type;

import mind.action.IActionType;
import mind.thought_exp.actions.IActionThought;

public class RecentActionMemory extends RecentThoughtMemory {

	private boolean succeeded;
	private IActionType<?> actionType;

	public RecentActionMemory(IActionThought thought, boolean succeeded) {
		super(thought);
		this.succeeded = succeeded;
		this.actionType = thought.getActionType();
	}

	public boolean succeeded() {
		return succeeded;
	}

	public IActionType<?> getActionType() {
		return actionType;
	}

	@Override
	public IActionThought getTopic() {
		return (IActionThought) super.getTopic();
	}

	@Override
	public MemoryCategory getType() {
		return MemoryCategory.RECENT_ACTION;
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj))
			return true;
		if (obj instanceof RecentThoughtMemory ram) {
			return this.getTopic().equals(ram.getTopic());
		}
		return false;
	}

}
