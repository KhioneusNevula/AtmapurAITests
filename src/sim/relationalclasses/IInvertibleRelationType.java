package sim.relationalclasses;

public interface IInvertibleRelationType<T extends IInvertibleRelationType<T>> {

	public T inverse();

	/**
	 * Whether this relationship is inverted from the usual active direction of the
	 * appropriate relationship, i.e. it indicates the passive-to-active direction
	 * or affected-to-affecter direction
	 * 
	 * @return
	 */
	boolean isInverseType();
}
