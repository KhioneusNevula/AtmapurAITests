package psych_first.actionstates.checks;

import culture.CulturalContext;
import psych_first.actionstates.ConditionSet;
import psych_first.actionstates.checks.Check.Fundamental;
import sociology.IProfile;

/**
 * The trait state representing that the profile is resolved
 * 
 * @author borah
 *
 */
public class IsKnownCheck extends Check<Fundamental> {

	public static final Fundamental IS_KNOWN = Fundamental.IS_KNOWN;

	public static final IsKnownCheck REQUIRE_RESOLVED = new IsKnownCheck(true);

	public static final IsKnownCheck REQUIRE_UNRESOLVED = new IsKnownCheck(false);

	private boolean checkIsKnown = true;

	public static IsKnownCheck get(ConditionSet from) {
		return (IsKnownCheck) from.getCondition(IS_KNOWN);
	}

	/**
	 * returns true if the given set requires the profile to be resolved, false if
	 * it requires it to be unresolved, and null if the given set doesn't have any
	 * such requirement
	 * 
	 * @param set
	 * @return
	 */
	public static Boolean requiresKnown(ConditionSet set) {
		IsKnownCheck get = get(set);
		if (get == null)
			return null;
		else
			return get.checkIsKnown;
	}

	IsKnownCheck(boolean checkIsKnown) {
		super(IS_KNOWN);
		this.checkIsKnown = checkIsKnown;

	}

	public IsKnownCheck() {
		this(true);
	}

	/**
	 * return false if this checks for the profile being UNknown rather than known
	 * 
	 * @return
	 */
	public boolean checksIfKnown() {
		return checkIsKnown;
	}

	@Override
	protected String idString() {
		return "isknown.fundamental";
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	/*
	 * protected double distanceTo(TraitStateAt other) { return
	 * Math.sqrt(Math.pow(this.getX() - other.getX(), 2) + Math.pow(this.getY() -
	 * other.getY(), 2)); }
	 */

	@Override
	public Boolean satisfies(IProfile p, CulturalContext ctxt) {

		return checkIsKnown == (p.getActualProfile() != null);
	}
	/*
	 * @Override public boolean satisfies(TraitState<?> other) { return other
	 * instanceof TraitStateAt && ((TraitStateAt) other).distanceTo(this) <=
	 * Actor.REACH; }
	 */

	@Override
	public String report() {
		return "must be " + (this.checkIsKnown ? "resolved" : "unresolved");
	}

	@Override
	public boolean satisfies(Check<?> other) {
		return super.satisfies(other) && (((IsKnownCheck) other).checkIsKnown == this.checkIsKnown);
	}

}
