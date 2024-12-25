package biology.sensing;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import actor.construction.properties.IAbilityStat;

public abstract class AbstractSense implements ISense {

	private String name;
	private Set<IAbilityStat<?>> ss;

	public AbstractSense(String name, Iterable<IAbilityStat<?>> stats) {
		this.name = name;
		this.ss = ImmutableSet.copyOf(stats);
	}

	public AbstractSense(String name, IAbilityStat<?>... stats) {
		this.name = name;
		this.ss = ImmutableSet.copyOf(stats);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int hashCode() {
		return this.name.hashCode() + this.ss.hashCode();
	}

	@Override
	public Collection<IAbilityStat<?>> getExpectedStats() {
		return ss;
	}

}
