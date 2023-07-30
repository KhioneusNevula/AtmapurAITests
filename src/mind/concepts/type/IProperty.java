package mind.concepts.type;

public interface IProperty extends IMeme {

	default boolean isSenseProperty() {
		return this instanceof SenseProperty;
	}

	default <T> SenseProperty<T> getAsSenseProperty() {
		return (SenseProperty<T>) this;
	}

	default boolean isSocialProperty() {
		return this instanceof Property;
	}

	default Property getAsSocialProperty() {
		return (Property) this;
	}
}
