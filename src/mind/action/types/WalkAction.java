package mind.action.types;

import java.util.Collection;
import java.util.Set;

import actor.Actor;
import mind.ICanAct;
import mind.IHasActor;
import mind.action.ActionType;
import mind.action.IAction;
import mind.goals.ITaskGoal;
import mind.goals.taskgoals.TravelTaskGoal;
import sim.Location;

public class WalkAction implements IAction {

	private Location target;
	private boolean reached;
	private Actor body;
	private boolean needsReach;

	private String failure = "n/a";

	public WalkAction(ITaskGoal goal) {
		this(((TravelTaskGoal) goal).targetLocation());
		this.needsReach = ((TravelTaskGoal) goal).onlyReachDistance();
	}

	public WalkAction(Location target) {
		this.target = target;

	}

	public Location targetLocation() {
		return target;
	}

	@Override
	public boolean canExecuteIndividual(ICanAct user, boolean pondering) {
		// TODO check if a walkable path can be made to this location or whatever and
		// that the individual can walk
		return true;
	}

	@Override
	public void beginExecutingIndividual(ICanAct forUser) {
		body = ((IHasActor) forUser).getActor();
	}

	@Override
	public boolean canContinueExecutingIndividual(ICanAct individual, int tick) {
		// TODO check path is still valid
		reached = needsReach ? body.distance(target) <= body.getReach() : body.at(target);
		return !reached;
	}

	@Override
	public boolean finishActionIndividual(ICanAct individual, int tick) {
		return reached;
	}

	@Override
	public void executionTickIndividual(ICanAct individual, int tick) {
		body.moveToward(target.getX(), target.getY(), body.getStep()); // individual.getProperty(Memory.SPEED).getInteger()
	}

	@Override
	public Collection<ITaskGoal> genConditionGoal(ICanAct user) {
		return Set.of();
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
