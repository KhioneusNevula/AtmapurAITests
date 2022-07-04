package sociology;

import sim.IHasProfile;
import sim.World;

public class TypeProfile extends Profile {

	private World world;

	public TypeProfile(String name, World of) {
		super(name);
		this.world = of;
	}

	public World getWorld() {
		return world;
	}

	@Override
	public IHasProfile getOwner() {
		throw new UnsupportedOperationException("Cannot access owner of type profile");
	}

	@Override
	public void setOwner(IHasProfile owner) {

		throw new UnsupportedOperationException("Cannot set owner of type profile");
	}

	@Override
	public TypeProfile getTypeProfile() {
		throw new UnsupportedOperationException("Type profile has no type of its own");
	}

	@Override
	public boolean isTypeProfile() {
		return true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "t;" + "\"" + this.getName() + "\"";
	}
}
