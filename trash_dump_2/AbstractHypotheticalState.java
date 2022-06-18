package psych.actionstates.states;

import java.util.TreeMap;

import psych.actionstates.ConditionSet;
import psych.actionstates.traits.TraitState;
import sociology.IProfile;

public abstract class AbstractHypotheticalState<Pro extends IProfile> implements WorldState {

	protected static final String USERSTRING = "user";

	protected TreeMap<String, Pro> stringToProfile = new TreeMap<>();
	protected TreeMap<Pro, ConditionSet> conditions = new TreeMap<>();

	public AbstractHypotheticalState(String... profiles) {
		newProfile(USERSTRING);
		for (String profile : profiles) {
			newProfile(profile);
		}
	}

	public Pro getUserProfile() {
		return getPlaceholder(USERSTRING);
	}

	protected abstract Pro createFromString(String name);

	public Pro getPlaceholder(String id) {
		return stringToProfile.get(id);
	}

	public ConditionSet getConditions(Pro pp) {
		return conditions.get(pp);
	}

	public ConditionSet removeProfile(String name) {
		Pro pp = stringToProfile.remove(name);
		if (pp == null)
			return null;
		ConditionSet set = conditions.remove(pp);

		return set;
	}

	/**
	 * Will return an existing one if such a profile already is present; creates a
	 * new empty placeholder for the given string
	 * 
	 * @param placeholder
	 * @return
	 */
	public Pro newProfile(String placeholder) {
		Pro pp = stringToProfile.get(placeholder);
		if (pp == null) {
			pp = createFromString(placeholder);
			stringToProfile.put(placeholder, pp);
			conditions.put(pp, new ConditionSet());

			return pp;
		} else {
			return pp;
		}
	}

	/**
	 * Adds conditions to the given profile. True if the profile exists
	 * 
	 * @param profile
	 * @param conditions
	 */
	public boolean addConditions(String profile, TraitState<?>... conditions) {
		Pro pp = this.getPlaceholder(profile);
		if (pp == null)
			return false;
		this.conditions.get(pp).addConditions(conditions);
		return true;
	}

	public boolean addConditionsToUserProfile(TraitState<?>... conds) {
		return addConditions(USERSTRING, conds);
	}

}
