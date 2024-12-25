package utilities;

/**
 * Represents a kind of relation that can be inverted. Can also just represent a
 * single bidirectional relation, in which case return self for
 * {@link #inverse()}
 * 
 * @author borah
 *
 * @param <T> this same class or a subclass, to represent the inverse relation
 */
public interface IInvertibleRelationType<T extends IInvertibleRelationType<?>> {

	/**
	 * Get the inverted relation
	 * 
	 * @return
	 */
	public T inverse();

	/**
	 * If this relation's inverse is equivalent to itself
	 * 
	 * @return
	 */
	public boolean bidirectional();
}
