package psych_first.perception.knowledge.events.types;

import psych_first.perception.knowledge.events.AbstractEvent;
import psych_first.perception.knowledge.events.EventType;
import psych_first.perception.knowledge.events.RelationType;
import sim.IHasProfile;
import sim.ILocatable;
import sim.World;

public class AbstractTravelEvent extends AbstractEvent {

	private ILocatable destination;
	private ILocatable origin;
	private IHasProfile traveler;

	/**
	 * 
	 * @param eventType
	 * @param inWorld
	 * @param startTime
	 * @param traveler  that which is traveling
	 * @param to        where to; must be a locatable/location
	 * @param from      where from; must be a locatable/location
	 */
	public AbstractTravelEvent(EventType<?> eventType, World inWorld, long startTime, IHasProfile traveler,
			ILocatable to, ILocatable from) {
		super(eventType, inWorld, startTime);

		this.origin = from;
		this.traveler = traveler;
		this.destination = to;
		this.addRelationshipToEvent(RelationType.OCCURRING_TO, traveler);
		if (to instanceof IHasProfile ipto && from instanceof IHasProfile ipfrom) {
			this.addRelationship(traveler, RelationType.TO, ipto);
			this.addRelationship(traveler, RelationType.FROM, ipfrom);
		} else {
			throw new IllegalArgumentException(to + " " + from);
		}
	}

	public IHasProfile getTraveler() {
		return traveler;
	}

	public ILocatable getDestination() {
		return destination;
	}

	public ILocatable getOrigin() {
		return origin;
	}

}
