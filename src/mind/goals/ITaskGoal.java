package mind.goals;

import java.util.Collection;
import java.util.Set;

import actor.Actor;
import mind.concepts.type.ILocationMeme;
import mind.concepts.type.IMeme;
import mind.concepts.type.IProfile;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.goals.question.Question;
import mind.memory.IPropertyData;
import mind.speech.IUtterance;
import mind.thought_exp.IThought;
import mind.thought_exp.IUpgradedHasKnowledge;
import mind.thought_exp.culture.UpgradedCulture;
import mind.thought_exp.memory.IBrainMemory;
import mind.thought_exp.memory.IUpgradedKnowledgeBase;

public interface ITaskGoal extends IGoal {

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
		public boolean useThoughtToCheckCompletion(IUpgradedHasKnowledge mind) {
			return false;
		}

		@Override
		public IThought checkCompletion(IUpgradedHasKnowledge mind) {
			return null;
		}

		@Override
		public boolean societalGoal() {
			return false;
		}

		@Override
		public Priority getPriority() {
			return Priority.TRIVIAL;
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
		public IProfile beneficiary() {
			return null;
		}

		@Override
		public boolean isComplete(IUpgradedHasKnowledge entity) {
			return true;
		}

		@Override
		public boolean isInvalid(IUpgradedHasKnowledge knower) {
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
	 * Gets the desired beneficiary of this task. This would be who acquires the
	 * Acquire tasks items; who is resurrected by the Resurrect tasks; what is
	 * stowed; etc.
	 * 
	 * @return
	 */
	public IProfile beneficiary();

	/**
	 * the location that is the target of this task goal
	 * 
	 * @return
	 */
	default ILocationMeme targetLocation() {
		return null;
	}

	/**
	 * Gets the target being or location of a transfer task or stow task; null if
	 * nothing
	 * 
	 * @return
	 */
	default IMeme transferItem() {
		return null;
	}

	/**
	 * gets the target item type or recipe or profile or set of those used by the
	 * task to complete it; this would be the item consumed by a consumer, or the
	 * healing item used to heal
	 */
	default Collection<IMeme> usedItem() {
		return Set.of();
	}

	/**
	 * the target of a social interaction
	 * 
	 * @return
	 */
	default Profile socialTarget() {
		return null;
	}

	default Collection<Profile> socialTargets() {
		return Set.of();
	}

	/**
	 * The group of profiles that would be harmed by this goal
	 * 
	 * @return
	 */
	default Collection<Profile> harmTargets() {
		return Set.of();
	}

	/**
	 * This is who would be harmed by this goal
	 * 
	 * @return
	 */
	default Profile harmTarget() {
		return null;
	}

	/**
	 * This woud be the question learned about in a learning goal
	 * 
	 * @return
	 */
	default Question learnInfo() {
		return null;
	}

	/**
	 * The information that needs to be communicated via this action
	 * 
	 * @return
	 */
	default IUtterance communicationInfo() {
		return null;
	}

	/**
	 * Ifi the goal is invalid for the entity it is put in
	 * 
	 * @return
	 */
	public boolean isInvalid(IUpgradedHasKnowledge knower);

	/**
	 * An item or set of items that is produced in the task, if it is a task which
	 * produces an item from nothing
	 * 
	 * @return
	 */
	default Collection<IMeme> producedItem() {
		return Set.of();
	}

	@Override
	default IMemeType getMemeType() {
		return MemeType.TASK_GOAL;
	}

	@Override
	default boolean equivalent(IGoal other) {
		return IGoal.super.equivalent(other) && other instanceof ITaskGoal
				&& ((ITaskGoal) other).getActionHint() == this.getActionHint();
	}

	/**
	 * Whether to create a new thought to check completion
	 * 
	 * @param mind
	 * @return
	 */
	public boolean useThoughtToCheckCompletion(IUpgradedHasKnowledge mind);

	/**
	 * Return a thought that will check if the goal is complete. This is only
	 * returned if isComplete returns false but useThoughtToCheckCompletion returns
	 * true.
	 * 
	 * @param mind
	 * @return
	 */
	public IThought checkCompletion(IUpgradedHasKnowledge mind);

	/**
	 * Return a thought that will check if the goal is complete, and *remember* the
	 * result. This is not used for normal goal-checking
	 * 
	 * @param mind
	 * @return
	 */
	public default IThought checkCompletionAndRemember(IUpgradedHasKnowledge mind) {
		return null;
	}

	/**
	 * check the result of the completion-checking thought
	 * 
	 * @param thought
	 * @return
	 */
	public default boolean checkResult(IThought thought) {
		return thought.getInformation() == Boolean.TRUE;
	}

	/**
	 * Uses the given knowledge base and the actor's contained properties to figure
	 * out if this has a given property
	 * 
	 * @param actor
	 * @param property
	 * @param entity
	 * @return
	 */
	public static IPropertyData getProperty(Actor actor, Property property, IUpgradedHasKnowledge entity) {
		if (property == Property.ANY)
			return IPropertyData.PRESENCE;
		IUpgradedKnowledgeBase knowledge = entity.getKnowledgeBase();
		IPropertyData dat = actor.getPropertyData(knowledge, property, true);
		IPropertyData dat2 = knowledge.getPropertyData(new Profile(actor), property);
		if (dat2.getKnownCount() > dat.getKnownCount()) {
			dat = dat2;
		}
		if (knowledge instanceof IBrainMemory) {
			for (UpgradedCulture cult : ((IBrainMemory) knowledge).cultures()) {
				IPropertyData twa = actor.getPropertyData(cult, property, true);
				if (twa.getKnownCount() > dat.getKnownCount())
					dat = twa;

				twa = cult.getPropertyData(new Profile(actor), property);
				if (twa.getKnownCount() > dat.getKnownCount())
					dat = twa;
			}

		}

		return dat;
	}

	/**
	 * Gets the target of this goal if the target is a meme
	 * 
	 * @return
	 */
	default IMeme getTargetMeme() {
		return null;
	}

}
