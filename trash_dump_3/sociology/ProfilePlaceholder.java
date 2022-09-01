package sociology;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import culture.CulturalContext;
import culture.Culture;
import psych_first.actionstates.ConditionSet;
import psych_first.perception.knowledge.IKnowledgeType;
import sim.IHasProfile;
import sociology.sociocon.Sociocat;
import sociology.sociocon.Sociocon;
import sociology.sociocon.Socioprop;

public class ProfilePlaceholder implements IProfile, IHasProfile {

	private ProfileType type;
	private Profile resolved;

	public ProfilePlaceholder(ProfileType type) {
		this.type = type;
	}

	public ProfileType getProfileType() {
		return type;
	}

	/**
	 * Tries to resolve this profile using the given conditions and set of possible
	 * profiles, null if it cannot TODO currently just choosing profile with least
	 * unfulfilled conditions
	 * 
	 * @param type - checks for type profiles if true and instance if false
	 */
	public static Profile tryFindResolution(ConditionSet conditions, CulturalContext ctxt, Iterable<Profile> known,
			boolean type) {
		List<Profile> complete = new ArrayList<>();
		// List<Profile> incomplete = new ArrayList<>();
		for (Profile profile : known) {
			ConditionSet oth = conditions.conditionsUnfulfilledBy(profile, ctxt);
			if (oth.isEmpty())
				complete.add(profile);
			// else if (!oth.equals(conditions)) incomplete.add(profile);
		}

		return complete.stream().findAny().orElse(null);

	}

	public ProfilePlaceholder resolve(Profile resolved) {
		if (this.type.isInstancePlaceholder() && resolved.isTypeProfile()) {
			throw new IllegalArgumentException("Resolved is type profile but this is for an instance profile");
		} else if (this.type.isTypePlaceholder() && !resolved.isTypeProfile()) {
			throw new IllegalArgumentException("Resolved is instance profile but this is for a type profile");
		}
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
	public boolean hasSociocat(Sociocat cat, CulturalContext ctxt) {
		return resolved == null ? null : resolved.hasSociocat(cat, ctxt);
	}

	@Override
	public <T> T setValue(Socioprop<T> prop, T val) {
		return null;
	}

	@Override
	public <T> T getValue(Socioprop<T> prop, CulturalContext ctxt) {
		return resolved == null ? null : resolved.getValue(prop, ctxt);
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

	@Override
	public boolean isTypeProfile() {
		return resolved == null ? false : this.resolved.isTypeProfile();
	}

	@Override
	public TypeProfile getTypeProfile() {
		return resolved == null ? null : this.resolved.getTypeProfile();
	}

	@Override
	public boolean hasValue(Socioprop<?> checker, CulturalContext ctxt) {
		return resolved == null ? false : this.resolved.hasValue(checker, ctxt);
	}

	@Override
	public Collection<Sociocon> getSocioconsFor(Culture cul) {
		return resolved == null ? Collections.emptySet() : resolved.getSocioconsFor(cul);
	}

	@Override
	public <T> T getInfo(IKnowledgeType<T> info, CulturalContext ctxt) {
		return resolved == null ? null : resolved.getInfo(info, ctxt);
	}

	@Override
	public boolean hasInfo(IKnowledgeType<?> info, CulturalContext ctxt) {
		return resolved == null ? null : resolved.hasInfo(info, ctxt);
	}

	@Override
	public Profile getProfile() {
		return this.resolved;
	}

	@Override
	public TypeProfile getType() {
		return resolved == null ? null : resolved.getTypeProfile();
	}

	@Override
	public UUID getUuid() {
		return resolved == null ? null : resolved.getOwner().getUuid();
	}

	@Override
	public boolean canBeAlive() {
		return resolved == null ? null : resolved.getOwner().canBeAlive();
	}

	@Override
	public boolean canBeSensed() {
		return resolved == null ? null : resolved.getOwner().canBeSensed();
	}

	@Override
	public boolean canThink() {
		return resolved == null ? null : resolved.getOwner().canThink();
	}
}
