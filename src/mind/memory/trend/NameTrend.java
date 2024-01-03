package mind.memory.trend;

import main.Pair;
import mind.concepts.type.IMeme;
import mind.linguistics.Language;
import mind.linguistics.NameWord;
import mind.thought_exp.memory.IUpgradedKnowledgeBase;

public class NameTrend extends Trend {

	private Pair<Language, NameWord> data;

	public NameTrend(IMeme concept, Language language, NameWord name) {
		super(concept);
		this.data = Pair.of(language, name);
	}

	@Override
	public Pair<Language, NameWord> getData() {
		return data;
	}

	@Override
	public TrendType getType() {
		return TrendType.NAME;
	}

	@Override
	protected void integrateTrend(IUpgradedKnowledgeBase know) {
		// TODO NAmTrend

	}

}
