package mind.thought_exp.actions;

import mind.action.ActionType;
import mind.action.IActionType;
import mind.concepts.type.ILocationMeme;
import mind.goals.ITaskGoal;
import mind.goals.taskgoals.TravelTaskGoal;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;

public class WalkActionThought extends AbstractActionThought {

	private ILocationMeme target;
	// private Path path;
	private boolean foundPath;
	private boolean onlyReach;
	private boolean reached;

	public WalkActionThought(ITaskGoal goal) {
		super(goal.getPriority());
		target = goal.targetLocation();
		if (goal instanceof TravelTaskGoal ttg) {
			this.onlyReach = ttg.onlyReachDistance();
		}
	}

	@Override
	public void startThinking(ICanThink memory, long worldTick) {
		// TODO pathfinding and stuff, check how much energy, yadda yadda
		foundPath = true;
	}

	@Override
	public boolean canExecuteIndividual(ICanThink user, int thoughtTicks, long worldTicks) {
		return foundPath;
	}

	@Override
	public void beginExecutingIndividual(ICanThink forUser, int thoughtTicks, long worldTicks) {

	}

	@Override
	public void executionTickIndividual(ICanThink individual, int actionTick, int thoughtTick) {
		individual.getAsHasActor().getActor().moveToward(target.getGeneralLocation().getX(),
				target.getGeneralLocation().getY(), individual.getAsHasActor().getActor().getStep());
		reached = onlyReach ? individual.getAsHasActor().getActor().reachable(target.getGeneralLocation())
				: individual.getAsHasActor().getActor().getLocation().equals(target);
	}

	@Override
	public boolean canContinueExecutingIndividual(ICanThink individual, int actionTick, int thoughtTick) {
		return !reached;
	}

	@Override
	public boolean finishActionIndividual(ICanThink individual, int actionTick, int thoughtTick, boolean interruption) {

		return reached;
	}

	@Override
	public IActionType<?> getType() {
		return ActionType.WALK;
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
		return other instanceof WalkActionThought wat && this.target.equals(wat.target);
	}

}
