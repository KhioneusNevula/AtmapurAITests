package psych_first.perception.knowledge.events.types;

import psych_first.perception.knowledge.events.EventType;
import sim.IHasProfile;
import sim.World;

public class TradeEvent extends AbstractGiveTakeEvent {

	/**
	 * toP1 and toP2 are the items given to P1 and P2 respectively
	 * 
	 * @param eventType
	 * @param inWorld
	 * @param startTime
	 * @param p1
	 * @param p2
	 * @param toP1      item to p1
	 * @param toP2      item to p2
	 */
	public TradeEvent(World inWorld, long startTime, IHasProfile p1, IHasProfile p2, IHasProfile toP1,
			IHasProfile toP2) {
		super(EventType.TRADE, inWorld, startTime, p1, p2, 2);
		this.addItemToTrader1(toP1);
		this.addItemToTrader2(toP2);

	}

	public IHasProfile getFirstTrader() {
		return this.getGiver();

	}

	public IHasProfile getSecondTrader() {
		return this.getReceiver();
	}

	public TradeEvent addItemToTrader1(IHasProfile item) {
		return (TradeEvent) super.addItem(item, false);
	}

	public TradeEvent addItemToTrader2(IHasProfile item) {
		return (TradeEvent) super.addItem(item, true);
	}

}
