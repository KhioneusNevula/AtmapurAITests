package psych_first.perception.knowledge.events.types;

import psych_first.perception.knowledge.events.AbstractEvent;
import psych_first.perception.knowledge.events.EventType;
import psych_first.perception.knowledge.events.RelationType;
import sim.IHasProfile;
import sim.World;
import sociology.Profile;

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
public class AbstractGiveTakeEvent extends AbstractEvent {

	private IHasProfile giver;
	private IHasProfile receiver;
	private boolean mutual;

	/**
	 * transfer some number of (later decided) items from the given profile to the
	 * other given profile
	 * 
	 * @param eventType
	 * @param inWorld
	 * @param startTime
	 * @param giver     who the item is FROM
	 * @param receiver  who the item is given TO, and therefore who the event is FOR
	 * @param taken     0 if the item was given by the giver, 1 if the item was
	 *                  taken by the receiver, 2 if it was a mutual trade (assumes
	 *                  multiple items were involved)
	 */
	public AbstractGiveTakeEvent(EventType<?> eventType, World inWorld, long startTime, IHasProfile giver,
			IHasProfile receiver, int taken) {
		super(eventType, inWorld, startTime);
		this.giver = giver;
		this.receiver = receiver;
		switch (taken) {
		case 0:
			this.addRelationshipToEvent(RelationType.BECAUSE_OF, giver);
			this.addRelationshipToEvent(RelationType.FOR, receiver);
			break;
		case 1:
			this.addRelationshipToEvent(RelationType.FOR, giver);
			this.addRelationshipToEvent(RelationType.BECAUSE_OF, receiver);
			break;
		case 2:
			this.addRelationshipToEvent(RelationType.FOR, giver);
			this.addRelationshipToEvent(RelationType.BECAUSE_OF, receiver);
			this.addRelationshipToEvent(RelationType.BECAUSE_OF, giver);
			this.addRelationshipToEvent(RelationType.FOR, receiver);
			mutual = true;
			break;
		default:
			throw new IllegalArgumentException(
					taken + " " + giver + " " + receiver + " " + this.getEventType().getEventID());
		}
	}

	/**
	 * the receiver
	 * 
	 * @return
	 */
	public IHasProfile getGiver() {
		return giver;
	}

	/**
	 * the giver
	 * 
	 * @return
	 */
	public IHasProfile getReceiver() {
		return receiver;
	}

	/**
	 * if the giving/receiving was mutual (multiple items must therefore have been
	 * involved)
	 * 
	 * @return
	 */
	public boolean wasMutual() {
		return mutual;
	}

	/**
	 * adds an item; true if it is given TO the receiver FROM the giver, false if
	 * given FROM the giver TO the receiver; if from receiver to giver, then
	 * mutuality is assumed
	 * 
	 * @param item
	 * @param givenTo
	 * @return
	 */
	protected AbstractGiveTakeEvent addItem(IHasProfile item, boolean toReceiver) {
		if (!toReceiver && !mutual)
			throw new IllegalStateException("not a mutual event");
		IHasProfile giver = toReceiver ? this.giver : this.receiver;
		IHasProfile receiver = toReceiver ? this.receiver : this.giver;
		this.addRelationshipToEvent(RelationType.OCCURRING_TO, item);
		this.addRelationship(item, RelationType.TO, receiver);
		this.addRelationship(item, RelationType.FROM, giver);
		return this;
	}

	/**
	 * same as {@link AbstractGiveTakeEvent#addItem(Profile, boolean)} but assuming
	 * that the item only goes from giver to receiver
	 * 
	 * @param item
	 * @return
	 */
	protected AbstractGiveTakeEvent addItem(IHasProfile item) {
		return this.addItem(item, true);
	}

}
