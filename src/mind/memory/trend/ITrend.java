package mind.memory.trend;

import mind.concepts.type.IMeme;

public interface ITrend extends IMeme, Cloneable {

	/**
	 * Whatever data object is stored with the concept of the trend
	 * 
	 * @return
	 */
	Object getData();

	IMeme getConcept();

	public TrendType getType();

	/**
	 * If this trend has been integrated into the culture
	 */
	boolean isIntegrated();

	/**
	 * Marks this trend as fully integrated into the culture
	 */
	void integrate();

	/**
	 * Clones this trend and un-integrates it
	 * 
	 * @return
	 */
	public ITrend cloneUnintegrated();

	public ITrend clone();

	/**
	 * If this is a deletion trend
	 * 
	 * @return
	 */
	public boolean isDeletion();

	public static enum TrendType {
		ACTION_KNOWLEDGE, GOAL, LOCATION_INFO, PROFILE_ASSOCIATIONS, PROPERTY_KNOWLEDGE, LANGUAGE_KNOWLEDGE, NAME
	}

}
