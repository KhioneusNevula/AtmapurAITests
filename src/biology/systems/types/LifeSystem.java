package biology.systems.types;

import biology.systems.EnergySystem;
import biology.systems.ISystemHolder;
import biology.systems.SystemType;
import energy.IEnergyUnit.EnergyUnit;
import energy.IEnergyUser;

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

	public LifeSystem(ISystemHolder owner, double max, double severe) {

		super(SystemType.LIFE, owner, EnergyUnit.LIFE, max, max);
		this.severe = severe;
		this.powerUse = max / 1000.0;
	}

	public LifeSystem(ISystemHolder owner, int max) {
		this(owner, max, ((double) max / 4));
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
