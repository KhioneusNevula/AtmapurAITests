package abilities;

import abilities.types.SystemType;

public class IntSystem extends NumericSystem<Integer> {
	public IntSystem(SystemType<?> type, ISystemHolder owner, int max, int min, int starting, String valname) {
		super(type, owner, max, min, starting, (a) -> a.intValue(), (a) -> a.doubleValue(), valname);
	}

}
