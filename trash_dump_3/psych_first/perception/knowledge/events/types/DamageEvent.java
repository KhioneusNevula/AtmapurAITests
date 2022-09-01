package psych_first.perception.knowledge.events.types;

import psych_first.perception.knowledge.events.EventType;
import sim.IHasProfile;
import sim.World;

public class DamageEvent extends AbstractHarmEvent {

	private double damage;

	public DamageEvent(World inWorld, long startTime, IHasProfile damaged, IHasProfile damager, boolean isPresent,
			double damage) {
		super(EventType.DAMAGE, inWorld, startTime, damaged, damager, isPresent);
		this.damage = damage;
	}

	public double getDamage() {
		return damage;
	}

}
