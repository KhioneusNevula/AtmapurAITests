package mind.action.types;

import actor.Actor;
import mind.action.ActionImpl;
import mind.action.ActionType;
import mind.goals.ITaskGoal;
import mind.goals.TaskHint;
import mind.goals.question.Question;
import mind.goals.taskgoals.TravelTaskGoal;
import mind.memory.IHasKnowledge;
import sim.Location;

public class SearchAction extends ActionImpl {

	private Location targetLoc;
	private Integer vecX;
	private Integer vecY;
	private Question question;

	public SearchAction(ITaskGoal goal) {
		super(ActionType.SEARCH, TaskHint.LEARN);
		this.question = goal.learnInfo();
	}

	@Override
	public boolean canExecuteIndividual(IHasKnowledge user, boolean pondering) {
		Actor actor = user.getAsHasActor().getActor();
		if (targetLoc != null && actor.distance(targetLoc) < actor.getReach())
			return true;
		if (pondering) {
			if (vecX == null) {
				vecX = (actor.rand().nextBoolean() ? 1 : -1) * actor.rand().nextInt(actor.getRadius() * 10);
				vecY = (actor.rand().nextBoolean() ? 1 : -1) * actor.rand().nextInt(actor.getRadius() * 10);
			} else {
				double dist = Math.sqrt(vecX * vecX + vecY * vecY) * (actor.rand().nextDouble() * 2);
				double angle = Math.atan2(vecY, vecX);
				angle += (actor.rand().nextBoolean() ? 1 : -1) * actor.rand().nextDouble() * Math.PI;
				vecX = (int) (Math.cos(angle) * dist);
				vecY = (int) (Math.sin(angle) * dist);
			}
			targetLoc = new Location(actor.getWorld().clampX(actor.getX() + vecX),
					actor.getWorld().clampY(actor.getY() + vecY));
			failure = "finding location for travel at " + targetLoc;
		}
		return false;
	}

	public Question getQuestion() {
		return question;
	}

	@Override
	public void beginExecutingIndividual(IHasKnowledge forUser) {
	}

	@Override
	public ITaskGoal genConditionGoal(IHasKnowledge user) {
		return new TravelTaskGoal(targetLoc, true);
	}

	@Override
	public int executionAttempts() {
		return 8;
	}

	@Override
	public String toString() {
		return "SearchA" + (targetLoc != null ? "{" + targetLoc + "}" : "");
	}

}
