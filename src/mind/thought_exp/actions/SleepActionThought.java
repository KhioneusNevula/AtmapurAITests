package mind.thought_exp.actions;

import mind.action.ActionType;
import mind.action.IActionType;
import mind.goals.ITaskGoal;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;
import mind.thought_exp.IUpgradedMind;

/**
 * TODO check how comfortable you are or whatever
 * 
 * @author borah
 *
 */
public class SleepActionThought extends AbstractActionThought {

	public SleepActionThought(ITaskGoal goal) {
		super(goal.getPriority());
	}

	@Override
	public void startThinking(ICanThink memory, long worldTick) {

	}

	@Override
	public boolean canExecuteIndividual(ICanThink user, int thoughtTicks, long worldTicks) {
		return true;
	}

	@Override
	public void beginExecutingIndividual(ICanThink forUser, int thoughtTicks, long worldTicks) {
		((IUpgradedMind) forUser).sleep();
	}

	@Override
	public boolean finishActionIndividual(ICanThink individual, int actionTick, int thoughtTick, boolean interruption) {
		return true;
	}

	@Override
	public IActionType<?> getType() {
		return ActionType.SLEEP;
	}

	@Override
	public boolean isLightweight() {
		return false;
	}

	@Override
	public void thinkTick(ICanThink memory, int ticks, long worldTick) {

	}

	@Override
	public void getInfoFromChild(IThought childThought, boolean interrupted, int ticks) {

	}

	@Override
	public boolean equivalent(IThought other) {
		return other instanceof SleepActionThought;
	}

}
