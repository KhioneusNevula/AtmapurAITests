package psych_first.actionstates.checks;

import culture.CulturalContext;
import sociology.Profile;

/**
 * storage mediums that can have their value checked
 * 
 * @author borah
 *
 * @param <T>
 */
public interface ICheckable<T> {

	public T getValue(Profile p, CulturalContext ctxt);

	public void setValue(Profile p, T value);

	public boolean hasValue(Profile p, CulturalContext ctxt);

	public String getName();
}
