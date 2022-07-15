package psych_first.perception.knowledge;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import culture.CulturalContext;
import psych_first.perception.knowledge.events.RelationType;
import sim.IHasProfile;
import sociology.Profile;
import sociology.TypeProfile;

public abstract class Occurrence implements IOccurrence {

	protected Profile profile;
	private UUID uuid = UUID.randomUUID();

	/**
	 * key = from, sub-map key = relation, sub-map value = to
	 */
	protected Map<IHasProfile, Map<IHasProfile, Set<RelationType>>> profilesRelations = new HashMap<>(0);

	public Occurrence() {

	}

	@Override
	public Profile getProfile() {
		return profile;
	}

	@Override
	public TypeProfile getType() {
		return this.profile.getTypeProfile();
	}

	@Override
	public boolean hasInfo(IKnowledgeType<?> type, CulturalContext ctxt) {
		// TODO event info
		return false;
	}

	@Override
	public <T> T getInfo(IKnowledgeType<T> type, CulturalContext ctxt) {
		// TODO event info
		return null;
	}

	@Override
	public UUID getUuid() {
		return this.uuid;
	}

	@Override
	public Collection<IHasProfile> getInvolved() {
		return this.profilesRelations.keySet();
	}

	/**
	 * gets profiles of the given relationship type for whom the profile parameter
	 * is subject (if isProfileSubject is true) or predicate (if not true)
	 * 
	 * @param type
	 * @param prof
	 * @param isProfileSubject
	 * @return
	 */
	@Override
	public Collection<IHasProfile> getProfiles(RelationType type, IHasProfile prof, boolean isProfileSubject) {
		Set<IHasProfile> ps = new HashSet<>();

		if (isProfileSubject) {
			Map<IHasProfile, Set<RelationType>> map = this.profilesRelations.get(prof);
			if (map == null)
				return ps;
			for (Map.Entry<IHasProfile, Set<RelationType>> entry : map.entrySet()) {
				if (entry.getValue().contains(type)) {
					ps.add(entry.getKey());
				}
			}
		} else {
			for (IHasProfile p : this.profilesRelations.keySet()) {
				if (profilesRelations.get(p).containsKey(prof)) {
					if (profilesRelations.get(p).get(prof).contains(type)) {
						ps.add(p);
					}
				}
			}
		}
		return ps;
	}

	protected void addRelationship(IHasProfile sub, RelationType type, IHasProfile pred) {
		if (!type.canBeSubject(sub) || !type.canBePredicate(pred)) {
			throw new IllegalArgumentException(sub + " " + type + " " + pred);
		}

		this.profilesRelations.computeIfAbsent(sub, (a) -> new HashMap<>(1))
				.computeIfAbsent(pred, (a) -> new HashSet<>(1)).add(type);
		if (type.hasConverse()) {
			this.profilesRelations.computeIfAbsent(pred, (a) -> new HashMap<>(1))
					.computeIfAbsent(sub, (a) -> new HashSet<>(1)).add(type.converse());
		}
		if (!this.profilesRelations.containsKey(pred)) {
			profilesRelations.put(pred, new HashMap<>(0));
		}
	}

	@Override
	public Collection<RelationType> getRelations(IHasProfile from, IHasProfile to) {
		if (!this.profilesRelations.containsKey(from))
			return null;
		for (Map.Entry<IHasProfile, Set<RelationType>> rp : this.profilesRelations.get(from).entrySet()) {
			if (rp.getKey().equals(to)) {
				return rp.getValue();
			}
		}
		return Collections.emptySet();
	}

	@Override
	public String report() {
		String r = this.toString() + "{";
		for (Entry<IHasProfile, Map<IHasProfile, Set<RelationType>>> en : this.profilesRelations.entrySet()) {

			for (IHasProfile other : en.getValue().keySet()) {
				String tr = "<" + en.getKey().getProfile() + " ";
				for (RelationType rel : en.getValue().get(other)) {
					tr += rel + ",";
				}
				tr = tr.substring(0, tr.length() - 1) + " " + other.getProfile() + ">";

				r += tr + ", ";
			}
		}
		return r + "}";

	}

	@Override
	public boolean hasRelation(IHasProfile sub, RelationType type, IHasProfile pred) {
		if (this.profilesRelations.containsKey(sub)) {
			if (this.profilesRelations.get(sub).containsKey(pred)) {
				if (type == null)
					return true;
				return this.profilesRelations.get(sub).get(pred).contains(type);
			}
		}
		return false;
	}

	@Override
	public Occurrence clone() {
		Occurrence clone;
		try {
			clone = (Occurrence) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
		clone.profilesRelations = new HashMap<>(this.profilesRelations);
		for (IHasProfile prof : this.profilesRelations.keySet()) {
			clone.profilesRelations.put(prof, new HashMap<>(this.profilesRelations.get(prof)));
			Map<IHasProfile, Set<RelationType>> map = clone.profilesRelations.get(prof);
			for (IHasProfile prof2 : this.profilesRelations.get(prof).keySet()) {
				map.put(prof2, new HashSet<>(map.get(prof2)));
			}
		}
		return clone;
	}

	@Override
	public String toString() {
		return super.toString() + "(" + this.getInvolved().size() + ")";
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Occurrence o && o.profilesRelations.equals(this.profilesRelations);
	}

}
