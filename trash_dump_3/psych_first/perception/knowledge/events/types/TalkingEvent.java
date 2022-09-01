package psych_first.perception.knowledge.events.types;

import psych_first.perception.knowledge.events.EventType;
import psych_first.perception.knowledge.events.RelationType;
import sim.IHasProfile;
import sim.World;

public class TalkingEvent extends AbstractSocialEvent {

	private IHasProfile primaryTopic;

	/**
	 * 
	 * @param eventType
	 * @param inWorld
	 * @param startTime
	 * @param initiator
	 * @param recipient
	 * @param topic     this would be the topic of conversation
	 */
	public TalkingEvent(EventType<?> eventType, World inWorld, long startTime, IHasProfile initiator,
			IHasProfile recipient, IHasProfile topic) {
		super(eventType, inWorld, startTime, initiator, recipient);
		this.addRelationshipToEvent(RelationType.ABOUT, topic);
		this.primaryTopic = topic;
	}

	public IHasProfile getPrimaryTopic() {
		return primaryTopic;
	}

	public TalkingEvent addTopic(IHasProfile topic) {
		this.addRelationshipToEvent(RelationType.ABOUT, topic);
		return this;
	}

}
