package mind.concepts.type;

/**
 * A representation of a piece of information
 * 
 * @author borah
 *
 */
public interface IMeme {

	/**
	 * the unique name of this concept for storage purposes and identification
	 * 
	 * @return
	 */
	public String getUniqueName();

	/**
	 * What type of meme this is
	 * 
	 * @return
	 */
	public IMemeType getMemeType();

	public static interface IMemeType {
		public String name();
	}

	public static enum MemeType implements IMemeType {
		PROFILE, PROPERTY, SENSE_PROPERTY, ACTION_TYPE, FEELING, QUESTION, GOAL, TASK_GOAL, EVENT, TREND, TRAITS,
		RELATIONSHIP, LOCATION, PHENOMENON_TYPE, LANGUAGE, PHONO_RULE, PHONEME, PHONOLOGY, NAME_WORD, GRAMMAR_SYSTEM,
		EVENT_ROLE, THOUGHT, NEED
	}

}
