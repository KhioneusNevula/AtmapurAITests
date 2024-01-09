package mind.thought_exp.memory.type;

import java.util.Collection;
import java.util.Map;

import mind.concepts.relations.IConceptRelationType;
import mind.concepts.type.IMeme;
import mind.thought_exp.IUpgradedMind;
import mind.thought_exp.memory.IBrainMemory;

public class RelationMemory extends AbstractMemory {

	IMeme first;
	IMeme second;
	Map<? extends IConceptRelationType, Collection<IMeme>> relationsRight;
	Map<? extends IConceptRelationType, Collection<IMeme>> relationsLeft;

	public RelationMemory(IMeme first, IMeme second,
			Map<? extends IConceptRelationType, Collection<IMeme>> relationsRight) {
		this(first, second, relationsRight, Map.of());
	}

	/**
	 * put bidirectional relations in relationsLeft
	 * 
	 * @param first
	 * @param second
	 * @param relationsRight
	 * @param relationsLeft
	 */
	public RelationMemory(IMeme first, IMeme second,
			Map<? extends IConceptRelationType, Collection<IMeme>> relationsRight,
			Map<? extends IConceptRelationType, Collection<IMeme>> relationsLeft) {
		this.first = first;
		this.second = second;
		this.relationsLeft = relationsLeft;
		this.relationsRight = relationsRight;
	}

	@Override
	public boolean apply(IBrainMemory toMind) {
		for (Map.Entry<? extends IConceptRelationType, Collection<IMeme>> entry : relationsRight.entrySet()) {
			toMind.learnRelation(first, second, entry.getKey(), entry.getValue());
		}
		for (Map.Entry<? extends IConceptRelationType, Collection<IMeme>> entry : relationsLeft.entrySet()) {
			toMind.learnRelation(second, first, entry.getKey(), entry.getValue());
		}
		return false;
	}

	@Override
	public void uponForgetting(IUpgradedMind toMind) {

	}

	@Override
	public MemoryCategory getType() {
		return MemoryCategory.AFFECT_RELATION;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof RelationMemory))
			return false;
		RelationMemory relmem = (RelationMemory) obj;
		return first.equals(relmem.first) && second.equals(relmem.second) && relationsLeft.equals(relmem.relationsLeft)
				&& relationsRight.equals(relmem.relationsRight);
	}

	@Override
	public int hashCode() {
		return first.hashCode() * second.hashCode() + relationsLeft.hashCode() * relationsRight.hashCode();
	}

	@Override
	public String toString() {
		return "relation-memory(" + first + "->" + second + ")";
	}

}
