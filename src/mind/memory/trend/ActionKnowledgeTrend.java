package mind.memory.trend;

import mind.action.IActionType;

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

}
