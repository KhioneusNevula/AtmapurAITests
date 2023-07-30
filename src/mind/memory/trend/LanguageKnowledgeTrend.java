package mind.memory.trend;

import mind.linguistics.Language;

public class LanguageKnowledgeTrend extends Trend {

	private boolean mainLanguage;

	public LanguageKnowledgeTrend(Language concept, boolean mainLanguage) {
		super(concept);
	}

	@Override
	public Language getConcept() {
		return (Language) super.getConcept();
	}

	@Override
	public Boolean getData() {
		return mainLanguage;
	}

	public boolean isMainLanguage() {
		return mainLanguage;
	}

	@Override
	public TrendType getType() {
		return TrendType.LANGUAGE_KNOWLEDGE;
	}

}
