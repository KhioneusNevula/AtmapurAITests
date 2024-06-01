package mind.thought_exp.actions;

import java.util.Collection;
import java.util.Map;

import actor.IPartAbility;
import mind.goals.IGoal.Priority;
import mind.goals.ITaskGoal;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;
import mind.thought_exp.IThoughtMemory;
import mind.thought_exp.IThoughtMemory.Interest;
import mind.thought_exp.ThoughtType;
import mind.thought_exp.memory.type.RecentActionMemory;
import mind.thought_exp.memory.type.RecentIntentionMemory;
import mind.thought_exp.type.AbstractThought;

public abstract class AbstractActionThought extends AbstractThought implements IActionThought {

	/**
	 * Whether the action failed while thinking
	 */
	private boolean failedPreemptively;

	/**
	 * If this action needs to generate new conditions
	 */
	private boolean failedCondition;
	/**
	 * Whether this action failed because the person using it was using the body
	 * parts/abilities necessary for the action
	 */
	private boolean failedPartsCheck;
	/**
	 * If this action was canceled
	 */
	private boolean canceled;
	/**
	 * If this action is completed, or otherwise unable to continue executing
	 */
	private boolean isDone;
	/**
	 * used in completion methods to register the action as complete next tick
	 */
	private boolean doneNextTick;
	/**
	 * If this action is executing, as opposed to "thinking"
	 */
	private boolean isExecuting;
	/**
	 * Whether the next thought tick ought to be the first tick of execution
	 */
	private boolean isExecutionFirstTick;
	/**
	 * Whether this actiton succeeded
	 */
	private boolean succeeded;

	/**
	 * How many ticks the action has been executing
	 */
	private int actionTick;

	/**
	 * Goal this action is intended to complete
	 */
	private ITaskGoal forGoal;

	/**
	 * Primary goal this action is working toward
	 */
	private ITaskGoal primaryGoal;

	private ITaskGoal pendingCondition;

	public AbstractActionThought() {
	}

	@Override
	public IThoughtType getThoughtType() {
		return ThoughtType.ACTION;
	}

	@Override
	public boolean isLightweight() {
		return false;
	}

	@Override
	public Interest shouldProduceRecentThoughtMemory(ICanThink mind, int finishingTicks, long worldTicks) {
		return Interest.FORGET;
	}

	@Override
	public Map<IThoughtMemory, Interest> produceMemories(ICanThink mind, int finishingTicks, long worldTicks) {
		RecentActionMemory ram = new RecentActionMemory(this, this.forGoal, this.primaryGoal, succeeded);
		if (this.pendingCondition != null) {
			return Map.of(ram, Interest.SHORT_TERM, new RecentIntentionMemory(pendingCondition, primaryGoal),
					Interest.SHORT_TERM);
		}
		return Map.of(ram, Interest.SHORT_TERM);
	}

	@Override
	public boolean isFinished(ICanThink memory, int ticks, long worldTick) {
		return isDone;
	}

	@Override
	public Priority getPriority() {
		return Priority.NORMAL;
	}

	@Override
	public void thinkTick(ICanThink memory, int ticks, long worldTick) {
		if (doneNextTick) { // action is complete
			this.isDone = true;
			this.releaseAbilitySlots(memory);
		} else if (this.hasPendingCondition()) { // action checked conditions and found that there are pending
													// conditions
			// ITaskGoal cond = this.getPendingCondition();
			memory.setFocusGoal(pendingCondition); // to speed up action selection; if there are other actions active,
													// theoretically this might be overriden
			this.isDone = true;
		} else if (this.isExecutionFirstTick) {
			this.isExecutionFirstTick = false;
			this.beginExecutingIndividual(memory, ticks, worldTick);
			this.actionTick = 0;
		} else if (this.isExecuting) {
			this.executionTickIndividual(memory, ticks, actionTick, worldTick);
			actionTick++;
		} else {
			this.onThinkingTick(memory, ticks, worldTick);
		}
	}

	@Override
	public boolean succeeded() {
		return succeeded;
	}

	@Override
	public String displayText() {
		return this.getActionType().getUniqueName();
	}

	@Override
	public boolean equivalent(IThought other) {
		return other instanceof IActionThought iat && this.getActionType().equals(iat.getActionType());
	}

	@Override
	public void getInfoFromChild(ICanThink mind, IThought childThought, boolean interrupted, int ticks) {
	}

	/**
	 * Return true if this action has pending conditions to be fulfilled before
	 * executing; if the ac@Override tion cannot execute, and has not generated
	 * pending conditions, the action is a failure
	 * 
	 * @param user
	 * @return
	 */
	protected boolean hasPendingCondition() {
		return this.failedCondition;
	}

	@Override
	public abstract void startThinking(ICanThink memory, long worldTick);

	/**
	 * Run any logic-checking code in here; this is called when the action is NOT
	 * executing. Not called immediately upon starting; see {@link #startThinking}
	 * 
	 * @param memory
	 * @param ticks
	 * @param worldticks
	 */
	protected abstract void onThinkingTick(ICanThink memory, int ticks, long worldticks);

	/**
	 * Called when an action is externally canceled
	 * 
	 * @param memory
	 * @param ticks
	 * @param worldTicks
	 */
	protected abstract void onCanceled(ICanThink memory, int ticks, long worldTicks);

	@Override
	public abstract boolean canExecuteIndividual(ICanThink user, int thoughtTicks, long worldTicks);

	@Override
	public abstract void beginExecutingIndividual(ICanThink forUser, int thoughtTicks, long worldTicks);

	@Override
	public abstract void executionTickIndividual(ICanThink individual, int actionTick, int thoughtTick, long worldTick);

	/**
	 * Marks this action as ready to execute the next tick, and assigns part
	 * ownership and all those things. If part ownership fails, then the action
	 * ends. Return false if part ownership fails, in case we want to do anything
	 * extra before closing the action.
	 */
	protected final boolean markActionStarted(ICanThink memory, int thinkTick, long worldTick) {
		Collection<IPartAbility> resSlots = memory.getRequiredAbilitySlots(this);
		if (memory.isAnySlotFull(resSlots)) {
			this.failedPartsCheck = true;
			return false;
		} else {
			memory.reserveAbilitySlots(resSlots, this);
			this.isExecutionFirstTick = true;
			this.isExecuting = true;
			return true;
		}
	}

	private void releaseAbilitySlots(ICanThink memory) {
		memory.releaseAbilitySlots(this);
	}

	/**
	 * Call this when an action fails conditions check and needs to generate pending
	 * conditions.
	 * 
	 * @param succeeded
	 */
	protected void markFailedConditionsCheck(ITaskGoal pendingCondition) {
		this.failedCondition = true;
		this.pendingCondition = pendingCondition;
	}

	/**
	 * Call this when the action is done executing. The action will be marked
	 * complete next tick
	 * 
	 * @param success
	 */
	protected void markActionCompleted(boolean success) {
		this.doneNextTick = true;
		this.succeeded = success;
	}

	/**
	 * retrieves the goal representing the condition the action needs in order to
	 * successfully execute. Return null if some error occurred
	 * 
	 * @return
	 */
	protected ITaskGoal getPendingCondition() {
		return this.pendingCondition;
	}

	/**
	 * Call this if the action fails in the Thinking Stage
	 */
	protected void markFailedPreemptively() {
		this.failedPreemptively = true;
		this.doneNextTick = true;
	}

	/**
	 * Gets this action's intended goal
	 * 
	 * @return
	 */
	@Override
	public final ITaskGoal getIntendedGoal() {
		return this.forGoal;
	}

	@Override
	public final AbstractActionThought setIntendedGoal(ITaskGoal goal) {
		this.forGoal = goal;
		return this;
	}

	/**
	 * Get the ultimate goal this action is part of completing
	 * 
	 * @return
	 */
	@Override
	public final ITaskGoal getPrimaryGoal() {
		return primaryGoal;
	}

	/**
	 * Get the ultimate goal this action is part of completing
	 * 
	 * @param primaryGoal
	 * @return
	 */
	@Override
	public final AbstractActionThought setPrimaryGoal(ITaskGoal primaryGoal) {
		this.primaryGoal = primaryGoal;
		return this;
	}

	@Override
	public final boolean failedPreemptively() {
		return this.failedPreemptively;
	}

	public final boolean failedPartsCheck() {
		return failedPartsCheck;
	}

	@Override
	public final void cancel(ICanThink memory, int ticks, long worldTicks) {
		onCanceled(memory, ticks, worldTicks);
		this.canceled = true;
		this.doneNextTick = true;
	}

	public final boolean wasCanceled() {
		return canceled;
	}

	@Override
	public final boolean executing() {
		return isExecuting;
	}

	@Override
	@Deprecated
	public final void start() {
		this.isExecutionFirstTick = true;
		this.isExecuting = true;
	}

}
