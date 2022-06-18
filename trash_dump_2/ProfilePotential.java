package psych.actionstates;

import psych.action.Action;
import sociology.IProfile;
import sociology.Profile;
import sociology.sociocon.IHasProfile;
import sociology.sociocon.Sociocat;
import sociology.sociocon.Sociocon;
import sociology.sociocon.Socioprop;

/**
 * Distinct from a profile placeholder--this is used in an action to indicate
 * the action's basic profile requirements, allowing it to express semi-complex
 * relationships between components but not requiring the profile to be resolved
 * 
 * @author borah
 *
 */
public class ProfilePotential implements IProfile, Comparable<ProfilePotential> {

	private String identity;
	private Action parent;

	public ProfilePotential(String identity) {
		this.identity = identity;
	}

	public ProfilePotential setParent(Action parent) {
		this.parent = parent;
		return this;
	}

	public Action getParent() {
		return parent;
	}

	public String getIdentity() {
		return identity;
	}

	@Override
	public String getName() {
		return "potential." + this.identity;
	}

	@Override
	public IHasProfile getOwner() {
		return null;
	}

	@Override
	public void setOwner(IHasProfile owner) {

	}

	@Override
	public boolean hasSociocon(Sociocon con) {
		return false;
	}

	@Override
	public boolean hasSociocat(Sociocat cat) {
		return false;
	}

	@Override
	public <T> T setValue(Socioprop<T> prop, T val) {
		throw new IllegalStateException("Profile potentials cannot be used like profiles");
	}

	@Override
	public <T> T getValue(Socioprop<T> prop) {
		throw new IllegalStateException("Profile potentials cannot be used like profiles");
	}

	@Override
	public Sociocon getSociocon(Sociocat cat, String name) {
		return null;
	}

	@Override
	public void addSociocon(Sociocon con) {

	}

	@Override
	public void removeSociocon(Sociocon con) {

	}

	@Override
	public String profileReport() {
		return "{potential:" + this.identity + "}";
	}

	@Override
	public int compareTo(ProfilePotential o) {
		return this.identity.compareTo(o.identity);
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public Profile getActualProfile() {
		return null;
	}

}
