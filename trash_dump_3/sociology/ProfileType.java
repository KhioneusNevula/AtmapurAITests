package sociology;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public abstract class ProfileType implements Comparable<ProfileType> {

	private static final Map<String, ProfileType> profiles = new TreeMap<>();
	public static final ProfileType.InstanceProfileType USER = new InstanceProfileType("USER");
	public static final ProfileType.InstanceProfileType TARGET = new InstanceProfileType("TARGET");
	public static final ProfileType.InstanceProfileType TOOL = new InstanceProfileType("TOOL");

	public final String id;

	private ProfileType(String id) {
		this.id = id;
		profiles.put(id, this);

	}

	public abstract ProfileType.TypeProfileType typeVersion();

	public abstract ProfileType.InstanceProfileType instanceVersion();

	public boolean isTypePlaceholder() {
		return this instanceof ProfileType.TypeProfileType;
	}

	public boolean isInstancePlaceholder() {
		return this instanceof ProfileType.InstanceProfileType;
	}

	public static Collection<ProfileType> values() {
		return profiles.values();
	}

	public static ProfileType valueOf(String id) {
		return profiles.get(id);
	}

	@Override
	public int compareTo(ProfileType o) {
		return this.id.compareTo(o.id);
	}

	@Override
	public String toString() {
		return this.id;
	}

	public static class TypeProfileType extends ProfileType {
		public final ProfileType.InstanceProfileType instanceVersion;

		private TypeProfileType(String outerID, ProfileType.InstanceProfileType instanceVersion) {
			super(outerID + "_TYPE");
			this.instanceVersion = instanceVersion;
		}

		@Override
		public ProfileType.InstanceProfileType instanceVersion() {
			return instanceVersion;
		}

		@Override
		public ProfileType.TypeProfileType typeVersion() {
			throw new UnsupportedOperationException("TypeProfile " + this.id + " has no typeVersion");
		}
	}

	public static class InstanceProfileType extends ProfileType {
		public final ProfileType.TypeProfileType typeVersion;

		private InstanceProfileType(String id) {
			super(id);
			this.typeVersion = new TypeProfileType(id, this);

		}

		@Override
		public ProfileType.InstanceProfileType instanceVersion() {
			throw new UnsupportedOperationException("InstanceProfile " + this.id + " has no instanceVersion");

		}

		@Override
		public ProfileType.TypeProfileType typeVersion() {
			return typeVersion;
		}
	}

}