package psychology.social.concepts;

public class TypeClass<T> extends Category {

	private Class<T> getClass;

	public TypeClass(String name, Class<T> forClass) {
		super(name, true);
		this.getClass = forClass;
	}

	public Class<T> getClassFor() {
		return getClass;
	}

}
