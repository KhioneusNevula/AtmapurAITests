package psych_first.perception.knowledge.events.types;

import psych_first.perception.knowledge.events.AbstractEvent;
import psych_first.perception.knowledge.events.EventType;
import psych_first.perception.knowledge.events.RelationType;
import sim.IHasProfile;
import sim.World;

public abstract class AbstractHarmEvent extends AbstractEvent {

	private IHasProfile primaryCause;

	private IHasProfile victim;

	/**
	 * last two params can be null
	 * 
	 * @param inWorld
	 * @param startTime
	 * @param harmed        the entity harmed
	 * @param primaryHarmer the entity which harmed, or null if an impersonal event
	 *                      caused harm
	 * @param isPresent     if the harmer was present for the death or causing it
	 *                      remotely
	 */
	public AbstractHarmEvent(EventType<? extends AbstractHarmEvent> type, World inWorld, long startTime,
			IHasProfile harmed, IHasProfile primaryHarmer, boolean isPresent) {
		super(type, inWorld, startTime);
		this.addRelationshipToEvent(RelationType.OCCURRING_TO, harmed);
		this.victim = harmed;
		if (primaryHarmer != null) {
			this.addRelationshipToEvent(RelationType.BECAUSE_OF, primaryHarmer);
			if (isPresent) {
				this.addRelationship(harmed, RelationType.PRESENT_WITH, primaryHarmer);
				this.addRelationshipToEvent(primaryHarmer, RelationType.AT);
			}
			this.primaryCause = primaryHarmer;
		}
	}

	public IHasProfile getPrimaryCause() {
		return primaryCause;
	}

	public IHasProfile getVictim() {
		return victim;
	}

	public AbstractHarmEvent addAccomplice(IHasProfile accomplice, boolean isPresent) {
		this.addRelationshipToEvent(RelationType.BECAUSE_OF, accomplice);
		if (isPresent) {
			this.addRelationship(accomplice, RelationType.PRESENT_WITH, this.victim);
			if (this.hasRelation(this.primaryCause, RelationType.PRESENT_WITH, this.victim)) {
				this.addRelationship(accomplice, RelationType.PRESENT_WITH, this.primaryCause);
				this.addRelationshipToEvent(accomplice, RelationType.AT);
			}

		}
		return this;
	}

	/**
	 * adds a tool used to kill; isPresent indicates if the killer had it with them
	 * or not
	 * 
	 * @param tool
	 * @param user
	 * @param isPresent
	 * @return
	 */
	public AbstractHarmEvent addTool(IHasProfile tool, IHasProfile user, boolean isPresent) {
		this.addRelationship(user, RelationType.USING, tool);
		if (isPresent)
			this.addRelationship(tool, RelationType.PRESENT_WITH, user);

		if (isPresent && this.hasRelation(user, RelationType.AT, false)) {
			this.addRelationshipToEvent(tool, RelationType.AT);
		}
		return this;
	}

}
