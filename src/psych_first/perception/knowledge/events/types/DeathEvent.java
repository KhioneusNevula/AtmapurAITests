package psych_first.perception.knowledge.events.types;

import psych_first.perception.knowledge.events.EventType;
import sim.IHasProfile;
import sim.World;

public class DeathEvent extends AbstractHarmEvent {

	/**
	 * last two params can be null
	 * 
	 * @param inWorld
	 * @param startTime
	 * @param killed        the entity killed; must be living
	 * @param primaryKiller the entity which killed, or null if an impersonal event
	 *                      caused the death
	 * @param isPresent     if the killer was present for the death or causing it
	 *                      remotely
	 */
	public DeathEvent(World inWorld, long startTime, IHasProfile killed, IHasProfile primaryKiller, boolean isPresent) {
		super(EventType.DEATH, inWorld, startTime, killed, primaryKiller, isPresent);

	}

	public IHasProfile getPrimaryKiller() {
		return this.getPrimaryCause();
	}

	public boolean hasKiller() {
		return getPrimaryKiller() != null;
	}

}
