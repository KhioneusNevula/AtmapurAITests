package psych.action.goal;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class RequirementWrapper implements Iterable<RequirementGoal> {

	private Set<RequirementGoal> requirements;

	public static final RequirementWrapper EMPTY = new RequirementWrapper();

	public static RequirementWrapper create(RequirementGoal... reqs) {
		if (reqs.length == 0)
			return EMPTY;
		else if (reqs.length == 1 && reqs[0].isEmpty())
			return EMPTY;
		return new RequirementWrapper(reqs);
	}

	private RequirementWrapper(RequirementGoal... requirements) {
		if (requirements.length != 0) {
			this.requirements = new HashSet<>();
		} else {
			this.requirements = Collections.EMPTY_SET;
		}
		for (RequirementGoal goal : requirements) {
			this.requirements.add(goal);
		}
	}

	public Iterable<RequirementGoal> getRequirements() {
		return requirements;
	}

	@Override
	public Iterator<RequirementGoal> iterator() {
		return requirements.iterator();
	}

	/**
	 * return number of states
	 * 
	 * @return
	 */
	public int numStates() {
		return this.requirements.size();
	}

	public int numConditions() {
		int num = 0;
		for (RequirementGoal state : this.requirements)
			num += state.getState().numConditions();
		return num;
	}

	public boolean isEmpty() {
		return this == EMPTY || this.requirements == null || this.requirements.isEmpty();
	}

	/**
	 * Whether this returns multiple requirement sets (i.e. multiple goals to be
	 * accomplished)
	 * 
	 * @return
	 */
	public boolean isMulti() {
		return requirements.size() > 1;
	}

	/**
	 * If this contains a single requirement set
	 * 
	 * @return
	 */
	public boolean isSingle() {
		return requirements.size() == 1;
	}

	public boolean contains(RequirementGoal req) {
		return requirements.contains(req);
	}

	/**
	 * Throws exception if not a single wrapper
	 * 
	 * @return
	 */
	public RequirementGoal getRequirement() {
		if (!isSingle())
			throw new IllegalStateException("Wrapper has " + requirements.size() + " states");
		return requirements.stream().findFirst().get();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ":" + this.requirements;
	}

}
