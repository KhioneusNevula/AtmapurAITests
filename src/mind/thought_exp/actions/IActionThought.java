package mind.thought_exp.actions;

import java.util.Collection;
import java.util.Set;

import actor.IPartAbility;
import mind.action.IActionType;
import mind.goals.ITaskGoal;
import mind.relationships.IParty;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;

/**
 * An instance of an action in memory.
 * 
 * How do actions work? Goal: I hungry (focus goal) Action: I eat
 * 
 * Okay, I don't have food. Therefore, Goal: Obtain food Action: Grab food
 * 
 * Okay, no food in grabbing distance. Therefore: Goal: Know location of food
 * Action: Search for food
 * 
 * Okay, I found food. Now, I remember I wanted to grab food? (Check memory:
 * recent action goal) Goal: Obtain food Action: Grab food
 * 
 * Okay, I obtained food. Now, I remember I wanted to eat food? (Check memory:
 * recent action goal) Goal: Hungry Action: Eat food
 * 
 * So we do a system of goal -> action. > If action is impossible, generate new
 * goal, make it (focus goal). Then store action and previous goal in memory, in
 * some ordered (e.g. stacked) fashion so we know our most recent. Once we
 * complete next action, check our memory.
 * 
 * @author borah
 *
 */
public interface IActionThought extends IThought {

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
	/*
	 * default void beginExecutingGroup(UpgradedGroup group, long worldTicks) {
	 * 
	 * }
	 */

	/*
	 * default boolean canContinueExecutingGroup(UpgradedGroup group, int
	 * actionTick) { return false; }
	 */

	/**
	 * a tick of execution for an individual; the "actionTick" parameter indicates
	 * how many ticks have passed since begining of action (0 ticks)
	 * 
	 * @param individual
	 * @param tick
	 * @return
	 */

	void executionTickIndividual(ICanThink individual, int actionTick, int thoughtTick, long worldTick);

	/*
	 * default void executionTickGroup(UpgradedGroup group, int tick) {
	 * 
	 * }
	 */

	/**
	 * Finish this action (called once canContinueExecuting returns false; return if
	 * action was successful)
	 * 
	 * @param individual
	 * @param tick
	 */

	public boolean finishActionIndividual(ICanThink individual, int actionTick, int thoughtTick, boolean interruption);

	/*
	 * default boolean finishActionGroup(UpgradedGroup group, int tick) { return
	 * false; }
	 */

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

	/**
	 * whether this action executed successfully; for a thinking action, whether
	 * this action produced a successful result
	 * 
	 * @return
	 */
	boolean succeeded();

	/**
	 * If this action has failed before it even started without being able to
	 * generate conditions; should be called during a think-tick
	 * 
	 * @return
	 */
	boolean failedPreemptively();

	/**
	 * Cancel this action, whether before it began or while it is in progress.
	 */
	void cancel(ICanThink memory, int ticks, long worldTicks);

	/**
	 * If this action is in Executing state; if this is false, the action is in the
	 * Thinking state
	 * 
	 * @return
	 */
	boolean executing();

	/**
	 * Mark this action as executing; assumes that preconditions of the action are
	 * met.
	 */
	@Deprecated()
	public void start();

	/**
	 * Whether this action thought failed because it didn't properly check its parts
	 * 
	 * @return
	 */
	boolean failedPartsCheck();

	/**
	 * Gets this action's intended goal
	 * 
	 * @return
	 */
	ITaskGoal getIntendedGoal();

	/**
	 * Sets this action thought's intended goal
	 * 
	 * @param goal
	 * @return
	 */
	AbstractActionThought setIntendedGoal(ITaskGoal goal);

	/**
	 * Get the ultimate goal this action is part of completing
	 * 
	 * @return
	 */
	ITaskGoal getPrimaryGoal();

	/**
	 * Get the ultimate goal this action is part of completing
	 * 
	 * @param primaryGoal
	 * @return
	 */
	AbstractActionThought setPrimaryGoal(ITaskGoal primaryGoal);

}
