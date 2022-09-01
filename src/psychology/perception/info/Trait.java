package psychology.perception.info;

import psychology.perception.Profile;

public abstract class Trait<T> implements InfoUnit<T> {

	private KDataType<T> dataType;
	private String name;

	public Trait(String name, KDataType<T> dataType) {
		this.dataType = dataType;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public KDataType<T> getDataType() {
		return dataType;
	}

	@Override
	public boolean hasValue(Profile toCheck) {
		return toCheck.hasTrait(this);
	}

	@Override
	public int hashCode() {
		return this.name.hashCode() + this.dataType.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Trait trait) {
			return this.name.equals(trait.name) && this.dataType.equals(trait.dataType);
		}
		return super.equals(obj);
	}

}
