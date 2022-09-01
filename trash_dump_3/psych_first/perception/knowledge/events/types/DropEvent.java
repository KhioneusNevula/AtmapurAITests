package psych_first.perception.knowledge.events.types;

import psych_first.perception.knowledge.events.EventType;
import sim.IHasProfile;
import sim.World;

public class DropEvent extends AbstractTransferItemEvent {

	public DropEvent(World inWorld, long startTime, IHasProfile initiator, IHasProfile item) {
		super(EventType.DROP, inWorld, startTime, initiator, item, true);
	}

	public DropEvent addItem(IHasProfile item) {
		return (DropEvent) this.addItem(item, true);
	}

}
