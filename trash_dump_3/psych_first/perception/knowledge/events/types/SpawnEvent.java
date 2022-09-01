package psych_first.perception.knowledge.events.types;

import psych_first.perception.knowledge.events.EventType;
import sim.IHasProfile;
import sim.World;

/**
 * might not get used. technically, entities shouldn't actually witness a
 * spawning
 * 
 * @author borah
 *
 */
public class SpawnEvent extends AbstractCreationEvent {

	/**
	 * 
	 * @param eventType
	 * @param inWorld
	 * @param startTime
	 * @param spawned   that which was spawned
	 */
	public SpawnEvent(World inWorld, long startTime, IHasProfile spawned) {
		super(EventType.SPAWN, inWorld, startTime, spawned, null);
	}

}
