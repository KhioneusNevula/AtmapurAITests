package civilization_and_minds;

import java.util.stream.Stream;

import civilization_and_minds.mind.goals.IGoal;
import civilization_and_minds.social.concepts.profile.Profile;
import sim.interfaces.IUniqueThing;

/**
 * An individual or group which makes decisions, such as a person, or a kingdom,
 * and has knowledge
 * 
 * @author borah
 *
 */
public interface IIntelligent extends IUniqueThing {

	/**
	 * Returns the profile representing the self of this entity
	 * 
	 * @return
	 */
	public Profile getSelfProfile();

	/**
	 * Update the agent at specific intervals. Groups usually tick slower than
	 * individuals
	 * 
	 * @param ticks
	 */
	public void tick(long ticks);

	/**
	 * Get the knowledge this entity contains
	 * 
	 * @return
	 */
	public IKnowledgeBase getKnowledge();

	/**
	 * Gets the {@link IAgent} representation of this entity
	 * 
	 * @return
	 */
	public IAgent getAgentRepresentation();

	/**
	 * If this entity has a parent group, get the parent group('s agent)
	 * 
	 * @return
	 */
	IAgent getParentAgent();

	/**
	 * Streams all goals this intelligent agent is <em>actively working toward</em>
	 * completing. Don't return goals that are only in memory, or not active. Some
	 * agents, such as civilizations, can complete many goals at once, while some
	 * agents, such as people, typically have like one or two goals. The result of
	 * {@link #getMainGoal()} should be included in the stream
	 * 
	 * @return
	 */
	public Stream<IGoal> getGoals();

	/**
	 * The most imperative goal of this intelligent agent. {@link #getGoals()}
	 * should include this in its stream
	 * 
	 * @return
	 */
	public IGoal getMainGoal();

	/**
	 * Number of goals this entity has
	 * 
	 * @return
	 */
	int goalCount();

}
