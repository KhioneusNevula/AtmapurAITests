package psychology.perception.inclination;

import psychology.Soul;
import psychology.perception.Profile;
import psychology.perception.memes.Association;

public abstract class Inclination {

	public static enum InclinationType {
		OPINION, BEHAVIOR, CONCEPT;
	}

	protected InclinationType type;

	public Inclination(InclinationType type) {
		this.type = type;
	}

	public InclinationType getType() {
		return type;
	}

	/**
	 * assume that this will only be called for an association that properly applies
	 * to this, and not, say, a random association. Profile is the profile of
	 * whatever the association is about, which can be oneself or null
	 * 
	 * @param forAssociation
	 * @return true if successful
	 */
	public abstract boolean onAssociation(Association<?> forAssociation, Soul ofSoul, Profile target);

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Inclination i) {
			return this.type.equals(i.type);
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return type.hashCode();
	}

}
