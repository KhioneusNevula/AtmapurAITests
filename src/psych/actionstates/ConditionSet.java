package psych.actionstates;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import psych.actionstates.traits.TraitState;

/**
 * NOT an immutable class
 * 
 * @author borah
 *
 */
public class ConditionSet implements Iterable<TraitState<?>> {

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

	public boolean isEmpty() {
		return conditions.isEmpty();
	}

	public ConditionSet clear() {
		this.conditions.clear();
		return this;
	}

	public <T> TraitState<T> getCondition(T checker) {
		return (TraitState<T>) conditions.get(checker);
	}

	public Collection<TraitState<?>> getAllConditions() {
		return this.conditions.values();
	}

	public Collection<Object> getCheckers() {
		return this.conditions.keySet();
	}

	public ConditionSet(TraitState<?>... conditions) {
		this.addConditions(conditions);

	}

	public ConditionSet(ConditionSet from) {
		for (TraitState<?> state : from) {
			this.addConditions(state);
		}
	}

	@Override
	public Iterator<TraitState<?>> iterator() {
		return conditions.values().iterator();
	}

	@Override
	public String toString() {
		return "ConditionSet for " + this.conditions.values();
	}

}
