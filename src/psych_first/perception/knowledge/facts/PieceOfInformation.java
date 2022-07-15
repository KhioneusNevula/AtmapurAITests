package psych_first.perception.knowledge.facts;

import java.util.HashMap;
import java.util.Map;

import psych_first.perception.knowledge.IInformation;
import psych_first.perception.knowledge.IOccurrence;
import psych_first.perception.knowledge.events.IEvent;
import sim.IHasProfile;

public class PieceOfInformation implements IInformation {

	private Map<IHasProfile, Circumstances> information = new HashMap<>(0);
	private IOccurrence occurrence;
	private boolean isTrue;

	public PieceOfInformation(IOccurrence occurrence, boolean isTrue) {
		this.occurrence = occurrence;
		for (IHasProfile i : occurrence.getInvolved()) {
			information.put(i, new Circumstances(i));
		}
		this.isTrue = isTrue;
	}

	public boolean isTrue() {
		return isTrue;
	}

	public IOccurrence getOccurrence() {
		return occurrence;
	}

	public boolean isEvent() {
		return occurrence instanceof IEvent;
	}

	public IEvent getEvent() {
		return (IEvent) occurrence;
	}

	/**
	 * the removal thing shouldn't happen, but anyway
	 * 
	 * @param for_
	 * @return
	 */
	public Circumstances getCircumstances(IHasProfile for_) {
		return occurrence.getInvolved().contains(for_) ? information.computeIfAbsent(for_, (a) -> new Circumstances(a))
				: information.remove(for_);

	}

	@Override
	public PieceOfInformation clone() throws CloneNotSupportedException {

		PieceOfInformation clone = (PieceOfInformation) super.clone();
		clone.information = new HashMap<>(this.information);
		for (IHasProfile p : this.information.keySet()) {
			clone.information.put(p, clone.information.get(p).clone());
		}
		clone.occurrence = (IOccurrence) this.occurrence.clone();
		return clone;
	}

	public boolean isInvolved(IHasProfile prof) {
		return this.occurrence.getInvolved().contains(prof);
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof PieceOfInformation poi && poi.occurrence.equals(this.occurrence)
				&& poi.information.equals(this.information);
	}

}
