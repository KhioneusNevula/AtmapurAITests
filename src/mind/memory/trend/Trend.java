package mind.memory.trend;

import mind.concepts.type.IMeme;

/**
 * A Trend in a culture; this will be stored in the culture and will spread
 * among people in it; individuals will have a chance of knowing it. May have
 * data that can be stored with the concept.
 */
public abstract class Trend implements IMeme {

	protected IMeme concept;

	public Trend(IMeme concept) {
		this.concept = concept;
	}

	/**
	 * Whatever data object is stored with the concept of the trend
	 * 
	 * @return
	 */
	public Object getData() {
		return null;
	}

	public IMeme getConcept() {
		return concept;
	}

	@Override
	public String getUniqueName() {
		return "trend_" + this.getClass().getSimpleName().replace("trend", "") + "_" + concept.getUniqueName();
	}

}
