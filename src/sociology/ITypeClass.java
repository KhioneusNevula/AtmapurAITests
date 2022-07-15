package sociology;

import sim.IHasProfile;

public interface ITypeClass extends IHasProfile {

	public TypeProfile getProfile();

	public default TypeProfile getType() {
		return this.getProfile();
	}

	/**
	 * called by world class to create a type profile for this "typeclass", whatever
	 * it may be
	 * 
	 * @param type
	 * @return
	 */
	public ITypeClass setProfile(TypeProfile type);
}
