package civilization_and_minds.group.types;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import civilization_and_minds.IAgent;
import civilization_and_minds.group.IGroup;
import civilization_and_minds.group.agents.SocietyGroupAgent;
import civilization_and_minds.group.purpose.IGroupPurpose.GroupType;
import civilization_and_minds.mind.IMind;
import civilization_and_minds.social.concepts.profile.Profile;
import sim.Tile;

/**
 * A class representing a single settlement
 * 
 * @author borah
 *
 */
public class SettlementGroup extends SocietyGroup {

	private IGroup state;
	private IMind leader;
	private Tile tile;
	private Collection<Tile> singleTile;
	private Collection<IMind> members;

	public SettlementGroup(SocietyGroupAgent agent, Profile self, Tile onTile) {
		super(agent, self);
		if (agent.getPurpose().getGroupType() != GroupType.SETTLEMENT)
			throw new IllegalArgumentException();
		this.tile = onTile;
		this.singleTile = Collections.singleton(tile);
		members = new HashSet<>();
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
		return singleTile;
	}

	@Override
	public Tile getCentralTerritory() {
		return tile;
	}

	@Override
	public int territoryCount() {
		return 1;
	}

	@Override
	public boolean isSmallGroup() {
		return true;
	}

	@Override
	public boolean actsInAbstract() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int count() {
		return members.size();
	}

	public Collection<IMind> getMembers() {
		return members;
	}

	@Override
	public IAgent getCentralPower() {
		return state == null ? (leader == null ? null : leader.getAgentRepresentation())
				: state.getAgentRepresentation();
	}

	@Override
	public boolean hasCentralPower() {
		return state != null || leader != null;
	}

	@Override
	public void tick(long ticks) {
		// TODO Auto-generated method stub

	}

}
