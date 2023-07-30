package mind.memory.events;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import mind.action.IActionType;
import mind.action.IInteractionInstance;
import mind.concepts.type.IMeme;
import phenomenon.IPhenomenonType;
import sim.Location;

public class Consequence implements IEvent, Comparable<Consequence> {

	private Multimap<EventRole, IMeme> roles = ImmutableMultimap.of();
	private IEventType eventType;
	private Set<IMeme> phenomProps = Set.of();

	@Override
	public String getUniqueName() {
		return "consequence_" + roles;
	}

	@Override
	public Collection<? extends IMeme> object() {
		return roles.get(EventRole.OBJECT);
	}

	@Override
	public Collection<? extends IMeme> cause() {
		return roles.get(EventRole.OBJECT);
	}

	@Override
	public <T extends IMeme> Collection<T> getForRole(EventRole role) {
		return (Collection<T>) roles.get(role);
	}

	@Override
	public Location location() {
		return null;
	}

	@Override
	public boolean hasLocation() {
		return false;
	}

	@Override
	public IActionType<?> action() {
		return this.eventType.asActionType();
	}

	@Override
	public boolean isAction() {
		return eventType.isActionType();
	}

	@Override
	public long time() {
		return -1;
	}

	@Override
	public IPhenomenonType phenomenon() {
		return eventType.asPhenomenonType();
	}

	@Override
	public Collection<IMeme> phenomenonProperties() {
		return phenomProps;
	}

	@Override
	public boolean isPhenomenon() {
		return eventType.isPhenomenonType();
	}

	@Override
	public IInteractionInstance interaction() {
		return null;
	}

	@Override
	public boolean isInteraction() {
		return eventType.isActionType() ? eventType.asActionType().isInteraction() : false;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Consequence con) {
			return con.eventType.equals(this.eventType) && con.roles.equals(this.roles)
					&& con.phenomProps.equals(this.phenomProps);
		}
		return false;
	}

	@Override
	public int compareTo(Consequence o) {
		return this.eventType.getUniqueName().compareTo(o.eventType.getUniqueName()) + roles.size() + phenomProps.size()
				- (o.roles.size() + o.phenomProps.size());
	}

	@Override
	public int hashCode() {
		return roles.hashCode() + eventType.hashCode() + phenomProps.hashCode();
	}

}
