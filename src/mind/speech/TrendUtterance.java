package mind.speech;

import mind.linguistics.Language;
import mind.memory.trend.Trend;

public class TrendUtterance extends Utterance {

	public TrendUtterance(Language language, Trend importantInfo) {
		super(language, importantInfo);
	}

	@Override
	public Trend mostImportantInfo() {
		return (Trend) super.mostImportantInfo();
	}

	public Trend getTrend() {
		return mostImportantInfo();
	}

}
