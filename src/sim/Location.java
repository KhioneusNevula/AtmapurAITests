package sim;

import java.util.UUID;

import culture.CulturalContext;
import psych_first.perception.knowledge.IKnowledgeType;
import sociology.InstanceProfile;
import sociology.Profile;
import sociology.TypeProfile;

public class Location implements IHasProfile, ILocatable {

	public static enum Fundamental implements IKnowledgeType<Location> {
		LOCATION;

		@Override
		public String getName() {
			return "location";
		}

		@Override
		public Class<Location> getValueClass() {
			return Location.class;
		}

		@Override
		public boolean isSocialKnowledge() {
			return false;
		}

		@Override
		public boolean isIdentitySpecific() {
			return true;
		}

	}

	private Profile profile;
	private int x;
	private int y;
	private World world;
	public static final String TYPE_STRING = "_location";

	public Location(int x, int y, World world) {
		this.x = x;
		this.y = y;
		this.world = world;
	}

	public Location(ILocatable l, World world) {
		this(l.getX(), l.getY(), world);
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

	/**
	 * locations have no internal info
	 */
	@Override
	public <T> T getInfo(IKnowledgeType<T> type, CulturalContext ctxt) {
		if (type.isSocialKnowledge() && this.hasProfile()) {
			return this.profile.getInfo(type, ctxt);
		}
		throw new UnsupportedOperationException(
				"Location " + x + "," + y + " has no internal info or profile is null: " + profile);
	}

	/**
	 * locations have no internal info
	 */
	@Override
	public boolean hasInfo(IKnowledgeType<?> type, CulturalContext ctxt) {
		if (type.isSocialKnowledge() && this.hasProfile()) {
			return this.profile.hasInfo(type, ctxt);
		}
		throw new UnsupportedOperationException(
				"Location " + x + "," + y + " has no internal info or profile is null: " + profile);
	}

	@Override
	public UUID getUuid() {

		throw new UnsupportedOperationException("Location " + this.x + "," + this.y + "does not have uuid");
	}

}
