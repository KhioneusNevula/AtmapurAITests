package sociology;

import java.util.ArrayList;
import java.util.List;

import psych.actionstates.ConditionSet;
import psych.actionstates.states.State.ProfileType;
import sim.IHasProfile;
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

	/**
	 * Tries to resolve this profile using the given conditions and set of possible
	 * profiles, null if it cannot TODO currently just choosing profile with least
	 * unfulfilled conditions
	 */
	public static Profile tryFindResolution(ConditionSet conditions, Iterable<Profile> known) {
		List<Profile> complete = new ArrayList<>();
		// List<Profile> incomplete = new ArrayList<>();
		for (Profile profile : known) {
			ConditionSet oth = conditions.conditionsUnfulfilledBy(profile);
			if (oth.isEmpty())
				complete.add(profile);
			// else if (!oth.equals(conditions)) incomplete.add(profile);
		}

		return complete.stream().findAny().orElse(null);

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
	public boolean equals(Object obj) {
		if (obj instanceof ProfilePlaceholder pp) {
			return this.resolved != null ? this.resolved.equals(pp.resolved) : (this.type == pp.type);
		}
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return "|" + getName() + (resolved == null ? "" : ": " + resolved) + "|";
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
