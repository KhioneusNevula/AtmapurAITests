package abilities.types;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import abilities.EnergySystem;
import abilities.ISystemHolder;
import abilities.SystemType;
import energy.IEnergyUnit.EnergyUnit;
import energy.IEnergyUser;
import psychology.perception.info.BruteTrait;
import psychology.perception.info.KDataType;

/**
 * by default, you have ten seconds to live if you are not absorbing energy and
 * have full stored energy
 * 
 * @author borah
 *
 */
public class LifeSystem extends EnergySystem implements IEnergyUser {

	private double severe;
	private double powerUse;

	public static enum HealthState {
		SURVIVING, DYING, DEAD;
	}

	public static final BruteTrait<Boolean> ALIVE_TRAIT = new BruteTrait<>("alive", KDataType.BOOLEAN);
	public static final BruteTrait<HealthState> HEALTH_TRAIT = new BruteTrait<>("health",
			KDataType.enumType(HealthState.class));

	public LifeSystem(ISystemHolder owner, double max, double severe) {

		super(SystemType.LIFE, owner, EnergyUnit.LIFE, max, max);
		this.severe = severe;
		this.powerUse = max / 300.0;
	}

	public LifeSystem(ISystemHolder owner, int max) {
		this(owner, max, ((double) max / 4));
	}

	@Override
	public <T> T updateTrait(BruteTrait<T> trait, Object oldval) {
		if (trait == ALIVE_TRAIT) {
			return (T) Boolean.valueOf(!this.isDead());
		} else if (trait == HEALTH_TRAIT) {
			if (this.isDead()) {
				return (T) HealthState.DEAD;
			} else if (this.isSevere()) {
				return (T) HealthState.DYING;
			} else {
				return (T) HealthState.SURVIVING;
			}
		}
		return super.updateTrait(trait, oldval);
	}

	@Override
	public Map<BruteTrait<?>, Object> initTraits() {
		return ImmutableMap.of(ALIVE_TRAIT, true, HEALTH_TRAIT, HealthState.SURVIVING);
	}

	public double getSevere() {
		return severe;
	}

	public void setSevere(int severe) {
		this.severe = severe;
	}

	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	protected void update(long ticks) {
		this.drainEnergy(getPowerUse());
	}

	public boolean isSevere() {
		return getEnergy() <= severe;
	}

	public boolean isDead() {
		return this.isEmpty();
	}

	@Override
	public double getPowerUse() {
		return this.powerUse;
	}

}
