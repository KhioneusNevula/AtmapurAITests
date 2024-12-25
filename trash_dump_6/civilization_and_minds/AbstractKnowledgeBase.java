package civilization_and_minds;

import java.util.Collections;
import java.util.stream.Stream;

import com.google.common.collect.Streams;

import civilization_and_minds.group.ICultureKnowledge;
import civilization_and_minds.social.concepts.IConcept;
import civilization_and_minds.social.concepts.profile.Profile;
import civilization_and_minds.social.concepts.relation.IConceptRelationType;
import civilization_and_minds.social.concepts.relation.KnowledgeWeb;
import utilities.RelationalGraph.IEdge;
import utilities.RelationalGraph.INode;

public abstract class AbstractKnowledgeBase implements IKnowledgeBase {

	protected KnowledgeWeb knowledge;
	protected ICultureKnowledge parent;
	protected boolean isStatic;

	protected AbstractKnowledgeBase(Profile selfProfile) {
		this.knowledge = new KnowledgeWeb(selfProfile);
	}

	@Override
	public Profile getSelfProfile() {
		return knowledge.getSelfProfile();
	}

	@Override
	public boolean relationExists(IConcept first, IConceptRelationType<?> relation, IConcept second,
			boolean checkParent) {
		if (!knowledge.edgeExists(first, second, relation)) {
			if (this.getParent() != null && checkParent) {
				return this.getParent().relationExists(first, relation, second, checkParent);
			}
			return false;
		}
		return true;
	}

	@Override
	public <T extends IConcept> T getRelationArgument(IConcept first, IConceptRelationType<?> relation, IConcept second,
			boolean checkParent) {
		IEdge<IConcept, IConceptRelationType<? extends IConceptRelationType<?>>, IConcept> edge = this.knowledge
				.getEdge(first, second, relation);
		if ((edge == null || edge.getData() == null) && checkParent && this.getParent() != null)
			return this.getParent().getRelationArgument(first, relation, second, checkParent);
		return (T) edge.getData();
	}

	@Override
	public boolean knows(IConcept concept, boolean checkParent) {
		if (!this.knowledge.containsNode(concept)) {
			if (checkParent && this.getParent() != null)
				return this.getParent().knows(concept, checkParent);
		}
		return true;
	}

	@Override
	public int countConcepts(boolean checkParent) {
		if (checkParent && this.getParent() != null) {
			return (int) this.getAllConcepts(checkParent).count();
		} else {
			return knowledge.getNodeSet().size();
		}
	}

	@Override
	public Stream<IConcept> getAllConcepts(boolean checkParent) {
		if (checkParent && this.getParent() != null) {
			return Streams.concat(knowledge.getNodeSet().stream().map((a) -> a.getData()),
					this.getParent().getAllConcepts(checkParent));
		}
		return knowledge.getNodeSet().stream().map((a) -> a.getData());
	}

	@Override
	public Stream<IConcept> getConnectedConcepts(IConcept toConcept, boolean checkParents) {
		INode<IConcept, IConceptRelationType<?>, IConcept> node = knowledge.getNode(toConcept);
		if (checkParents && this.getParent() != null) {
			if (node == null) {
				return this.getParent().getConnectedConcepts(toConcept, checkParents);
			} else {
				return Streams.concat(node.getAllConnectedNodes().stream().map((a) -> a.getData()),
						this.getParent().getConnectedConcepts(toConcept, checkParents));
			}
		} else {
			if (node == null) {
				return Collections.<IConcept>emptySet().stream();
			} else {
				return node.getAllConnectedNodes().stream().map((a) -> a.getData());
			}
		}
	}

	@Override
	public Stream<IConcept> getConnectedConcepts(IConcept toConcept, IConceptRelationType<?> relation,
			boolean checkParents) {
		INode<IConcept, IConceptRelationType<?>, IConcept> node = knowledge.getNode(toConcept);
		if (checkParents && this.getParent() != null) {
			if (node == null) {
				return this.getParent().getConnectedConcepts(toConcept, relation, checkParents);
			} else {
				return Streams.concat(node.getAllConnectedNodes(relation).stream().map((a) -> a.getData()),
						this.getParent().getConnectedConcepts(toConcept, relation, checkParents));
			}
		} else {
			if (node == null) {
				return Collections.<IConcept>emptySet().stream();
			} else {
				return node.getAllConnectedNodes(relation).stream().map((a) -> a.getData());
			}
		}
	}

	@Override
	public Stream<IConceptRelationType<?>> getRelationTypes(IConcept conceptOne, IConcept conceptTwo,
			boolean checkParents) {
		INode<IConcept, IConceptRelationType<?>, IConcept> node = knowledge.getNode(conceptOne);
		INode<IConcept, IConceptRelationType<?>, IConcept> node2 = knowledge.getNode(conceptTwo);
		if (checkParents && this.getParent() != null) {
			if (node == null || node2 == null) {
				return this.getParent().getRelationTypes(conceptOne, conceptTwo, checkParents);
			} else {
				return Streams.concat(node.getAllConnectedRelationTypes(node2).stream(),
						this.getParent().getRelationTypes(conceptOne, conceptTwo, checkParents));
			}
		} else {
			if (node == null) {
				return Collections.<IConceptRelationType<?>>emptySet().stream();
			} else {
				return node.getAllConnectedRelationTypes(node2).stream();
			}
		}
	}

	@Override
	public int countConnectedConcepts(IConcept toConcept, boolean checkParent) {
		return (int) this.getConnectedConcepts(toConcept, checkParent).count();
	}

	@Override
	public int countConnectedConcepts(IConcept toConcept, IConceptRelationType<?> type, boolean checkParent) {
		return (int) this.getConnectedConcepts(toConcept, type, checkParent).count();
	}

	@Override
	public boolean changeArgument(IConcept first, IConceptRelationType<?> type, IConcept second, IConcept argument) {
		if (this.isStatic)
			return false;
		IEdge<IConcept, IConceptRelationType<?>, IConcept> edge = knowledge.getEdge(first, second, type);
		if (edge == null)
			return false;
		edge.setData(argument);
		return true;
	}

	@Override
	public boolean deleteArgument(IConcept first, IConceptRelationType<?> type, IConcept second) {
		if (this.isStatic)
			return false;
		IEdge<IConcept, IConceptRelationType<?>, IConcept> edge = knowledge.getEdge(first, second, type);
		if (edge == null)
			return false;
		edge.setData(null);
		return true;
	}

	@Override
	public IConcept forgetRelationship(IConcept first, IConceptRelationType<?> type, IConcept second) {
		if (this.isStatic)
			return null;
		IEdge<IConcept, IConceptRelationType<? extends IConceptRelationType<?>>, IConcept> edge = this.knowledge
				.removeEdge(first, second, type);
		if (edge == null)
			return null;
		return edge.getData();
	}

	@Override
	public boolean learnNewRelationship(IConcept first, IConceptRelationType<?> type, IConcept second) {
		return this.learnNewRelationship(first, type, second, null);
	}

	@Override
	public boolean learnNewRelationship(IConcept first, IConceptRelationType<?> type, IConcept second,
			IConcept argument) {
		if (this.isStatic)
			return false;
		if (this.knowledge.edgeExists(first, second, type))
			return false;
		IEdge<IConcept, IConceptRelationType<?>, IConcept> edge = this.knowledge.createEdge(first, second, type);
		if (argument != null)
			edge.setData(argument);
		return true;
	}

	@Override
	public ICultureKnowledge getParent() {
		return this.parent;
	}

	@Override
	public boolean isStatic() {
		return this.isStatic;
	}

	@Override
	public String report() {
		return "culture" + (this.isStatic ? "*" : "") + this.knowledge.toString();
	}

}
