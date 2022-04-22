package psych;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import psych.conditions.ProfileCondition;
import psych.conditions.RelationalCondition;

public class ActionNode {

	private ActionSearch search;
	private ActionNode next;
	private Set<ActionNode> previous = new HashSet<>();
	private Action action;
	private Set<RelationalCondition> unsatisfiedConditions = new HashSet<>();
	private Map<String, Set<ProfileCondition<?>>> unsatisfiedProfiles = new HashMap<>();

	public ActionNode(ActionSearch creator, ActionNode previous, Action action) {

	}

	public static class RelationalConditionSet {
		private Set<RelationalCondition> conditions = new HashSet<>();

		public RelationalConditionSet(Collection<RelationalCondition> col) {
			this.conditions.addAll(col);
		}

		public int numMatches(RelationalConditionSet other) {
			int ma = 0;
			for (RelationalCondition con : conditions) {
				for (RelationalCondition othercon : conditions) {
					if (con.matches(othercon)) {
						ma++;
					}
				}
			}
			return ma;
		}
	}
}
