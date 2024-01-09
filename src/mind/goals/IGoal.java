package mind.goals;

import mind.concepts.type.IMeme;
import mind.thought_exp.IUpgradedHasKnowledge;

/**
 * a potential goal; something that is needed by an individual or society and
 * may be traded
 * 
 * @author borah
 *
 */
public interface IGoal extends IMeme {

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
		PERSONAL,
		/**
		 * Placeholder purpose for a goal that indicates goals which do nothing. This
		 * may be the end of a goal stack, or a middle element indicating a lack of
		 * conditions between
		 */
		NONE;

	}

	default IConduct asConduct() {
		return (IConduct) this;
	}

	default IPersonalRelationship asPersonalRelationship() {
		return (IPersonalRelationship) this;
	}

	default IResource asResource() {
		return (IResource) this;
	}

	default IRoleGoal asRoleGoal() {
		return (IRoleGoal) this;
	}

	default ITaskGoal asTask() {
		return (ITaskGoal) this;
	}

	/**
	 * the type of-goal this is
	 */
	public Type getGoalType();

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
	 * Checks whether the goal is complete for a certain entity.
	 */
	boolean isComplete(IUpgradedHasKnowledge entity);

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

	public Priority getPriority();

	@Override
	default IMemeType getMemeType() {
		return MemeType.GOAL;
	}

	public static enum Priority {
		/**
		 * a goal of this priority is not driven by rational need; this would be
		 * something resultant of insanity or whatever
		 */
		OBSESSION,
		/** the goal of this priority is mortal in nature */
		VITAL,
		/** this is a serious priority, but not life-threatening in nature */
		SERIOUS,
		/** this priority is preferred but not imminent */
		NORMAL,
		/** this thing is of least priority */
		TRIVIAL
	}

}
