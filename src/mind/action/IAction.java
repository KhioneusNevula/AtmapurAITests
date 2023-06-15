package mind.action;

import java.util.Collection;
import java.util.Set;

import actor.IPartAbility;
import mind.concepts.type.IConcept;
import mind.goals.ITaskGoal;
import mind.goals.ITaskHint;
import mind.memory.IHasKnowledge;
import sim.Location;

/**
 * An instance of an action in memory
 * 
 * @author borah
 *
 */
public interface IAction {

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
	 * if this action targets a location, this returns the location
	 * 
	 * @return
	 */
	default Location targetLocation() {
		return null;
	}

	/**
	 * if the target is multiple locations, this returns that
	 * 
	 * @return
	 */
	default Iterable<Location> targetLocations() {
		return Set.of();
	}

	/**
	 * if the target is a concept, this returns it
	 * 
	 * @return
	 */
	default IConcept targetConcept() {
		return null;
	}

	/**
	 * if the target is multiple concepts
	 */
	default Iterable<IConcept> targetConcepts() {
		return Set.of();
	}

	/**
	 * What use this task is for
	 * 
	 * @return
	 */
	default Collection<ITaskHint> getUsageHints() {
		return getType().getUsage();
	}

	/**
	 * Whether this action can execute in current circumstances. Also determines the
	 * circumstances needed to generate a goal of execution, if any. Return false if
	 * a goal should be generated.
	 * 
	 * @param user
	 * @param pondering if the action is right about to execute, this is false; this
	 *                  method is always called with "pondering" as true while
	 *                  determining actions, and "pondering" as false when the
	 *                  action is about to execute. This is to allow for the
	 *                  "canExecute" method to prepare necessary information for the
	 *                  action's execution
	 * @return
	 */
	public boolean canExecuteIndividual(IHasKnowledge user, boolean pondering);

	/**
	 * {@link IAction#canExecuteIndividual}, but for a group
	 * 
	 * @param group
	 * @return
	 */
	default boolean canExecuteGroup(IHasKnowledge group, boolean pondering) {
		return false;
	}

	/**
	 * Begins executing the action, if in an individual
	 * 
	 * @param forUser
	 */
	public void beginExecutingIndividual(IHasKnowledge forUser);

	/**
	 * {@link IAction#beginExecutingIndividual} but for a group
	 * 
	 * @param forUser
	 */
	default void beginExecutingGroup(IHasKnowledge group) {

	}

	/**
	 * whether the action can execute continuously; checked every tick
	 * 
	 * @param individual
	 * @return
	 */
	default boolean canContinueExecutingIndividual(IHasKnowledge individual, int tick) {
		return false;
	}

	default boolean canContinueExecutingGroup(IHasKnowledge group, int tick) {
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
	default void executionTickIndividual(IHasKnowledge individual, int tick) {

	}

	default void executionTickGroup(IHasKnowledge group, int tick) {

	}

	/**
	 * Finish this action (called once canContinueExecuting returns false; return if
	 * action was successful)
	 * 
	 * @param individual
	 * @param tick
	 */
	default boolean finishActionIndividual(IHasKnowledge individual, int tick) {
		return true;
	}

	default boolean finishActionGroup(IHasKnowledge group, int tick) {
		return false;
	}

	/**
	 * Generates goals representing the conditions the action needs in order to
	 * successfully execute. Return an empty set if this action is generally failing
	 * at life adn should stop being considered.
	 * 
	 * @return
	 */
	public Collection<ITaskGoal> genConditionGoal(IHasKnowledge user);

	/**
	 * Generates whatever action this needs to in order to work (e.g. for actions
	 * whose hint is TaskHint.All)
	 * 
	 * @return
	 */
	default Collection<IAction> genExtraActions(IHasKnowledge user) {
		return Set.of();
	}

	/**
	 * Gets the type of action -- eating, moving, etc
	 * 
	 * @return
	 */
	public IActionType<?> getType();

	/**
	 * Returns what abilities this action uses
	 * 
	 * @return
	 */
	default Collection<? extends IPartAbility> usesAbilities() {
		return getType().getUsedAbilities();
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
	 * Gives a reason why an action is now unviable
	 * 
	 * @return
	 */
	default String reasonForUnviability() {
		return "action is impossible";
	}

}
