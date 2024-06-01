package mind.thought_exp.type;

import java.util.Collection;
import java.util.Iterator;

import mind.action.IActionType;
import mind.goals.IGoal.Priority;
import mind.goals.ITaskGoal;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;
import mind.thought_exp.IThoughtMemory.Interest;
import mind.thought_exp.ThoughtType;

/**
 * This thought will generate an action to complete a goal
 * 
 * @author borah
 *
 */
public class IntentionThought extends AbstractThought {

	private Iterator<IActionType<?>> iter;

	public IntentionThought(ITaskGoal goal) {
		this.goal = goal;
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
	public Interest shouldProduceRecentThoughtMemory(ICanThink mind, int finishingTicks, long worldTicks) {
		return Interest.FORGET;
	}

	@Override
	public void startThinking(ICanThink memory, long worldTick) {
		Collection<IActionType<?>> thinkOfActions = memory.getMindMemory().getCollectionFromMindAndCultures(
				(a) -> a.<IActionType<?>>getKnownConceptsOfType(MemeType.ACTION_TYPE), memory.rand());
		iter = thinkOfActions.iterator();
	}

	@Override
	public void thinkTick(ICanThink memory, int ticks, long worldTick) {
		for (int i = 0; i < memory.getMaxFocusObjects() && iter.hasNext(); i++) {

		}
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
	public void getInfoFromChild(ICanThink mind, IThought childThought, boolean interrupted, int ticks) {
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
