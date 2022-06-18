package sociology;

import psych.actionstates.states.State.ProfileType;
import sociology.sociocon.IHasProfile;
import sociology.sociocon.Sociocat;
import sociology.sociocon.Sociocon;
import sociology.sociocon.Socioprop;

public class ProfilePlaceholder implements IProfile {

	private ProfileType type;
	private Profile resolved;

	public ProfilePlaceholder(ProfileType type) {
		this.type = type;
	}

	public ProfileType getType() {
		return type;
	}

	public ProfilePlaceholder resolve(Profile resolved) {
		this.resolved = resolved;
		return this;
	}

	public Profile getResolved() {
		return resolved;
	}

	public boolean isResolved() {
		return resolved != null;
	}

	public Profile unresolve() {
		Profile resolved = this.resolved;
		this.resolved = null;
		return resolved;
	}

	@Override
	public String getName() {
		return this.type.toString();
	}

	@Override
	public IHasProfile getOwner() {
		return resolved == null ? null : resolved.getOwner();
	}

	@Override
	public void setOwner(IHasProfile owner) {

	}

	@Override
	public boolean hasSociocon(Sociocon con) {
		return resolved == null ? null : resolved.hasSociocon(con);
	}

	@Override
	public boolean hasSociocat(Sociocat cat) {
		return resolved == null ? null : resolved.hasSociocat(cat);
	}

	@Override
	public <T> T setValue(Socioprop<T> prop, T val) {
		return null;
	}

	@Override
	public <T> T getValue(Socioprop<T> prop) {
		return resolved == null ? null : resolved.getValue(prop);
	}

	@Override
	public Sociocon getSociocon(Sociocat cat, String name) {
		return resolved == null ? null : resolved.getSociocon(cat, name);
	}

	@Override
	public void addSociocon(Sociocon con) {

	}

	@Override
	public void removeSociocon(Sociocon con) {

	}

	@Override
	public String profileReport() {
		return resolved == null ? this.type.toString() : this.resolved.profileReport();
	}

	@Override
	public Profile getActualProfile() {
		return resolved;
	}

}
