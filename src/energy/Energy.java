package energy;

import energy.IEnergyUnit.EnergyUnit;

public class Energy extends Number {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final IEnergyUnit units;
	private final double raw;
	public final double energy;

	private Energy(IEnergyUnit t, double raw, double energy) {
		this.units = t;
		this.raw = raw;
		this.energy = energy;
	}

	public double getRaw() {
		return raw;
	}

	public double getEnergy() {
		return energy;
	}

	public IEnergyUnit getUnits() {
		return units;
	}

	/**
	 * a packet of raw energy
	 * 
	 * @param raw
	 * @return
	 */
	public static Energy raw(double raw) {
		return new Energy(EnergyUnit.RAW, raw, raw);
	}

	/**
	 * gets energy with the value assumed to be in units of the given type
	 * 
	 * @param type
	 * @param value
	 * @return
	 */
	public static Energy of(IEnergyUnit type, double value) {
		return new Energy(type, type.convertToRaw(value), value);
	}

	/**
	 * gets energy with the value assumed to be in raw units
	 * 
	 * @param type
	 * @param value
	 * @return
	 */
	public static Energy ofRaw(IEnergyUnit type, double raw) {
		return new Energy(type, raw, type.convertFromRaw(raw));
	}

	@Override
	public int intValue() {
		return (int) energy;
	}

	@Override
	public long longValue() {
		return (long) energy;
	}

	@Override
	public float floatValue() {
		return (float) energy;
	}

	@Override
	public double doubleValue() {
		return (double) energy;
	}

	/**
	 * return the power of this energy over the given time
	 * 
	 * @param ticks
	 * @return
	 */
	public Power per(int ticks) {

		return new Power(this, ticks);
	}

	public Energy in(IEnergyUnit other) {
		return Energy.of(other, this.units.convertTo(other, raw));
	}

	/**
	 * adds this energy to the other energy, returns a new energy with the units of
	 * this one
	 */
	public Energy plus(Energy other) {
		return Energy.of(units, units.convertFromRaw(this.raw + other.raw));
	}

	/**
	 * subtract other energy from this energy; return new energy with units of this
	 * one
	 */
	public Energy minus(Energy other) {
		return Energy.of(units, units.convertFromRaw(this.raw - other.raw));
	}

	public static double convert(IEnergyUnit from, double value, IEnergyUnit to) {
		return to.convertFromRaw(from.convertToRaw(value));
	}

}
