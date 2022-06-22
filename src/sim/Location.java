package sim;

import sociology.Profile;

public class Location implements IHasProfile, ILocatable {

	private Profile profile;
	private int x;
	private int y;
	private World world;

	public Location(int x, int y, World world, boolean makeProfile) {
		this.x = x;
		this.y = y;
		if (makeProfile)
			makeProfile();
		this.world = world;
	}

	public Location(ILocatable l, World world, boolean makeProfile) {
		this(l.getX(), l.getY(), world, makeProfile);
	}

	public Location makeProfile() {
		if (this.profile == null)
			this.profile = new Profile(this, "Location(" + x + "," + y + ")");
		return this;
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

}