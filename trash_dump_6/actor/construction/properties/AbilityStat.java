package actor.construction.properties;

public class AbilityStat<T> implements IAbilityStat<T> {

	private Class<? super T> valueClass;
	private String name;
	private String kind;
	private T defaultV;

	/**
	 * @param name     the name of the stat
	 * @param kind     what sense or type of sense this stat (ordinarily) applies to
	 * @param vClass   what class of value this stat stores
	 * @param defaultV what the default return value for this statistic is
	 */
	public AbilityStat(String name, String kind, Class<? super T> vClass, T defaultV) {
		this.name = name;
		this.kind = kind;
		this.valueClass = vClass;
		this.defaultV = defaultV;
	}

	@Override
	public String getUniqueName() {
		return "stat_" + kind + "_" + name;
	}

	@Override
	public int hashCode() {
		return this.getUniqueName().hashCode() + this.valueClass.hashCode();
	}

	@Override
	public Class<? super T> getValueClass() {
		return valueClass;
	}

	@Override
	public T defaultValue() {
		return this.defaultV;
	}

}
