package psych.actionstates.traits;

import entity.Actor;
import entity.IPhysicalExistence;
import psych.actionstates.traits.TraitStateAt.AtChecker;
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

	private int x;
	private int y;

	public TraitStateAt(int x, int y) {
		super(AT);

		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	protected String idString() {
		return "at.fundamental";
	}

	protected double distanceTo(TraitStateAt other) {
		return Math.sqrt(Math.pow(this.getX() - other.getX(), 2) + Math.pow(this.getY() - other.getY(), 2));
	}

	@Override
	public boolean satisfies(Profile p) {
		if (p.getOwner() instanceof Actor a) {
			return a.getWorld().getAt(x, y).contains(a);
		}
		return false;
	}

	@Override
	public void updateToMatch(Profile p) {
		if (p.getOwner() instanceof IPhysicalExistence ipe) {
			this.x = ipe.getX();
			this.y = ipe.getY();
		}
	}

	@Override
	public boolean satisfies(TraitState<?> other) {
		return other instanceof TraitStateAt && ((TraitStateAt) other).distanceTo(this) <= Actor.REACH;
	}

}
