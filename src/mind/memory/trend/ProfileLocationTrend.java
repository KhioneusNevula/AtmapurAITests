package mind.memory.trend;

import mind.concepts.type.Profile;
import sim.Location;

public class ProfileLocationTrend extends Trend {

	private Location location;

	public ProfileLocationTrend(Profile concept, Location location) {
		super(concept);
		this.location = location;
	}

	@Override
	public Profile getConcept() {
		return (Profile) super.getConcept();
	}

	@Override
	public Location getData() {
		return location;
	}

}
