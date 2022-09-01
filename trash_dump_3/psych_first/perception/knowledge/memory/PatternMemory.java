package psych_first.perception.knowledge.memory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import main.ImmutableCollection;
import psych_first.mind.IMindPart;
import psych_first.mind.Mind;
import psych_first.perception.knowledge.IInformation;
import psych_first.perception.knowledge.IOccurrence;
import psych_first.perception.knowledge.events.EventType;
import psych_first.perception.knowledge.events.IEvent;
import psych_first.perception.knowledge.events.RelationType;
import psych_first.perception.knowledge.facts.PieceOfInformation;
import psych_first.perception.senses.Certainty;
import sim.IHasProfile;

public class PatternMemory implements IMindPart {

	protected Table<Object, RelationType, Map<Object, Certainty>> memoryRelations = HashBasedTable.create();
	protected Map<Object, Integer> access = new HashMap<>(0);

	private Mind mind;

	public PatternMemory(Mind mind) {
		this.mind = mind;
	}

	public Collection<Object> getInformation(Object sub, RelationType type) {
		return this.getInformation(sub, type, true);
	}

	public Collection<Object> getInformation(RelationType type, Object pred) {
		return this.getInformation(pred, type, false);
	}

	/**
	 * gets infos of the given relationship type for whom the parameter is subject
	 * (if isProfileSubject is true) or predicate (if not true)
	 * 
	 * @param type
	 * @param prof
	 * @param isProfileSubject
	 * @return
	 */
	public Collection<Object> getInformation(Object prof, RelationType type, boolean isProfileSubject) {

		Set<Object> ps = new HashSet<>();

		if (isProfileSubject) {
			ps.addAll(memoryRelations.get(prof, type).keySet());
		} else {
			ps.addAll(memoryRelations.get(prof, type.converse()).keySet());
		}
		return ps;
	}

	public void access(Object memory) {
		this.access.put(memory, access.getOrDefault(memory, 1) + 1);
	}

	public int getAccesses(Object memory) {
		return this.access.getOrDefault(memory, 1);
	}

	/**
	 * removes this memory along with all its relationships
	 * 
	 * @param memory
	 */
	public void remove(Object memory) {
		this.memoryRelations.rowMap().remove(memory);
		for (Object otherc : this.memoryRelations.rowMap().keySet()) {
			for (RelationType type : this.memoryRelations.row(otherc).keySet()) {
				memoryRelations.remove(otherc, type);
			}
		}
	}

	/**
	 * if rt == null, remove all relationships that are in this direction.
	 * 
	 * @param sub
	 * @param rt
	 * @param pred
	 */
	public void removeRelationship(Object sub, RelationType rt, Object pred) {

		if (!this.memoryRelations.containsRow(sub))
			return;

		if (rt == null) {
			for (RelationType type : memoryRelations.row(sub).keySet()) {
				if (memoryRelations.contains(sub, type)) {
					memoryRelations.get(sub, type).remove(pred);
				}
			}

			for (RelationType type : memoryRelations.row(pred).keySet()) {
				if (memoryRelations.contains(pred, type)) {
					memoryRelations.get(pred, type).remove(sub);
				}
			}

		} else {

			if (memoryRelations.contains(sub, rt)) {
				memoryRelations.get(sub, rt).remove(pred);
			}
			if (memoryRelations.contains(pred, rt)) {
				memoryRelations.get(pred, rt).remove(sub);
			}
		}

	}

	private void checkType(Object of) {
		if (!(of instanceof IInformation) && !(of instanceof UUID))
			throw new IllegalArgumentException("" + of);
	}

	/**
	 * returns Certainty.FALSE if this relationship is not present
	 */
	public Certainty getCertainty(Object sub, RelationType type, Object pred) {
		Map<Object, Certainty> vals = this.memoryRelations.get(sub, type);
		return vals.getOrDefault(pred, Certainty.FALSE);
	}

	/**
	 * certainty can be null and will be recognized as total certainty if so; added
	 * knowledge must be the UUID of a piece of information or something such as an
	 * EventType
	 * 
	 * @param sub
	 * @param type
	 * @param pred
	 * @param cert
	 */
	protected void addRelationship(Object sub, RelationType type, Object pred, Certainty cert) {

		if (cert == Certainty.FALSE)
			throw new IllegalArgumentException(sub + " " + type + " " + pred);

		if (cert == null)
			cert = Certainty.COMPLETELY_CERTAIN;

		checkType(sub);
		checkType(pred);

		IInformation subObject = sub instanceof IInformation ? (IInformation) sub
				: this.mind.getNoosphere().getInfo((UUID) sub);

		IInformation predObject = pred instanceof IInformation ? (IInformation) pred
				: this.mind.getNoosphere().getInfo((UUID) pred);

		if (!type.canBeSubject(subObject) || !type.canBePredicate(predObject)) {
			throw new IllegalArgumentException(sub + " " + type + " " + pred);
		}

		this.memoryRelations.row(sub).computeIfAbsent(type, (a) -> new HashMap<>()).put(pred, cert);

		this.memoryRelations.row(pred).computeIfAbsent(type.converse(), (a) -> new HashMap<>()).put(sub, cert);
	}

	public Collection<RelationType> getRelations(Object from, Object to) {
		if (!memoryRelations.containsRow(from))
			return Collections.emptySet();
		Set<RelationType> set = new HashSet<>();
		for (RelationType type : memoryRelations.row(from).keySet()) {
			if (memoryRelations.get(from, type).containsKey(to)) {
				set.add(type);
			}

		}
		return set;
	}

	public boolean hasRelation(Object sub, RelationType type, Object pred) {
		return this.memoryRelations.get(sub, type).containsKey(pred);
	}

	/**
	 * adds the knowledge of an event to this web of information; event must be a
	 * "pieceofinformation"; give a certainty value to indicate the certainty of the
	 * information
	 * 
	 * @param event
	 */
	public void learnEvent(UUID eventid, Certainty cert) {

		PieceOfInformation info = this.mind.getNoosphere().getInfo(eventid, PieceOfInformation.class);

		IOccurrence occ = info.getOccurrence();
		IEvent event = occ.asEvent();
		EventType<?> type = event.getEventType();
		this.addRelationship(type, RelationType.IS_SUPERCATEGORY_OF, eventid, cert);

		for (IHasProfile po : occ.getInvolved()) {

			// TODO actually get identity
			this.addRelationship(info.getCircumstances(po), RelationType.ALONG_WITH, eventid, cert);
			for (IHasProfile other : occ.getProfiles(null, po, true)) {
				for (RelationType rt : occ.getRelations(po, other)) {
					this.addRelationship(po, rt, other, cert);
				}
			}

			if (event != null) {
				for (RelationType rt : event.getRelationTo(po)) {
					this.addRelationship(eventid, rt, po, cert);
				}
			}
		}
	}

	@Override
	public Mind getMind() {
		return mind;
	}

	@Override
	public void update(int ticks) {
		// TODO Auto-generated method stub

	}

	@Override
	public String report() {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<Object> getMemories() {
		return new ImmutableCollection<>(this.memoryRelations.rowKeySet());
	}

	public boolean isKnown(Object info) {
		return this.memoryRelations.containsRow(info);
	}

}
