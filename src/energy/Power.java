package energy;

import energy.IEnergyUnit.EnergyUnit;

public class Power extends Number {

	private final IEnergyUnit units;

	public final double power;
	private final double raw;

	private Power(IEnergyUnit units, double raw, double power) {
		this.units = units;
		this.raw = raw;
		this.power = power;
	}

	Power(Energy energy, int time) {
		this.units = energy.units;
		this.power = energy.energy / time;
		this.raw = energy.getRaw() / time;
	}

	/**
	 * return power for the given units/tick with the given value in those
	 * units/tick
	 * 
	 * @param unit
	 * @param power
	 * @return
	 */
	public static Power of(IEnergyUnit unit, double power) {
		return new Power(unit, unit.convertToRaw(power), power);
	}

	/**
	 * return power for given units/tick with the give value in raw units/tick
	 * 
	 * @param unit
	 * @param raw
	 * @return
	 */
	public static Power ofRaw(IEnergyUnit unit, double raw) {
		return new Power(unit, raw, unit.convertFromRaw(raw));
	}

	/**
	 * return power in raw units
	 * 
	 * @param raw
	 * @return
	 */
	public static Power raw(double raw) {
		return new Power(EnergyUnit.RAW, raw, raw);
	}

	public IEnergyUnit getUnits() {
		return units;
	}

	public double getPower() {
		return power;
	}

	public double getRaw() {
		return raw;
	}

	/**
	 * returns the energy of this power when calculated over the given time
	 * 
	 * @param ticks
	 * @return
	 */
	public Energy over(int ticks) {
		return Energy.of(units, power * ticks);
	}

	@Override
	public double doubleValue() {
		return power;
	}

	@Override
	public float floatValue() {
		return (float) power;
	}

	@Override
	public int intValue() {
		return (int) power;
	}

	@Override
	public long longValue() {
		return (long) power;
	}

	public Power in(IEnergyUnit other) {
		return new Power(other, raw, this.units.convertTo(other, raw));
	}

	/**
	 * adds this power to the other power, returns a new power with the units of
	 * this one
	 */
	public Power plus(Power other) {
		return new Power(units, units.convertFromRaw(this.raw + other.raw), this.raw + other.raw);
	}

	/**
	 * subtract other power from this power; return new power with units of this one
	 */
	public Power minus(Power other) {
		return new Power(units, units.convertFromRaw(this.raw - other.raw), this.raw - other.raw);
	}

}
