package sociology.sociocon;

import sociology.Profile;

/**
 * A data structure for each instance of a profile to store the values of
 * properties of its associated sociocons in
 * 
 * @author borah
 *
 * @param <T>
 */
public class PropertyHolder<T> {
	private Socioprop<T> property;
	private Sociocon owner;
	private Profile at;
	private T value;

	PropertyHolder(Socioprop<T> property, T startValue, Sociocon owner, Profile p) {
		this.property = property;
		this.value = startValue;
		this.owner = owner;
		this.at = p;
	}

	PropertyHolder(Socioprop<T> property, Sociocon owner, Profile p) {
		this(property, property.getInitialValue(p), owner, p);
	}

	public Sociocon getOwner() {
		return owner;
	}

	public Socioprop<T> getProperty() {
		return property;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public void setOwner(Sociocon owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return "{" + this.property.getName() + "=" + this.value + "}";
	}
}
