package psych_first.perception.knowledge.events.types;

import psych_first.perception.knowledge.events.EventType;
import sim.IHasProfile;
import sim.World;

public class PickupEvent extends AbstractTransferItemEvent {

	public PickupEvent(World inWorld, long startTime, IHasProfile initiator, IHasProfile item) {
		super(EventType.PICKUP, inWorld, startTime, initiator, item, false);
	}

	public PickupEvent addItem(IHasProfile item) {
		return (PickupEvent) this.addItem(item, false);
	}

}
