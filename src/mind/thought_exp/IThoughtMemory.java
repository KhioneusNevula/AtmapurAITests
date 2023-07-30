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
	 * Gets the main topic of this memory, if applicable (and, logically, the target
	 * of its associated feelings)
	 * 
	 * @return
	 */
	public IMeme getTopic();

}
