package civilization_and_minds;

import sim.GameUniverse;

/**
 * Class representing all world knowledge. I.e., events that occurred
 * 
 * @author borah
 *
 */
public class Noosphere {

	private GameUniverse universe;

	public Noosphere(GameUniverse universe) {
		this.universe = universe;
	}

	public GameUniverse getUniverse() {
		return universe;
	}

}
