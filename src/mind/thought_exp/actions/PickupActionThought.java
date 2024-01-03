package mind.thought_exp.actions;

import java.util.Iterator;

import actor.Actor;
import main.Pair;
import mind.action.ActionType;
import mind.action.IActionType;
import mind.concepts.relations.ConceptRelationType;
import mind.concepts.type.ILocationMeme;
import mind.concepts.type.IMeme;
import mind.concepts.type.IProfile;
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
	private IProfile toAcquireProfile;
	private Property property;
	private IProfile profile;
	private boolean checkingUnreachableSensed;
	private boolean checkProfileLocation;
	private ILocationMeme goTo;
	private int tries = 5;

	public PickupActionThought(ITaskGoal goal) {
		super(goal);
		this.toAcquireMeme = goal.transferItem();
	}

	@Override
	public void startThinking(ICanThink memory, long worldTick) {
		if (toAcquireMeme instanceof Property) {
			this.property = (Property) toAcquireMeme;
		} else {
			this.profile = (IProfile) toAcquireMeme;
		}
	}

	@Override
	public void thinkTick(ICanThink memory, int ticks, long worldTick) {
		if (checkProfileLocation) {
			checkProfileLocation = false;
			this.goTo = memory.getKnowledgeBase()
					.<ILocationMeme>getConceptsWithRelation(profile, ConceptRelationType.FOUND_AT).iterator().next();
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
				if (memory.hasActor() && memory.getAsHasActor().getActor().getName().equals("bobzy"))
					System.out.print(""); // TODO remove this dumbery
				this.failPreemptively();
			}
		} else if (toAcquire != null) {
			if (toAcquire.isRemoved()
					|| toAcquire.getHeld() != null && toAcquire.getHeld().equals(memory.getAsHasActor().getActor())) {
				if (memory.hasActor() && memory.getAsHasActor().getActor().getName().equals("bobzy"))
					System.out.print(""); // TODO remove this dumbery
				this.failPreemptively();
			} else {
				if (!memory.getAsHasActor().getActor().reachable(toAcquire)
						&& this.getPendingCondition(memory) == null) {
					if (goTo == null)
						goTo = toAcquire.getLocation();
					this.postConditionForExecution(new TravelTaskGoal(goTo, true));
				}
			}
		}
	}

	// TODO eventually also add, like, constructs or whatever
	@Override
	public void getInfoFromChild(IThought childThought, boolean interrupted, int ticks) {
		if (childThought instanceof CheckSensedActorsThought csat) {

			if (!checkingUnreachableSensed) {
				this.checkingUnreachableSensed = true;
				if (!csat.getInformation().isEmpty()) {
					this.toAcquire = csat.getInformation().iterator().next();
				} else {
					if (property != null) {
						this.postChildThought(new CheckSensedActorsThought(property), ticks);
					} else if (this.profile != null) {
						this.checkProfileLocation = true;
					}
				}
			} else

			{
				if (!csat.getInformation().isEmpty()) {
					if (profile != null) {
						Iterator<Actor> ai = csat.getInformation().iterator();
						while (ai.hasNext()) {
							Actor a = ai.next();
							if (a.getUUID().equals(profile.getUUID())) {
								this.toAcquire = a;
								this.goTo = a.getLocation();
								break;
							}
						}
					} else {
						this.toAcquire = csat.getInformation().iterator().next();
						this.goTo = toAcquire.getLocation();
					}
				} else {
					this.postChildThought(new CheckKnownProfilesThought(property, true), ticks);
				}
			}
		} else if (childThought instanceof CheckKnownProfilesThought ckat) {
			if (!ckat.getInformation().isEmpty()) {
				Pair<IProfile, ILocationMeme> pair = ckat.getInformation().iterator().next();
				this.toAcquireProfile = pair.getFirst();
				this.goTo = pair.getSecond();
				this.postConditionForExecution(new TravelTaskGoal(goTo, true));
			} else {
				tries--;
			}
		}
	}

	@Override
	public boolean canExecuteIndividual(ICanThink user, int thoughtTicks, long worldTicks) {
		return (toAcquire != null && user.getAsHasActor().getActor().reachable(toAcquire));
	}

	@Override
	public void beginExecutingIndividual(ICanThink forUser, int thoughtTicks, long worldTicks) {
		if (!toAcquire.isRemoved() && toAcquire.getHeld() == null) {
			forUser.getAsHasActor().getActor().pickUp(toAcquire);
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
