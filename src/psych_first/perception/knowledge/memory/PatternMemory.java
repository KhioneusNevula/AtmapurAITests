package psych_first.perception.knowledge.memory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import main.ImmutableCollection;
import psych_first.mind.IMindPart;
import psych_first.mind.Mind;
import psych_first.perception.knowledge.ICircumstance;
import psych_first.perception.knowledge.events.RelationType;
import psych_first.perception.knowledge.facts.Circumstances;
import psych_first.perception.knowledge.facts.PieceOfInformation;
import sim.IHasProfile;

public class PatternMemory implements IMindPart {

	protected Map<ICircumstance, Map<ICircumstance, Set<RelationType>>> memoryRelations = new HashMap<>(0);
	protected Map<ICircumstance, Integer> access = new HashMap<>(0);

	private Mind mind;

	public PatternMemory(Mind mind) {
		this.mind = mind;
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
	public Collection<ICircumstance> getInformation(RelationType type, ICircumstance prof, boolean isProfileSubject) {
		Set<ICircumstance> ps = new HashSet<>();

		if (isProfileSubject) {
			Map<ICircumstance, Set<RelationType>> map = this.memoryRelations.get(prof);
			if (map == null)
				return ps;
			for (Map.Entry<ICircumstance, Set<RelationType>> entry : map.entrySet()) {
				if (entry.getValue().contains(type)) {
					ps.add(entry.getKey());
				}
			}
		} else {
			for (ICircumstance p : this.memoryRelations.keySet()) {
				if (this.memoryRelations.get(p).containsKey(prof)) {
					if (this.memoryRelations.get(p).get(prof).contains(type)) {
						ps.add(p);
					}
				}
			}
		}
		return ps;
	}

	public void access(ICircumstance memory) {
		this.access.put(memory, access.getOrDefault(memory, 1) + 1);
	}

	public int getAccesses(ICircumstance memory) {
		return this.access.getOrDefault(memory, 1);
	}

	public void remove(ICircumstance memory) {
		this.memoryRelations.remove(memory);
		for (ICircumstance otherc : this.memoryRelations.keySet()) {
			memoryRelations.get(otherc).remove(memory);
		}
	}

	/**
	 * if rt == null, remove all relationships that are in this direction. if
	 * otherDirection, also remove relationships that are in the other direction
	 * 
	 * @param sub
	 * @param rt
	 * @param pred
	 */
	public void removeRelationship(ICircumstance sub, RelationType rt, ICircumstance pred, boolean otherDirection) {

		if (!this.memoryRelations.containsKey(sub))
			return;

		if (rt == null) {
			memoryRelations.get(sub).remove(pred);
			if (otherDirection && this.memoryRelations.containsKey(pred)) {
				memoryRelations.get(pred).remove(sub);

			}

		} else {

			RelationType[] rts = rt.hasConverse() ? new RelationType[] { rt, rt.converse() }
					: new RelationType[] { rt };
			for (RelationType r : rts) {
				if (memoryRelations.get(sub).containsKey(pred)) {
					memoryRelations.get(sub).get(pred).remove(r);
				}
				if (otherDirection && this.memoryRelations.containsKey(pred)
						&& this.memoryRelations.get(pred).containsKey(sub)) {
					memoryRelations.get(pred).get(sub).remove(r);
				}
			}
		}

	}

	protected void addRelationship(ICircumstance sub, RelationType type, ICircumstance pred) {

		if (!type.getSubjectType().allowsCircumstance() || !type.getPredicateType().allowsCircumstance()) {
			throw new IllegalArgumentException(sub + " " + type + " " + pred);
		}

		this.memoryRelations.computeIfAbsent(sub, (a) -> new HashMap<>(1))
				.computeIfAbsent(pred, (a) -> new HashSet<>(1)).add(type);
		if (type.hasConverse()) {
			this.memoryRelations.computeIfAbsent(pred, (a) -> new HashMap<>(1))
					.computeIfAbsent(sub, (a) -> new HashSet<>(1)).add(type.converse());
		}
	}

	public Collection<RelationType> getRelations(ICircumstance from, ICircumstance to) {
		if (!this.memoryRelations.containsKey(from))
			return null;
		for (Map.Entry<ICircumstance, Set<RelationType>> rp : this.memoryRelations.get(from).entrySet()) {
			if (rp.getKey().equals(to)) {
				return rp.getValue();
			}
		}
		return Collections.emptySet();
	}

	public boolean hasRelation(ICircumstance sub, RelationType type, ICircumstance pred) {
		if (this.memoryRelations.containsKey(sub)) {
			if (this.memoryRelations.get(sub).containsKey(pred)) {
				if (type == null)
					return true;
				return this.memoryRelations.get(sub).get(pred).contains(type);
			}
		}
		return false;
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

	public Collection<ICircumstance> getMemories() {
		return new ImmutableCollection<>(this.memoryRelations.keySet());
	}

	public boolean isKnown(ICircumstance info) {
		for (ICircumstance mem : this.memoryRelations.keySet()) {
			if (mem.equals(info)) {
				return true;
			}
		}
		return false;
	}

	public void addRelationship(PieceOfInformation piece, RelationType rt, PieceOfInformation piece2) {
		for (IHasProfile p : piece.getOccurrence().getInvolved()) {
			Circumstances c = piece.getCircumstances(p);
			this.addRelationship(c, RelationType.ALONG_WITH, piece.getOccurrence());
		}
		for (IHasProfile p : piece2.getOccurrence().getInvolved()) {
			Circumstances c = piece2.getCircumstances(p);
			this.addRelationship(c, RelationType.ALONG_WITH, piece2.getOccurrence());
		}
		this.addRelationship(piece.getOccurrence(), rt, piece2.getOccurrence());
	}

}
