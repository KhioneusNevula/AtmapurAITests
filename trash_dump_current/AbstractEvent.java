package mind.memory.events;

import java.util.Collection;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import mind.concepts.type.IMeme;
import mind.concepts.type.Profile;
import sim.Location;

public abstract class AbstractEvent implements IEvent {

	private Multimap<EventRole, ? extends IMeme> profiles = ImmutableMultimap.of();
	private Location location;
	private String name;
	private long time;

	public AbstractEvent(String uname, Location location, long time) {
		this.name = uname;
		this.time = time;
		this.location = location;
	}

	protected void add(EventRole role, Collection<? extends IMeme> obj) {

		ImmutableMultimap.Builder<EventRole, IMeme> builder = ImmutableMultimap.builder();
		builder.putAll(profiles);
		obj.forEach((a) -> builder.put(role, a));
		profiles = builder.build();
	}

	@Override
	public String getUniqueName() {
		return this.name;
	}

	@Override
	public Collection<Profile> object() {
		return this.getForRole(EventRole.OBJECT);
	}

	@Override
	public Collection<Profile> cause() {
		return this.getForRole(EventRole.CAUSER);
	}

	@Override
	public boolean hasLocation() {
		return location != null;
	}

	@Override
	public Location location() {
		return this.location;
	}

	@Override
	public long time() {
		return time;
	}

	@Override
	public <T extends IMeme> Collection<T> getForRole(EventRole role) {
		return (Collection<T>) this.profiles.get(role);
	}

}
