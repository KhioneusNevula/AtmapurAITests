package abilities;

import abilities.types.SystemType;

public class DoubleSystem extends NumericSystem<Double> {
	public DoubleSystem(SystemType<?> type, ISystemHolder owner, double max, double min, double starting,
			String valname) {
		super(type, owner, max, min, starting, (a) -> a, (a) -> a, valname);
	}

}
