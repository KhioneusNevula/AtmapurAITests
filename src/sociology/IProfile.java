package sociology;

import sociology.sociocon.IHasProfile;
import sociology.sociocon.Sociocat;
import sociology.sociocon.Sociocon;
import sociology.sociocon.Socioprop;

public interface IProfile {

	public String getName();

	public IHasProfile getOwner();

	public void setOwner(IHasProfile owner);

	public boolean hasSociocon(Sociocon con);

	public boolean hasSociocat(Sociocat cat);

	/**
	 * Changes the value of the given property and returns the old one
	 * 
	 * @param <T>
	 * @param prop
	 * @return
	 */
	public <T> T setValue(Socioprop<T> prop, T val);

	/**
	 * Returns the value of the given property as stored in the profile
	 * 
	 * @param <T>
	 * @param prop
	 * @return
	 */
	public <T> T getValue(Socioprop<T> prop);

	public Sociocon getSociocon(Sociocat cat, String name);

	public void addSociocon(Sociocon con);

	public void removeSociocon(Sociocon con);

	public String profileReport();

	/**
	 * Can be null; used so that resolvedprofiles can be accessed easily
	 * 
	 * @return
	 */
	public Profile getActualProfile();
}
