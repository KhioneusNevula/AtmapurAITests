package psych_first.actionstates.states;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import culture.CulturalContext;
import psych_first.action.goal.NeedGoal;
import psych_first.actionstates.ConditionSet;
import psych_first.actionstates.checks.numeric.IntCheck;
import psych_first.mind.Need;
import sociology.ProfilePlaceholder;
import sociology.ProfileType;

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

	public ActionState(NeedGoal ng) {
		this.putProfilePlaceholder(ProfileType.USER, new ProfilePlaceholder(ProfileType.USER));
		this.getFor(ProfileType.USER).addConditions(new IntCheck<Need>(ng.getFocus(), ng.getLevel(), null));
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
		Map<Object, ConditionSet> cons = new HashMap<>();
		for (ProfileType t : conditions.keySet()) {
			if (!conditions.get(t).isEmpty()) {
				cons.put(this.profiles.containsKey(t) ? this.profiles.get(t) : t, conditions.get(t));
			}
		}
		return cons.toString();
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
	public void eliminateResolvedConditions(ProfilePlaceholder pp, CulturalContext ctxt) {
		if (!pp.isResolved())
			throw new IllegalArgumentException("Placeholder " + pp + " must be RESOLVED");
		ConditionSet set = this.conditions.get(pp.getType());
		ConditionSet new_ = set.conditionsUnfulfilledBy(pp.getActualProfile(), ctxt);
		set.clear();
		set.addConditions(new_);

	}

}
