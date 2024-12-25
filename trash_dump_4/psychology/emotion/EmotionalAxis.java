package psychology.emotion;

/**
 * describes things which are fundamental to an entity which emotions increase
 * or decrease.
 * 
 * @author borah
 *
 */
public class EmotionalAxis {

	/**
	 * describes whether the axis of this feeling
	 * 
	 * @author borah
	 *
	 */
	public static enum Positivity {
		POSITIVE, NEGATIVE, NEUTRAL;

		public boolean isPositive() {
			return this == POSITIVE;
		}

		public boolean isNegative() {
			return this == NEGATIVE;
		}

	}

	private String name;

	/**
	 * how much something fits what the entity wants
	 */
	public static final EmotionalAxis DESIRABILITY = new EmotionalAxis("desirability");

	/**
	 * how expected something is
	 */
	public static final EmotionalAxis ACTIVATION = new EmotionalAxis("activation");

	/**
	 * a final axis for incomprehensible, eldritch ideas
	 */
	public static final EmotionalAxis STRANGE = new EmotionalAxis("strange");

	public EmotionalAxis(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
