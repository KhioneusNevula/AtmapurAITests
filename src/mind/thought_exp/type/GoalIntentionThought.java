package mind.thought_exp.type;

import java.awt.Color;
import java.util.Collection;
import java.util.Stack;

import mind.action.IActionType;
import mind.goals.IGoal.Priority;
import mind.goals.ITaskGoal;
import mind.thought_exp.memory.IUpgradedKnowledgeBase.Interest;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;
import mind.thought_exp.ThoughtType;
import mind.thought_exp.actions.IActionThought;
import sim.WorldGraphics;

public class GoalIntentionThought extends AbstractThought {

	private boolean ended;
	private Stack<IActionThought> actions = new Stack<>();
	private Phase phase = Phase.PLANNING;
	private IActionThought considering = null;
	private IActionThought executing = null;

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
		// TODO Auto-generated method stub
		switch (phase) {
		case PLANNING:
			Collection<IActionType<?>> possibleActions = memory.getKnowledgeBase()
					.getPossibleActions(this.getGoal().getActionHint());

			break;
		case INITIATING:
			executing = actions.pop();
			executing.beginExecutingIndividual(memory, ticks, worldTick);
			phase = Phase.ACTING;
			break;
		case ACTING:

			break;
		case REVIEW:

			break;
		}
	}

	@Override
	public void getInfoFromChild(IThought childThought, boolean interrupted, int ticks) {
		// TODO Auto-generated method stub
		if (childThought == executing) {

		}
	}

	@Override
	public boolean isFinished(ICanThink memory, int ticks, long worldTick) {
		return ended;
	}

	@Override
	public void renderThoughtView(WorldGraphics g, int boxWidth, int boxHeight) {
		g.textSize(20);
		String phase = this.phase + "";
		float w = g.textWidth(phase);
		g.text(phase, boxWidth / 2 - w / 2, 30);
		g.textSize(15);
		float h = g.textAscent();
		float border = g.textDescent() * 2;
		float y = 30 + h + border;
		if (!this.actions.empty()) {
			for (IActionThought at : this.actions) {
				String txt = at.displayText();
				w = g.textWidth(txt);
				g.text(txt, boxWidth / 2 - w / 2, y);
				y += border + h;
			}
		}
		if (considering != null) {
			String txt = "considering action v";
			w = g.textWidth(txt);
			g.text(txt, boxWidth / 2 - w / 2, y);
			y += border + h;
			txt = considering.displayText();
			w = g.textWidth(txt);
			g.text(txt, boxWidth / 2 - w / 2, y);
		}
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
		PLANNING, INITIATING, ACTING, REVIEW
	}

}
/*
 * @Override public void thinkTick(ICanThink memory, int ticks, long worldTick)
 * { switch (phase) { case PLANNING: if (this.nextPush != null) { if
 * (this.childThoughts().contains(nextPush)) {
 * 
 * actions.push(nextPush); nextPush = null;
 * 
 * } else { throw new IllegalStateException(); } } else { IActionThought
 * operativeAction = this.actions.empty() ? null : this.actions.peek(); if
 * (pendingChildren != null && this.pendingChildren.contains(operativeAction))
 * return; ITaskGoal goal = operativeAction == null ? this.getGoal() :
 * (ITaskGoal) operativeAction.getPendingCondition(memory);
 * 
 * boolean complete = goal == null || goal.isComplete(memory); // TODO
 * eventually extend this to a more complicated cause/effect framework? if
 * (operativeAction != null && !memory.hasThought(operativeAction)) { if
 * (memory.getAsHasActor().getActor().getName().equals("bobzy")) {
 * System.out.print(""); } this.ended = true; // just forcequit the thought bc
 * it's malformed for some reason ig return; } if (!complete) {
 * Collection<IActionType<?>> aTypes = memory.getKnowledgeBase()
 * .getPossibleActions(goal.getActionHint());
 * 
 * Iterator<IActionType<?>> aIter = aTypes.iterator(); if
 * (memory.isMindMemory()) { aIter = Iterators.concat(aIter,
 * memory.getMindMemory()
 * .getPossibleActionsFromCulture(goal.getActionHint()).values().iterator()); }
 * List<IActionType<?>> potentialActions = new ArrayList<>(); for (int i = 0; i
 * <= memory.getMaxFocusObjects() && aIter.hasNext(); i++) { IActionType<?> act
 * = aIter.next(); if (!act.isViable(memory, goal)) { i--; continue; } for
 * (IMeme con : act.requiredConcepts(goal)) { if
 * (!memory.getKnowledgeBase().isKnown(con)) { i--; continue; } }
 * potentialActions.add(act); } if (potentialActions.isEmpty()) { this.ended =
 * true; } else { Collections.shuffle(potentialActions, memory.rand());
 * IActionType<?> selection = potentialActions.iterator().next(); IActionThought
 * action = selection.genActionThought(goal); this.postChildThought(action,
 * ticks); // this.actions.push(action); nextPush = action; } } else { if (goal
 * == this.getGoal()) { this.ended = true;
 * 
 * } else if (operativeAction != null) { operativeAction.notifyShouldResume();
 * 
 * if (operativeAction.canExecuteIndividual(memory, ticks, worldTick)) {
 * 
 * this.phase = Phase.ACTING; } else if (operativeAction.failedPreemptively()) {
 * if (operativeAction.getType() == ActionType.PICK_UP) { System.out.print("");
 * } dI = actions.indexOf(operativeAction); this.phase = Phase.REVIEW; } } } }
 * break; case ACTING: if (this.actions.isEmpty()) { this.phase =
 * Phase.PLANNING; } else { if (this.current == null) {
 * 
 * this.current = actions.peek(); Collection<IPartAbility> reqs =
 * memory.getRequiredAbilitySlots(current); if (!memory.isAnySlotFull(reqs)) {
 * memory.reserveAbilitySlots(reqs, current); current.start(); } } } break; case
 * REVIEW:
 * 
 * if (memory.hasActor() &&
 * memory.getAsHasActor().getActor().getName().equals("bobzy")) { if (current !=
 * null && current.isAction() && current.asAction().getType() == ActionType.EAT)
 * { System.out.print(""); } } int deleteFrom = dI; for (int i = 0; i <
 * actions.size(); i++) { IActionThought action = actions.get(i); if (i == dI ||
 * !this.childThoughts(ThoughtType.ACTION).contains(action) ||
 * action.getGoal().isComplete(memory)) { deleteFrom = i; break; } } if
 * (deleteFrom != -1) {
 * 
 * List<IActionThought> acs = actions.subList(deleteFrom, actions.size());
 * acs.forEach((a) -> a.cancel()); acs.clear(); } this.current = null; if
 * (actions.empty()) { this.ended = true; } else { this.phase = Phase.PLANNING;
 * } break; } }
 * 
 * @Override public void getInfoFromChild(IThought childThought, boolean
 * interrupted, int ticks) { if (childThought instanceof IActionThought iat) {
 * this.phase = Phase.REVIEW; dI = actions.indexOf(iat); //
 * this.actions.remove(iat); if (childThought instanceof PickupActionThought) {
 * System.out.print(""); } current = iat; } }
 */