package psych_first.perception.knowledge.events.types;

import psych_first.perception.knowledge.events.AbstractEvent;
import psych_first.perception.knowledge.events.EventType;
import psych_first.perception.knowledge.events.RelationType;
import sim.IHasProfile;
import sim.World;

public abstract class AbstractSocialEvent extends AbstractEvent {

	private IHasProfile initiator;
	private IHasProfile recipient;

	/**
	 * 
	 * @param eventType
	 * @param inWorld
	 * @param startTime
	 * @param initiator the initiator of this event; must be able to have a mind
	 * @param recipient the recipient of this event; must be able to have a mind
	 */
	public AbstractSocialEvent(EventType<?> eventType, World inWorld, long startTime, IHasProfile initiator,
			IHasProfile recipient) {
		super(eventType, inWorld, startTime);

		this.addRelationshipToEvent(RelationType.BECAUSE_OF, initiator);
		this.addRelationshipToEvent(RelationType.OCCURRING_TO, recipient);

	}

	public IHasProfile getInitiator() {
		return initiator;
	}

	public IHasProfile getRecipient() {
		return recipient;
	}

	public AbstractSocialEvent addParticipant(IHasProfile part) {

		this.addRelationshipToEvent(RelationType.OCCURRING_TO, part);
		return this;
	}

}
