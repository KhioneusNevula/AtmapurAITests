package psych.actionstates.states;

import java.util.HashMap;

import psych.actionstates.ConditionSet;
import sim.World;
import sociology.IProfile;
import sociology.Profile;

/**
 * The state of the world as it truly is; gets updated by observations of
 * entities but not on its own
 * 
 * @author borah
 *
 */
public class ActualState implements WorldState {

	private HashMap<Profile, ConditionSet> conditions = new HashMap<>();

	private World owner;

	public ActualState(World world) {
		this.owner = world;
	}

	public World getWorld() {
		return owner;
	}

	public void recognizeProfile(Profile being) {
		this.conditions.put(being, new ConditionSet());
	}

	/**
	 * Updates this "actual state" based on the given profile
	 */
	public void updateStateToMatch(Profile observed) {

	}

	@Override
	public ConditionSet getFor(IProfile key) {
		return conditions.get(key);
	}

}
