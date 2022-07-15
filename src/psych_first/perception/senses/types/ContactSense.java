package psych_first.perception.senses.types;

import java.util.Collection;

import psych_first.perception.senses.Sense;
import psych_first.perception.senses.SensorSystem;
import psych_first.perception.senses.SensoryAttribute;
import psych_first.perception.senses.SensoryInput;
import psych_first.perception.senses.SensoryOutput;
import sim.ILocatable;

public class ContactSense extends Sense {

	public ContactSense(String id, int standardLevel) {
		super(id, standardLevel);
	}

	@Override
	public boolean canSense(SensorSystem sensor, SensoryOutput output) {
		if (sensor.getMind().getOwner() instanceof ILocatable l && output.hasLocation()) {
			return l.at(output.getLocation()) || l.adjacentTo(output.getLocation());
		}
		return false;
	}

	@Override
	public Collection<SensoryAttribute<?>> senses(SensorSystem sensor, SensoryInput input) {
		return input.getAttributes();
	}

}
