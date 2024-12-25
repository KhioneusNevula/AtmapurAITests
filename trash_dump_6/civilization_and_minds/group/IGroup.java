package civilization_and_minds.group;

import civilization_and_minds.IAgent;
import civilization_and_minds.IIntelligent;
import civilization_and_minds.group.agents.IGroupAgent;
import civilization_and_minds.group.purpose.IGroupPurpose;

/**
 * A kind of group entity consisting of multiple constituent beings
 * 
 * @author borah
 *
 */
public interface IGroup extends IIntelligent {

	/**
	 * Returns the purpose of the group, i.e. the factor that makes it a group. For
	 * example, a role, or an ethnicity
	 * 
	 * @return
	 */
	public IGroupPurpose getPurpose();

	@Override
	public ICultureKnowledge getKnowledge();

	/**
	 * whether this group is small enough to be counted precisely.
	 * 
	 * @return
	 */
	public boolean isSmallGroup();

	/**
	 * Whether the group's members are not all loaded, and therefore it must act in
	 * the abstract
	 * 
	 * @return
	 */
	public boolean actsInAbstract();

	/**
	 * Return count (or estimate of count) of entities in this group, or -1 if count
	 * tracking is irrelevant/unfeasible.
	 * 
	 * @return
	 */
	public int count();

	/**
	 * The central power of th8is group, if applicable
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

	@Override
	public IGroupAgent getAgentRepresentation();

}
