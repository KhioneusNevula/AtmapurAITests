package energy;

public class EnergyStorage implements IEnergyStorage {

	private double energy;
	private double maxEnergy;
	private IEnergyUnit unit;
	private double maxSupply;
	private double maxDrain;

	public EnergyStorage(IEnergyUnit unit, double maxEnergy, double maxSupply, double maxDrain) {
		this.unit = unit;
		this.energy = 0;
		this.maxSupply = maxSupply;
		this.maxDrain = maxDrain;

		this.maxEnergy = maxEnergy;
	}

	public EnergyStorage(IEnergyUnit unit, double maxEnergy, double maxTransfer) {
		this(unit, maxEnergy, maxTransfer, maxTransfer);
	}

	public EnergyStorage(IEnergyUnit unit, double maxEnergy) {
		this(unit, maxEnergy, maxEnergy, maxEnergy);
	}

	@Override
	public double getMaxDrain() {
		return maxDrain;
	}

	@Override
	public double getMaxSupply() {
		return maxSupply;
	}

	public void setMaxDrain(double maxDrain) {
		this.maxDrain = maxDrain;
	}

	public void setMaxSupply(double maxSupply) {
		this.maxSupply = maxSupply;
	}

	public void setMaxEnergy(double maxEnergy) {
		this.maxEnergy = maxEnergy;
	}

	public void changeUnits(IEnergyUnit unit) {
		this.energy = this.unit.convertTo(unit, energy);
		this.maxDrain = this.unit.convertTo(unit, maxDrain);
		this.maxEnergy = this.unit.convertTo(unit, maxEnergy);
		this.maxSupply = this.unit.convertTo(unit, maxSupply);
		this.unit = unit;
	}

	@Override
	public IEnergyUnit getEnergyUnits() {
		return unit;
	}

	@Override
	public double getEnergy() {

		return energy;
	}

	@Override
	public double getMaxEnergy() {
		return maxEnergy;
	}

	public void setEnergy(double amount) {
		this.energy = Math.max(0, Math.min(this.maxEnergy, amount));
	}

	@Override
	public double supplyEnergy(double amount, boolean virtual) {
		if (amount < 0) {
			throw new IllegalArgumentException("Cannot add negative amount " + amount);
		}
		double space = Math.min(this.maxSupply, this.maxEnergy - this.energy);
		double overflow = Math.max(0, amount - space);
		double add = amount - overflow;
		if (!virtual) {
			this.energy += add;
		}
		return overflow;
	}

	@Override
	public double drainEnergy(double amount, boolean virtual) {
		if (amount < 0) {
			throw new IllegalArgumentException("Cannot remove negative amount " + amount);
		}
		double drain = Math.min(Math.min(amount, energy), maxDrain);
		if (!virtual) {
			this.energy -= drain;
		}
		return drain;
	}

}
