package sociology;

import sim.IHasProfile;
import sim.World;

public class InstanceProfile extends Profile {

	private TypeProfile type;
	private IHasProfile owner;

	public InstanceProfile(IHasProfile owner, TypeProfile type, String name) {
		super(name);
		this.owner = owner;
		this.type = type;
	}

	@Override
	public TypeProfile getTypeProfile() {
		return type;
	}

	@Override
	public boolean isTypeProfile() {
		return false;
	}

	public IHasProfile getOwner() {
		return owner;
	}

	public void setOwner(IHasProfile owner) {
		this.owner = owner;
	}

	@Override
	public World getWorld() {
		return owner.getWorld();
	}

}
