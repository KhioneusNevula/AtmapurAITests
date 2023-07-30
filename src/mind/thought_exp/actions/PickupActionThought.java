package mind.thought_exp.actions;

import actor.Actor;
import main.Pair;
import mind.action.ActionType;
import mind.action.IActionType;
import mind.concepts.type.ILocationMeme;
import mind.concepts.type.IMeme;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.goals.ITaskGoal;
import mind.goals.taskgoals.TravelTaskGoal;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;
import mind.thought_exp.ThoughtType;
import mind.thought_exp.info_thoughts.CheckKnownProfilesThought;
import mind.thought_exp.info_thoughts.CheckSensedActorsThought;
import sim.WorldGraphics;

public class PickupActionThought extends AbstractActionThought {

	private IMeme toAcquireMeme;
	private Actor toAcquire;
	private Profile toAcquireProfile;
	private Property property;
	private Profile profile;
	private boolean checkProfileLocation;
	private ILocationMeme goTo;
	private int tries = 5;

	public PickupActionThought(ITaskGoal goal) {
		super(goal.getPriority());
		this.toAcquireMeme = goal.transferItem();
	}

	@Override
	public void startThinking(ICanThink memory, long worldTick) {
		if (toAcquireMeme instanceof Property) {
			this.property = (Property) toAcquireMeme;
		} else {
			this.profile = (Profile) toAcquireMeme;
		}
	}

	@Override
	public void thinkTick(ICanThink memory, int ticks, long worldTick) {
		if (checkProfileLocation) {
			checkProfileLocation = false;
			this.goTo = memory.getKnowledgeBase().getLocation(profile);
		}
		if (this.childThoughts(ThoughtType.FIND_MEMORY_INFO).isEmpty() && toAcquire == null) {
			if (toAcquireProfile != null) {
				this.postChildThought(new CheckSensedActorsThought(toAcquireProfile), ticks);
			} else if (property != null) {
				this.postChildThought(
						new CheckSensedActorsThought(property, (a) -> memory.getAsHasActor().getActor().reachable(a)),
						ticks);

			} else {
				this.postChildThought(new CheckSensedActorsThought(profile), ticks);

			}
			tries--;
			if (tries <= 0) {
				this.setFailureReason("too many attempts");
				this.failPreemptively();
			}
		} else if (toAcquire != null) {
			if (!memory.getAsHasActor().getActor().reachable(toAcquire) && this.getPendingCondition(memory) == null) {
				goTo = toAcquire.getLocation();
				this.postConditionForExecution(new TravelTaskGoal(goTo, true));
			}
		}
	}

	// TODO eventually also add, like, constructs or whatever
	@Override
	public void getInfoFromChild(IThought childThought, boolean interrupted, int ticks) {
		if (childThought instanceof CheckSensedActorsThought csat) {
			if (!csat.getInformation().isEmpty()) {
				this.toAcquire = csat.getInformation().iterator().next();
			} else {
				if (property != null) {
					this.postChildThought(new CheckKnownProfilesThought(property, true), ticks);
				} else if (this.profile != null) {
					this.checkProfileLocation = true;
				}
			}
		} else if (childThought instanceof CheckKnownProfilesThought ckat) {
			if (!ckat.getInformation().isEmpty()) {
				Pair<Profile, ILocationMeme> pair = ckat.getInformation().iterator().next();
				this.toAcquireProfile = pair.getFirst();
				this.goTo = pair.getSecond();
				this.postConditionForExecution(new TravelTaskGoal(goTo, true));
			}
		}
	}

	@Override
	public boolean canExecuteIndividual(ICanThink user, int thoughtTicks, long worldTicks) {
		return (toAcquire != null && user.getAsHasActor().getActor().reachable(toAcquire));
	}

	@Override
	public void beginExecutingIndividual(ICanThink forUser, int thoughtTicks, long worldTicks) {
		forUser.getAsHasActor().getActor().pickUp(toAcquire);
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
	public IActionType<?> getType() {
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

}
