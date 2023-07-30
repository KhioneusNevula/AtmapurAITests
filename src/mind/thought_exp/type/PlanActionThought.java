package mind.thought_exp.type;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import mind.action.IActionType;
import mind.concepts.type.IMeme;
import mind.goals.IGoal.Priority;
import mind.goals.ITaskGoal;
import mind.goals.ITaskHint;
import mind.memory.IKnowledgeBase.Interest;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;
import mind.thought_exp.ThoughtType;
import mind.thought_exp.actions.IActionThought;

public class PlanActionThought extends AbstractThought {

	private IActionType<?> actionType;
	private Collection<IActionType<?>> failedActions = new TreeSet<>(
			(a, b) -> a.getUniqueName().compareTo(b.getUniqueName()));
	private IActionThought action;
	private ITaskHint hint;
	private boolean failed;
	private boolean actionSucceeded;

	public PlanActionThought(ITaskGoal goal) {
		this.goal = goal;
		this.hint = goal.getActionHint();
	}

	public PlanActionThought addFailedActions(Collection<IActionType<?>> failed) {
		failedActions.addAll(failed);
		return this;
	}

	@Override
	public Collection<IActionType<?>> getInformation() {
		return failedActions;
	}

	public IActionType<?> getActionType() {
		return actionType;
	}

	@Override
	public ITaskGoal getGoal() {
		return (ITaskGoal) super.getGoal();
	}

	@Override
	public IThoughtType getThoughtType() {
		return ThoughtType.PLAN_ACTION;
	}

	@Override
	public boolean isLightweight() {
		return true;
	}

	@Override
	public Interest shouldBecomeMemory(ICanThink mind, int finishingTicks, long worldTicks) {
		return Interest.FORGET;
	}

	@Override
	public void startThinking(ICanThink mind, long worldTick) {

	}

	public boolean failed() {
		return failed;
	}

	public boolean succeeded() {
		return actionSucceeded;
	}

	@Override
	public boolean isFinished(ICanThink memory, int ticks, long worldTick) {
		return actionSucceeded || failed;
	}

	@Override
	public Priority getPriority() {
		return goal.getPriority();
	}

	@Override
	public void thinkTick(ICanThink mind, int ticks, long worldTick) {

		if (this.goal.isComplete(mind)) {
			if (action != null) {
				this.action.cancel();
			}
			this.actionSucceeded = true;

		} else {

			if (action == null) {

				Set<IActionType<?>> potentialActions = new HashSet<>(mind.getMindMemory().getPossibleActions(hint));

				potentialActions.addAll(mind.getMindMemory().getPossibleActionsFromCulture(hint).values());
				for (IActionType<?> atype : Set.copyOf(potentialActions)) {

					if (this.failedActions.contains(atype)) {
						continue;
					}

					if (!atype.isViable(mind, getGoal())) {
						this.failedActions.add(atype);
						continue;
					}
					for (IMeme meme : atype.requiredConcepts(getGoal())) {
						if (!mind.getKnowledgeBase().isKnown(meme)) {
							this.failedActions.add(atype);
							continue;
						}
					}
				}
				if (potentialActions.isEmpty()) {
					failed = true;
				} else {
					this.actionType = potentialActions.stream().reduce(null,
							(a, b) -> a == null ? b : (mind.rand().nextBoolean() ? a : b));
					action = actionType.genActionThought(getGoal());
					if (action == null) {

					}
					this.postChildThought(action, ticks);
				}
			} else {
				if (action.hasPendingCondition(mind) && this.childThoughts(ThoughtType.PLAN_ACTION).isEmpty()) {
					this.postChildThought(new PlanActionThought(action.getPendingCondition(mind)), ticks);
				}

			}
		}
	}

	@Override
	public void getInfoFromChild(IThought childThought, boolean interrupted, int ticks) {
		if (childThought instanceof PlanActionThought pat) {
			if (pat.failed) {
				this.failedActions.add(actionType);
				this.actionType = null;
				if (this.action != null) {
					this.action.cancel();
				}
			} else if (pat.actionSucceeded) {
				if (this.action != null) {
					this.action.notifyShouldResume();
				}
			}

		} else if (childThought instanceof IActionThought action) {
			this.actionSucceeded = action.succeeded();
		}
	}

	@Override
	public void notifyOfParentCompletion() {
		if (!this.actionSucceeded) {
			this.failed = true; // basically just ensure this ends
			if (action != null)
				action.cancel();
		}
	}

	@Override
	public String displayText() {
		return "planning for goal " + this.goal.toString();
	}

	@Override
	public boolean equivalent(IThought other) {
		if (other.getGoal() != null && other instanceof PlanActionThought) {
			return this.goal.equivalent(other.getGoal());
		}
		return false;
	}

}
