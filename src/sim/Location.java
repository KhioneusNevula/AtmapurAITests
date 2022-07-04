package sim;

import sociology.InstanceProfile;
import sociology.Profile;
import sociology.TypeProfile;

public class Location implements IHasProfile, ILocatable {

	private Profile profile;
	private int x;
	private int y;
	private World world;
	public static final String TYPE_STRING = "_location";

	public Location(int x, int y, World world, boolean makeProfile) {
		this.x = x;
		this.y = y;
		this.world = world;
		if (makeProfile)
			makeProfile();
	}

	public Location(ILocatable l, World world, boolean makeProfile) {
		this(l.getX(), l.getY(), world, makeProfile);
	}

	public Location makeProfile() {
		if (this.profile == null) {
			TypeProfile locPr = this.world.getOrCreateTypeProfile(TYPE_STRING);
			this.profile = new InstanceProfile(this, locPr, "Location(" + x + "," + y + ")");
		}
		return this;
	}

	@Override
	public TypeProfile getType() {
		return this.getProfile() != null ? this.getProfile().getTypeProfile()
				: this.world.getOrCreateTypeProfile(TYPE_STRING);
	}

	public Location setProfile(Profile p) {
		this.profile = p;
		return this;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public Profile getProfile() {
		return profile;
	}

	@Override
	public Location getLocation() {
		return this;
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public String toString() {
		return "L(" + x + "," + y + ")";
	}

}
