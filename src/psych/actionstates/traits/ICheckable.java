package psych.actionstates.traits;

import sociology.Profile;

public interface ICheckable<T> {

	public T getValue(Profile p);

	public void setValue(Profile p, T value);

	public String getName();
}
