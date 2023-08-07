package mind.thought_exp.actions;

import actor.Actor;
import mind.action.ActionType;
import mind.action.IActionType;
import mind.goals.ITaskGoal;
import mind.goals.question.Question;
import mind.goals.question.Question.QuestionType;
import mind.goals.taskgoals.TravelTaskGoal;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;
import sim.Location;

public class SearchActionThought extends AbstractActionThought {

	private Question question;
	private Location targetLoc;
	private int triesLeft;
	private Integer vecX;
	private Integer vecY;

	public SearchActionThought(ITaskGoal goal) {
		super(goal);
		this.question = goal.learnInfo();
		if (question.getType() != QuestionType.LOCATION) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void startThinking(ICanThink memory, long worldTick) {
		triesLeft = memory.rand().nextInt(4) + 4;
	}

	@Override
	public void thinkTick(ICanThink user, int ticks, long worldTick) {
		Actor actor = user.getAsHasActor().getActor();
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
		this.postConditionForExecution(new TravelTaskGoal(targetLoc, true));
		triesLeft--;

	}

	@Override
	public boolean canExecuteIndividual(ICanThink user, int thoughtTicks, long worldTicks) {
		return triesLeft == 0;
	}

	@Override
	public void beginExecutingIndividual(ICanThink forUser, int thoughtTicks, long worldTicks) {

	}

	@Override
	public boolean finishActionIndividual(ICanThink individual, int actionTick, int thoughtTick, boolean interruption) {
		return true;
	}

	@Override
	public IActionType<?> getType() {
		return ActionType.SEARCH;
	}

	@Override
	public boolean isLightweight() {
		return false;
	}

	@Override
	public void getInfoFromChild(IThought childThought, boolean interrupted, int ticks) {

	}

	@Override
	public boolean equivalent(IThought other) {
		return other instanceof SearchActionThought sat && this.question.equals(sat.question);
	}

}
