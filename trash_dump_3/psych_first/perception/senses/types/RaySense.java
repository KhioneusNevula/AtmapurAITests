package psych_first.perception.senses.types;

import java.util.Collection;

import psych_first.perception.knowledge.IdentityKnowledge;
import psych_first.perception.senses.Sense;
import psych_first.perception.senses.SensorSystem;
import psych_first.perception.senses.SensoryAttribute;
import psych_first.perception.senses.SensoryInput;
import psych_first.perception.senses.SensoryOutput;
import sim.ILocatable;
import sim.Location;

public class RaySense extends Sense {

	public final IdentityKnowledge<Integer> DISTANCE;
	public final IdentityKnowledge<Integer> ANGLE;

	public RaySense(String id, int standardLevel) {
		super(id, standardLevel);
		DISTANCE = new IdentityKnowledge<>(id + "_distance", int.class);
		ANGLE = new IdentityKnowledge<>(id + "_angle", int.class);
	}

	@Override
	public boolean canSense(SensorSystem sensor, SensoryOutput output) {
		// TODO for now treat like radial sense; later make more relevant
		if (output.hasLocation() && sensor.getMind().getOwner() instanceof ILocatable loc) {
			Location l = output.getLocation();
			double dist = l.distance(loc);
			if (dist <= sensor.getInfo(DISTANCE)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * TODO cannot sense things behind, inside, etc
	 */
	@Override
	public Collection<SensoryAttribute<?>> senses(SensorSystem sensor, SensoryInput input) {
		return input.getAttributes();
	}

}
