package mind.goals;

import java.util.Collection;
import java.util.Set;

import actor.Actor;
import mind.Culture;
import mind.concepts.type.IConcept;
import mind.concepts.type.IProfile;
import mind.concepts.type.Property;
import mind.memory.IHasKnowledge;
import mind.memory.IKnowledgeBase;
import mind.memory.IPropertyData;
import mind.memory.Memory;
import sim.Location;

public interface ITaskGoal extends IGoal {

	/**
	 * represents a goal which does not have any conditions
	 */
	public static final ITaskGoal CONTINUE = new ITaskGoal() {
		@Override
		public Type getGoalType() {
			return Type.NONE;
		}

		@Override
		public boolean isDone() {
			return false;
		}

		@Override
		public boolean societalGoal() {
			return false;
		}

		@Override
		public boolean individualGoal() {
			return false;
		}

		@Override
		public ITaskHint getActionHint() {
			return TaskHint.NONE;
		}

		@Override
		public IProfile getTarget() {
			return null;
		}

		@Override
		public boolean isComplete(IHasKnowledge entity) {
			return false;
		}

		@Override
		public boolean isInvalid(IHasKnowledge knower) {
			return false;
		}

		@Override
		public String getUniqueName() {
			return "goal_CONTINUE";
		}

		@Override
		public String toString() {
			return getUniqueName();
		}
	};

	/**
	 * Represents the top of a goal stack; the goal indicating that requirements are
	 * complete
	 */
	public static final ITaskGoal FINISHED = new ITaskGoal() {
		@Override
		public Type getGoalType() {
			return Type.NONE;
		}

		@Override
		public boolean isDone() {
			return true;
		}

		@Override
		public boolean societalGoal() {
			return false;
		}

		@Override
		public boolean individualGoal() {
			return false;
		}

		@Override
		public ITaskHint getActionHint() {
			return TaskHint.NONE;
		}

		@Override
		public IProfile getTarget() {
			return null;
		}

		@Override
		public boolean isComplete(IHasKnowledge entity) {
			return true;
		}

		@Override
		public boolean isInvalid(IHasKnowledge knower) {
			return false;
		}

		@Override
		public String getUniqueName() {
			return "goal_FINISHED";
		}

		@Override
		public String toString() {
			return getUniqueName();
		}
	};

	public static final ITaskGoal UNKNOWN = new ITaskGoal() {

		@Override
		public ITaskHint getActionHint() {
			return TaskHint.NONE;
		}

		@Override
		public IProfile getTarget() {
			return null;
		}

		@Override
		public boolean individualGoal() {
			return false;
		}

		@Override
		public boolean societalGoal() {
			return false;
		}

		@Override
		public boolean isComplete(IHasKnowledge entity) {
			return false;
		}

		@Override
		public boolean isInvalid(IHasKnowledge knower) {
			return false;
		}

		@Override
		public String getUniqueName() {
			return "goal_UNKNOWN";
		}

		@Override
		public String toString() {
			return getUniqueName();
		}
	};

	@Override
	default Type getGoalType() {
		return Type.TASK;
	}

	/**
	 * a hint for what action should complete this goal
	 * 
	 * @return
	 */
	public ITaskHint getActionHint();

	/**
	 * Gets the desired target of this task. This would be who acquires the Acquire
	 * tasks items; who is resurrected by the Resurrect tasks; what is stowed; etc.
	 * Almost always the self.
	 * 
	 * @return
	 */
	public IProfile getTarget();

	/**
	 * the location that is the target of this task goal
	 * 
	 * @return
	 */
	default Location getTargetLocation() {
		return null;
	}

	/**
	 * Gets the target being or location of a transfer task or stow task; null if
	 * nothing
	 * 
	 * @return
	 */
	default IConcept transferTarget() {
		return null;
	}

	/**
	 * gets the target item type or recipe or profile or set of those used by the
	 * task to complete it; this would be the item consumed by a consumer, or the
	 * healing item used to heal
	 */
	default Collection<IConcept> useTarget() {
		return Set.of();
	}

	/**
	 * This woud be the topic learned about in a learning goal
	 * 
	 * @return
	 */
	default IConcept getLearnTarget() {
		return null;
	}

	/**
	 * Ifi the goal is invalid for the entity it is put in
	 * 
	 * @return
	 */
	public boolean isInvalid(IHasKnowledge knower);

	/**
	 * An item or set of items that is produced in the task, if it is a task which
	 * produces an item from nothing
	 * 
	 * @return
	 */
	default Collection<IConcept> producedItem() {
		return Set.of();
	}

	default boolean isUnknown() {
		return this == UNKNOWN;
	}

	@Override
	default boolean equivalent(IGoal other) {
		return IGoal.super.equivalent(other) && other instanceof ITaskGoal
				&& ((ITaskGoal) other).getActionHint() == this.getActionHint();
	}

	public static IPropertyData getProperty(Actor actor, Property property, IHasKnowledge entity) {
		IKnowledgeBase knowledge = entity.getKnowledgeBase();
		if (knowledge instanceof Culture) {
			return actor.getPropertyData(knowledge, property);
		} else if (knowledge instanceof Memory) {
			IPropertyData dat = actor.getPropertyData(knowledge, property);
			if (dat != null)
				return dat;
			for (Culture cult : ((Memory) knowledge).cultures()) {
				IPropertyData twa = actor.getPropertyData(cult, property);
				if (twa != null) {
					if (dat == null || twa.getKnownCount() > dat.getKnownCount())
						dat = twa;
				}
			}
			return dat;
		}
		return null;
	}
}
