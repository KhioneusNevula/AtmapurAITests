package psych_first.perception.knowledge;

import java.util.Collection;

import psych_first.mind.Mind;
import psych_first.perception.knowledge.events.IEvent;
import psych_first.perception.senses.SensoryInput;
import sociology.sociocon.PropertyHolder;
import sociology.sociocon.Sociocon;

public abstract class Association {

	private Sociocon sociocon;

	public Association(Sociocon sociocon) {
		this.sociocon = sociocon;
	}

	public Sociocon getSociocon() {
		return sociocon;
	}

	/**
	 * return null if the sociocon can't be applied to the given profile, or a (can
	 * be empty) collection of socioprops that can be applied alongside the sociocon
	 * 
	 * @param prof
	 * @param viewer
	 * @param event  can be null
	 * @return
	 */
	public abstract Collection<PropertyHolder<?>> associate(SensoryInput in, Mind viewer, IEvent event);

}
