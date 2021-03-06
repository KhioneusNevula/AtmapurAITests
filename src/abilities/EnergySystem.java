package abilities;

import abilities.types.SystemType;
import energy.EnergyStorage;
import energy.IEnergyStorage;
import energy.IEnergyUnit;

public class EnergySystem extends EntitySystem implements IEnergyStorage {

	private EnergyStorage storage;

	public EnergySystem(SystemType<?> type, ISystemHolder owner, IEnergyUnit unit, double maxEnergy,
			double startEnergy) {
		super(type, owner);
		storage = new EnergyStorage(unit, maxEnergy);
		storage.setEnergy(startEnergy);
	}

	public EnergyStorage getStorage() {
		return storage;
	}

	@Override
	public String report() {
		return this.toString() + "{energy=" + this.getEnergyUnits().disp(getEnergy()) + "}";
	}

	@Override
	public IEnergyUnit getEnergyUnits() {
		return storage.getEnergyUnits();
	}

	@Override
	public double getEnergy() {
		return storage.getEnergy();
	}

	@Override
	public double getMaxEnergy() {
		return storage.getMaxEnergy();
	}

	@Override
	public double getMaxSupply() {
		return storage.getMaxSupply();
	}

	@Override
	public double getMaxDrain() {
		return storage.getMaxDrain();
	}

	@Override
	public double supplyEnergy(double amount, boolean virtual) {
		return storage.supplyEnergy(amount, virtual);
	}

	@Override
	public double drainEnergy(double amount, boolean virtual) {
		return storage.drainEnergy(amount, virtual);
	}

}
