package psych_first.perception.knowledge.events.types;

import psych_first.perception.knowledge.events.EventType;
import psych_first.perception.knowledge.events.RelationType;
import sim.IHasProfile;
import sim.ILocatable;
import sim.World;

public class AppearEvent extends AbstractTravelEvent {

	public AppearEvent(World inWorld, long startTime, IHasProfile traveler, ILocatable to, ILocatable from) {
		super(EventType.APPEAR, inWorld, startTime, traveler, to, from);
	}

	public AppearEvent addCauser(IHasProfile causer) {
		this.addRelationshipToEvent(RelationType.BECAUSE_OF, causer);

		return this;
	}

	public AppearEvent addTool(IHasProfile tool, IHasProfile... users) {
		for (IHasProfile user : users) {
			this.addRelationship(user, RelationType.USING, tool);
		}
		return this;
	}

}
