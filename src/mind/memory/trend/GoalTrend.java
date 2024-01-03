package mind.memory.trend;

import mind.goals.IGoal;
import mind.thought_exp.memory.IUpgradedKnowledgeBase;

public class GoalTrend extends Trend {

	public GoalTrend(IGoal concept) {
		super(concept);
	}

	@Override
	public IGoal getConcept() {
		return (IGoal) super.getConcept();
	}

	@Override
	public TrendType getType() {
		return TrendType.GOAL;
	}

	@Override
	protected void integrateTrend(IUpgradedKnowledgeBase know) {
		if (this.isDeletion()) {
			know.forgetGoal(getConcept());
		} else {
			know.learnGoal(getConcept());
		}
	}

}
