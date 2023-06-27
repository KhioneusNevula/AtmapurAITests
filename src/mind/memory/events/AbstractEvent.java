package mind.memory.events;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import mind.concepts.type.Profile;
import sim.Location;

public abstract class AbstractEvent implements IEvent {

	private Set<Profile> objects = Set.of();
	private Set<Profile> causes = Set.of();
	private Location location;
	private String name;
	private long time;

	public AbstractEvent(String uname, Location location, long time) {
		this.name = uname;
		this.time = time;
		this.location = location;
	}

	protected void addObject(Profile... obj) {
		this.objects = ImmutableSet.<Profile>builder().addAll(objects).add(obj).build();
	}

	protected void addCause(Profile... obj) {
		this.causes = ImmutableSet.<Profile>builder().addAll(causes).add(obj).build();
	}

	@Override
	public String getUniqueName() {
		return this.name;
	}

	@Override
	public Collection<Profile> object() {
		return this.objects;
	}

	@Override
	public Collection<Profile> cause() {
		return this.causes;
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

}
