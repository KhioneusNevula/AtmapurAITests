package civilization_and_minds.mind;

import java.util.stream.Stream;

import actor.Actor;
import civilization_and_minds.IIntelligent;
import civilization_and_minds.group.agents.IGroupAgent;
import civilization_and_minds.mind.mechanics.IMemoryKnowledge;
import metaphysical.soul.ISoul;

/**
 * For complex souls which have more than just standard AI, we need a mind
 * 
 * @author borah
 *
 */
public interface IMind extends IIntelligent {

	@Override
	public IMemoryKnowledge getKnowledge();

	/**
	 * If this mind has any subgroups which is not necessarily its full parent (i.e.
	 * its actual culture), such as adventurers in a party, workers with multiple
	 * roles, or something similar, get those entities's agents.
	 * 
	 * @return
	 */
	public Stream<? extends IGroupAgent> getImmediateGroups();

	/**
	 * If this individual is a member of the ggiven group
	 * 
	 * @param group
	 * @return
	 */
	public boolean memberOf(IGroupAgent group);

	/**
	 * Gets the soul which contains this mind
	 * 
	 * @return
	 */
	public ISoul getContainingSoul();

	/**
	 * Gets the actor containing this mind
	 * 
	 * @return
	 */
	public Actor getContainingActor();

	/**
	 * report info about this mind
	 * 
	 * @return
	 */
	public String report();

}
