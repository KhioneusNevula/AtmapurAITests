package psych_first.perception.knowledge.events.types;

import psych_first.perception.knowledge.events.EventType;
import sim.IHasProfile;
import sim.World;

public class GiveEvent extends AbstractGiveTakeEvent {

	/**
	 * 
	 * @param eventType
	 * @param inWorld
	 * @param startTime
	 * @param giver
	 * @param receiver
	 * @param gift
	 */
	public GiveEvent(World inWorld, long startTime, IHasProfile giver, IHasProfile receiver, IHasProfile gift) {
		super(EventType.GIVE, inWorld, startTime, giver, receiver, 0);
		this.addItem(gift);
	}

	@Override
	public AbstractGiveTakeEvent addItem(IHasProfile item) {
		return super.addItem(item);
	}

}
