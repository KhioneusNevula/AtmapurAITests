package mind.action.types;

import actor.Actor;
import mind.IHasActor;
import mind.action.ActionType;
import mind.action.IAction;
import mind.concepts.type.ILocationMeme;
import mind.goals.ITaskGoal;
import mind.goals.taskgoals.TravelTaskGoal;
import mind.memory.IHasKnowledge;

public class WalkAction implements IAction {

	private ILocationMeme target;
	private boolean reached;
	private Actor body;
	private boolean needsReach;

	private String failure = "n/a";

	public WalkAction(ITaskGoal goal) {
		this(((TravelTaskGoal) goal).targetLocation());
		this.needsReach = ((TravelTaskGoal) goal).onlyReachDistance();
	}

	public WalkAction(ILocationMeme target) {
		this.target = target;

	}

	public ILocationMeme targetLocation() {
		return target;
	}

	@Override
	public boolean canExecuteIndividual(IHasKnowledge user, boolean pondering) {
		// TODO check if a walkable path can be made to this location or whatever and
		// that the individual can walk
		return true;
	}

	@Override
	public void beginExecutingIndividual(IHasKnowledge forUser) {
		body = ((IHasActor) forUser).getActor();
	}

	@Override
	public boolean canContinueExecutingIndividual(IHasKnowledge individual, int tick) {
		// TODO check path is still valid
		reached = needsReach ? body.distance(target.getGeneralLocation()) <= body.getReach()
				: body.at(target.getGeneralLocation());
		return !reached;
	}

	@Override
	public boolean finishActionIndividual(IHasKnowledge individual, int tick) {
		return reached;
	}

	@Override
	public void executionTickIndividual(IHasKnowledge individual, int tick) {
		body.moveToward(target.getGeneralLocation().getX(), target.getGeneralLocation().getY(), body.getStep()); // individual.getProperty(Memory.SPEED).getInteger()
	}

	@Override
	public ITaskGoal genConditionGoal(IHasKnowledge user) {
		return null;
	}

	@Override
	public ActionType<WalkAction> getType() {
		return ActionType.WALK;
	}

	@Override
	public String toString() {
		return "WalkA{" + this.target + "}";
	}

	@Override
	public String reasonForLastFailure() {
		return failure;
	}

}
