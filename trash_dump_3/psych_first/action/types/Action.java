package psych_first.action.types;

import java.util.EnumSet;

import culture.CulturalContext;
import psych_first.action.ActionType;
import psych_first.action.goal.Goal;
import psych_first.action.goal.RequirementGoal;
import psych_first.action.goal.RequirementWrapper;
import psych_first.action.goal.Task;
import psych_first.actionstates.checks.IProfileDependentCheck;
import psych_first.actionstates.states.StateBuilder;
import psych_first.mind.Mind;
import psych_first.mind.Will;
import sim.IHasProfile;
import sociology.ProfileType;
import sociology.sociocon.IPurposeElement;
import sociology.sociocon.IPurposeSource;

/**
 * Actions are remembered in the mind and executed in stack form. Each action
 * has a user (performer) and possibly a target and a tool. However, any other
 * involved entities/profiles/etc need to be set up as a MentalConstruct
 * 
 * @author borah
 *
 */
public abstract class Action implements IPurposeElement {

	public static final Action COOK = new CookAction();
	public static final Action EAT = new EatAction();
	public static final Action MOVE = new MoveAction();
	public static final Action PICKUP = new PickupAction();
	public static final Action SEARCH = new SearchAction();

	private IPurposeSource origin = null;
	private String name;
	private int actionType = ActionType.INTERACTION.bits;

	// TODO completion state, required state

	public Action(String name) {
		this.name = name;
	}

	protected void setActionType(int type) {
		this.actionType = type;
	}

	protected void setActionType(ActionType... types) {
		this.actionType = ActionType.taskTypesBitFlag(EnumSet.of(types[0], types));
	}

	/**
	 * Whether this action can be executed for the given being; requirements are the
	 * requirements of this state and allow for selecting the resolved
	 * profileplaceholders; task allows for the action to store info
	 */
	public final boolean _canExecute(IHasProfile actor, Will will, Task task, Goal requirements, Goal result) {
		return canExecute(actor, will, task, requirements, result);
	}

	public final int _startExecution(IHasProfile actor, Will will, Task task, Goal requirements, Goal result) {
		return this.startExecution(actor, will, task, requirements, result);
	}

	public final boolean _canContinueExecution(IHasProfile actor, Will will, Task task, Goal requirements,
			Goal result) {

		return canContinueExecution(actor, will, task, requirements, result);
	}

	public final void _update(IHasProfile actor, Will will, Task task, Goal requirements, Goal result) {
		update(actor, will, task, requirements, result);
		task.changeExecutionTicks(this, -1);
	}

	public final String _complete(IHasProfile actor, Will will, Task task, Goal reqs, Goal result, int ticksLeft) {
		return complete(actor, will, task, reqs, result, ticksLeft);
	}

	/**
	 * This is called when the action is completed, returning whether completion was
	 * successful or not as a String for the reason why it was unsuccessful or null
	 * if it was successful. ticksLeft indicates whether the initial stages of prior
	 * completion were successful; 0 ticksLeft meant successful completion, > 0
	 * means interrupted completion, and < 0 means unsuccessful completion due to
	 * other reasons. This should match up; i.e. if the ticksLeft is negative, then
	 * this action must fail.
	 * 
	 * @param will
	 * @param task
	 * @param reqs
	 * @return
	 */
	protected abstract String complete(IHasProfile actor, Will will, Task task, Goal reqs, Goal result, int ticksLeft);

	/**
	 * Called continuously while this action is continuing to execute
	 * 
	 * @param will
	 * @param task
	 * @param requirements
	 */
	protected void update(IHasProfile actor, Will will, Task task, Goal requirements, Goal result) {

	}

	/**
	 * Whether this action can continue executing (if it's a continuous action). If
	 * not, then call "complete". Default implementation checks if there are
	 * remaining "execution ticks"
	 * 
	 * @param will
	 * @param task
	 * @param requirements
	 * @return
	 */
	protected boolean canContinueExecution(IHasProfile actor, Will will, Task task, Goal requirements, Goal result) {
		return task.getExecutionTicks(this) > 0;
	}

	/**
	 * interprets the problem with the attempt at beginning executing this action,
	 * given the requirements and result; for issues in continuing execution, refer
	 * to the complete() method
	 * 
	 * @param requirements
	 * @param result
	 * @return
	 */
	public String interpretExecutionProblem(Goal requirements, Goal result, Object execInfo, CulturalContext ctxt) {

		return "undefined problem:reqs=" + requirements.goalReport() + ", result=" + result.goalReport();
	}

	protected boolean isPlaceholderResolved(IProfileDependentCheck c, Goal g) {
		return this.isPlaceholderResolved(c.getProfilePlaceholder().getProfileType(), g);
	}

	protected boolean isPlaceholderResolved(ProfileType type, Goal g) {
		if (g instanceof RequirementGoal req) {
			return req.getState().getProfile(type).isResolved();
		}
		return false;
	}

	protected <T> boolean doesPlaceholderSatisfy(Goal g, ProfileType withcondition, T checker, CulturalContext ctxt) {
		if (g instanceof RequirementGoal goal && goal.getState().getFor(withcondition).hasConditionFor(checker)
				&& goal.getState().getProfile(withcondition) != null)
			return goal.getState().getFor(withcondition).getCondition(checker)
					.satisfies(goal.getState().getProfile(withcondition), ctxt) == Boolean.TRUE;
		return false;
	}

	/**
	 * Whether this action can actually execute; usually, should return true if the
	 * given requirements are complete
	 * 
	 * @param will
	 * @param task
	 * @param requirements
	 * @return
	 */
	protected boolean canExecute(IHasProfile actor, Will will, Task task, Goal requirements, Goal result) {
		requirements.goalUpdate(will.getMind());
		return requirements.isComplete();
	}

	/**
	 * Execute this action; return -1 if the first stage of execution was not
	 * completed successfully
	 * 
	 * @param requirements is the generated requirements of this action during
	 *                     action selection
	 * @param result       is the desired result of this action
	 * 
	 * @param actor
	 * @return the number of ticks to continue executing, or a negative number if
	 *         the first stage was not completed
	 */
	protected abstract int startExecution(IHasProfile actor, Will will, Task task, Goal requirements, Goal result);

	@Override
	public IPurposeSource getOrigin() {
		return origin;
	}

	/**
	 * Determines what the {@link psych_first.action.ActionType} of this action is;
	 * may be more than one task type
	 * 
	 * @return
	 */
	public int getActionType() {
		return actionType;
	}

	public Action setOrigin(IPurposeSource origin) {
		this.origin = origin;
		return this;
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * 
	 * 
	 * @param fromMind
	 * @param usingRequirements
	 * @return
	 */
	public final RequirementWrapper generateRequirements(Mind fromMind, Goal goal) {

		if (goal instanceof RequirementGoal rg) {
			return this.generateRequirements(fromMind, goal, StateBuilder.start(rg.getState()));
		}
		return this.generateRequirements(fromMind, goal, StateBuilder.start());
	}

	/**
	 * Using the given desired goal, generate the goal(s) that need to be completed
	 * to complete this action using the StateBuilder to build them (if the desired
	 * goal is a requirement goal, the statebuilder will be based on it). Return an
	 * empty wrapper or wrapper with one empty state if this action completes all
	 * conditions. Return null if the action is not compatible with the previous
	 * one.
	 * 
	 * @param fromMind
	 * @param goal
	 * @param builder
	 * @return
	 */
	public abstract RequirementWrapper generateRequirements(Mind fromMind, Goal goal, StateBuilder builder);

	/**
	 * TODO tiredness calculation
	 */

	/****/
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ":" + this.name + (this.origin != null ? ";of: " + this.origin : "");
	}

	/**
	 * TODO add a "type profile" for things; this method will look at a conditionset
	 * and determine whether the given value is checked for by the socioprops and
	 * sociocon checks of this conditionset . Example -- if a conditionset checks
	 * whether something is a certain type of uncookable food (sociocon check), then
	 * it implicitly includes a check for whether the food's socioprops include
	 * uncookable = true
	 * 
	 * @param equal whether to check for equality or inequality to the given value
	 * @param value the value to test for
	 * @return
	 *//*
		 * protected static <T> boolean isCheckPresent(ConditionSet cons, Socioprop<T>
		 * property, boolean equal, T value) { Check<Socioprop<T>> propcheck =
		 * cons.getCondition(property); if (propcheck != null && propcheck instanceof
		 * SociopropMatchingCheck<T> ch) { return equal ? ch.getValue().equals(value) :
		 * !ch.getValue().equals(value); } for (Check<Sociocon> socioconChecks :
		 * cons.<Sociocon>getConditionsForPredicate((a) -> a.getChecker() instanceof
		 * Sociocon)) { SocioconCheck check = (SocioconCheck) socioconChecks; } return
		 * false; }
		 */

}
