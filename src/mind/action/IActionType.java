package mind.action;

import java.util.Collection;
import java.util.Set;

import actor.IPartAbility;
import mind.concepts.type.IMeme;
import mind.goals.ITaskGoal;
import mind.goals.ITaskHint;
import mind.memory.IHasKnowledge;

public interface IActionType<T extends IAction> extends IMeme {
	/**
	 * if this action type requires an actor to execute
	 * 
	 * @return
	 */
	public boolean requiresActor();

	/**
	 * whether this ActionType represents a procedurally generated action
	 * 
	 * @return
	 */
	public boolean isGenerated();

	/**
	 * generates an action Instance based on this goal
	 * 
	 * @param fromNeed
	 * @return
	 */
	public T genAction(ITaskGoal fromNeed);

	public String getName();

	public Set<ITaskHint> getUsage();

	/**
	 * Whether this action is viable for this individual (e.g. a person without legs
	 * can't use a walk action). This questions whether to add this action to the
	 * list of "potential" actions and checks for things like, does the user have
	 * the needed body parts? It should be a low effort check
	 * 
	 * @param user
	 * @return
	 */
	public boolean isViable(IHasKnowledge user, ITaskGoal forGoal);

	/**
	 * Required known concepts for a given goal
	 * 
	 * @param forGoal
	 * @return
	 */
	public Collection<IMeme> requiredConcepts(ITaskGoal forGoal);

	/**
	 * what abilities of a creature this action uses
	 * 
	 * @return
	 */
	public Collection<? extends IPartAbility> getUsedAbilities();

	/**
	 * If this action requires a multipart body
	 */
	default boolean requiresMultipartBody() {
		return !this.getUsedAbilities().isEmpty();
	}

	@Override
	default String getUniqueName() {
		return "action_" + this.getName();
	}

}
