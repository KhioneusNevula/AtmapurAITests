package psych_first.perception.knowledge.events.types;

import psych_first.perception.knowledge.events.EventType;
import psych_first.perception.knowledge.events.RelationType;
import sim.IHasProfile;
import sim.World;

public class SproutEvent extends AbstractCreationEvent {

	private IHasProfile seed;

	/**
	 * 
	 * @param eventType
	 * @param inWorld
	 * @param startTime
	 * @param plant
	 * @param cultivator the farmer/whoever; can be null
	 * @param seed       the seed which this plant sprouts from
	 */
	public SproutEvent(World inWorld, long startTime, IHasProfile plant, IHasProfile cultivator, IHasProfile seed) {
		super(EventType.SPROUT, inWorld, startTime, plant, cultivator);
		this.seed = seed;

		this.addRelationship(plant, RelationType.FROM, seed);

	}

	public IHasProfile getSeed() {
		return seed;
	}

	public SproutEvent addCultivator(IHasProfile creator) {
		return (SproutEvent) super.addCreator(creator);
	}

	public SproutEvent addTool(IHasProfile tool, IHasProfile... usedBy) {
		for (IHasProfile user : usedBy) {
			this.addRelationship(user, RelationType.USING, tool);
		}
		return this;
	}

}
