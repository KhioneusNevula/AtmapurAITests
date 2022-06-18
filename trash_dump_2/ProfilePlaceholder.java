package psych.actionstates;

import sociology.IProfile;
import sociology.Profile;
import sociology.sociocon.IHasProfile;
import sociology.sociocon.Sociocat;
import sociology.sociocon.Sociocon;
import sociology.sociocon.Socioprop;

/**
 * Used when making an action plan as a way of figuring out which profiles are
 * used in the plan and which are known/unknown
 * 
 * @author borah
 *
 */
public class ProfilePlaceholder implements IProfile, Comparable<ProfilePlaceholder> {

	private Profile resolvedProfile;
	private String identity;

	public ProfilePlaceholder(String identity) {
		this.identity = identity;
	}

	public String getIdentity() {
		return identity;
	}

	public ProfilePlaceholder resolve(Profile resolved) {
		this.resolvedProfile = resolved;
		return this;
	}

	public Profile getResolvedProfile() {
		return resolvedProfile;
	}

	@Override
	public String getName() {
		return resolvedProfile == null ? this.identity : resolvedProfile.getName();
	}

	@Override
	public IHasProfile getOwner() {
		return resolvedProfile == null ? null : resolvedProfile.getOwner();
	}

	@Override
	public void setOwner(IHasProfile owner) {
		if (resolvedProfile != null)
			resolvedProfile.setOwner(owner);
	}

	@Override
	public boolean hasSociocon(Sociocon con) {
		return resolvedProfile == null ? false : resolvedProfile.hasSociocon(con);
	}

	@Override
	public boolean hasSociocat(Sociocat cat) {
		return resolvedProfile == null ? false : resolvedProfile.hasSociocat(cat);
	}

	@Override
	public <T> T setValue(Socioprop<T> prop, T val) {
		return resolvedProfile == null ? null : resolvedProfile.setValue(prop, val);
	}

	@Override
	public <T> T getValue(Socioprop<T> prop) {
		return resolvedProfile == null ? null : resolvedProfile.getValue(prop);
	}

	@Override
	public Sociocon getSociocon(Sociocat cat, String name) {
		return resolvedProfile == null ? null : resolvedProfile.getSociocon(cat, name);
	}

	@Override
	public void addSociocon(Sociocon con) {
		if (resolvedProfile != null) {
			this.addSociocon(con);
		}
	}

	@Override
	public void removeSociocon(Sociocon con) {
		if (resolvedProfile != null) {
			this.removeSociocon(con);
		}
	}

	@Override
	public String profileReport() {
		return resolvedProfile != null ? resolvedProfile.profileReport() : "{placeholder:" + this.identity + "}";
	}

	@Override
	public int compareTo(ProfilePlaceholder o) {
		return this.identity.compareTo(o.identity);
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public Profile getActualProfile() {
		return resolvedProfile;
	}

}
