package mind.feeling;

import mind.concepts.type.IMeme;

/**
 * An emotion can be constructed along these various axes. The emotion is a
 * factor that is added onto the individual's own feelings (and then applied to
 * an arctangent function to keep it between 1 and -1). For example: Happiness:
 * high pleasure, high willingness <br>
 * Sadness: high displeasure, high unwillingness <br>
 * Anger: high aggression <br>
 * Disgust: high aversion, high displeasure, high unwillingness <br>
 * Fear: high aversion, high stress <br>
 * Surprise: medium stress<br>
 * Desire: high attraction, high pleasure<br>
 * Confusion: medium stress, high confusion<br>
 * Pain: high pain<br>
 * Discomfort: high discomfort<br>
 * 
 * @author borah
 *
 */
public interface IFeeling extends IMeme, Comparable<IFeeling> {

	public static final Feeling NONE = new Feeling() {

		public Feeling setForAxis(Axis axis, float trait) {
			throw new UnsupportedOperationException();
		};
	};

	/**
	 * the factor of emotional happiness of this emotion (e.g. happiness has 1.0f
	 * enjoyment factor). Enjoyment increases the desirability of whatever produces
	 * this emotion.
	 */
	public float enjoyment();

	/**
	 * the factor of unhappiness of this emotion (e.g. sadness has 1.0f
	 * unhappiness). Unhappiness decreases the desirability of whatever produces
	 * this emotion. It is equivalent to -enjoyment.
	 * 
	 * @return
	 */
	public float unhappiness();

	/**
	 * the factor of aggression of this emotion (e.g. anger has 1.0f aggression)
	 * 
	 * @return
	 */
	public float aggression();

	/**
	 * The factor of attraction toward the target of this emotion, e.g. how much it
	 * is liked and/or wanted
	 * 
	 * @return
	 */
	public float attraction();

	/**
	 * the factor of aversion toward the target of this emotion (how much it is
	 * unwanted and avoided), equivalent to -attraction
	 * 
	 * @return
	 */
	public float aversion();

	/**
	 * the factor of stress of this emotion e.g. how much trauma it causes
	 * 
	 * @return
	 */
	public float stress();

	/**
	 * The factor of excitement of this emotion, e.g. the factor of mental arousal.
	 * Adds extra willingness and speed and whatever
	 * 
	 * @return
	 */
	public float excitement();

	/**
	 * The factor of unwilligness of this emotion, e.g. the factor of mental
	 * unwillingness/laziness. It is equivalent to -excitement
	 * 
	 * @return
	 */
	public float unwillingness();

	/**
	 * The factor of confusion of this emotion
	 * 
	 * @return
	 */
	public float confusion();

	/**
	 * The factor of physical pleasure of this emotion. Attraction + pleasure is
	 * essentially equivalent to lust(?)
	 * 
	 * @return
	 */
	public float pleasure();

	/**
	 * the factor of physical discomfort of this emotion
	 * 
	 * @return
	 */
	public float discomfort();

	/**
	 * The factor of physical pain of this emotion
	 * 
	 * @return
	 */
	public float pain();

	/**
	 * We can just keep this for now to represent, idk, supernatural feeligns or
	 * some thing
	 * 
	 * @return
	 */
	public float weird();

	/**
	 * This represents the need to prioritize the needs of the target of this
	 * feeling (it does nothing without a target)
	 * 
	 * @return
	 */
	public float love();

	/**
	 * This represents desire to hurt or otherwise deny toward the target of this
	 * feeling
	 * 
	 * @return
	 */
	public float hate();

	public float get(Axis axis);

	@Override
	default IMemeType getMemeType() {
		return MemeType.FEELING;
	}

	public static enum Axis {
		ENJOYMENT(true), PAIN, WEIRD, DISCOMFORT, PLEASURE, CONFUSION, EXCITEMENT(true), STRESS, AGGRESSION,
		ATTRACTION(true), LOVE(true);

		private boolean canBeNegative;

		private Axis() {
		}

		private Axis(boolean cbn) {
			this.canBeNegative = cbn;
		}

		public boolean canBeNegative() {
			return canBeNegative;
		}

	}

}
