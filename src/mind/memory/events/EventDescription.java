package mind.memory.events;

import java.util.Collection;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import mind.action.IActionType;
import mind.concepts.type.IMeme;
import mind.memory.events.IEvent.EventRole;
import phenomenon.IPhenomenonType;

public class EventDescription implements Comparable<EventDescription> {

	private IEventType eventType;
	private Multimap<EventRole, IMeme> props = ImmutableMultimap.of();

	public EventDescription(IActionType<?> atype) {
		this.eventType = atype;
	}

	public EventDescription(IPhenomenonType atype) {
		this.eventType = atype;
	}

	public EventDescription addProperties(EventRole forRole, Collection<IMeme> props) {
		ImmutableMultimap.Builder<EventRole, IMeme> builder = ImmutableMultimap.<EventRole, IMeme>builder()
				.putAll(this.props);
		props.forEach((a) -> builder.put(forRole, a));
		this.props = builder.build();
		return this;
	}

	public boolean isAction() {
		return this.eventType instanceof IActionType;
	}

	public boolean isPhenomenon() {
		return this.eventType instanceof IPhenomenonType;
	}

	public IActionType<?> getAsActionType() {
		return (IActionType<?>) eventType;
	}

	public IPhenomenonType getAsPhenomenonType() {
		return (IPhenomenonType) eventType;
	}

	public IEventType getEventType() {
		return eventType;
	}

	public Collection<IMeme> getProperties(EventRole forRole) {
		return this.props.get(forRole);
	}

	@Override
	public int compareTo(EventDescription o) {
		return eventType.getUniqueName().compareTo(o.eventType.getUniqueName()) + props.size() - o.props.size();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EventDescription ed) {
			return eventType.equals(ed.eventType) && props.equals(ed.props);
		}
		return false;
	}
}
