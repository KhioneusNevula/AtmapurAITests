package mind.action;

import java.util.Collection;
import java.util.Set;

import mind.IMind;
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
		((IMind) forUser).sleep(forUser.worldTicks());
	}

	@Override
	public Collection<ITaskGoal> genConditionGoal(IHasKnowledge user) {
		return Set.of(); // TODO some go to bed type thing idk?
	}

	@Override
	public ActionType<SleepAction> getType() {
		return ActionType.SLEEP;
	}

	@Override
	public String toString() {
		return "SleepAction";
	}

	@Override
	public String reasonForLastFailure() {
		return failure;
	}

}
