package civilization_and_minds.group.purpose;

import sim.Tile;

public class SettlementPurpose extends SocietyPurpose {

	private Tile tile;

	public SettlementPurpose(String name, Tile tile) {
		super(name, GroupType.SETTLEMENT);
		this.tile = tile;
	}

	/**
	 * Get the tile this settlement is localized to
	 * 
	 * @return
	 */
	public Tile getTile() {
		return tile;
	}

}
