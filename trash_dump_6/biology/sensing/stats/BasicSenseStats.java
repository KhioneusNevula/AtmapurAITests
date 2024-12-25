package biology.sensing.stats;

import java.util.Collection;
import java.util.Collections;

import actor.construction.properties.AbilityStat;
import sim.physicality.ExistencePlane;
import sim.physicality.IInteractability;

/**
 * The standard/basic statistics for senses
 * 
 * @author borah
 *
 */
public final class BasicSenseStats {

	private BasicSenseStats() {
	}

	/**
	 * Range of sensing for general sensing
	 */
	public static final AbilityStat<Integer> DISTANCE = new AbilityStat<>("distance", "sense", Integer.class, 0);
	/**
	 * What planes of existence one can generally sense on
	 */
	public static final AbilityStat<Collection<IInteractability>> EXISTENCE_PLANES = new AbilityStat<Collection<IInteractability>>(
			"planes", "sense", Collection.class, Collections.singleton(ExistencePlane.PHYSICAL));

	/**
	 * How well this vision can penetrate obscuring clouds; 1.0f means complete
	 * penetration, 0.0f means standard penetration
	 */
	public static final AbilityStat<Float> CLOUD_PENETRATION = new AbilityStat<>("cloud_penetration", "sight",
			Float.class, 0.0f);
	/**
	 * How well this vision can penetrate darkness; 1.0f means complete penetration,
	 * 0.0f means standard penetration
	 */
	public static final AbilityStat<Float> DARKVISION = new AbilityStat<>("darkvision", "sight", Float.class, 0.0f);

	/**
	 * How well this sense can penetrate through solid things, e.g. x-ray vision for
	 * sight, or how well you can hear a heartbeat (For example). TODO figure out
	 * how materials determine this stat
	 */
	public static final AbilityStat<Float> MATERIAL_PENETRATION = new AbilityStat<>("material_penetration", "sense",
			Float.class, 0.0f);
}
