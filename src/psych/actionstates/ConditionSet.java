package psych.actionstates;

import java.util.HashMap;
import java.util.Map;

import psych.actionstates.traits.TraitState;

/**
 * NOT an immutable class
 * 
 * @author borah
 *
 */
public class ConditionSet {

	/**
	 * Keys are the "checker" for the object, so as to simplify the process of
	 * finding the right condition
	 */
	private Map<Object, TraitState<?>> conditions = new HashMap<>();

	public ConditionSet addConditions(TraitState<?>... conditions) {
		for (TraitState<?> con : conditions) {
			this.conditions.put(con.getChecker(), con);
		}
		return this;
	}

	public ConditionSet removeConditions(TraitState<?>... conditions) {
		for (TraitState<?> con : conditions) {
			this.conditions.remove(con.getChecker(), con);
		}
		return this;
	}

	public ConditionSet clear() {
		this.conditions.clear();
		return this;
	}

	public ConditionSet(TraitState<?>... conditions) {
		this.addConditions(conditions);

	}

}
