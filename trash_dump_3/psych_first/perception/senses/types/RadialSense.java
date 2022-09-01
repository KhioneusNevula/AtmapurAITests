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

/**
 * radial senses have their "level" meaning the radius at which they can sense
 * 
 * @author borah
 *
 */
public class RadialSense extends Sense {

	public final IdentityKnowledge<Integer> DISTANCE;

	public RadialSense(String name, int stlevel) {
		super(name, stlevel);
		DISTANCE = new IdentityKnowledge<>(name + "_distance", int.class);

	}

	@Override
	public boolean canSense(SensorSystem sensor, SensoryOutput output) {
		if (output.hasLocation() && sensor.getMind().getOwner() instanceof ILocatable loc) {
			Location l = output.getLocation();
			double dist = l.distance(loc);
			if (dist <= sensor.getInfo(DISTANCE)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Collection<SensoryAttribute<?>> senses(SensorSystem sensor, SensoryInput put) {

		return put.getAttributes();
	}

}
