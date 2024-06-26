package mind.thought_exp.actions;

import java.util.Collection;
import java.util.Set;

import actor.IPartAbility;
import mind.action.IActionType;
import mind.goals.ITaskGoal;
import mind.goals.ITaskHint;
import mind.relationships.IParty;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;
import mind.thought_exp.IUpgradedHasKnowledge;
import mind.thought_exp.culture.UpgradedGroup;

/**
 * An instance of an action in memory
 * 
 * @author borah
 *
 */
public interface IActionThought extends IThought {

	/**
	 * whether this action can be done by a group
	 * 
	 * @return
	 */
	default boolean performableByGroup() {
		return false;
	}

	/**
	 * whether this action can be done by an individual
	 * 
	 * @return
	 */
	default boolean performableByIndividual() {
		return true;
	}

	/**
	 * What use this task is for
	 * 
	 * @return
	 */
	default Collection<ITaskHint> getUsageHints() {
		return getActionType().getUsage();
	}

	/**
	 * For action thoughts, this is where you do the pre-checks to ensure the mind
	 * has all the necessary thoughts and whatever and memories
	 */
	@Override
	void startThinking(ICanThink memory, long worldTick);

	/**
	 * Whether this action can execute in current circumstances. If true, the action
	 * begins executing. If false, the action should generate its necessary
	 * conditions
	 * 
	 * @param user
	 * @return
	 */
	public boolean canExecuteIndividual(ICanThink user, int thoughtTicks, long worldTicks);

	/**
	 * {@link IAction#canExecuteIndividual}, but for a group
	 * 
	 * @param group
	 * @return
	 */
	default boolean canExecuteGroup(UpgradedGroup group, long worldTicks) {
		return false;
	}

	/**
	 * Begins executing the action, if in an individual
	 * 
	 * @param forUser
	 */
	public void beginExecutingIndividual(ICanThink forUser, int thoughtTicks, long worldTicks);

	/**
	 * {@link IAction#beginExecutingIndividual} but for a group
	 * 
	 * @param forUser
	 */
	default void beginExecutingGroup(UpgradedGroup group, long worldTicks) {

	}

	/**
	 * whether the action can execute continuously; checked every tick
	 * 
	 * @param individual
	 * @return
	 */
	default boolean canContinueExecutingIndividual(ICanThink individual, int actionTick, int thoughtTick) {
		return false;
	}

	default boolean canContinueExecutingGroup(UpgradedGroup group, int actionTick) {
		return false;
	}

	/**
	 * a tick of execution for an individual; the "tick" parameter indicates how
	 * many ticks have passed since begining (0 ticks)
	 * 
	 * @param individual
	 * @param tick
	 * @return
	 */
	default void executionTickIndividual(ICanThink individual, int actionTick, int thoughtTick) {

	}

	default void executionTickGroup(UpgradedGroup group, int tick) {

	}

	/**
	 * Finish this action (called once canContinueExecuting returns false; return if
	 * action was successful)
	 * 
	 * @param individual
	 * @param tick
	 */
	public default boolean finishActionIndividual(ICanThink individual, int actionTick, int thoughtTick,
			boolean interruption) {
		return !failedPreemptively();
	}

	default boolean finishActionGroup(UpgradedGroup group, int tick) {
		return false;
	}

	/**
	 * retrieves the goal representing the condition the action needs in order to
	 * successfully execute. Return null if some error occurred
	 * 
	 * @return
	 */
	public ITaskGoal getPendingCondition(IUpgradedHasKnowledge user);

	/**
	 * Return true if this action has pending conditions to be fulfilled before
	 * executing; if the action cannot execute, and has not generated pending
	 * conditions, the action is a failure
	 * 
	 * @param user
	 * @return
	 */
	public boolean hasPendingCondition(IUpgradedHasKnowledge user);

	/**
	 * Gets the type of action -- eating, moving, etc
	 * 
	 * @return
	 */
	public IActionType<?> getActionType();

	/**
	 * Returns what abilities this action uses
	 * 
	 * @return
	 */
	default Collection<? extends IPartAbility> usesAbilities() {
		return getActionType().getUsedAbilities();
	}

	/**
	 * Gives a reason why an action has to generate new goals
	 * 
	 * @return
	 */
	default String reasonForLastFailure() {
		return "failed to execute";
	}

	/**
	 * The selected target of an action which involves two parties, like socializing
	 * 
	 * @return
	 */
	default IParty target() {
		return null;
	}

	/**
	 * For actions with multiple interaction targets, technically
	 * 
	 * @return
	 */
	default Collection<IParty> targets() {
		return Set.of();
	}

	boolean succeeded();

	/**
	 * If this action has failed before it even started; should be called during a
	 * think-tick
	 * 
	 * @return
	 */
	boolean failedPreemptively();

	/**
	 * Cancel this action
	 */
	void cancel();

	/**
	 * If this action is in the Started phase yet; if this is false, the action is
	 * in the Thinking phase
	 * 
	 * @return
	 */
	boolean started();

	/**
	 * Start this action; assumes that preconditions of the action are met.
	 */
	public void start();

	/**
	 * Whether this thought has created short term memories that ought to be checked
	 * 
	 * @return
	 */
	boolean addedShortTermMemories();

	/**
	 * Indicates to this thought that the previous thought changed some short-term
	 * memories which the next thought can check
	 */
	public void notifyCheckMemories();

}
