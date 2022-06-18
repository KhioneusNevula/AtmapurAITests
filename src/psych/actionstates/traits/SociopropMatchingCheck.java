package psych.actionstates.traits;

import java.util.Objects;

import sociology.Profile;
import sociology.sociocon.Socioprop;

/**
 * For matching socioprops with non-numeric values
 * 
 * @author borah
 *
 * @param <T>
 */
public class SociopropMatchingCheck<T> extends SociopropTrait<T> {

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
	public Boolean satisfies(Profile p) {
		return Objects.equals(value, p.getValue(getChecker())) || Objects.equals(p.getValue(getChecker()), value);
	}

	/*
	 * @Override public boolean satisfies(TraitState<?> other) { return
	 * super.satisfies(other) && this.getType() == other.getType(); }
	 */

	@Override
	public void updateToMatch(Profile p) {
		this.value = p.getValue(getChecker());
	}

}
