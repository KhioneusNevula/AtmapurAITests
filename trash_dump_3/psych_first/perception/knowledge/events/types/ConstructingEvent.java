package psych_first.perception.knowledge.events.types;

import psych_first.perception.knowledge.events.EventType;
import psych_first.perception.knowledge.events.RelationType;
import sim.IHasProfile;
import sim.World;

public class ConstructingEvent extends AbstractCreationEvent {

	public ConstructingEvent(World inWorld, long startTime, IHasProfile creation, IHasProfile creator) {
		super(EventType.CONSTRUCT, inWorld, startTime, creation, creator);
	}

	@Override
	public ConstructingEvent addCreator(IHasProfile creator) {
		return (ConstructingEvent) super.addCreator(creator);
	}

	/**
	 * 
	 * @param tool   the tool used
	 * @param usedBy those the tool is used by
	 * @return
	 */
	public ConstructingEvent addTool(IHasProfile tool, IHasProfile... usedBy) {
		for (IHasProfile user : usedBy) {
			this.addRelationship(user, RelationType.USING, tool);
		}
		return this;
	}

}
