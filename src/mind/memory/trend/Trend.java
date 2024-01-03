package mind.memory.trend;

import mind.concepts.type.IMeme;
import mind.thought_exp.memory.IUpgradedKnowledgeBase;

/**
 * A Trend in a culture; this will be stored in the culture and will spread
 * among people in it; individuals will have a chance of knowing it. May have
 * data that can be stored with the concept.
 */
public abstract class Trend implements ITrend {

	protected IMeme concept;
	/**
	 * If this trend is not yet fully integrated into the culture. This flag merely
	 * allows individuals who have stored the trend in their memory to know when to
	 * delete it from their memory
	 */
	private boolean integrated;
	private boolean deletion;

	public Trend(IMeme concept) {
		this.concept = concept;
	}

	@Override
	public boolean isDeletion() {
		return deletion;
	}

	/**
	 * Makes this trend a deletion trend. If this cannot be directly converted to a
	 * deletion trend, return the deletion variant of this trend from this method
	 * 
	 * @return
	 */
	public Trend setDeletionTrend() {
		this.deletion = true;
		return this;
	}

	/**
	 * Whatever data object is stored with the concept of the trend
	 * 
	 * @return
	 */
	@Override
	public Object getData() {
		return null;
	}

	@Override
	public IMeme getConcept() {
		return concept;
	}

	@Override
	public String getUniqueName() {
		return "trend_" + this.getClass().getSimpleName().replace("trend", "") + "_" + concept.getUniqueName();
	}

	@Override
	public IMemeType getMemeType() {
		return MemeType.TREND;
	}

	/**
	 * If this trend has been integrated into the culture
	 */
	@Override
	public boolean isIntegrated() {
		return integrated;
	}

	@Override
	public final void integrate(IUpgradedKnowledgeBase know) {
		this.integrated = true;
	}

	protected abstract void integrateTrend(IUpgradedKnowledgeBase know);

	@Override
	public Trend cloneUnintegrated() {
		Trend t = clone();
		t.integrated = false;
		return t;
	}

	@Override
	public Trend clone() {
		try {
			return (Trend) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ITrend trend) {
			return this.concept.equals(trend.getConcept()) && (this.deletion == trend.isDeletion())
					&& (this.getData() == null ? trend.getData() == null

							: (trend.getData() == null ? false : this.getData().equals(trend.getData())));
		}
		return false;
	}

	@Override
	public String toString() {
		return "trend_" + this.concept;
	}

}
