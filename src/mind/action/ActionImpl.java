package mind.action;

import java.util.Collection;
import java.util.Set;

import mind.goals.ITaskHint;

public abstract class ActionImpl implements IAction {
	protected String failure = "n/a";
	private IActionType<?> actionType;
	private Set<ITaskHint> hints;

	public ActionImpl(IActionType<?> actionType, ITaskHint... usage) {
		this.actionType = actionType;
		this.hints = Set.of(usage);
	}

	@Override
	public Collection<ITaskHint> getUsageHints() {
		return hints;
	}

	@Override
	public IActionType<?> getType() {
		return this.actionType;
	}

}
