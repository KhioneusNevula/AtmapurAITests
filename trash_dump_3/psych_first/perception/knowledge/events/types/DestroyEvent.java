package psych_first.perception.knowledge.events.types;

import psych_first.perception.knowledge.events.EventType;
import sim.IHasProfile;
import sim.World;

public class DestroyEvent extends AbstractHarmEvent {

	public DestroyEvent(World inWorld, long startTime, IHasProfile destroyed, IHasProfile primaryDestroyer,
			boolean isPresent) {
		super(EventType.DESTROY, inWorld, startTime, destroyed, primaryDestroyer, isPresent);
	}

	public IHasProfile getDestroyed() {
		return super.getVictim();
	}

	public IHasProfile getDestroyer() {
		return super.getPrimaryCause();
	}

}
