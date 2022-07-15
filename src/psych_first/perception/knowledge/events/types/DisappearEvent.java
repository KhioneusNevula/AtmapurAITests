package psych_first.perception.knowledge.events.types;

import psych_first.perception.knowledge.events.EventType;
import psych_first.perception.knowledge.events.RelationType;
import sim.IHasProfile;
import sim.ILocatable;
import sim.World;

public class DisappearEvent extends AbstractTravelEvent {

	public DisappearEvent(World inWorld, long startTime, IHasProfile traveler, ILocatable to, ILocatable from) {
		super(EventType.DISAPPEAR, inWorld, startTime, traveler, to, from);
	}

	public DisappearEvent addCauser(IHasProfile causer) {
		this.addRelationshipToEvent(RelationType.BECAUSE_OF, causer);

		return this;
	}

	public DisappearEvent addTool(IHasProfile tool, IHasProfile... users) {
		for (IHasProfile user : users) {
			this.addRelationship(user, RelationType.USING, tool);
		}
		return this;
	}

}
