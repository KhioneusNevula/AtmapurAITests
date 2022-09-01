package sociology;

import sim.IHasProfile;

public class TypeProfile extends Profile {

	public TypeProfile(String name) {
		super(name);
	}

	@Override
	public IHasProfile getOwner() {
		return null;
	}

	@Override
	public void setOwner(IHasProfile owner) {

		throw new UnsupportedOperationException("Cannot set owner of type profile");
	}

	@Override
	public TypeProfile getTypeProfile() {
		return null;
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

	@Override
	public boolean equals(Object obj) {
		return obj instanceof TypeProfile tp && tp.getName().equals(this.getName());
	}
}
