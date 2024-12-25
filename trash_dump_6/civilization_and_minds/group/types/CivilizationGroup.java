package civilization_and_minds.group.types;

import java.util.Collection;
import java.util.TreeSet;

import civilization_and_minds.IAgent;
import civilization_and_minds.group.IGroup;
import civilization_and_minds.group.agents.SocietyGroupAgent;
import civilization_and_minds.social.concepts.profile.Profile;
import sim.Tile;

/**
 * A civilization-level group; an entity which spans multiple pieces of land
 * 
 * @author borah
 *
 */
public class CivilizationGroup extends SocietyGroup {

	private IGroup state;
	private Tile centralTerritory;
	private Collection<Tile> territories;

	public CivilizationGroup(SocietyGroupAgent agent, Profile self) {
		super(agent, self);
		territories = new TreeSet<>();
	}

	@Override
	public void tick(long ticks) {
		// TODO Civilization tick method
	}

	@Override
	public boolean isSmallGroup() {
		return false;
	}

	@Override
	public boolean actsInAbstract() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasCentralPower() {
		return state != null;
	}

	@Override
	public IAgent getCentralPower() {
		return state != null ? state.getAgentRepresentation() : null;
	}

	@Override
	public IGroup getState() {
		return state;
	}

	@Override
	public boolean hasCentralState() {
		return state != null;
	}

	@Override
	public Collection<Tile> getTerritory() {
		return territories;
	}

	@Override
	public Tile getCentralTerritory() {
		return centralTerritory;
	}

	@Override
	public int territoryCount() {
		return territories.size();
	}

}
