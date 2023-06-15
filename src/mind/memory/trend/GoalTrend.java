package mind.memory.trend;

import mind.goals.IGoal;

public class GoalTrend extends Trend {

	public GoalTrend(IGoal concept) {
		super(concept);
	}

	@Override
	public IGoal getConcept() {
		return (IGoal) super.getConcept();
	}

}
