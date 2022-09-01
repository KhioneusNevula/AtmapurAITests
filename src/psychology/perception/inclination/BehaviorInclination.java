package psychology.perception.inclination;

import psychology.Soul;
import psychology.perception.Profile;
import psychology.perception.memes.Association;

public class BehaviorInclination extends Inclination {

	public BehaviorInclination() {
		super(InclinationType.BEHAVIOR);
	}

	@Override
	public boolean onAssociation(Association<?> forAssociation, Soul ofSoul, Profile target) {
		// TODO figure out behavior inclinations
		return false;
	}

	@Override
	public int hashCode() {
		// TODO behaviorinclination hashcode
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO behaviorinclination equals
		return super.equals(obj);
	}

}
