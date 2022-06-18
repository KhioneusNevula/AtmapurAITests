package psych.actionstates.states;

import java.util.TreeMap;

import psych.actionstates.ConditionSet;
import psych.actionstates.ProfilePlaceholder;
import psych.actionstates.ProfilePotential;
import psych.actionstates.traits.TraitState;
import sociology.IProfile;

/**
 * A perceived state of the world when mapping actions and action progress while
 * planning a sequence of actions
 * 
 * @author borah
 *
 */
public class TheoreticState extends AbstractHypotheticalState<ProfilePlaceholder> {

	private TreeMap<ProfilePlaceholder, ProfilePotential> placeholderToPotential = new TreeMap<>();
	private TreeMap<ProfilePotential, ProfilePlaceholder> potentialToPlaceholder = new TreeMap<>();

	public TheoreticState(String... profiles) {
		super(profiles);
	}

	public ProfilePotential getAssociatedPotential(ProfilePlaceholder pp) {
		return placeholderToPotential.get(pp);
	}

	public ProfilePlaceholder getAssociatedPlaceholder(ProfilePotential pp) {
		return potentialToPlaceholder.get(pp);
	}

	public ProfilePotential unassignPlaceholder(ProfilePlaceholder p) {
		ProfilePotential pp = placeholderToPotential.remove(p);
		if (pp == null)
			return pp;
		potentialToPlaceholder.remove(pp);
		return pp;
	}

	public void assignPotentialToPlaceholder(ProfilePlaceholder key, ProfilePotential value) {
		this.placeholderToPotential.put(key, value);
		this.potentialToPlaceholder.put(value, key);
	}

	public ConditionSet removeProfile(String name) {
		ProfilePlaceholder pp = stringToProfile.remove(name);
		if (pp == null)
			return null;
		ConditionSet set = conditions.remove(pp);
		ProfilePotential aa = placeholderToPotential.remove(pp);
		if (aa != null)
			potentialToPlaceholder.remove(aa);

		return set;
	}

	public boolean addConditionsToUserProfile(TraitState<?>... conds) {
		return addConditions(USERSTRING, conds);
	}

	@Override
	public ConditionSet getFor(IProfile key) {
		IProfile ikey = key instanceof ProfilePotential ? potentialToPlaceholder.get(key) : key;
		if (ikey == null)
			return null;
		return conditions.get(ikey);
	}

	@Override
	protected ProfilePlaceholder createFromString(String name) {
		return new ProfilePlaceholder(name);
	}

}
