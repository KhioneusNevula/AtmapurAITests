package civilization_and_minds;

import civilization_and_minds.group.agents.IGroupAgent;
import sim.interfaces.IUniqueThing;

/**
 * An entity which can be part of groups and act in abstract. {@link IAgent}s,
 * as opposed to actors and groups, can be heavily abstracted. E.g. when
 * generating history, most interactions are through these.
 * 
 * @author borah
 *
 */
public interface IAgent extends IUniqueThing {

	/**
	 * Gets the in-world entity which represents this member. Can be a person or
	 * group. If {@link #isInWorld()} returns false, behavior is undefined.
	 * 
	 * @return
	 */
	public IIntelligent getEntity();

	/**
	 * IF this agent has an in-world representation currently.
	 * 
	 * @return
	 */
	public boolean isInWorld();

	/**
	 * Whether this agent's knowledge base is accessible.
	 * 
	 * @return
	 */
	public boolean isKnowledgeAccessible();

	/**
	 * Wehther a reduced or diminished representation of this agent's knowledge is
	 * accessible (e.g. if the agent is not in the world and is stored in a
	 * mitigated form)
	 * 
	 * @return
	 */
	public boolean isReducedKnowledgeAccessible();

	/**
	 * Return the knowledge base of this agent. Note that if
	 * {@link #isKnowledgeAccessible()} returns false, this will have an undefined
	 * result
	 * 
	 * @return
	 */
	public IKnowledgeBase getKnowledgeBase();

	/**
	 * Return the reduced knowledge base of this agent. Note that if
	 * {@link #isReducedKnowledgeAccessible()} returns false, this will have an
	 * undefined result
	 * 
	 * @return
	 */
	public IKnowledgeBase getReducedKnowledgeBase();

	/**
	 * If this agent represents a group
	 * 
	 * @return
	 */
	public boolean isGroup();

	/**
	 * The display name of the agent in screens and stuff
	 */
	public String getSimpleName();

	/**
	 * Gets the parent agent of this agent, i.e. the containing group
	 * 
	 * @return
	 */
	public IGroupAgent getParent();

}
