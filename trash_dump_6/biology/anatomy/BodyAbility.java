package biology.anatomy;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import actor.construction.physical.IPartAbility;
import actor.construction.properties.IAbilityStat;

public enum BodyAbility implements IPartAbility {

	/**
	 * typically a property of the brain, indicating a being can have a soul (mind)
	 */
	HAVE_SOUL,
	/** pump blood, usually */
	PUMP_LIFE_ESSENCE,
	/** typically a property of wombs */
	GESTATE,
	/** e.g. ovaries store eggs */
	STORE_EGGS,
	/** where eggs or offspring emerge */
	GIVE_BIRTH,
	/** puts sperm in eggs */
	FERTILIZE,
	/** e.g. testicles store seed */
	STORE_SEED,
	/** e.g. a tail is prehensile */
	PREHENSILE, GRASP, WALK, SPEAK, EAT, CAST_POWER, FLY;

	private Set<IAbilityStat<?>> stats;

	private BodyAbility(IAbilityStat<?>... stats) {
		this.stats = ImmutableSet.copyOf(stats);
	}

	@Override
	public Set<IAbilityStat<?>> getExpectedStats() {
		return stats;
	}

	@Override
	public String getName() {
		return name();
	}
}