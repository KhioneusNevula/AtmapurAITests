package mind.personal;

public interface IEmotion {

	/**
	 * the factor of pleasurability of this emotion (e.g. happiness has 1.0f
	 * pleasurability factor)
	 */
	public float pleasure();

	/**
	 * the factor of displeasure of this emotion (e.g. sadness has 1.0f displeasure)
	 * 
	 * @return
	 */
	public boolean displeasure();

	/**
	 * the factor of aggression of this emotion (e.g. anger has 1.0f aggression)
	 * 
	 * @return
	 */
	public boolean aggression();

	/**
	 * the factor of aversion toward the target of this emotion (e.g. fear has 1.0f
	 * aversion whereas disgust has maybe 0.5f aversion)
	 * 
	 * @return
	 */
	public boolean aversion();

	/**
	 * the factor of stress of this emotion (e.g. fear has 1.0f stress while
	 * surprise only has like 0.2f stress)
	 * 
	 * @return
	 */
	public boolean stress();
}
