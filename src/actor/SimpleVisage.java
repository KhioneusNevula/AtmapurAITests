package actor;

import java.util.Collection;
import java.util.Set;

import biology.systems.types.ISensor;
import mind.concepts.type.SenseProperty;

public class SimpleVisage implements IVisage {

	private IUniqueExistence owner;

	public SimpleVisage(IUniqueExistence owner) {
		this.owner = owner;
	}

	@Override
	public IUniqueExistence getOwner() {
		return owner;
	}

	@Override
	public Collection<SenseProperty<?>> getSensableTraits(ISensor sensor) {
		return Set.of();
	}

	@Override
	public <T> T getTrait(SenseProperty<T> property) {
		return null;
	}

	@Override
	public ITemplate getSpecies() {
		return owner.getSpecies();
	}

	@Override
	public boolean isInvisible() {
		return false;
	}

}
