package mind.action;

import java.util.Collection;
import java.util.Set;

import actor.Actor;
import mind.IHasActor;
import mind.goals.ITaskGoal;
import mind.goals.taskgoals.TravelTaskGoal;
import mind.memory.IHasKnowledge;
import sim.Location;

public class WalkAction implements IAction {

	private Location target;
	private Set<Location> targetAsSet;
	private boolean reached;
	private Actor body;
	private boolean needsReach;

	private String failure = "path blocked";

	public WalkAction(ITaskGoal goal) {
		this(((TravelTaskGoal) goal).getTargetLocation());
		this.needsReach = ((TravelTaskGoal) goal).onlyReachDistance();
	}

	public WalkAction(Location target) {
		this.target = target;

		targetAsSet = Set.of(target);
	}

	@Override
	public Location targetLocation() {
		return target;
	}

	@Override
	public Iterable<Location> targetLocations() {
		return targetAsSet;
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
		reached = needsReach ? body.distance(target) <= body.REACH : body.at(target);
		return !reached;
	}

	@Override
	public boolean finishActionIndividual(IHasKnowledge individual, int tick) {
		return reached;
	}

	@Override
	public void executionTickIndividual(IHasKnowledge individual, int tick) {
		body.moveToward(target.getX(), target.getY(), 10); // individual.getProperty(Memory.SPEED).getInteger()
	}

	@Override
	public Collection<ITaskGoal> genConditionGoal(IHasKnowledge user) {
		return Set.of();
	}

	@Override
	public ActionType<WalkAction> getType() {
		return ActionType.WALK;
	}

	@Override
	public String toString() {
		return "WalkAction{" + this.target + "}";
	}

	@Override
	public String reasonForLastFailure() {
		return failure;
	}

}
