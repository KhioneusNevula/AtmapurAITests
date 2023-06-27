package mind.action.types;

import java.util.Collection;
import java.util.Set;

import mind.ICanAct;
import mind.IMind;
import mind.action.ActionType;
import mind.action.IAction;
import mind.goals.ITaskGoal;

public class SleepAction implements IAction {

	private String failure = "uncomfortable";

	public SleepAction(ITaskGoal task) {

	}

	@Override
	public boolean canExecuteIndividual(ICanAct user, boolean pondering) {
		return true;
	}

	@Override
	public void beginExecutingIndividual(ICanAct forUser) {
		((IMind) forUser).sleep(forUser.worldTicks());
	}

	@Override
	public Collection<ITaskGoal> genConditionGoal(ICanAct user) {
		return Set.of(); // TODO some go to bed type thing idk?
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
