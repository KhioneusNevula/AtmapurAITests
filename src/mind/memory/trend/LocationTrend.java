package mind.memory.trend;

import mind.concepts.type.ILocationMeme;
import mind.concepts.type.Profile;
import mind.thought_exp.memory.IUpgradedKnowledgeBase;

public class LocationTrend extends Trend {

	private ILocationMeme location;

	public LocationTrend(Profile concept, ILocationMeme location) {
		super(concept);
		this.location = location;
	}

	@Override
	public Profile getConcept() {
		return (Profile) super.getConcept();
	}

	@Override
	public ILocationMeme getData() {
		return location;
	}

	@Override
	public TrendType getType() {
		return TrendType.LOCATION_INFO;
	}

	@Override
	public String toString() {
		return super.toString() + "_" + this.location;
	}

	@Override
	protected void integrateTrend(IUpgradedKnowledgeBase know) {
		// TODO location trend
	}

}
