package mind.thought_exp.type;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import com.google.common.collect.Iterators;

import mind.action.ActionType;
import mind.action.IActionType;
import mind.concepts.type.IMeme;
import mind.goals.IGoal.Priority;
import mind.goals.ITaskGoal;
import mind.memory.IKnowledgeBase.Interest;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;
import mind.thought_exp.ThoughtType;
import mind.thought_exp.actions.IActionThought;

public class GoalIntentionThought extends AbstractThought {

	private boolean ended;
	private Stack<IActionThought> actions = new Stack<>();
	private Phase phase = Phase.PLANNING;
	private IActionThought current;

	public GoalIntentionThought(ITaskGoal mainGoal) {
		this.goal = mainGoal;
	}

	@Override
	public IThoughtType getThoughtType() {
		return ThoughtType.INTENTION;
	}

	@Override
	public boolean isLightweight() {
		return false;
	}

	@Override
	public Interest shouldBecomeMemory(ICanThink mind, int finishingTicks, long worldTicks) {
		return Interest.FORGET;
	}

	@Override
	public void startThinking(ICanThink memory, long worldTick) {

	}

	@Override
	public void thinkTick(ICanThink memory, int ticks, long worldTick) {
		switch (phase) {
		case PLANNING:
			IActionThought operativeAction = this.actions.empty() ? null : this.actions.peek();
			ITaskGoal goal = operativeAction == null ? this.getGoal()
					: (ITaskGoal) operativeAction.getPendingCondition(memory);

			boolean complete = goal == null || goal.isComplete(memory);
			// TODO eventually extend this to a more complicated cause/effect framework?
			if (operativeAction != null && operativeAction.getType() == ActionType.EAT) {
				System.out.print("");
			}
			if (!complete) {
				Collection<IActionType<?>> aTypes = memory.getKnowledgeBase().getPossibleActions(goal.getActionHint());

				Iterator<IActionType<?>> aIter = aTypes.iterator();
				if (memory.isMindMemory()) {
					aIter = Iterators.concat(aIter, memory.getMindMemory()
							.getPossibleActionsFromCulture(goal.getActionHint()).values().iterator());
				}
				List<IActionType<?>> potentialActions = new ArrayList<>();
				for (int i = 0; i <= memory.getMaxFocusObjects() && aIter.hasNext(); i++) {
					IActionType<?> act = aIter.next();
					if (!act.isViable(memory, goal)) {
						i--;
						continue;
					}
					for (IMeme con : act.requiredConcepts(goal)) {
						if (!memory.getKnowledgeBase().isKnown(con)) {
							i--;
							continue;
						}
					}
					potentialActions.add(act);
				}
				if (potentialActions.isEmpty()) {
					this.ended = true;
				} else {
					Collections.shuffle(potentialActions, memory.rand());
					IActionType<?> selection = potentialActions.iterator().next();
					IActionThought action = selection.genActionThought(goal);
					this.postChildThought(action, ticks);
					this.actions.push(action);
				}
			} else {
				if (goal == this.getGoal()) {
					this.ended = true;
				} else if (operativeAction != null) {
					operativeAction.notifyShouldResume();
					if (operativeAction.getType() == ActionType.EAT) {
						System.out.print("");
					}
					if (operativeAction.canExecuteIndividual(memory, ticks, worldTick)) {

						this.phase = Phase.ACTING;
					} else if (operativeAction.failedPreemptively()) {
						actions.pop();
					}
				}
			}

			break;
		case ACTING:
			if (this.actions.isEmpty()) {
				this.phase = Phase.PLANNING;
			} else {
				if (this.current == null) {
					IActionThought latest = actions.pop();
					latest.start();
					this.current = latest;
				}
			}
			break;
		case REVIEW:
			int deleteFrom = -1;
			for (int i = 0; i < actions.size(); i++) {
				IActionThought action = actions.get(i);
				if (action.getGoal().isComplete(memory)) {
					deleteFrom = i;
					break;
				}
			}
			if (deleteFrom != -1) {
				actions.subList(deleteFrom, actions.size()).clear();
			}
			if (actions.empty()) {
				this.ended = true;
			} else {
				this.phase = Phase.PLANNING;
			}
			break;
		}
	}

	@Override
	public void getInfoFromChild(IThought childThought, boolean interrupted, int ticks) {
		if (childThought instanceof IActionThought iat) {
			this.phase = Phase.REVIEW;
			this.current = null;
		}
	}

	@Override
	public boolean isFinished(ICanThink memory, int ticks, long worldTick) {
		return ended;
	}

	@Override
	public Priority getPriority() {
		return this.goal.getPriority();
	}

	@Override
	public ITaskGoal getGoal() {
		return (ITaskGoal) super.getGoal();
	}

	@Override
	public String displayText() {
		return "intend to complete goal " + this.goal;
	}

	private static final Color BOX_COLOR = Color.magenta;

	@Override
	public Color getBoxColor() {
		return BOX_COLOR;
	}

	@Override
	public boolean equivalent(IThought other) {
		return other instanceof GoalIntentionThought && this.goal.equivalent(other.getGoal());
	}

	public static enum Phase {
		PLANNING, ACTING, REVIEW
	}

}
