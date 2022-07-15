package psych_first.perception.knowledge.events;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import main.Pair;
import psych_first.perception.knowledge.Occurrence;
import sim.IHasProfile;
import sim.World;
import sociology.InstanceProfile;

public abstract class AbstractEvent extends Occurrence implements IEvent {

	private World world;
	private EventType<?> eventType;
	private long startTime;

	/**
	 * maps of relations between the profile (from) and this circumstance (to)
	 */
	private Map<IHasProfile, Set<RelationType>> profilesRelationsToCircumstance = new HashMap<>(0);
	/**
	 * map of relations between this circumstance (from) and these profiles (to)
	 */
	private Map<IHasProfile, Set<RelationType>> circumstanceRelationsToProfiles = new HashMap<>(0);

	public AbstractEvent(EventType<?> eventType, World inWorld, long startTime) {

		this.startTime = startTime;
		this.world = inWorld;
		this.eventType = eventType;
		this.profile = new InstanceProfile(this, world.getOrCreateTypeProfile(this.eventType.getEventID()),
				this.eventType.getEventID());
	}

	@Override
	public Long since() {
		return startTime;
	}

	@Override
	public World getWorld() {
		return this.world;
	}

	@Override
	public EventType<?> getEventType() {
		return this.eventType;
	}

	/**
	 * gets the profile with the given relationship to the event itself
	 * 
	 * @param type
	 * @param isEventSubject
	 * @return
	 */
	@Override
	public Collection<IHasProfile> getProfiles(RelationType type, boolean isEventSubject) {
		Set<IHasProfile> ps = new HashSet<>();

		for (Map.Entry<IHasProfile, Set<RelationType>> rts : (isEventSubject ? this.circumstanceRelationsToProfiles
				: this.profilesRelationsToCircumstance).entrySet()) {
			if (rts.getValue().contains(type)) {
				ps.add(rts.getKey());
			}
		}
		return ps;
	}

	/**
	 * adds a profile to this event
	 * 
	 * @param prof         the profile
	 * @param subjectEvent the relation between this profile and this event, where
	 *                     the event is the subject, may be null
	 * @param predEvent    the relation between this profile and this event, where
	 *                     the event is the predicate, may be null but both cannot
	 *                     be null
	 * @param withOthers   the relation between this profile and other profiles
	 */
	protected void putProfile(IHasProfile prof, Collection<RelationType> subjectEvent,
			Collection<RelationType> predEvent, Collection<Pair<RelationType, IHasProfile>> withOthers) {
		if (this.circumstanceRelationsToProfiles.containsKey(prof)
				|| this.profilesRelationsToCircumstance.containsKey(prof)) {
			throw new IllegalArgumentException(prof + " " + this.toString());
		}
		for (RelationType type : subjectEvent) {
			this.addRelationshipToEvent(prof, type, true);
		}

		for (RelationType type : predEvent) {
			this.addRelationshipToEvent(prof, type, false);
		}
		for (Pair<RelationType, IHasProfile> pair : withOthers) {
			this.addRelationship(prof, pair.getFirst(), pair.getSecond());
		}

	}

	protected void addRelationshipToEvent(IHasProfile prof, RelationType type, boolean eventSubject) {
		boolean checkprof = eventSubject ? type.canBePredicate(prof) : type.canBeSubject(prof);
		boolean checkev = eventSubject ? type.canBeSubject(this) : type.canBePredicate(this);
		if (!checkprof || !checkev) {
			throw new IllegalArgumentException(type + " " + prof + " " + this.toString());
		}

		Map<IHasProfile, Set<RelationType>> addMap = eventSubject ? circumstanceRelationsToProfiles
				: profilesRelationsToCircumstance;
		Map<IHasProfile, Set<RelationType>> otherMap = eventSubject ? profilesRelationsToCircumstance
				: circumstanceRelationsToProfiles;

		addMap.computeIfAbsent(prof, (a) -> new HashSet<>(1)).add(type);

		if (type.hasConverse()) {
			otherMap.computeIfAbsent(prof, (a) -> new HashSet<>(1)).add(type.converse());
		}

		if (!this.profilesRelations.containsKey(prof)) {
			profilesRelations.put(prof, new HashMap<>(1));
		}

	}

	/**
	 * adds the relationship with the event as the predicate
	 * 
	 * @param prof
	 * @param type
	 */
	protected void addRelationshipToEvent(IHasProfile prof, RelationType type) {
		this.addRelationshipToEvent(prof, type, false);
	}

	/**
	 * adds the relationship with the event as the subject
	 * 
	 * @param type
	 * @param prof
	 */
	protected void addRelationshipToEvent(RelationType type, IHasProfile prof) {
		this.addRelationshipToEvent(prof, type, true);
	}

	@Override
	public Collection<RelationType> getRelationFrom(IHasProfile from) {

		return this.profilesRelationsToCircumstance.get(from);
	}

	@Override
	public Collection<RelationType> getRelationTo(IHasProfile to) {
		return this.circumstanceRelationsToProfiles.get(to);
	}

	@Override
	public String report() {
		String r = super.report();
		r = r.substring(0, r.length() - 1);

		for (Entry<IHasProfile, Set<RelationType>> entry : this.profilesRelationsToCircumstance.entrySet()) {
			String tr = "<" + entry.getKey().getProfile() + " ";
			for (RelationType rel : entry.getValue()) {
				tr += rel + ",";
			}
			tr = tr.substring(0, tr.length() - 1) + " " + this + ">";
			r += tr + ",";
		}

		for (Entry<IHasProfile, Set<RelationType>> entry : this.circumstanceRelationsToProfiles.entrySet()) {
			String tr = "<" + this + " ";
			for (RelationType rel : entry.getValue()) {
				tr += rel + ",";
			}
			tr = tr.substring(0, tr.length() - 1) + " " + entry.getKey().getProfile() + ">";
			r += tr + ",";
		}

		return r + "}";
	}

	@Override
	public boolean hasRelation(IHasProfile prof, RelationType type, boolean isEventSubject) {
		Map<IHasProfile, Set<RelationType>> map = isEventSubject ? this.circumstanceRelationsToProfiles
				: this.profilesRelationsToCircumstance;
		if (map.containsKey(prof)) {
			if (type == null)
				return true;
			return map.get(prof).contains(type);
		}
		return false;
	}

	@Override
	public String toString() {
		return this.eventType.getEventID();
	}

	@Override
	public AbstractEvent clone() {
		AbstractEvent clone = (AbstractEvent) super.clone();
		clone.circumstanceRelationsToProfiles = new HashMap<>(this.circumstanceRelationsToProfiles);
		clone.profilesRelationsToCircumstance = new HashMap<>(this.profilesRelationsToCircumstance);
		for (IHasProfile prof : this.circumstanceRelationsToProfiles.keySet()) {
			clone.circumstanceRelationsToProfiles.put(prof,
					new HashSet<>(clone.circumstanceRelationsToProfiles.get(prof)));
		}
		for (IHasProfile prof : this.profilesRelationsToCircumstance.keySet()) {
			clone.profilesRelationsToCircumstance.put(prof,
					new HashSet<>(clone.profilesRelationsToCircumstance.get(prof)));
		}
		return clone;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof AbstractEvent ae && ae.startTime == this.startTime && ae.eventType.equals(this.eventType)
				&& ae.circumstanceRelationsToProfiles.equals(this.circumstanceRelationsToProfiles)
				&& ae.profilesRelationsToCircumstance.equals(this.profilesRelationsToCircumstance);
	}

}
