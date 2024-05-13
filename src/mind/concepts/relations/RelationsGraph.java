package mind.concepts.relations;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import mind.concepts.type.IMeme;

public class RelationsGraph extends AbstractRelationalGraph<IConceptRelationType, Collection<IMeme>, IMeme> {

	public RelationsGraph() {
		this(false);
	}

	public RelationsGraph(boolean allowBare) {
		super(allowBare);
	}

	@Override
	protected int compare(IConceptRelationType one, IConceptRelationType two) {
		return one.idString().compareTo(two.idString());
	}

	/**
	 * Whether this concept is equivalent to or a subtype of another concept,
	 * checked using a BFS basically and the ConceptRelationType subset_of
	 * 
	 * @param one
	 * @param other
	 * @return
	 */
	public boolean isSubtypeOf(IMeme one, IMeme other) {
		return relationsTraverseTo(one, other, ConceptRelationType.SUBSET_OF);
	}

	/**
	 * Whether One is connected to Other by any number of the given relation type;
	 * return true if one is equal to other
	 * 
	 * @param one
	 * @param other
	 * @param type
	 * @return
	 */
	public synchronized boolean relationsTraverseTo(IMeme one, IMeme other, IConceptRelationType type) {
		if (one.equals(other))
			return true;
		Node root = this.getNode(one);
		Node goal = this.getNode(other);
		if (root == null || goal == null)
			return false;
		UUID visitID = UUID.randomUUID();
		return this.traverseForRelation(root, goal, type, visitID);
	}

	private boolean traverseForRelation(Node root, Node goal, IConceptRelationType type, UUID visitID) {
		if (root.equals(goal))
			return true;
		root.setVisitID(visitID);
		Map<IMeme, IEdge<IConceptRelationType, Collection<IMeme>, IMeme>> map = root.getEdgesOfType(type);
		if (map.containsKey(goal.getData()))
			return true;
		for (Entry<IMeme, IEdge<IConceptRelationType, Collection<IMeme>, IMeme>> entry : map.entrySet()) {
			if (entry.getValue().getRight().getVisitID() != null
					&& entry.getValue().getRight().getVisitID().equals(visitID))
				continue;
			boolean suc = traverseForRelation(entry.getValue().getRight(), goal, type, visitID);
			if (suc)
				return true;
		}
		return false;
	}

}
