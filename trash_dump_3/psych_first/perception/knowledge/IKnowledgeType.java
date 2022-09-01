package psych_first.perception.knowledge;

import sim.IHasInfo;

public interface IKnowledgeType<T> {

	public String getName();

	/**
	 * Can be boolean for any type of knowledge that just checks if present
	 * 
	 * @return
	 */
	public Class<T> getValueClass();

	/**
	 * whether this info would be found in a profile
	 * 
	 * @return
	 */
	public boolean isSocialKnowledge();

	/**
	 * if this info pertains to identities (including profiles) but not to, say,
	 * events, skills, etc
	 * 
	 * @return
	 */
	public boolean isIdentitySpecific();

	/**
	 * if this is a categorical type of knowledge with many elements
	 * 
	 * @return
	 */
	public default boolean isCategory() {
		return this instanceof IKnowledgeCategory;
	}

	/**
	 * converts the given data to a double
	 * 
	 * @return
	 */
	public double convertData(IHasInfo owner, T o);

	public static interface IBooleanKnowledge extends IKnowledgeType<Boolean> {

		default Class<Boolean> getValueClass() {
			return boolean.class;
		}

	}
}
