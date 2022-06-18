package psych.actionstates.traits;

import java.util.Collection;

import sociology.Profile;
import sociology.sociocon.Socioprop;

/**
 * Checks if the given value is in a multiple-value property of a profile TODO
 * for later
 * 
 * @author borah
 *
 * @param <T>
 */
public class SociopropContainmentCheck<T> extends SociopropTrait<T> {

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

}
