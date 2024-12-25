package civilization_and_minds.group.agents;

import java.util.Collection;
import java.util.TreeSet;
import java.util.UUID;

import civilization_and_minds.IAgent;
import civilization_and_minds.group.IGroup;
import civilization_and_minds.group.purpose.SocietyPurpose;
import civilization_and_minds.group.types.ISocietyGroup;
import sim.Tile;

public class SocietyGroupAgent extends GroupAgent {

	private int count;
	private IGroupAgent state;
	private Collection<Tile> allTiles;

	public SocietyGroupAgent(SocietyPurpose purpose, UUID id) {
		super(purpose, id);
		if (!purpose.getGroupType().isSociety())
			throw new IllegalArgumentException();
		allTiles = new TreeSet<>();
	}

	/**
	 * The territory this society covers
	 * 
	 * @return
	 */
	public Collection<Tile> getTerritory() {
		return allTiles;
	}

	@Override
	public SocietyGroupAgent setEntity(IGroup entity) {
		if (!(entity instanceof ISocietyGroup))
			throw new IllegalArgumentException();
		return (SocietyGroupAgent) super.setEntity(entity);
	}

	@Override
	public ISocietyGroup getEntity() {
		return (ISocietyGroup) super.getEntity();
	}

	@Override
	public boolean isSmallGroup() {
		return false;
	}

	/**
	 * Set an estimate of the number of members of this society
	 * 
	 * @param count
	 */
	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public int count() {
		return count;
	}

	public void setState(IGroupAgent state) {
		this.state = state;
	}

	/**
	 * Return the group with central power in this society
	 * 
	 * @return
	 */
	public IGroupAgent getState() {
		return state;
	}

	/**
	 * If this society has a state
	 * 
	 * @return
	 */
	public boolean hasState() {
		return state != null;
	}

	@Override
	public SocietyPurpose getPurpose() {
		return (SocietyPurpose) super.getPurpose();
	}

	@Override
	public IAgent getCentralPower() {
		return state;
	}

	@Override
	public boolean hasCentralPower() {
		return state != null;
	}

}
