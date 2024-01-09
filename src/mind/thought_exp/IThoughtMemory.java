package mind.thought_exp;

import mind.concepts.type.IMeme;
import mind.feeling.IFeeling;
import mind.thought_exp.memory.IBrainMemory;

/**
 * A memory of a thought
 * 
 * @author borah
 *
 */
public interface IThoughtMemory {

	public static enum MemoryCategory {
		REMEMBER_PROFILE(20), RECENT_ACTION(10), EVENT(20), RECENT_THOUGHT(10), AFFECT_PROPERTY(3), AFFECT_RELATION(3);

		/** the default max for short-term memories of this kind, or -1 if no cap */
		public final int usualCap;

		private MemoryCategory(int usualCap) {
			this.usualCap = usualCap;
		}
	}

	public static enum Interest {
		/** indicate this memory/profile should never be forgotten by any means */
		CORE_MEMORY,
		/**
		 * indicate this memory/profile will be remembered for a while but can be
		 * forgotten
		 */
		REMEMBER,
		/**
		 * indicate this memory/profile will be remembered until the next sleep cycle
		 */
		SHORT_TERM,
		/** indicate this is to be forgotten after it applies effects */
		FORGET
	}

	/**
	 * Applies appropriate memory changes to this mind; return true if the memory
	 * object should be stored in long-term memory
	 * 
	 * @param toMind
	 */
	public boolean apply(IBrainMemory toMind);

	/**
	 * What to do when this memory is about to be deleted. For example, a
	 * learnProfile memory might roll a check to determine whether an individual
	 * should be forgotten about when this memory is deleted
	 * 
	 * @param toMind
	 * @return
	 */
	public void uponForgetting(IUpgradedMind toMind);

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

	/**
	 * what category of memory this is
	 */
	public MemoryCategory getType();

}
