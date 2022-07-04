package psych_first.actionstates.checks;

import java.util.Objects;

import culture.CulturalContext;
import sociology.IProfile;
import sociology.sociocon.Socioprop;

/**
 * For matching socioprops with non-numeric values
 * 
 * @author borah
 *
 * @param <T>
 */
public class SociopropMatchingCheck<T> extends SociopropCheck<T> {

	private T value;

	SociopropMatchingCheck(Socioprop<T> checker, T value) {
		super(checker);
		this.value = value;
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
		T val = getValue(p, ctxt);
		return p.getActualProfile() == null ? null

				: Objects.equals(value, val) || Objects.equals(val, value);
	}

	@Override
	public String report() {
		return "value of " + this.getChecker() + " equals " + this.value;
	}

}
