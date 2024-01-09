package mind.thought_exp.type;

import mind.goals.IGoal.Priority;
import mind.goals.ITaskGoal;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;
import mind.thought_exp.IThoughtMemory;
import mind.thought_exp.IThoughtMemory.Interest;
import mind.thought_exp.ThoughtType;

public class IntentionThought extends AbstractThought {

	private boolean done = false;

	public IntentionThought(ITaskGoal goal) {
		this.setGoal(goal);
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
	public IThoughtMemory.Interest shouldBecomeMemory(ICanThink mind, int finishingTicks, long worldTicks) {
		return IThoughtMemory.Interest.SHORT_TERM;
	}

	@Override
	public void startThinking(ICanThink memory, long worldTick) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isFinished(ICanThink memory, int ticks, long worldTick) {
		return done;
	}

	@Override
	public Priority getPriority() {
		return goal.getPriority();
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
		return "intend to complete " + this.goal;
	}

	@Override
	public boolean equivalent(IThought other) {
		return other.getGoal() != null && this.getGoal().equivalent(other.getGoal());
	}

}
