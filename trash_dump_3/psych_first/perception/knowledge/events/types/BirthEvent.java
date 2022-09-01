package psych_first.perception.knowledge.events.types;

import psych_first.perception.knowledge.events.EventType;
import psych_first.perception.knowledge.events.RelationType;
import sim.IHasProfile;
import sim.World;

public class BirthEvent extends AbstractCreationEvent {

	private IHasProfile egg;

	/**
	 * 
	 * @param eventType
	 * @param inWorld
	 * @param startTime
	 * @param creation
	 * @param birther   the actual creator, i.e., the birthgiver or the egglayer
	 * @param egg       can be null; the egg which hatched into this organism
	 */
	public BirthEvent(World inWorld, long startTime, IHasProfile creation, IHasProfile birther, IHasProfile egg) {
		super(EventType.BIRTH, inWorld, startTime, creation, birther);
		this.egg = egg;
		if (egg != null) {
			this.addRelationship(birther, RelationType.USING, egg);
			this.addRelationship(creation, RelationType.FROM, egg);
		} else {

			this.addRelationship(creation, RelationType.FROM, birther);
		}
	}

	public IHasProfile getEgg() {
		return egg;
	}

	public boolean hasEgg() {
		return egg != null;
	}

	@Override
	public BirthEvent addCreator(IHasProfile creator) {
		return (BirthEvent) super.addCreator(creator);
	}

}
