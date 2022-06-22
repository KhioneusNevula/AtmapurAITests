package psych.actionstates.checks;

import sociology.Profile;

/**
 * storage mediums that can have their value checked
 * 
 * @author borah
 *
 * @param <T>
 */
public interface ICheckable<T> {

	public T getValue(Profile p);

	public void setValue(Profile p, T value);

	public String getName();
}
