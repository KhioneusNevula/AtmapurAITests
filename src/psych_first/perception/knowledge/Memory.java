package psych_first.perception.knowledge;

import java.util.Collection;

import psych_first.mind.IMindPart;
import psych_first.perception.knowledge.facts.PieceOfInformation;
import sim.IHasProfile;

/**
 * all "access" booleans are to indicate the memory has been accessed so it is
 * not forgotten. Not all memory types will forget memories this way
 * 
 * @author borah
 *
 */
public interface Memory extends IMindPart {

	public default <T> boolean knows(IKnowledgeCategory<T> category, Object val) {
		if (!hasCategory(category))
			return false;
		return getCategory(category, false).contains(val);
	}

	public default <T> Collection<T> getCategory(IKnowledgeCategory<T> category, boolean access) {
		return this.getInfo(category, access);
	}

	public default boolean hasCategory(IKnowledgeCategory<?> category) {

		return this.hasInfo(category);
	}

	/**
	 * gets the info of the given type
	 * 
	 * @param <T>
	 * @param type
	 * @return
	 */
	public <T> T getInfo(IKnowledgeType<T> type, boolean access);

	/**
	 * gets the info of the given type
	 * 
	 * @param type
	 * @return
	 */
	public boolean hasInfo(IKnowledgeType<?> type);

	/**
	 * gets the identity information for whatever this is
	 * 
	 * @param forP
	 * @return
	 */
	public Identity getIdentity(IHasProfile forP, boolean access);

	/**
	 * gets the memories as a raw collection
	 * 
	 * @return
	 */
	public Collection<MemorySnapshot> getMemories();

	/**
	 * if this info is known
	 */
	public boolean isKnown(PieceOfInformation info);

	/**
	 * adds this info;
	 * 
	 * @param knowledge
	 */
	public void addInfo(PieceOfInformation knowledge);

}
