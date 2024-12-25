package civilization_and_minds.mind.action.thought;

import civilization_and_minds.mind.IMind;
import civilization_and_minds.mind.action.IActionThought;
import civilization_and_minds.mind.goals.IGoal;

public class EatActionThought implements IActionThought {

	public EatActionThought(IGoal goal) {

	}

	@Override
	public boolean isComplete(IMind mind, long ticks) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void runTick(IMind mind, long ticks) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean checksGoals() {
		// TODO Auto-generated method stub
		return false;
	}

}
