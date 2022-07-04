package abilities.types;

import energy.IEnergyUnit.EnergyUnit;
import abilities.EnergySystem;
import abilities.ISystemHolder;
import energy.IEnergyUser;

public class LifeSystem extends EnergySystem implements IEnergyUser {

	private double severe;

	protected LifeSystem(ISystemHolder owner, Object[] args) {

		this(owner, (double) args[0], (double) args[1]);
	}

	public LifeSystem(ISystemHolder owner, double max, double severe) {

		super(SystemType.LIFE, owner, EnergyUnit.LIFE, max, max);
		this.severe = severe;
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
	public boolean isConstantUpdate() {
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
		return 0.1;
	}

}
