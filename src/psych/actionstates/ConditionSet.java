package psych.actionstates;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import psych.actionstates.checks.Check;
import sociology.Profile;

/**
 * NOT an immutable class
 * 
 * @author borah
 *
 */
public class ConditionSet implements Iterable<Check<?>> {

	/**
	 * Keys are the "checker" for the object, so as to simplify the process of
	 * finding the right condition
	 */
	private Map<Object, Check<?>> conditions = new HashMap<>();

	/**
	 * returns self
	 * 
	 * @param cons
	 * @return
	 */
	public ConditionSet addConditions(ConditionSet cons) {
		conditions.putAll(cons.conditions);
		return this;
	}

	public ConditionSet addConditions(Check<?>... conditions) {
		for (Check<?> con : conditions) {
			this.conditions.put(con.getChecker(), con);
		}
		return this;
	}

	public ConditionSet removeConditions(Check<?>... conditions) {
		for (Check<?> con : conditions) {
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

	/**
	 * Whether this has a condition for the given checker
	 * 
	 * @param checker
	 * @return
	 */
	public boolean hasConditionFor(Object checker) {
		return conditions.containsKey(checker);
	}

	public boolean hasConditionForPredicate(Predicate<Check<?>> pred) {
		return conditions.values().stream().anyMatch(pred);
	}

	public <T> Set<Check<T>> getConditionsForPredicate(Predicate<Check<?>> pred) {
		return conditions.values().stream().filter(pred).map((a) -> (Check<T>) a).collect(Collectors.toSet());
	}

	/**
	 * Whether this has the given condition
	 * 
	 * @param trait
	 * @return
	 */
	public boolean hasCondition(Check<?> trait) {
		return conditions.containsValue(trait);
	}

	public <T> Check<T> getCondition(T checker) {
		return (Check<T>) conditions.get(checker);
	}

	public Collection<Check<?>> getAllConditions() {
		return this.conditions.values();
	}

	public Collection<Object> getCheckers() {
		return this.conditions.keySet();
	}

	public ConditionSet(Check<?>... conditions) {
		this.addConditions(conditions);

	}

	public ConditionSet(ConditionSet from) {
		for (Check<?> state : from) {
			this.addConditions(state);
		}
	}

	@Override
	public Iterator<Check<?>> iterator() {
		return conditions.values().iterator();
	}

	@Override
	public String toString() {
		return "conditions"
				+ this.conditions.values().stream().map((a) -> "\n" + a.report()).collect(Collectors.toSet());
	}

	public ConditionSet conditionsUnfulfilledBy(Profile param) {

		ConditionSet set = new ConditionSet();

		for (Object checker : getCheckers()) {
			Check<?> thisT = getCondition(checker);
			if (thisT.satisfies(param) != Boolean.TRUE) {
				set.addConditions(thisT);
			}
		}
		return set;

	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ConditionSet cs) {
			return this.conditions.equals(cs.conditions);
		}
		return super.equals(obj);
	}

}
