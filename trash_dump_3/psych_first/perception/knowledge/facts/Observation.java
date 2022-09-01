package psych_first.perception.knowledge.facts;

import psych_first.perception.knowledge.Occurrence;
import psych_first.perception.knowledge.events.RelationType;
import sim.IHasProfile;

public class Observation extends Occurrence {

	public Observation() {
	}

	@Override
	public void addRelationship(IHasProfile sub, RelationType type, IHasProfile pred) {
		if (!type.isStatic()) {
			throw new IllegalArgumentException(type + " " + sub + " " + pred);
		}
		super.addRelationship(sub, type, pred);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.getInvolved().size() + ")";
	}

	@Override
	public Observation clone() {
		return (Observation) super.clone();
	}

}
