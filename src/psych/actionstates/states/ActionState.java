package psych.actionstates.states;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import psych.actionstates.ConditionSet;
import psych.actionstates.checks.Check;
import sociology.ProfilePlaceholder;

public class ActionState implements State {

	private TreeMap<ProfileType, ConditionSet> conditions = new TreeMap<>();
	private TreeMap<ProfileType, ProfilePlaceholder> profiles = new TreeMap<>();

	public ActionState(Map<ProfileType, ProfilePlaceholder> profiles) {
		this();
		this.profiles.putAll(profiles);

	}

	public ActionState() {
		for (ProfileType type : ProfileType.values()) {
			conditions.put(type, new ConditionSet());
		}
	}

	@Override
	public ActionState setProfilePlaceholders(Map<ProfileType, ProfilePlaceholder> values) {
		this.profiles.putAll(values);
		return this;
	}

	@Override
	public ActionState putProfilePlaceholder(ProfileType type, ProfilePlaceholder pp) {
		this.profiles.put(type, pp);
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

	@Override
	public ActionState clone() throws CloneNotSupportedException {
		ActionState state = new ActionState(this.profiles);
		for (ProfileType type : this.conditions.keySet()) {
			state.conditions.put(type, new ConditionSet(this.conditions.get(type)));
		}
		return state;
	}

	@Override
	public void eliminateResolvedConditions(ProfilePlaceholder pp) {
		if (!pp.isResolved())
			throw new IllegalArgumentException("Placeholder " + pp + " must be RESOLVED");
		ConditionSet set = this.conditions.get(pp.getType());
		for (Check<?> check : set) {
			if (check.satisfies(pp.getActualProfile())) {
				set.removeConditions(check);
			}
		}

	}

}
