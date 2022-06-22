package psych.actionstates.checks;

import java.util.Collection;

import sociology.Profile;
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
	public Boolean satisfies(Profile p) {
		return ((Collection) p.getValue(this.getChecker())).contains(value);
	}

	/*
	 * public boolean satisfies(TraitState<?> other) { return super.satisfies(other)
	 * && this.getType() == other.getType(); }
	 */

	@Override
	public void updateToMatch(Profile p) {

	}

	@Override
	public String report() {
		return "";
	}
}
