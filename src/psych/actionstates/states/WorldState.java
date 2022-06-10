package psych.actionstates.states;

import psych.actionstates.ConditionSet;
import sociology.IProfile;

public interface WorldState {

	public ConditionSet getFor(IProfile key);

}
