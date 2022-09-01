package psych_first.perception.knowledge.events.types;

import psych_first.perception.knowledge.events.EventType;
import sim.IHasProfile;
import sim.World;

public class TakeEvent extends AbstractGiveTakeEvent {

	/**
	 * 
	 * @param eventType
	 * @param inWorld
	 * @param startTime
	 * @param from
	 * @param taker
	 * @param taken
	 */
	public TakeEvent(World inWorld, long startTime, IHasProfile from, IHasProfile taker, IHasProfile taken) {
		super(EventType.TAKE, inWorld, startTime, from, taker, 1);
		this.addItem(taken);
	}

	public AbstractGiveTakeEvent addItem(IHasProfile item) {
		return super.addItem(item);
	}

}
