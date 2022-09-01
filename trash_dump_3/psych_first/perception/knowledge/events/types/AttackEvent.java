package psych_first.perception.knowledge.events.types;

import java.util.Objects;

import psych_first.perception.knowledge.events.EventType;
import sim.IHasProfile;
import sim.World;

public class AttackEvent extends AbstractHarmEvent {

	private double damage;

	/**
	 * 
	 * @param eventType
	 * @param inWorld
	 * @param startTime
	 * @param attacked  should be alive
	 * @param attacker  should not be null
	 * @param isPresent whether the attacker was present or if they were attacking
	 *                  remotely
	 */
	public AttackEvent(World inWorld, long startTime, IHasProfile attacked, IHasProfile attacker, boolean isPresent,
			double damage) {
		super(EventType.ATTACK, inWorld, startTime, attacked, Objects.requireNonNull(attacker), isPresent);

		this.damage = damage;

	}

	public double getDamage() {
		return damage;
	}

	public IHasProfile getAttacker() {
		return super.getPrimaryCause();
	}

	public IHasProfile getAttacked() {
		return super.getVictim();
	}

}
