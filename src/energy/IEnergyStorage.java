package energy;

public interface IEnergyStorage extends IEnergyHandler {

	/**
	 * current amount of energy
	 * 
	 * @return
	 */
	public double getEnergy();

	/**
	 * max amount of energy storable
	 * 
	 * @return
	 */
	public double getMaxCapacity();

	/**
	 * max amount of energy that can be directly supplied
	 * 
	 * @return
	 */
	public double getMaxSupply();

	/**
	 * max amount of energy that can be drained
	 * 
	 * @return
	 */
	public double getMaxDrain();

	/**
	 * whether this energy storage is full
	 * 
	 * @return
	 */
	public default boolean isFull() {
		return getEnergy() >= getMaxCapacity();
	}

	/**
	 * a double representing the decimal percentage of how much is stored
	 * 
	 * @return
	 */
	public default double getPercent() {
		return this.getEnergy() / this.getMaxCapacity();
	}

	/**
	 * whether this energy storage is empty
	 * 
	 * @return
	 */
	public default boolean isEmpty() {
		return getEnergy() <= 0;
	}

	/**
	 * whether this energy holder has infinite capacity
	 * 
	 * @return
	 */
	public default boolean isBottomless() {
		return getMaxCapacity() == Double.POSITIVE_INFINITY;
	}

	/**
	 * tries to add as much energy as possible and returns the overflow; if
	 * "virtual" is true then don't actually add energy but return how much WOULD
	 * overflow
	 * 
	 * @param amount
	 * @return
	 */
	public double supplyEnergy(double amount, boolean virtual);

	/**
	 * tries to add energy and returns the overflow
	 * 
	 * @param amount
	 * @return
	 */
	public default double supplyEnergy(double amount) {
		return this.supplyEnergy(amount, false);
	}

	/**
	 * tries to drain as much energy as possible; returns how much was drained; if
	 * virtual is true don't actually remove energy but return how much WOULD be
	 * removed
	 * 
	 * @param amount
	 * @return
	 */
	public double drainEnergy(double amount, boolean virtual);

	/**
	 * tries to drain as much energy as possible; returns how much was drained
	 * 
	 * @param amount
	 * @return
	 */
	public default double drainEnergy(double amount) {
		return this.drainEnergy(amount, false);
	}

	/**
	 * Move energy from this storage to the other; if units are different, just
	 * convert
	 * 
	 * @param to
	 * @param maxMove the max amount of energy to move
	 */
	public default void supplyEnergy(IEnergyStorage to, double maxMove) {
		double moving = this.drainEnergy(maxMove);
		boolean flag = to.getEnergyUnits() != this.getEnergyUnits();
		if (flag)
			moving = Energy.convert(getEnergyUnits(), moving, to.getEnergyUnits());
		double overflow = to.supplyEnergy(moving);
		if (flag)
			overflow = Energy.convert(to.getEnergyUnits(), overflow, getEnergyUnits());
		this.supplyEnergy(overflow);
	}

	public default void drainEnergy(IEnergyStorage from, double maxDrain) {
		from.supplyEnergy(this, maxDrain);
	}

}
