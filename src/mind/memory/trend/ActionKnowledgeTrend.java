package mind.memory.trend;

import mind.action.IActionType;
import mind.thought_exp.memory.IUpgradedKnowledgeBase;

public class ActionKnowledgeTrend extends Trend {

	public ActionKnowledgeTrend(IActionType<?> concept) {
		super(concept);
	}

	@Override
	public IActionType<?> getConcept() {
		return (IActionType<?>) super.getConcept();
	}

	@Override
	public TrendType getType() {
		return TrendType.ACTION_KNOWLEDGE;
	}

	@Override
	protected void integrateTrend(IUpgradedKnowledgeBase know) {
		if (this.isDeletion()) {
			know.forgetConcept(concept);
		} else {
			know.learnConcept(concept);
		}
	}

}
