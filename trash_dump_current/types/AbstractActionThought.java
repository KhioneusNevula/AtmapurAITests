package mind.thought_exp.actions;

import java.awt.Color;
import java.util.Map;

import mind.goals.IGoal.Priority;
import mind.goals.ITaskGoal;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;
import mind.thought_exp.IThoughtMemory;
import mind.thought_exp.IThoughtMemory.Interest;
import mind.thought_exp.IUpgradedHasKnowledge;
import mind.thought_exp.ThoughtType;
import mind.thought_exp.memory.type.RecentActionMemory;
import mind.thought_exp.type.AbstractThought;
import processing.core.PConstants;
import sim.WorldGraphics;

public abstract class AbstractActionThought extends AbstractThought implements IActionThought {

	protected boolean succeeded;
	protected Priority priority;
	private ITaskGoal pendingCondition;
	private int actionTicks = -1;
	private boolean started;
	private boolean ended;
	private boolean shouldCancel;
	private boolean shouldInterrupt;
	private String failureReason;
	private boolean preemptivelyFailed;

	private boolean addedShortTermMemories;
	private boolean oughtToCheckShortTermMemories;

	public AbstractActionThought(ITaskGoal goal) {
		this.priority = goal.getPriority();
		this.goal = goal;
	}

	@Override
	public ITaskGoal getGoal() {
		return (ITaskGoal) super.getGoal();
	}

	public final Priority getPriority() {
		return priority;
	}

	protected final void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}

	@Override
	public final String reasonForLastFailure() {
		return failureReason;
	}

	/**
	 * Gets the ticks this action has been running
	 */
	public final int getActionTicks() {
		return actionTicks;
	}

	@Override
	public final IThoughtType getThoughtType() {
		return ThoughtType.ACTION;
	}

	@Override
	public final boolean isFinished(ICanThink memory, int ticks, long worldTick) {
		return ended || shouldCancel;
	}

	@Override
	public final void actionTick(ICanThink memory, int ticks, long worldTick) {
		if (this.shouldCancel) {
			return;
		}
		if (this.shouldInterrupt) {
			this.succeeded = this.finishActionIndividual(memory, Math.max(actionTicks, 0), ticks, true);
			this.shouldCancel = true;
		} else {
			if (!this.started) {
				if (this.failedPreemptively()) {
					this.shouldCancel = true;
				}
			} else {
				if (this.actionTicks < 0) {
					this.beginExecutingIndividual(memory, ticks, worldTick);
					this.actionTicks = 0;
				} else {
					if (this.canContinueExecutingIndividual(memory, actionTicks, ticks)) {
						this.executionTickIndividual(memory, actionTicks, ticks);
						this.actionTicks++;
					} else {
						this.succeeded = this.finishActionIndividual(memory, actionTicks, ticks, false);
						memory.releaseAbilitySlots(this);
						this.ended = true;
					}
				}
			}
		}
	}

	@Override
	public final void interruptThought(ICanThink memory, int ticks, long worldTick) {
		this.succeeded = this.finishActionIndividual(memory, actionTicks, ticks, true);
		this.ended = true;
	}

	/**
	 * Post a condition for execution of this action.
	 * 
	 * @param memory
	 * @param thinkTicks
	 * @param worldTick
	 * @return
	 */
	protected final void postConditionForExecution(ITaskGoal condition) {
		this.pendingCondition = condition;
		this.ended = true;
	}

	/**
	 * Marks this method as ready to execute without needing to post a condition
	 */
	protected final void markAsReadyToExecute() {
		this.ended = true;
	}

	@Override
	public boolean pauseThought(ICanThink memory, int ticks, long wTicks) {
		return !started ? super.pauseThought(memory, ticks, wTicks) : false; // if action has started it cannot be
																				// paused
	}

	/*
	 * @Override public final boolean shouldPause(ICanThink mind, int ticks, long
	 * worldTicks) { return this.pendingCondition != null; }
	 * 
	 * @Override public final boolean resume(ICanThink memory, int ticks, long
	 * worldTick) { if (shouldResume) { pendingCondition = null;
	 * this.uponResume(memory, ticks, worldTick); } return !shouldResume; }
	 * 
	 * protected void uponResume(ICanThink mind, int ticks, long worldTick) {
	 * 
	 * }
	 */

	@Override
	public String displayText() {
		return (started ? "doing " : "pondering ") + "action " + this.getActionType().getName();
	}

	@Override
	public void renderThoughtView(WorldGraphics g, int boxWidth, int boxHeight) {
		g.textSize(20);
		g.textAlign(PConstants.CENTER);
		String text = "";
		for (IThought thought : this.childThoughts()) {
			text += thought.displayText() + "\n";
		}
		g.text(text, boxWidth / 2, 20);
	}

	@Override
	public final ITaskGoal getPendingCondition(IUpgradedHasKnowledge user) {
		return pendingCondition;
	}

	@Override
	public final boolean hasPendingCondition(IUpgradedHasKnowledge user) {
		return pendingCondition != null;
	}

	@Override
	public final boolean addedShortTermMemories() {
		return addedShortTermMemories;
	}

	/**
	 * Used to indicate that this action has added short term memories which ought
	 * to be checked by the next action
	 */
	protected final void markAddedShortTermMemories() {
		this.addedShortTermMemories = true;
	}

	/**
	 * Whether this thought ought to check short term memories
	 * 
	 * @return
	 */
	protected final boolean oughtToCheckShortTermMemories() {
		return oughtToCheckShortTermMemories;
	}

	@Override
	public final void notifyCheckMemories() {
		this.oughtToCheckShortTermMemories = true;
	}

	/**
	 * Cancel this action
	 */
	@Override
	public void cancel() {
		if (!this.started) {
			this.shouldCancel = true;
		} else {
			shouldInterrupt = true;
		}
	}

	@Override
	public final boolean succeeded() {
		return succeeded;
	}

	@Override
	public final boolean failedPreemptively() {
		return preemptivelyFailed;
	}

	protected final void failPreemptively() {
		this.preemptivelyFailed = true;
		this.ended = true;
	}

	/**
	 * If this action is executing
	 * 
	 * @return
	 */
	@Override
	public final boolean started() {
		return started;
	}

	/**
	 * Whether this action is in the pondering stage
	 * 
	 * @return
	 */
	protected final boolean isPondering() {
		return !started;
	}

	/**
	 * Whether this action is in the executing stage
	 * 
	 * @return
	 */
	protected final boolean isExecuting() {
		return started;
	}

	@Override
	public final void start() {
		this.started = true;
		this.ended = false;
	}

	@Override
	public IThoughtMemory.Interest shouldProduceRecentThoughtMemory(ICanThink mind, int finishingTicks,
			long worldTicks) {
		return IThoughtMemory.Interest.FORGET; // TODO action memory stuff
	}

	@Override
	public Map<IThoughtMemory, Interest> produceMemories(ICanThink mind, int finishingTicks, long worldTicks) {
		return Map.of(new RecentActionMemory(this, succeeded), Interest.SHORT_TERM);
	}

	private static final Color ACTIVE_BOX_COLOR = Color.cyan;
	private static final Color THINKING_BOX_COLOR = new Color(115, 79, 150);

	@Override
	public Color getBoxColor() {
		return this.started ? ACTIVE_BOX_COLOR : THINKING_BOX_COLOR;
	}

	@Override
	public String getUniqueName() {
		return this.getClass().getSimpleName().toLowerCase() + "_" + goal.getUniqueName();
	}

}
