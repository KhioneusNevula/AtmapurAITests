package mind.action.types;

import mind.action.ActionType;
import mind.action.IAction;
import mind.goals.ITaskGoal;
import mind.memory.IHasKnowledge;

public class SleepAction implements IAction {

	private String failure = "uncomfortable";

	public SleepAction(ITaskGoal task) {

	}

	@Override
	public boolean canExecuteIndividual(IHasKnowledge user, boolean pondering) {
		return true;
	}

	@Override
	public void beginExecutingIndividual(IHasKnowledge forUser) {
		// TODO figiure out sleeping
	}

	@Override
	public ITaskGoal genConditionGoal(IHasKnowledge user) {
		return null; // TODO some go to bed type thing idk?
	}

	@Override
	public ActionType<SleepAction> getType() {
		return ActionType.SLEEP;
	}

	@Override
	public String toString() {
		return "SleepA";
	}

	@Override
	public String reasonForLastFailure() {
		return failure;
	}

}
