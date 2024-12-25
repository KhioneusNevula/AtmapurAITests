package mind.thought_exp.actions;

import java.util.Collection;

import actor.Actor;
import mind.action.ActionType;
import mind.action.IActionType;
import mind.concepts.relations.ConceptRelationType;
import mind.concepts.type.ILocationMeme;
import mind.concepts.type.IMeme;
import mind.concepts.type.IProfile;
import mind.concepts.type.Property;
import mind.goals.ITaskGoal;
import mind.goals.question.Question;
import mind.goals.taskgoals.LearnTaskGoal;
import mind.goals.taskgoals.TravelTaskGoal;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;
import mind.thought_exp.IThoughtMemory.MemoryCategory;
import mind.thought_exp.info_thoughts.CheckSensedActorsThought;
import mind.thought_exp.memory.type.MemoryWrapper;
import mind.thought_exp.memory.type.RecentActionMemory;
import sim.WorldGraphics;

public class PickupActionThought extends AbstractActionThought {

	private Actor toAcquire;
	private IMeme toAcquireMeme;
	private ILocationMeme goTo;
	private IProfile goToProf;
	private Phase phase = Phase.VERIFYING;

	public PickupActionThought(ITaskGoal goal) {
		super(goal);
		toAcquireMeme = goal.transferItem();
		if (!(toAcquireMeme instanceof IProfile || toAcquireMeme instanceof Property)) {
			throw new IllegalArgumentException();
		}
		if (started())
			phase = Phase.EXECUTING;
	}

	@Override
	public void startThinking(ICanThink memory, long worldTick) {

	}

	@Override
	public boolean canExecuteIndividual(ICanThink user, int thoughtTicks, long worldTicks) {

		return toAcquire != null;
	}

	@Override
	public void beginExecutingIndividual(ICanThink forUser, int thoughtTicks, long worldTicks) {

		for (MemoryWrapper mw : forUser.getMindMemory().getShortTermMemoriesOfType(MemoryCategory.RECENT_ACTION)) {
			RecentActionMemory ram = (RecentActionMemory) mw.getMemory();
			if (ram.getActionType() == ActionType.PICK_UP) {
				PickupActionThought puat = (PickupActionThought) ram.getTopic();
				if (puat.toAcquireMeme.equals(this.toAcquireMeme) && puat.isPondering() && puat.succeeded()) {
					toAcquire = puat.toAcquire;
					this.phase = Phase.COMPLETE;
					break;
				}
			}
		}
		if (toAcquire == null) {
			for (MemoryWrapper mw : forUser.getMindMemory()
					.getShortTermMemoriesOfType(MemoryCategory.REMEMBER_FOR_PURPOSE)) {

			}
		}
		if (forUser.getAsHasActor().getActor().reachable(toAcquire)
				&& !(toAcquire.getHeld() != null && toAcquire.getHeld().equals(forUser.getAsHasActor().getActor()))
				&& !toAcquire.isRemoved()) {
			forUser.getAsHasActor().getActor().pickUp(toAcquire);
			succeeded = true;
		} else {
			this.succeeded = false;
		}

	}

	@Override
	public void thinkTick(ICanThink memory, int ticks, long worldTick) {
		switch (phase) {
		case VERIFYING:
			if (toAcquire == null) {
				if (toAcquireMeme instanceof IProfile prof) {
					this.postChildThought(new CheckSensedActorsThought(prof, getGoal()), ticks);
					this.markAddedShortTermMemories();
					this.phase = Phase.SEARCHING;
				} else if (toAcquireMeme instanceof Property prop) {
					this.postChildThought(
							new CheckSensedActorsThought(prop,
									(a) -> !(a.getHeld() != null
											&& a.getHeld().equals(memory.getAsHasActor().getActor())),
									getGoal()),
							ticks);
					this.markAddedShortTermMemories();
					this.phase = Phase.SEARCHING;
				}
			} else {
				this.phase = Phase.COMPLETE;
			}
			break;
		case SEARCHING:

			break;
		case NEED_LOCATION:
			if (toAcquireMeme instanceof IProfile prof) {
				Collection<ILocationMeme> pos = memory.getKnowledgeBase().getConceptsWithRelation(toAcquireMeme,
						ConceptRelationType.FOUND_AT);
				if (pos.isEmpty()) {
					this.postConditionForExecution(new LearnTaskGoal(Question.askLocation(prof)));
				} else {
					this.goTo = pos.iterator().next();
					this.postConditionForExecution(new TravelTaskGoal(goTo, true));
				}

			} else if (toAcquireMeme instanceof Property prop) {
				this.postConditionForExecution(new LearnTaskGoal(Question.askLocation(prop)));
			}
			break;
		case COMPLETE:
			this.markAsReadyToExecute();
			this.succeeded = true;

			break;
		case EXECUTING:
			break;
		}
	}

	@Override
	public void getInfoFromChild(ICanThink mind, IThought childThought, boolean interrupted, int ticks) {
		if (childThought instanceof CheckSensedActorsThought csat) {
			if (csat.getInformation().isEmpty()) {
				this.phase = Phase.NEED_LOCATION;
			} else {
				this.phase = Phase.COMPLETE;
				this.toAcquire = csat.getInformation().iterator().next();
				if (!mind.getAsHasActor().getActor().reachable(toAcquire)) {
					this.phase = Phase.NEED_LOCATION;
				}
			}
		}
	}

	@Override
	public void renderThoughtView(WorldGraphics g, int boxWidth, int boxHeight) {
		super.renderThoughtView(g, boxWidth, boxHeight);
		if (this.toAcquire != null) {
			g.translate(-toAcquire.getX(), -toAcquire.getY());
			g.translate(boxWidth / 2, boxHeight - 20);
			toAcquire.draw(g);
		}
	}

	@Override
	public IActionType<?> getActionType() {
		return ActionType.PICK_UP;
	}

	@Override
	public boolean isLightweight() {
		return false;
	}

	@Override
	public boolean equivalent(IThought other) {
		return other instanceof PickupActionThought
				&& this.toAcquireMeme.equals(((PickupActionThought) other).toAcquireMeme);
	}

	private static enum Phase {
		VERIFYING, SEARCHING, NEED_LOCATION, COMPLETE, EXECUTING
	}

}
