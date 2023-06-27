package mind.action.types;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import actor.Actor;
import mind.ICanAct;
import mind.action.ActionType;
import mind.action.IAction;
import mind.concepts.type.IMeme;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.goals.ITaskGoal;
import mind.goals.question.Question;
import mind.goals.taskgoals.LearnTaskGoal;
import mind.goals.taskgoals.TravelTaskGoal;
import sim.Location;

public class PickUpAction implements IAction {

	private IMeme acquireWhat;
	private Actor specificTarget;
	private Profile nearestTarget;
	private Actor nearestActorTarget;
	private Location targetLoc;
	private Actor body;
	private boolean success;
	private String failure = "n/a";
	private boolean handsFull;

	public PickUpAction(ITaskGoal goal) {
		this(goal.transferItem());
	}

	public PickUpAction(IMeme acquireWhat) {
		this.acquireWhat = acquireWhat;
	}

	public IMeme getAcquireTarget() {
		return acquireWhat;
	}

	private boolean findTargetsAndStuff(ICanAct user, boolean pondering) {

		if (acquireWhat instanceof Profile) {
			Profile profile = (Profile) acquireWhat;
			this.nearestTarget = profile;
			this.targetLoc = user.getKnowledgeBase().getLocation(profile);
			Actor ac = user.getMindMemory().getSenses().getActorFor(profile);
			if (ac != null && user.getAsHasActor().getActor().reachable(targetLoc)) {
				this.specificTarget = ac;
				this.nearestActorTarget = ac;
				failure = "success";
				return true;
			}
			failure = "unreachable specific target " + targetLoc + " " + profile + " pondering=" + pondering;
			return false;
		} else if (acquireWhat instanceof Property) {
			Property property = (Property) acquireWhat;
			// how to memory hmm
			Collection<Profile> profiles = user.getMindMemory().getSenses().getAllSensedProfiles();
			Location ownLoc = user.getMindMemory().getLocation(user.getMindMemory().getSelfProfile());
			if (ownLoc == null)
				return false;
			failure = "pondering=" + pondering + ", no actor can be sensed";
			for (Profile prof : profiles) {
				Actor actor = user.getMindMemory().getSenses().getActorFor(prof);
				Location loc = user.getMindMemory().getLocation(prof);
				if (loc == null)
					loc = user.getMindMemory().getLocationsFromCulture(prof).values().stream().findAny().orElse(null);
				if (loc == null)
					continue;// loc = actor.getLocation();
				failure = "pondering=" + pondering + ", no instances of " + property + " can be sensed " + actor;
				if (actor != null && (ITaskGoal.getProperty(actor, property, user).isPresent()
						|| user.getMindMemory().hasProperty(prof, property))) {
					if (this.targetLoc == null || this.targetLoc.distance(ownLoc) > loc.distance(ownLoc)) {
						/*
						 * if (user.getAsHasActor().getActor().getName().equals("bobzy"))
						 * System.out.println(actor + " " + property + " " + pondering);
						 */
						this.nearestActorTarget = actor;
						this.nearestTarget = prof;
						this.targetLoc = loc;
						if (user.getAsHasActor().getActor().reachable(loc)) {
							this.specificTarget = actor;
							if (actor.isRemoved())
								continue;
							failure = "success";
							return true;
						}
						failure = "pondering=" + pondering + ", unreachable target " + targetLoc + " "
								+ (nearestActorTarget == null ? nearestTarget : nearestActorTarget) + " from senses";
					}

				}

			}
			if (nearestActorTarget == null) {
				profiles = new TreeSet<>(user.getKnowledgeBase().getProfilesWithProperty(property));
				profiles.addAll(user.getMindMemory().getProfilesWithPropertyFromCulture(property).values());
				failure += " and nothing is remembered";
				for (Profile prof : profiles) {
					Location loc = user.getMindMemory().getLocation(prof);
					if (loc == null)
						loc = user.getMindMemory().getLocationsFromCulture(prof).values().stream().findAny()
								.orElse(null);
					if (loc == null)
						continue;
					if (nearestTarget == null || this.targetLoc.distance(ownLoc) > loc.distance(ownLoc)) {
						this.nearestTarget = prof;
						this.targetLoc = loc;
						failure = "pondering=" + pondering + ", unreachable target " + loc + " from memory";
					}
				}
			}
			return false;

		}
		throw new IllegalStateException(acquireWhat.getClass().getName());
	}

	@Override
	public boolean canExecuteIndividual(ICanAct user, boolean pondering) {
		// currently, if pondering it can check its memory, but not if it is not in the
		// pondering stage
		body = user.getAsHasActor().getActor();
		if (body.getHeld() != null) {
			handsFull = true;
			return false;
		}
		if (pondering) {
			return this.findTargetsAndStuff(user, pondering);
		} else {
			if (specificTarget != null && this.body.reachable(specificTarget)) {
				if (specificTarget.isRemoved())
					return false;
				failure = "success";
				return true;
			} else if (this.nearestActorTarget != null && this.body.reachable(nearestActorTarget)) {
				failure = "success";
				if (nearestActorTarget.isRemoved())
					return false;
				this.specificTarget = nearestActorTarget;
				return true;
			} else {
				return this.findTargetsAndStuff(user, pondering);
			}
		}
	}

	@Override
	public void beginExecutingIndividual(ICanAct forUser) {
		success = body.pickUp(specificTarget);
	}

	@Override
	public boolean finishActionIndividual(ICanAct individual, int tick) {
		return success;
	}

	@Override
	public Collection<ITaskGoal> genConditionGoal(ICanAct user) {
		if (handsFull) {
			return Set.of();
		}
		if (targetLoc == null) {
			return Set.of(new LearnTaskGoal(Question.askLocation(acquireWhat)));
		}
		return Set.of(new TravelTaskGoal(targetLoc, true));
	}

	@Override
	public ActionType<?> getType() {
		return ActionType.PICK_UP;
	}

	@Override
	public String reasonForLastFailure() {
		return failure;
	}

	@Override
	public String toString() {
		return "PickupA{" + this.acquireWhat + (this.targetLoc != null ? ",at:" + this.targetLoc : "")
				+ (this.nearestActorTarget != null ? ",a:" + this.nearestActorTarget
						: (this.nearestTarget != null ? ",a:" + this.nearestTarget : ""))
				+ "}";
	}

}
