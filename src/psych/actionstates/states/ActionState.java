package psych.actionstates.states;

import java.util.TreeMap;

import psych.actionstates.ConditionSet;

/**
 * A hypothetical worldstate that is required by an action or a result of an
 * action
 * 
 * @author borah
 *
 */
public class ActionState implements WorldState {

	@Override
	public TreeMap<String, ConditionSet> getConditions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConditionSet getUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TreeMap<String, ConditionSet> computeUnsatisfiedConditions(WorldState result) {
		// TODO Auto-generated method stub
		return null;
	}

}
