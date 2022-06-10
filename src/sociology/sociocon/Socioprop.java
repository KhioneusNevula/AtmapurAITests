package sociology.sociocon;

import java.util.function.Function;

import psych.actionstates.traits.ICheckable;
import sociology.Profile;

/**
 * These are representations of the properties of a sociocon in a singleton
 * form; Socioprops do not change based on world state and instead are just
 * descriptors for different values stored in Profiles.
 * 
 * @author borah
 *
 * @param <T>
 */
public class Socioprop<T> implements IPurposeElement, ICheckable<T> {

	private Function<Profile, T> initialValue;
	private Function<Profile, T> checkValue = (p) -> p.getValue(this);
	private Class<T> type;
	private String name;

	private IPurposeSource origin = null;

	Socioprop(String name, Class<T> type, Function<Profile, T> initialValue) {
		this.name = name;
		this.type = type;
		this.initialValue = initialValue;
	}

	public Socioprop<T> setOrigin(IPurposeSource origin) {
		this.origin = origin;
		return this;
	}

	public Socioprop<T> getValueFunction(Function<Profile, T> get) {
		this.checkValue = get;
		return this;
	}

	public T checkValue(Profile t) {
		return checkValue.apply(t);
	}

	public T getInitialValue(Profile for_) {
		return initialValue.apply(for_);
	}

	public Class<T> getType() {
		return type;
	}

	/**
	 * this is the sociocategory or system this property originates from; for
	 * categories, this would be the highest parent which provides this property.
	 * may be null
	 */
	public IPurposeSource getOrigin() {
		return origin;
	}

	public String getName() {
		return name;
	}

	/**
	 * Returns a holder for this property and a value
	 * 
	 * @param startingValue
	 * @return
	 */
	public PropertyHolder<T> createFor(Sociocon owner, T startingValue, Profile p) {
		return new PropertyHolder<>(this, startingValue, owner, p);
	}

	/**
	 * Creates a holder for this property with a default value
	 */
	public PropertyHolder<T> createFor(Sociocon owner, Profile p) {
		return new PropertyHolder<>(this, owner, p);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Socioprop other) {
			return this.name.equals(other.name) && this.origin == other.origin;

		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode() * this.name.hashCode() + (this.origin == null ? 0 : this.origin.hashCode());
	}

	@Override
	public String toString() {
		return "{socioprop-\"" + this.name + "\"," + this.type + ".class"
				+ (this.origin == null ? ""
						: ",becauseof:" + (this.origin instanceof Sociocat ? ((Sociocat) this.origin).name()
								: (this.origin.getClass().getSimpleName())))
				+ "}";
	}

	@Override
	public T getValue(Profile p) {
		return p.getValue(this);
	}

	@Override
	public void setValue(Profile p, T value) {
		p.setValue(this, value);
	}

}
