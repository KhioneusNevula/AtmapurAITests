package psych_first.perception.knowledge.events.types;

import psych_first.perception.knowledge.events.AbstractEvent;
import psych_first.perception.knowledge.events.EventType;
import sim.World;

/**
 * TODO flesh out interactionevent class
 * 
 * @author borah
 *
 */
public abstract class AbstractInteractionEvent extends AbstractEvent {

	public AbstractInteractionEvent(EventType<?> eventType, World inWorld, long startTime) {
		super(eventType, inWorld, startTime);
	}

}
