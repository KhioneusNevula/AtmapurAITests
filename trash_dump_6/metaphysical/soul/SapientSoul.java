package metaphysical.soul;

import java.util.UUID;

import civilization_and_minds.mind.IMind;
import sim.interfaces.IObjectType;

public abstract class SapientSoul extends AbstractSoul {

	private IMind mind;

	public SapientSoul(UUID id, IMind mind) {
		super(id);
		this.mind = mind;
	}

	@Override
	public void tick(long worldTick) {
		mind.tick(worldTick);
	}

	@Override
	public boolean hasMind() {
		return true;
	}

	@Override
	public IMind getMind() {
		return mind;
	}

	@Override
	public IObjectType getObjectType() {
		return SoulType.SAPIENT;
	}

	@Override
	public String report() {
		return "sapientsoul(mind={" + this.mind.report() + "})";
	}

}
