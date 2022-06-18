package psych.actionstates.traits;

import entity.Actor;
import entity.ILocatable;
import psych.actionstates.traits.TraitStateAt.AtChecker;
import sociology.IProfile;
import sociology.Profile;

/**
 * The trait state representing the state of being at a location
 * 
 * @author borah
 *
 */
public class TraitStateAt extends TraitState<AtChecker> {

	public static final AtChecker AT = AtChecker.AT;

	public static enum AtChecker {
		AT
	}

	private IProfile parameter;

	public TraitStateAt(IProfile parameter) {
		super(AT);
		this.parameter = parameter;

	}

	public IProfile getParameter() {
		return parameter;
	}

	public ILocatable getLocation() {
		return this.parameter.getOwner() instanceof ILocatable ? (ILocatable) this.parameter.getOwner() : null;
	}

	@Override
	protected String idString() {
		return "at.fundamental";
	}

	/*
	 * protected double distanceTo(TraitStateAt other) { return
	 * Math.sqrt(Math.pow(this.getX() - other.getX(), 2) + Math.pow(this.getY() -
	 * other.getY(), 2)); }
	 */

	@Override
	public Boolean satisfies(Profile p) {
		if (p.getOwner() instanceof ILocatable l) {
			if (this.parameter.getOwner() instanceof ILocatable l2) {
				return l2.distance(l) <= Actor.REACH; // TODO obviously make this make more sense
			} else {
				return null;
			}
		}
		return false;
	}

	@Override
	public void updateToMatch(Profile p) {
		if (!(p.getOwner() instanceof ILocatable))
			throw new IllegalArgumentException("Profile " + p + " must be locatable");
		this.parameter = ((ILocatable) p.getOwner()).getLocation().getProfile();
	}

	/*
	 * @Override public boolean satisfies(TraitState<?> other) { return other
	 * instanceof TraitStateAt && ((TraitStateAt) other).distanceTo(this) <=
	 * Actor.REACH; }
	 */

}
