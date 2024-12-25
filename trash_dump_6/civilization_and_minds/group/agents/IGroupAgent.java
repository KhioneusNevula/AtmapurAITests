package civilization_and_minds.group.agents;

import civilization_and_minds.IAgent;
import civilization_and_minds.group.ICultureKnowledge;
import civilization_and_minds.group.purpose.IGroupPurpose;

public interface IGroupAgent extends IAgent {

	/**
	 * Returns the purpose of the group agent, i.e. the factor that makes it a
	 * group. For example, a role, or an ethnicity
	 * 
	 * @return
	 */
	public IGroupPurpose getPurpose();

	@Override
	public ICultureKnowledge getKnowledgeBase();

	@Override
	public ICultureKnowledge getReducedKnowledgeBase();

	/**
	 * whether this group is small enough to be counted precisely.
	 * 
	 * @return
	 */
	public boolean isSmallGroup();

	/**
	 * Return count (or estimate of count) of entities in this group, or -1 if count
	 * tracking is irrelevant/unfeasible.
	 * 
	 * @return
	 */
	public int count();

	/**
	 * If thsi group has a central power, return it
	 * 
	 * @return
	 */
	public IAgent getCentralPower();

	/**
	 * If this group has a central power
	 * 
	 * @return
	 */
	public boolean hasCentralPower();
}
