package psychology.perception;

import java.util.HashMap;
import java.util.Map;

import psychology.ISoulPart;
import psychology.Soul;

/**
 * the part of the mind that handles thoughts
 * 
 * @author borah
 *
 */
public class Thoughtzone implements ISoulPart {

	private Soul soul;

	/**
	 * how many focus points are there for thoughts
	 * 
	 * @return
	 */
	private int focus;

	/**
	 * thoughts mapped to how much focus they use
	 */
	private Map<Thought, Integer> thoughts = new HashMap<>();

	public Thoughtzone(Soul soul) {
		this.soul = soul;
	}

	@Override
	public String report() {
		// TODO Thoughtzone report()
		return null;
	}

	@Override
	public Soul getSoul() {
		return soul;
	}

}
