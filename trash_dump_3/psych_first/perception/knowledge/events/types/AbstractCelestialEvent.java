package psych_first.perception.knowledge.events.types;

import psych_first.perception.knowledge.events.AbstractEvent;
import psych_first.perception.knowledge.events.EventType;
import psych_first.perception.knowledge.events.RelationType;
import sim.IHasProfile;
import sim.World;

public abstract class AbstractCelestialEvent extends AbstractEvent {

	private IHasProfile celestialBody;

	public AbstractCelestialEvent(EventType<? extends AbstractCelestialEvent> eventType, World inWorld, long startTime,
			IHasProfile celestialBody) {
		super(eventType, inWorld, startTime);
		this.celestialBody = celestialBody;
		this.addRelationshipToEvent(RelationType.OCCURRING_TO, celestialBody);

	}

	public IHasProfile getCelestialBody() {
		return celestialBody;
	}

}
