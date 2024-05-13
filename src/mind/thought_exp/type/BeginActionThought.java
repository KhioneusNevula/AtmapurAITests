package mind.thought_exp.type;

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
import mind.thought_exp.actions.IActionThought;
import mind.thought_exp.memory.type.RecentActionMemory;

public class BeginActionThought extends AbstractThought {

	private ITaskGoal ultimateGoal;
	private boolean done;
	private IActionThought suggestedAction;
	private RecentActionMemory failureMemory;

	public BeginActionThought(ITaskGoal immediateGoal, ITaskGoal ultimateGoal, IActionThought suggestedAction) {
		this.goal = immediateGoal;
		this.ultimateGoal = ultimateGoal;
		this.suggestedAction = suggestedAction;
	}

	@Override
	public IThoughtType getThoughtType() {
		return ThoughtType.PLAN_ACTION;
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
		if (failureMemory != null) {
			return Map.of(failureMemory, Interest.SHORT_TERM);
		}
		return super.produceMemories(mind, finishingTicks, worldTicks);
	}

	@Override
	public void startThinking(ICanThink memory, long worldTick) {
		boolean hadToGenerate = false;
		Collection<IPartAbility> resSlots = memory.getRequiredAbilitySlots(suggestedAction);
		if (memory.isAnySlotFull(resSlots)) {
			this.done = true;
			this.failureMemory = new RecentActionMemory(suggestedAction, false);
		} else {
			memory.reserveAbilitySlots(resSlots, suggestedAction);
			this.suggestedAction.start();
			this.postChildThought(suggestedAction, 0);
		}

	}

	@Override
	public boolean isFinished(ICanThink memory, int ticks, long worldTick) {

		return done;
	}

	@Override
	public Priority getPriority() {
		return Priority.NORMAL;
	}

	@Override
	public void thinkTick(ICanThink memory, int ticks, long worldTick) {

	}

	@Override
	public void getInfoFromChild(ICanThink memory, IThought childThought, boolean interrupted, int ticks) {

	}

	@Override
	public ITaskGoal getGoal() {
		return (ITaskGoal) super.getGoal();
	}

	@Override
	public String displayText() {
		return "begin action " + this.suggestedAction.getActionType() + " for goal " + this.goal;
	}

	@Override
	public boolean equivalent(IThought other) {
		return other instanceof BeginActionThought && this.goal.equals(other.getGoal()) && this.suggestedAction
				.getActionType().equals(((BeginActionThought) other).suggestedAction.getActionType());
	}

}
