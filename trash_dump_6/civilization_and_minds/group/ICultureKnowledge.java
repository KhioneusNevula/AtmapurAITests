package civilization_and_minds.group;

import civilization_and_minds.IKnowledgeBase;

/**
 * A collective repository of knowledge
 * 
 * @author borah
 *
 */
public interface ICultureKnowledge extends IKnowledgeBase {

	@Override
	public ICultureKnowledge getParent();

	/**
	 * Whether this culture combines pieces of knowledge from two cultures
	 * 
	 * @return
	 */
	public boolean isHybrid();

}
