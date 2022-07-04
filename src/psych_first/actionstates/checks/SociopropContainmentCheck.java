package psych_first.actionstates.checks;

import java.util.Collection;

import culture.CulturalContext;
import sociology.IProfile;
import sociology.sociocon.Socioprop;

/**
 * TODO for later Checks if the given value is in a multiple-value property of a
 * profile
 * 
 * 
 * @author borah
 *
 * @param <T>
 */
public class SociopropContainmentCheck<T> extends SociopropCheck<T> {

	private T value;

	SociopropContainmentCheck(Socioprop<T> checker, T value) {
		super(checker);
		this.value = value;
		this.setType(ConditionType.CONTAINS);
	}

	/**
	 * The value being checked against
	 * 
	 * @return
	 */
	public T getValue() {
		return value;
	}

	@Override
	public Boolean satisfies(IProfile p, CulturalContext ctxt) {
		Collection c = (Collection) getValue(p, ctxt);
		if (c == null)
			return false;
		return c.contains(value);
	}

	@Override
	public String report() {
		return "";
	}
}
