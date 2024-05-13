package mind.thought_exp.actions;

import java.util.Iterator;

import actor.Actor;
import actor.IUniqueExistence;
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
import mind.thought_exp.IThoughtMemory.MemoryCategory;
import mind.thought_exp.ThoughtType;
import mind.thought_exp.info_thoughts.CheckKnownProfilesThought;
import mind.thought_exp.info_thoughts.CheckSensedActorsThought;
import mind.thought_exp.memory.type.ImportantWorldObjectsMemory;
import mind.thought_exp.memory.type.MemoryWrapper;
import sim.WorldGraphics;

public class x_PickupActionThought extends AbstractActionThought {

	private IMeme toAcquireMeme;
	private Actor toAcquire;
	private IProfile toAcquireProfile;
	private Property property;
	private IProfile profile;
	private boolean checkingUnreachableSensed;
	private boolean checkProfileLocation;
	private ILocationMeme goTo;

	public x_PickupActionThought(ITaskGoal goal) {
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
		if (toAcquire == null && this.childThoughts(ThoughtType.FIND_MEMORY_INFO).isEmpty()) {
			if (oughtToCheckShortTermMemories()) {
				for (MemoryWrapper mw : memory.getMindMemory()
						.getShortTermMemoriesOfType(MemoryCategory.REMEMBER_FOR_PURPOSE)) {
					if (mw.getMemory() instanceof ImportantWorldObjectsMemory iwom) {
						Property prop = iwom.getProperty();
						if (prop.equals(toAcquireMeme)) {
							IUniqueExistence ex = iwom.getObjects().iterator().next();
							if (ex instanceof Actor) toAcquire = (Actor) ex;
						} else if (toAcquireMeme instanceof IProfile ip) {
							for (IUniqueExistence ex : iwom.getObjects()) {
								if (ex.getUUID().equals(ip.getUUID()) && ex instanceof Actor) {
									toAcquire = (Actor) ex;
									break;
								}
							}
						}
					}
				}
			}
			if (toAcquire == null) {
				if (toAcquireProfile != null) {
					this.postChildThought(new CheckSensedActorsThought(toAcquireProfile, this.getGoal()), ticks);
				} else if (property != null) {
					this.postChildThought(new CheckSensedActorsThought(property,
							(a) -> memory.getAsHasActor().getActor().reachable(a), this.getGoal()), ticks);
				} else {
					this.postChildThought(new CheckSensedActorsThought(profile, this.getGoal()), ticks);

				}
				this.markAddedShortTermMemories();
			}
		} else if (toAcquire != null) {
			if (toAcquire.isRemoved()
					|| toAcquire.getHeld() != null && toAcquire.getHeld().equals(memory.getAsHasActor().getActor())) {
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
	public void getInfoFromChild(ICanThink memory, IThought childThought, boolean interrupted, int ticks) {
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
						this.goTo = ((Actor) toAcquire).getLocation();
					}
				} else {
					this.postChildThought(new CheckKnownProfilesThought(property, true, null), ticks);
				}
			}
		} else if (childThought instanceof CheckKnownProfilesThought ckat) {
			if (!ckat.getInformation().isEmpty()) {
				Iterator<IProfile> profs = ckat.getInformation().iterator();
				while (goTo == null && profs.hasNext()) {
					IProfile pair = profs.next();
					this.toAcquireProfile = pair;
					this.goTo = memory.getKnowledgeBase()
							.<ILocationMeme>getConceptsWithRelation(pair, ConceptRelationType.FOUND_AT).iterator()
							.next();
				}
				if (goTo != null) {
					this.postConditionForExecution(new TravelTaskGoal(goTo, true));
				}
			} else {
				this.failPreemptively();
			}
		}
	}

	@Override
	public boolean canExecuteIndividual(ICanThink user, int thoughtTicks, long worldTicks) {
		return (toAcquire instanceof Actor && user.getAsHasActor().getActor().reachable((Actor)toAcquire));
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

}
