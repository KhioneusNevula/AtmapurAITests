package sim;

/**
 * TODO complete sensory handler
 * 
 * @author borah
 *
 */
public class SensoryHandler {

	// private Set<SensorSystem> sensors = new HashSet<>();
	private World world;

	public SensoryHandler(World world) {
		this.world = world;
	}

	public World getWorld() {
		return world;
	}

}
