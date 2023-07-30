package mind.memory.events;

import mind.action.IActionType;
import mind.concepts.type.IMeme;
import phenomenon.IPhenomenonType;
import phenomenon.PhenomenonType;
import phenomenon.PhenomenonType;

public interface IEventType extends IMeme {
	/**
	 * intentionally empty
	 */
	default IActionType<?> asActionType() {
		return (IActionType<?>) this;
	}

	default boolean isActionType() {
		return this instanceof IActionType;
	}

	default boolean isPhenomenonType() {
		return this instanceof PhenomenonType;
	}

	default IPhenomenonType asPhenomenonType() {
		return (IPhenomenonType) this;
	}
}
