package psych_first.perception.knowledge.events.types;

import psych_first.perception.knowledge.events.AbstractEvent;
import psych_first.perception.knowledge.events.EventType;
import psych_first.perception.knowledge.events.RelationType;
import sim.IHasProfile;
import sim.World;

/**
 * 
 * {@link RelationType#BECAUSE_OF BECAUSE_OF} -> that which is giving/taking<br>
 * 
 * {@link RelationType#FOR FOR}-> that to which is given/taken<br>
 * {@link RelationType#OCCURRING_TO OCCURRED_TO} -> that which is being
 * exchanged<br>
 * that which is being exchanged {@link RelationType#FROM FROM} that which is
 * giving<br>
 * that which is being exchanged {@link RelationType#TO TO} that which is
 * taking/receiving<br>
 * 
 * @author borah
 *
 */
public class AbstractTransferItemEvent extends AbstractEvent {

	private IHasProfile initiator;

	/**
	 * transfer some number of (later decided) items from the given profile to or
	 * from the "ground"
	 * 
	 * @param eventType
	 * @param inWorld
	 * @param startTime
	 * @param initiator
	 */
	public AbstractTransferItemEvent(EventType<?> eventType, World inWorld, long startTime, IHasProfile initiator,
			IHasProfile item, boolean dropped) {
		super(eventType, inWorld, startTime);
		this.initiator = initiator;

		this.addRelationshipToEvent(RelationType.BECAUSE_OF, initiator);
		this.addItem(item, dropped);

	}

	/**
	 * the giver
	 * 
	 * @return
	 */
	public IHasProfile getInitiator() {
		return initiator;
	}

	/**
	 * adds an item; true if it is dropped, false if it is picked up
	 * 
	 * @param item
	 * @param givenTo
	 * @return
	 */
	protected AbstractTransferItemEvent addItem(IHasProfile item, boolean dropped) {

		this.addRelationshipToEvent(RelationType.OCCURRING_TO, item);
		if (dropped) {
			this.addRelationship(item, RelationType.FROM, initiator);
		} else {
			this.addRelationship(item, RelationType.TO, initiator);
		}
		return this;
	}

}
