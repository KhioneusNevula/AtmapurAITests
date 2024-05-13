package mind.thought_exp.type;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import main.Pair;
import mind.action.IActionType;
import mind.concepts.relations.ConceptRelationType;
import mind.goals.IGoal.Priority;
import mind.goals.ITaskGoal;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;
import mind.thought_exp.IThoughtMemory;
import mind.thought_exp.IThoughtMemory.Interest;
import mind.thought_exp.IThoughtMemory.MemoryCategory;
import mind.thought_exp.ThoughtType;
import mind.thought_exp.actions.IActionThought;
import mind.thought_exp.memory.type.MemoryWrapper;
import mind.thought_exp.memory.type.RecentActionMemory;
import mind.thought_exp.memory.type.RecentIntentionMemory;
import sim.WorldGraphics;

public class IntentionThought extends AbstractThought {

	private boolean done = false;
	private Phase phase = Phase.FINDING_ACTION;
	private IActionThought action;
	private IActionType<?> actionType;
	private Stack<Pair<IActionType<?>, ITaskGoal>> goalStack;
	/** if an action sequence fails try another */
	private Iterator<IActionType<?>> actionIterator;
	private int times = 0;

	public IntentionThought(ITaskGoal goal) {
		this.setGoal(goal);
		goalStack = new Stack<>();
	}

	private ITaskGoal mostRecentGoal() {
		return this.goalStack.empty() ? this.getGoal() : this.goalStack.peek().getSecond();
	}

	@Override
	public ITaskGoal getGoal() {
		return (ITaskGoal) super.getGoal();
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
	public IThoughtMemory.Interest shouldProduceRecentThoughtMemory(ICanThink mind, int finishingTicks,
			long worldTicks) {
		return IThoughtMemory.Interest.FORGET;
	}

	@Override
	public Map<IThoughtMemory, Interest> produceMemories(ICanThink mind, int finishingTicks, long worldTicks) {

		return Map.of(new RecentIntentionMemory(getGoal(), goalStack, times).setPriority(Priority.SERIOUS),
				Interest.SHORT_TERM);
	}

	@Override
	public void startThinking(ICanThink memory, long worldTick) {

		Collection<MemoryWrapper> stm = memory.getMindMemory()
				.getShortTermMemoriesOfType(MemoryCategory.RECENT_INTENTION);

		RecentIntentionMemory rim = null;
		for (MemoryWrapper mw : stm) {
			if (mw.getMemory() instanceof RecentIntentionMemory mem) {
				if (this.goal.equals(mem.getPrimaryGoal())) {
					rim = mem;
					this.goalStack = mem.goalStack();
					this.times = mem.getTimes();
				}
			}
		}
		if (rim != null)
			memory.getMindMemory().forgetMemory(Interest.SHORT_TERM, rim);
		if (!done && this.mostRecentGoal() != null) {
			Collection<IActionType<?>> actions = memory.getMindMemory().getCollectionFromMindAndCultures(
					(b) -> b.getConceptsWithRelation(mostRecentGoal(), ConceptRelationType.USED_FOR.inverse()),
					memory.rand());

			actionIterator = actions.iterator();
		}
	}

	@Override
	public void thinkTick(ICanThink memory, int ticks, long worldTick) {
		switch (phase) {
		case ACTION_CHECKING: {

		}
			break;
		case CHECKING_GOAL: {

		}
			break;
		case FINDING_ACTION: {
			if (!actionIterator.hasNext())
				this.phase = Phase.FINISH;
			else {
				actionType = actionIterator.next();
				boolean canDo = true;
				for (MemoryWrapper memw : memory.getMindMemory()
						.getShortTermMemoriesOfType(MemoryCategory.RECENT_ACTION)) {
					if (memw.getMemory() instanceof RecentActionMemory ram) {
						if (ram.getActionType().equals(actionType) && !ram.succeeded()) {
							canDo = false;
							break;
						}
					}
				}

			}
		}
			break;

		case CHECK_EXECUTION: {
		}
			break;
		case EXECUTE: {
			this.times++;
			this.done = true;

		}
			break;

		case FINISH: {
			// Check main goal completion
		}
			break;
		case FINISH_CHECK: {

		}
			break;
		}

	}

	@Override
	public void getInfoFromChild(ICanThink memory, IThought childThought, boolean interrupted, int ticks) {

	}

	@Override
	public boolean isFinished(ICanThink memory, int ticks, long worldTick) {
		return done;
	}

	public IThought getSubsequentThought(boolean interrupted) {
		return null;

	}

	@Override
	public Priority getPriority() {
		return goal.getPriority();
	}

	@Override
	public String displayText() {
		return "intend to complete " + this.goal;
	}

	@Override
	public void renderThoughtView(WorldGraphics g, int boxWidth, int boxHeight) {
		String tostr = "intend to complete " + this.goal;
		if (this.phase == Phase.CHECKING_GOAL) {
			tostr += ": checking " + this.mostRecentGoal();
		} else if (this.phase == Phase.ACTION_CHECKING) {
			tostr += ": checking " + this.actionType;
		}
		g.textSize(20);
		float w = g.textWidth(tostr);
		float h = g.textAscent() + g.textDescent();
		g.text(tostr, boxWidth / 2 - w / 2, boxHeight / 2 + h / 2);
	}

	@Override
	public boolean equivalent(IThought other) {
		return other.getGoal() != null && this.getGoal().equivalent(other.getGoal());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IntentionThought it) {
			return this.getGoal().equals(it.getGoal())
					&& (this.action == null ? it.action == null : (it.action != null && this.action.equals(it.action)));
		}
		return false;
	}

	public static enum Phase {
		ACTION_CHECKING, FINDING_ACTION, CHECKING_GOAL, CHECK_EXECUTION, EXECUTE, FINISH, FINISH_CHECK
	}

}
