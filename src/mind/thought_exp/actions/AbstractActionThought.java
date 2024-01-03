package mind.thought_exp.actions;

import java.awt.Color;

import mind.goals.IGoal.Priority;
import mind.goals.ITaskGoal;
import mind.thought_exp.IUpgradedHasKnowledge;
import mind.thought_exp.memory.IUpgradedKnowledgeBase.Interest;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;
import mind.thought_exp.ThoughtType;
import mind.thought_exp.type.AbstractThought;
import sim.WorldGraphics;

public abstract class AbstractActionThought extends AbstractThought implements IActionThought {

	private boolean succeeded;
	protected Priority priority;
	private ITaskGoal pendingCondition;
	private int actionTicks = -1;
	private boolean started;
	private boolean ended;
	private boolean shouldResume;
	private boolean shouldCancel;
	private boolean shouldInterrupt;
	private String failureReason;
	private boolean preemptivelyFailed;

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
		return (ended || shouldCancel);
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
					if (this instanceof EatActionThought) {
						System.out.print("");
					}
					this.ended = true;
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
						if (this instanceof EatActionThought) {
							System.out.print(""); // TODO remove this dumbery
						}
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
	 * Specify the specific condition to be returned if the action cannot execute;
	 * 
	 * @param memory
	 * @param thinkTicks
	 * @param worldTick
	 * @return
	 */
	protected final void postConditionForExecution(ITaskGoal condition) {
		this.pendingCondition = condition;
	}

	@Override
	public boolean pauseThought(ICanThink memory, int ticks, long wTicks) {
		return !started ? super.pauseThought(memory, ticks, wTicks) : false; // if action has started it cannot be
																				// paused
	}

	@Override
	public final boolean shouldPause(ICanThink mind, int ticks, long worldTicks) {
		return this.pendingCondition != null;
	}

	@Override
	public final boolean resume(ICanThink memory, int ticks, long worldTick) {
		if (shouldResume) {
			pendingCondition = null;
			this.uponResume(memory, ticks, worldTick);
		}
		return !shouldResume;
	}

	protected void uponResume(ICanThink mind, int ticks, long worldTick) {

	}

	@Override
	public String displayText() {
		return (started ? "doing " : "pondering ") + "action " + this.getType().getName();
	}

	@Override
	public void renderThoughtView(WorldGraphics g, int boxWidth, int boxHeight) {
		g.textSize(20);
		g.textAlign(g.CENTER);
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

	/**
	 * Notify an action thought that it should resume
	 */
	@Override
	public final void notifyShouldResume() {
		shouldResume = true;
	}

	/**
	 * Cancel this action
	 */
	@Override
	public void cancel() {
		if (this instanceof PickupActionThought) {
			System.out.print("");
		}
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
	}

	/**
	 * If this action has started yet
	 * 
	 * @return
	 */
	@Override
	public final boolean started() {
		return started;
	}

	@Override
	public final void start() {
		this.started = true;
	}

	@Override
	public Interest shouldBecomeMemory(ICanThink mind, int finishingTicks, long worldTicks) {
		return Interest.SHORT_TERM; // TODO action memory stuff
	}

	private static final Color ACTIVE_BOX_COLOR = Color.cyan;
	private static final Color THINKING_BOX_COLOR = new Color(115, 79, 150);

	@Override
	public Color getBoxColor() {
		return this.started ? ACTIVE_BOX_COLOR : THINKING_BOX_COLOR;
	}

}
