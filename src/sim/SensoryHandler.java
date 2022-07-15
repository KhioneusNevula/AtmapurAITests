package sim;

import java.util.HashSet;
import java.util.Set;

import psych_first.perception.senses.SensorSystem;
import psych_first.perception.senses.SensoryOutput;

public class SensoryHandler {

	private Set<SensorSystem> sensors = new HashSet<>();
	private World world;

	public SensoryHandler(World world) {
		this.world = world;
	}

	public World getWorld() {
		return world;
	}

	public void register(SensorSystem sen) {
		this.sensors.add(sen);
	}

	public void deregister(SensorSystem sen) {
		this.sensors.remove(sen);
	}

	public void postSensory(SensoryOutput sen) {
		for (SensorSystem s : sensors) {

			s.process(sen);
		}
	}

}
