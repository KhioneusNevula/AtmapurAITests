package abilities;

import java.util.function.Function;

public class NumericSystem<T extends Number> extends ESystem {

	protected T value;
	private T maxValue;
	private T minValue;
	private String valueName;
	private Function<Double, T> numberCast1;
	private Function<T, Double> numberCast2;

	/**
	 * 
	 * @param type
	 * @param owner
	 * @param maxValue   the max value of this system's numeric storage
	 * @param minValue   the min value
	 * @param startValue what value to start at
	 */
	public NumericSystem(SystemType<?> type, ISystemHolder owner, T maxValue, T minValue, T startValue,
			Function<Double, T> numberCast1, Function<T, Double> numberCast2, String valueName) {
		super(type, owner);
		this.maxValue = maxValue;
		this.numberCast1 = numberCast1;
		this.numberCast2 = numberCast2;
		this.minValue = minValue;
		this.value = startValue;
		this.valueName = valueName;
	}

	public String getValueName() {
		return valueName;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = this.numberCast1
				.apply(Math.min(maxValue.doubleValue(), Math.max(minValue.doubleValue(), value.doubleValue())));
	}

	public void changeValue(T by) {
		this.setValue(this.numberCast1.apply(getValue().doubleValue() + by.doubleValue()));
	}

	public T getMaxValue() {
		return maxValue;
	}

	public T getMinValue() {
		return minValue;
	}

	public boolean isMinimum() {
		return this.value.doubleValue() <= this.minValue.doubleValue();
	}

	public boolean isMaxed() {
		return this.value.doubleValue() >= this.maxValue.doubleValue();
	}

	@Override
	public String report() {
		return this.getType() + "{" + this.valueName + "=" + this.value + "}";
	}

}
