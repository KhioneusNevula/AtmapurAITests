package psych.actionstates.states;

import java.util.Arrays;
import java.util.List;

import psych.actionstates.ConditionSet;
import psych.actionstates.ProfilePlaceholder;
import psych.actionstates.ProfilePotential;
import psych.actionstates.traits.TraitState;
import sociology.IProfile;

/**
 * A hypothetical worldstate that is required by an action or a result of an
 * action
 * 
 * @author borah
 *
 */
public class ActionState extends AbstractHypotheticalState<ProfilePotential> {

	public ActionState(String... profiles) {
		super(profiles);
	}

	public ProfilePotential findBestMatch(ProfilePlaceholder forP, ConditionSet set, ProfilePotential... already) {
		ProfilePotential matcher = null;
		List<ProfilePotential> used = Arrays.asList(already);
		int maxCount = 0;

		for (ProfilePotential pp : this.conditions.keySet()) {
			if (used.contains(pp))
				continue;
			int i = 0;
			ConditionSet ppset = conditions.get(pp);
			for (TraitState<?> ts : set) {
				TraitState<?> c = ppset.getCondition(ts.getChecker());
				if (c != null && ts.satisfies(c)) {
					i++;
				}
			}
			if (i > maxCount) {
				maxCount = i;
				matcher = pp;
			}
		}
		return matcher;
	}

	@Override
	public ConditionSet getFor(IProfile key) {
		if (key instanceof ProfilePotential)
			return conditions.get(key);
		throw new IllegalArgumentException("key must be instance of ProfilePotential");
	}

	@Override
	protected ProfilePotential createFromString(String name) {
		return new ProfilePotential(name);
	}

}
