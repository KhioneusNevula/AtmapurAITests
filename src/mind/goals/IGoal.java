package mind.goals;

import mind.concepts.type.IConcept;
import mind.memory.IHasKnowledge;

/**
 * a potential goal; something that is needed by an individual or society and
 * may be traded
 * 
 * @author borah
 *
 */
public interface IGoal extends IConcept {

	public static enum Type {
		/** Membership to a group/role */
		MEMBERSHIP,
		/** creation of a role or a change in something cultural */
		STRUCTURE,
		/** A flow of resources */
		FLOW,
		/** A continuously necessary conduct (a Role to be part of) */
		CONDUCT,
		/** A goal that must be completed by a certain deadline */
		TASK,
		/**
		 * the essential relationship trade; feelings of belonging, friendship, and love
		 */
		COMMUNITY,
		/**
		 * Placeholder purpose for a goal that indicates goals which do nothing. This
		 * may be the end of a goal stack, or a middle element indicating a lack of
		 * conditions between
		 */
		NONE;
	}

	/**
	 * the type of-goal this is
	 */
	public Type getGoalType();

	/**
	 * Whether this is an unknown goal
	 * 
	 * @return
	 */
	default boolean isUnknown() {
		return false;
	}

	/**
	 * Whether this is a placeholder goal indicating finishedness
	 * 
	 * @return
	 */
	default boolean isDone() {
		return false;
	}

	/**
	 * Whether this goal is an individual's goal
	 * 
	 * @return
	 */
	public boolean individualGoal();

	/**
	 * Whether this goal is a societal-level goal
	 * 
	 * @return
	 */
	public boolean societalGoal();

	/**
	 * Checks whether the goal is complete for a certain entity, by the action if
	 * applicable
	 */
	boolean isComplete(IHasKnowledge entity);

	/**
	 * Whether these two goals are functionally equivalent. Note that the first goal
	 * is the "replacer" goal -- this is the goal which will be checked to replace
	 * the argument. So the goal that is the argument should be the "vaguer" goal,
	 * if a more specific goal is to replace a vaguer goal
	 * 
	 * @param other
	 * @return
	 */
	default boolean equivalent(IGoal other) {
		return this.getGoalType() == other.getGoalType();
	}

}
