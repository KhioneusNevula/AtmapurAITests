package psych.actionstates.traits;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import sociology.Profile;
import sociology.sociocon.Sociocat;
import sociology.sociocon.Sociocon;

public abstract class TraitState<T> {

	private ConditionType type = ConditionType.EQUAL;

	/**
	 * a = absent, p = present
	 */
	private static Map<Sociocat, SociocatTrait> pCatTraits = new TreeMap<>();
	private static Map<Sociocat, SociocatTrait> aCatTraits = new TreeMap<>();
	private static Map<Sociocon, SocioconTrait> pConTraits = new TreeMap<>();
	private static Map<Sociocon, SocioconTrait> aConTraits = new TreeMap<>();

	/**
	 * The thing to be checked--the socioprop, sociocon, etc. If there is no
	 * reasonable thing to check, just use an enum singleton
	 */
	private T checker;

	public enum ConditionType {
		EQUAL, NOT_EQUAL, GREATER, LESS, BETWEEN, CONTAINS
	}

	public TraitState(T checker) {
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
	 * Checks whether the profile satisfies this trait's condition; null if this
	 * cannot be determined
	 * 
	 * @param p
	 * @return
	 */
	public abstract Boolean satisfies(Profile p);

	/**
	 * Changes this trait state to match the given profile; used for ActualState
	 * computation
	 * 
	 * @param p
	 * @return
	 */
	public abstract void updateToMatch(Profile p);

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

	public boolean satisfies(TraitState<?> other) {
		return Objects.equals(this.checker, other.checker) || Objects.equals(other.checker, this.checker);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TraitState<?>ac) {
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
	public static TraitStateAt createLocationCheck(Profile location) {
		return new TraitStateAt(location);
	}

	public static SociocatTrait createSociocatTrait(Sociocat cat, boolean present) {
		Map<Sociocat, SociocatTrait> map = present ? pCatTraits : aCatTraits;

		return map.computeIfAbsent(cat, (a) -> new SociocatTrait(cat, present));

	}

	public static SocioconTrait createSocioconTrait(Sociocon cat, boolean present) {
		Map<Sociocon, SocioconTrait> map = present ? pConTraits : aConTraits;

		return map.computeIfAbsent(cat, (a) -> new SocioconTrait(cat, present));

	}

}
