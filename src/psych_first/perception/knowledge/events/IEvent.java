package psych_first.perception.knowledge.events;

import java.util.Collection;

import psych_first.perception.knowledge.IOccurrence;
import sim.IHasProfile;
import sim.World;

public interface IEvent extends IOccurrence {

	public World getWorld();

	/**
	 * returns the time this event started, or null if the start time is irrelevant
	 * 
	 * @return
	 */
	public Long since();

	public psych_first.perception.knowledge.events.EventType<?> getEventType();

	/**
	 * gets the relation this profile has with this event, with the profile as the
	 * subject
	 * 
	 * @param from
	 * @return
	 */
	public Collection<RelationType> getRelationFrom(IHasProfile from);

	/**
	 * gets the relation this event has with this profile, with the event as the
	 * subject
	 * 
	 * @param to
	 * @return
	 */
	public Collection<RelationType> getRelationTo(IHasProfile to);

	/**
	 * gets the profiles with the given relationship to the event itself
	 * 
	 * @param type
	 * @param isEventSubject
	 * @return
	 */
	Collection<IHasProfile> getProfiles(RelationType type, boolean isEventSubject);

	/**
	 * whether this profile has this relationship with this event; null for "type"
	 * to check if it has any relatonship at all
	 * 
	 * @param prof
	 * @param type           can be null
	 * @param isEventSubject
	 * @return
	 */
	public boolean hasRelation(IHasProfile prof, RelationType type, boolean isEventSubject);

}
