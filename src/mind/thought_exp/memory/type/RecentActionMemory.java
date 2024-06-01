package mind.thought_exp.memory.type;

import mind.action.IActionType;
import mind.goals.ITaskGoal;
import mind.thought_exp.actions.IActionThought;

public class RecentActionMemory extends RecentThoughtMemory {

	private boolean succeeded;
	private IActionType<?> actionType;
	private ITaskGoal goal;
	private ITaskGoal primaryGoal;

	/**
	 * 
	 * @param thought     the action itself
	 * @param forGoal     the goal the action was going for
	 * @param primaryGoal the ultimate goal the action needs to complete; this is in
	 *                    order to organize sequences of actions all in the same
	 *                    motive
	 * @param succeeded
	 */
	public RecentActionMemory(IActionThought thought, ITaskGoal forGoal, ITaskGoal primaryGoal, boolean succeeded) {
		super(thought);
		this.succeeded = succeeded;
		this.actionType = thought.getActionType();
		this.goal = forGoal;
		this.primaryGoal = primaryGoal;
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

	public ITaskGoal getGoal() {
		return goal;
	}

	public ITaskGoal getPrimaryGoal() {
		return primaryGoal;
	}

	@Override
	public MemoryCategory getType() {
		return MemoryCategory.RECENT_ACTION;
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj))
			return true;
		if (obj instanceof RecentActionMemory ram) {
			return this.getTopic().equals(ram.getTopic()) && this.goal.equals(ram.goal)
					&& this.primaryGoal.equals(ram.goal);
		}
		return false;
	}

}
