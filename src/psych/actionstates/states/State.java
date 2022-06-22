package psych.actionstates.states;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import psych.actionstates.ConditionSet;
import psych.actionstates.checks.Check;
import sociology.Profile;
import sociology.ProfilePlaceholder;

public interface State extends Cloneable {

	public abstract static class ProfileType implements Comparable<ProfileType> {

		public static final InstanceProfileType USER = new InstanceProfileType("USER");
		public static final InstanceProfileType TARGET = new InstanceProfileType("TARGET");
		public static final InstanceProfileType TOOL = new InstanceProfileType("TOOL");
		private static final Map<String, ProfileType> profiles = new TreeMap<>();

		public final String id;

		private ProfileType(String id) {
			this.id = id;
			profiles.put(id, this);

		}

		public abstract TypeProfileType typeVersion();

		public abstract InstanceProfileType instanceVersion();

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

		public static class TypeProfileType extends ProfileType {
			public final InstanceProfileType instanceVersion;

			private TypeProfileType(String outerID, InstanceProfileType instanceVersion) {
				super(outerID + "_TYPE");
				this.instanceVersion = instanceVersion;
			}

			@Override
			public InstanceProfileType instanceVersion() {
				return instanceVersion;
			}

			@Override
			public TypeProfileType typeVersion() {
				throw new UnsupportedOperationException("TypeProfile " + this.id + " has no typeVersion");
			}
		}

		public static class InstanceProfileType extends ProfileType {
			public final TypeProfileType typeVersion;

			private InstanceProfileType(String id) {
				super(id);
				this.typeVersion = new TypeProfileType(id, this);

			}

			@Override
			public InstanceProfileType instanceVersion() {
				throw new UnsupportedOperationException("InstanceProfile " + this.id + " has no instanceVersion");

			}

			@Override
			public TypeProfileType typeVersion() {
				return typeVersion;
			}
		}

	}

	/**
	 * Gets the placeholder for this profile; null if this state doesn't use that
	 * specific profile
	 * 
	 * @param key
	 * @return
	 */
	public ProfilePlaceholder getProfile(ProfileType key);

	/**
	 * Get the associated conditions for the given profile; may be null if the
	 * profile is not relevant
	 * 
	 * @param key
	 * @return
	 */
	public ConditionSet getFor(ProfileType key);

	public Collection<ConditionSet> getAllConditions();

	/**
	 * Whether this state has no actual conditions
	 * 
	 * @return
	 */
	public default boolean isEmpty() {
		for (ConditionSet con : getAllConditions()) {
			if (!con.isEmpty())
				return false;
		}
		return true;
	}

	public default int numConditions() {
		return (int) getAllConditions().stream().flatMap((a) -> a.getAllConditions().stream()).count();
	}

	/**
	 * Returns the State representing all conditions that remain unfulfilled given
	 * the State parameter. Assumes that "this" object is the requirements, and the
	 * parameter is the result
	 * 
	 * @param result
	 * @return
	 */

	public default State conditionsUnfulfilledBy(State result) {
		ActionState state = new ActionState();
		for (ProfileType type : ProfileType.values()) {
			ConditionSet otherCons = result.getFor(type);
			ConditionSet thisCons = this.getFor(type);
			if (thisCons == null)
				continue;

			for (Object checker : thisCons.getCheckers()) {
				Check<?> thisT = thisCons.getCondition(checker);
				Check<?> otherT = otherCons.getCondition(checker);
				if (otherT == null || !otherT.satisfies(thisT)) {
					state.getFor(type).addConditions(thisT);
				}
			}

		}
		return state;
	}

	/**
	 * Profile is the profile of the user
	 * 
	 * @param param
	 * @return
	 */
	public default State conditionsUnfulfilledBy(Profile param) {
		ActionState state = new ActionState();
		for (ProfileType type : ProfileType.values()) {
			ConditionSet thisCons = this.getFor(type);
			if (thisCons == null)
				continue;

			state.getFor(type).addConditions(thisCons.conditionsUnfulfilledBy(param));

		}
		return state;

	}

	/**
	 * elimintates conditions resolved by the given RESOLVED profile placeholder
	 */
	public void eliminateResolvedConditions(ProfilePlaceholder pp);

	public String conditionsString();

	ActionState putProfilePlaceholder(ProfileType type, ProfilePlaceholder pp);

	ActionState setProfilePlaceholders(Map<ProfileType, ProfilePlaceholder> values);

	public State clone() throws CloneNotSupportedException;

}
