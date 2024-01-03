package mind.thought_exp;

import mind.concepts.type.IMeme;
import mind.feeling.IFeeling;

/**
 * A memory of a thought
 * 
 * @author borah
 *
 */
public interface IThoughtMemory {

	public static enum Type {
		RECENT_THOUGHT(10), RECENT_ACTION(10), AFFECT_PROPERTY(3), AFFECT_RELATION(3), EVENT(20);

		/** the default max for short-term memories of this kind, or -1 if no cap */
		public final int usualCap;

		private Type(int usualCap) {
			this.usualCap = usualCap;
		}
	}

	/**
	 * Applies appropriate memory changes to this mind; return true if the memory
	 * object itself should be stored as an object in the mind
	 * 
	 * @param toMind
	 */
	public boolean apply(IUpgradedMind toMind);

	/**
	 * gets the feeling of this memory
	 * 
	 * @return
	 */
	public IFeeling getFeeling();

	/**
	 * -1 if the feeling just lasts for an indefinite time
	 * 
	 * @return
	 */
	public int getFeelingDuration();

	/**
	 * Gets the main topic of this memory, if applicable (and, logically, the target
	 * of its associated feelings)
	 * 
	 * @return
	 */
	public IMeme getTopic();

	public Type getType();

}
