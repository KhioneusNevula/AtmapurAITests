package psych_first.actionstates.checks;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Predicate;

import culture.CulturalContext;
import psych_first.actionstates.checks.numeric.NumericCheck;
import sociology.IProfile;
import sociology.Profile;
import sociology.sociocon.Sociocat;
import sociology.sociocon.Sociocon;
import sociology.sociocon.Socioprop;

public abstract class Check<T> {

	public enum Fundamental {
		AT, IS_KNOWN
	}

	public static final Predicate<Check<?>> PREDICATE_IS_PROFILE_DEPENDENT = (a) -> a instanceof IProfileDependentCheck;
	public static final Predicate<Check<?>> PREDICATE_IS_NUMERIC = (a) -> a instanceof NumericCheck;
	public static final Predicate<Check<?>> PREDICATE_EQUAL_CHECK = (a) -> a.type == ConditionType.EQUAL;
	public static final Predicate<Check<?>> PREDICATE_BETWEEN_CHECK = (a) -> a.type == ConditionType.BETWEEN;
	public static final Predicate<Check<?>> PREDICATE_NOT_EQUAL_CHECK = (a) -> a.type == ConditionType.NOT_EQUAL;
	public static final Predicate<Check<?>> PREDICATE_GREATER_THAN_CHECK = (a) -> a.type == ConditionType.GREATER;
	public static final Predicate<Check<?>> PREDICATE_LESS_THAN_CHECK = (a) -> a.type == ConditionType.LESS;
	public static final Predicate<Check<?>> PREDICATE_CONTAINMENT_CHECK = (a) -> a.type == ConditionType.CONTAINS;

	private ConditionType type = ConditionType.EQUAL;

	/**
	 * a = absent, p = present
	 */
	private static Map<Sociocat, SociocatCheck> pCatTraits = new TreeMap<>();
	private static Map<Sociocat, SociocatCheck> aCatTraits = new TreeMap<>();
	private static Map<Sociocon, SocioconCheck> pConTraits = new TreeMap<>();
	private static Map<Sociocon, SocioconCheck> aConTraits = new TreeMap<>();

	/**
	 * The thing to be checked--the socioprop, sociocon, etc. If there is no
	 * reasonable thing to check, just use an enum singleton
	 */
	private T checker;

	public enum ConditionType {
		EQUAL, NOT_EQUAL, GREATER, LESS, BETWEEN, CONTAINS
	}

	public Check(T checker) {
		this.checker = checker;
	}

	/**
	 * The value to be checked--socioprop, sociocon, etc
	 * 
	 * @return
	 */
	public T getChecker() {
		return checker;
	}

	public void setChecker(T checker) {
		this.checker = checker;
	}

	/**
	 * returns id string; components should be separated by . periods
	 * 
	 * @return
	 */
	protected abstract String idString();

	public String getId() {
		return "trait." + this.getClass().getSimpleName() + "." + idString();
	}

	@Override
	public String toString() {
		return this.getId() + " for=" + this.getChecker() + " type=" + this.type;
	}

	protected void setType(ConditionType type) {
		this.type = type;
	}

	public ConditionType getType() {
		return type;
	}

	/**
	 * Checks whether the profile satisfies this trait's condition (with a given
	 * cultural context); null if this cannot be determined
	 * 
	 * @param p
	 * @return
	 */
	public abstract Boolean satisfies(IProfile p, CulturalContext ctxt);

	/**
	 * Checkes whether this trait state (assumed to be a result) fulfills the other
	 * trait (assumed to be a condition) This fulfillment need not be exact, but
	 * more of a guideline. Exactness is preferred, but if the fulfillment is not
	 * specific (as in real life not all information may be known, naturally) that
	 * is fine
	 * 
	 * @param other
	 * @return
	 */

	public boolean satisfies(Check<?> other) {
		return Objects.equals(this.checker, other.checker) || Objects.equals(other.checker, this.checker);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Check<?>ac) {
			return super.equals(obj)
					|| (this.checker != null && ac.checker != null ? this.checker.equals(ac.checker) : false)
							&& this.type == ac.type;
		}
		return false;
	}

	/**
	 * Creates a location check using TraitStateAt
	 * 
	 * @return
	 */
	public static AtCheck createLocationCheck(IProfile location) {
		return new AtCheck(location);
	}

	/**
	 * Creates a check for whether the profile is known -- or, if known is false,
	 * unknown
	 * 
	 * @param known
	 * @return
	 */
	public static IsKnownCheck checkIfKnown(boolean known) {
		return known ? IsKnownCheck.REQUIRE_RESOLVED : IsKnownCheck.REQUIRE_UNRESOLVED;
	}

	public static IsKnownCheck checkIfKnown() {
		return checkIfKnown(true);
	}

	public static IsKnownCheck checkIfUnknown() {
		return checkIfKnown(false);
	}

	public static SociocatCheck createSociocatCheck(Sociocat cat, boolean present) {
		Map<Sociocat, SociocatCheck> map = present ? pCatTraits : aCatTraits;

		return map.computeIfAbsent(cat, (a) -> new SociocatCheck(cat, present));

	}

	public static <T> SociopropMatchingCheck<T> createMatchingCheck(Socioprop<T> prop, T value) {
		return new SociopropMatchingCheck<T>(prop, value);
	}

	public static SocioconCheck createSocioconCheck(Sociocon cat, boolean present) {
		Map<Sociocon, SocioconCheck> map = present ? pConTraits : aConTraits;

		return map.computeIfAbsent(cat, (a) -> new SocioconCheck(cat, present));

	}

	public static SociopropProfileMatchingCheck createProfileMatchCheck(Socioprop<Profile> property,
			IProfile matchWith) {
		return new SociopropProfileMatchingCheck(property, matchWith);
	}

	public abstract String report();

}
