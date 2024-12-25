package metaphysical.soul;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import actor.Actor;
import actor.construction.physical.IComponentPart;
import metaphysical.soul.generator.ISoulGenerator;

public abstract class AbstractSoul implements ISoul {

	private UUID id;
	private Actor owner;
	private IComponentPart container;
	private Collection<IComponentPart> containerSingleton = Collections.emptySet();
	private ISoulGenerator soulgen;

	public AbstractSoul(UUID id) {
		this.id = id;
	}

	@Override
	public ISoulGenerator getSoulGenerator() {
		return soulgen;
	}

	public void setSoulGenerator(ISoulGenerator soulgen) {
		this.soulgen = soulgen;
	}

	@Override
	public UUID getUUID() {
		return id;
	}

	@Override
	public String getUniqueName() {
		return "soul" + this.id;
	}

	@Override
	public SpiritType getSpiritType() {
		return SpiritType.SOUL;
	}

	@Override
	public Actor getContainerEntity() {
		return owner;
	}

	@Override
	public int tetherCount() {
		return containerSingleton.size();
	}

	@Override
	public Collection<IComponentPart> getTethers() {
		return containerSingleton;
	}

	@Override
	public IComponentPart getTether() {
		return container;
	}

	@Override
	public boolean isTetheredToWhole() {
		return false;
	}

	@Override
	public boolean isTetheredToSinglePart() {
		return true;
	}

	@Override
	public void tetherSpirit(Actor newActor, Collection<IComponentPart> newParts) {
		if (newParts == null || newParts.size() != 1) {
			throw new IllegalArgumentException("Soul must tether to one part");
		}
		this.owner = newActor;
		this.container = newParts.iterator().next();
		this.containerSingleton = Collections.singleton(container);
	}

	@Override
	public void onRemove(long worldTick) {
		// TODO go to afterlife ig idk
		System.out.println(this + " detached from " + this.container);
		this.container = null;
		this.containerSingleton = Collections.emptySet();
	}

	@Override
	public boolean isTethered() {
		return container != null;
	}

	@Override
	public String toString() {
		return this.getUniqueName();
	}

}
