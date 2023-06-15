package mind.memory.trend;

import mind.concepts.type.IConcept;

/**
 * A Trend in a culture; this will be stored in the culture and will spread
 * among people in it; individuals will have a chance of knowing it. May have
 * data that can be stored with the concept.
 */
public abstract class Trend {

	protected IConcept concept;
	/**
	 * What percent of people have adopted this trend -- the percent chance someone
	 * random will know it, essentially
	 */
	protected float percentage = 0f;

	public Trend(IConcept concept) {
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

	public IConcept getConcept() {
		return concept;
	}

	public float getPercentage() {
		return percentage;
	}

	public void setPercentage(float percentage) {
		if (percentage < 0 || percentage > 1)
			throw new IllegalArgumentException("" + percentage);
		this.percentage = percentage;
	}

}
