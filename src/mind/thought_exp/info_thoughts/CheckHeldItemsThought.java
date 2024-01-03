package mind.thought_exp.info_thoughts;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

import actor.Actor;
import mind.concepts.type.IMeme;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.goals.IGoal.Priority;
import mind.goals.ITaskGoal;
import mind.memory.IPropertyData;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;
import mind.thought_exp.ThoughtType;
import mind.thought_exp.type.AbstractInformationThought;

public class CheckHeldItemsThought extends AbstractInformationThought<Collection<Actor>> {

	private Profile profile;
	private Property property;
	private String failure;

	public CheckHeldItemsThought(Profile profile) {
		this.profile = profile;
	}

	public CheckHeldItemsThought(Property property) {
		this.property = property;
	}

	@Override
	public IMeme thoughtTopic() {
		return profile != null ? profile : property;
	}

	@Override
	public IThoughtType getThoughtType() {
		return ThoughtType.FIND_MEMORY_INFO;
	}

	@Override
	public boolean isLightweight() {
		return true;
	}

	@Override
	public void startThinking(ICanThink mind, long worldTick) {
		information = new TreeSet<>((a, b) -> (int) (mind.getAsHasActor().getActor().distance(a)
				- mind.getAsHasActor().getActor().distance(b)));
		Collection<Actor> actors = mind.senseActors(mind.getAsHasActor().getActor().getWorld(), worldTick);
		if (actors.isEmpty()) {
			failure = "no actors sensed";
			if (mind.getAsHasActor().getActor().getName().equals("bobzy")) {
				System.out.print("");
			}
			return;
		}

		Iterator<Actor> actorIterator = actors.iterator();
		failure = "nothing held";
		for (int i = 0; actorIterator.hasNext() && i < mind.getMaxFocusObjects();) {
			Actor actor = actorIterator.next();
			/*
			 * if (actor instanceof Food && actor.getPossessor() != null &&
			 * mind.getAsHasActor().getActor().getName().equals("bobzy")) {
			 * System.out.print(""); }
			 */
			/*
			 * TODO use the held property
			 *//*
				 * IPropertyData dat = ITaskGoal.getProperty(actor, BasicProperties.HELD, mind);
				 */
			if /*
				 * (dat.isPresent() &&
				 * dat.getProfileProp().equals(mind.getKnowledgeBase().getSelfProfile()))
				 */
			(actor.getPossessor() != null && actor.getPossessor().getUUID().equals(mind.getUUID())) {
				if (property != null) {
					IPropertyData dat = ITaskGoal.getProperty(actor, property, mind);
					failure = "not holding food";
					if (dat.isPresent()) {
						information.add(actor);
					}
				} else {
					if (actor.getUUID().equals(profile.getUUID())) {
						information.add(actor);
						break;
					}
				}
				i++;
			}
		}
		if (!information.isEmpty())
			failure = "n/a";

	}

	@Override
	public boolean isFinished(ICanThink memory, int ticks, long worldTick) {
		return true;
	}

	@Override
	public Priority getPriority() {
		return Priority.TRIVIAL;
	}

	public String getFailureReason() {
		return failure;
	}

	@Override
	public void thinkTick(ICanThink memory, int ticks, long worldTick) {

	}

	@Override
	public void getInfoFromChild(IThought childThought, boolean interrupted, int ticks) {

	}

	@Override
	public String displayText() {
		return "identify held entities with " + (profile != null ? "profile " + profile : "property " + property);
	}

	@Override
	public boolean equivalent(IThought other) {
		if (other instanceof CheckHeldItemsThought chifp)
			return (this.profile != null ? chifp.profile != null && this.profile.equals(chifp.profile)
					: chifp.profile == null)
					&& (this.property != null ? chifp.property != null && this.property.equals(chifp.property)
							: chifp.property == null);
		return false;
	}

}
