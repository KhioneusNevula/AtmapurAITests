package psych.actionstates.states;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import psych.actionstates.ConditionSet;
import sociology.ProfilePlaceholder;

public class ActionState implements State {

	private EnumMap<ProfileType, ConditionSet> conditions = new EnumMap<>(ProfileType.class);
	private EnumMap<ProfileType, ProfilePlaceholder> profiles = new EnumMap<>(ProfileType.class);

	public ActionState(Map<ProfileType, ProfilePlaceholder> profiles) {
		this();
		this.profiles.putAll(profiles);
	}

	public ActionState() {
		for (ProfileType type : ProfileType.values()) {
			conditions.put(type, new ConditionSet());
		}
	}

	public ActionState setProfilePlaceholders(Map<ProfileType, ProfilePlaceholder> values) {
		this.profiles.putAll(values);
		return this;
	}

	@Override
	public ConditionSet getFor(ProfileType key) {
		return conditions.get(key);
	}

	@Override
	public Collection<ConditionSet> getAllConditions() {
		return conditions.values();
	}

	@Override
	public ProfilePlaceholder getProfile(ProfileType key) {
		return profiles.get(key);
	}

	@Override
	public String conditionsString() {
		return this.conditions.toString();
	}

}
