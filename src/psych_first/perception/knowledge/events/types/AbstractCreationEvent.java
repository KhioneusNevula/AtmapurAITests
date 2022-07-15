package psych_first.perception.knowledge.events.types;

import psych_first.perception.knowledge.events.AbstractEvent;
import psych_first.perception.knowledge.events.EventType;
import psych_first.perception.knowledge.events.RelationType;
import sim.IHasProfile;
import sim.World;

public class AbstractCreationEvent extends AbstractEvent {

	private IHasProfile creation;

	private IHasProfile creator;

	/**
	 * 
	 * @param eventType
	 * @param inWorld
	 * @param startTime
	 * @param creation
	 * @param creator   can be null
	 */
	public AbstractCreationEvent(EventType<?> eventType, World inWorld, long startTime, IHasProfile creation,
			IHasProfile creator) {
		super(eventType, inWorld, startTime);
		this.creation = creation;
		this.creator = creator;
		this.addRelationshipToEvent(RelationType.OCCURRING_TO, creation);
		this.addRelationshipToEvent(creation, RelationType.BECAUSE_OF);
		if (creator != null) {
			this.addRelationshipToEvent(RelationType.BECAUSE_OF, creator);
			this.addRelationship(creation, RelationType.BECAUSE_OF, creator);

		}
	}

	public IHasProfile getCreation() {
		return creation;
	}

	public IHasProfile getCreator() {
		return creator;
	}

	public boolean hasCreator() {
		return creator != null;
	}

	/**
	 * @param creator
	 * @return
	 */
	protected AbstractCreationEvent addCreator(IHasProfile creator) {
		this.addRelationshipToEvent(RelationType.BECAUSE_OF, creator);

		for (IHasProfile creation : this.getProfiles(RelationType.BECAUSE_OF, false)) {

			this.addRelationship(creation, RelationType.BECAUSE_OF, creator);
		}
		return this;
	}

}
