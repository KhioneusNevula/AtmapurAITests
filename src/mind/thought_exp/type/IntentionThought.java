package mind.thought_exp.type;

import mind.goals.IGoal.Priority;
import mind.goals.ITaskGoal;
import mind.thought_exp.memory.IUpgradedKnowledgeBase.Interest;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;
import mind.thought_exp.ThoughtType;

public class IntentionThought extends AbstractThought {

	private ITaskGoal goal;

	public IntentionThought(ITaskGoal goal) {
		this.goal = goal;
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
		return Interest.SHORT_TERM;
	}

	@Override
	public void startThinking(ICanThink memory, long worldTick) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isFinished(ICanThink memory, int ticks, long worldTick) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Priority getPriority() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void thinkTick(ICanThink memory, int ticks, long worldTick) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getInfoFromChild(IThought childThought, boolean interrupted, int ticks) {
		// TODO Auto-generated method stub

	}

	@Override
	public String displayText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equivalent(IThought other) {
		// TODO Auto-generated method stub
		return false;
	}

}
