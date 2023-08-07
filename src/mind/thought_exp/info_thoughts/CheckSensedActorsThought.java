package mind.thought_exp.info_thoughts;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.function.Predicate;

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

public class CheckSensedActorsThought extends AbstractInformationThought<Collection<Actor>> {

	private Profile profile;
	private Property property;
	private String failure;
	private Predicate<Actor> predicate;

	public CheckSensedActorsThought(Profile profile) {
		this.profile = profile;
	}

	public CheckSensedActorsThought(Property property, Predicate<Actor> predicate) {
		this.property = property;
		this.predicate = predicate;
	}

	public CheckSensedActorsThought(Property property) {
		this(property, (a) -> true);
	}

	public CheckSensedActorsThought(Predicate<Actor> actor) {
		this(Property.ANY, actor);
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
			return;
		}

		Iterator<Actor> actorIterator = actors.iterator();
		failure = "nothing is there";
		for (int i = 0; actorIterator.hasNext() && i < mind.getMaxFocusObjects();) {
			Actor actor = actorIterator.next();
			if (property != null) {
				IPropertyData dat = ITaskGoal.getProperty(actor, property, mind);
				failure = "nothing with property";

				if (dat.isPresent())
					if (predicate.test(actor)) {
						information.add(actor);
					}
			} else {
				/**
				 * TODO use recognition abilities with this
				 */
				if (this.profile.getUUID().equals(actor.getUUID())) {
					information.add(actor);
					break;
				}
			}
			i++;

		}
		if (!information.isEmpty())
			failure = "n/a";
		else
			failure = "nothing found";

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
		return "identify entities with " + (profile != null ? "profile " + profile : "property " + property);
	}

	@Override
	public boolean equivalent(IThought other) {
		if (other instanceof CheckSensedActorsThought chifp)
			return (this.profile != null ? chifp.profile != null && this.profile.equals(chifp.profile)
					: chifp.profile == null)
					&& (this.property != null ? chifp.property != null && this.property.equals(chifp.property)
							: chifp.property == null);
		return false;
	}

}
