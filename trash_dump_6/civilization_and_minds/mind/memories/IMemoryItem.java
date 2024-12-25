package civilization_and_minds.mind.memories;

import civilization_and_minds.mind.mechanics.IMemoryKnowledge;
import civilization_and_minds.social.concepts.IConcept;

/**
 * A single piece of info that gets stored in memory.
 * 
 * @author borah
 *
 */
public interface IMemoryItem {

	public static enum Section {
		/** do not store memory */
		FORGET,
		/** store memory until sleep, or until deleted manually */
		SHORT_TERM,
		/** store memory for idk a week */
		LONG_TERM,
		/** never delete memory */
		CORE
	}

	/**
	 * What kind of info this memory stores
	 * 
	 * @author borah
	 *
	 */
	public static enum MemoryType {
		/** stores info about a recent goal-- whether it was completed, for example */
		GOAL,
		/** stores a recent action */
		ACTION,
		/** stores a recent feeling */
		FEELING,
		/** creates a new relation */
		RELATION,
		/** stores memory of an event */
		EVENT,
		/** any other kind of info */
		OTHER
	}

	/**
	 * Return the main piece of info this memory stores
	 * 
	 * @return
	 */
	public IConcept getMainInfo();

	/**
	 * Get the kind of memory this is
	 * 
	 * @return
	 */
	public MemoryType getMemoryType();

	/**
	 * Apply memory effects to this memory entity. E.g. a relationship memory forms
	 * the given relationship
	 * 
	 * @param mems
	 * @return whether the effect succeeded
	 */
	public boolean applyEffects(IMemoryKnowledge mems);

	/**
	 * Remove memory effects. E.g. a relationship-related memory can remove the
	 * relationship it formed. Only use this for amnesia-type effects. Regular
	 * short-term deletion should not use this method.
	 * 
	 * @param mem
	 * @return whether the effect was removed
	 */
	public boolean removeEffects(IMemoryKnowledge mem);
}
