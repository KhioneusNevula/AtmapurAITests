package sim.relationalclasses;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

import mind.concepts.type.IMeme;
import mind.thought_exp.IThoughtMemory;
import sim.relationalclasses.AbstractRelationalGraph.Edge.InverseView;

/**
 * 
 * @author borah
 *
 * @param <RelationType>      the relation type, either IConceptRelationType or
 *                            RelationType
 * @param <RelationArguments> the argument type, either Collection<MemeType> or
 *                            Collection<Relationship>
 */
public abstract class AbstractRelationalGraph<RelationType extends IInvertibleRelationType<RelationType>, RelationArguments, MemeType extends IMeme> {

	public interface IEdge<RelationType extends IInvertibleRelationType<RelationType>, RelationArguments, MemeType extends IMeme> {

		RelationArguments getArgs();

		/**
		 * The view of this edge with left and right switched
		 * 
		 * @return
		 */
		IEdge<RelationType, RelationArguments, MemeType> inverseView();

		/** if this is only the inverse view of an edge */
		default boolean isInverseEdge() {
			return this instanceof InverseView;
		}

		void setArgs(RelationArguments args);

		RelationType getType();

		/**
		 * if this meme is the left node of this edge
		 * 
		 * @param meme
		 * @return
		 */
		boolean isLeft(MemeType meme);

		/**
		 * if this meme is the right node of this edge
		 * 
		 * @param meme
		 * @return
		 */
		boolean isRight(MemeType meme);

		AbstractRelationalGraph<RelationType, RelationArguments, MemeType>.Node getLeft();

		AbstractRelationalGraph<RelationType, RelationArguments, MemeType>.Node getRight();

		/**
		 * 0 - 1, indicates that the edge is an uncertain connection if less than 1
		 * 
		 * @return
		 */
		float sureness();

		/**
		 * Remove this source from this relation's sources; if there are other sources
		 * still keeping this relation connected, return false, otherwise return true.
		 * Return false if the thought is not part of the sources at all
		 * 
		 * @param thought
		 * @return
		 */
		public boolean attemptDelete(IThoughtMemory thought);

		/**
		 * Add this thought-memory as a source for this piece of information
		 * 
		 * @param source
		 */
		public void addSource(IThoughtMemory source);

		/**
		 * Return all thought-memories acting as sources for this connection
		 * 
		 * @return
		 */
		public Collection<IThoughtMemory> getSources();

		public static String toStringMethod(IEdge<?, ?, ?> edge) {
			return "(" + edge.getLeft().data + "):=" + edge.getType() + "{" + edge.getArgs() + "}=>("
					+ edge.getRight().data + ")" + (edge.sureness() < 1 ? "[s=" + edge.sureness() + "]" : "")
					+ (edge.getSources().isEmpty() ? "" : "[from=" + edge.getSources() + "]");
		}

	}

	public class Edge implements IEdge<RelationType, RelationArguments, MemeType> {
		private RelationType type;
		private RelationArguments args;
		private Node left;
		private Node right;
		private InverseView inverse;
		private float sureness = 1;
		/** source of the knowledge */
		private HashSet<IThoughtMemory> sources;

		@Override
		public void addSource(IThoughtMemory source) {
			if (sources == null)
				sources = new HashSet<>();
			sources.add(source);
		}

		@Override
		public boolean attemptDelete(IThoughtMemory thought) {
			if (sources == null)
				return false;
			if (sources.remove(thought)) {
				if (sources.isEmpty())
					return true;
			}
			return false;
		}

		@Override
		public Collection<IThoughtMemory> getSources() {
			return sources == null ? Collections.emptySet() : sources;
		}

		@Override
		public RelationType getType() {
			return type;
		}

		@Override
		public boolean isLeft(MemeType meme) {
			return this.left.data.equals(meme);
		}

		@Override
		public boolean isRight(MemeType meme) {
			return this.right.data.equals(meme);
		}

		@Override
		public Node getLeft() {
			return left;
		}

		@Override
		public Node getRight() {
			return right;
		}

		@Override
		public RelationArguments getArgs() {
			return args;
		}

		@Override
		public void setArgs(RelationArguments args) {
			this.args = args;
		}

		public Edge(Node left, Node right, RelationType type, RelationArguments args) {
			this.type = type;
			this.args = args;
			this.left = left;
			this.right = right;
			this.inverse = new InverseView();
		}

		@Override
		public InverseView inverseView() {
			return inverse;
		}

		@Override
		public int hashCode() {
			return type.hashCode() + args.hashCode() + left.data.hashCode() * right.data.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof IEdge) {
				IEdge edge = (IEdge) obj;
				return this.type.equals(edge.getType()) && this.args.equals(edge.getArgs())
						&& left.data.equals(edge.getLeft().data) && right.data.equals(edge.getRight().data);
			}
			return false;
		}

		@Override
		public String toString() {
			return IEdge.toStringMethod(this);
		}

		@Override
		public float sureness() {
			return sureness;
		}

		public Edge setSureness(float sureness) {
			this.sureness = sureness;
			return this;
		}

		public class InverseView implements IEdge<RelationType, RelationArguments, MemeType> {

			@Override
			public RelationArguments getArgs() {
				return Edge.this.getArgs();
			}

			public void setArgs(RelationArguments args) {
				Edge.this.setArgs(args);
			}

			@Override
			public RelationType getType() {
				return Edge.this.getType().inverse();
			}

			@Override
			public boolean isLeft(MemeType meme) {
				return Edge.this.isRight(meme);
			}

			@Override
			public boolean isRight(MemeType meme) {
				return Edge.this.isLeft(meme);
			}

			@Override
			public Node getLeft() {
				return Edge.this.getRight();
			}

			@Override
			public Node getRight() {
				return Edge.this.getLeft();
			}

			@Override
			public Edge inverseView() {
				return Edge.this;
			}

			@Override
			public boolean equals(Object obj) {
				if (obj instanceof IEdge) {
					IEdge edge = (IEdge) obj;
					return Edge.this.equals(edge.inverseView());
				}
				return false;
			}

			@Override
			public float sureness() {
				return Edge.this.sureness();
			}

			@Override
			public boolean attemptDelete(IThoughtMemory thought) {
				return Edge.this.attemptDelete(thought);
			}

			@Override
			public Collection<IThoughtMemory> getSources() {
				return Edge.this.getSources();
			}

			@Override
			public void addSource(IThoughtMemory source) {
				Edge.this.addSource(source);
			}

			@Override
			public String toString() {
				return IEdge.toStringMethod(this);
			}

		}
	}

	public class Node implements Comparable<Node> {
		private MemeType data;
		private Table<RelationType, MemeType, IEdge<RelationType, RelationArguments, MemeType>> edges;
		private UUID visitid;

		protected Node(MemeType data) {
			this.data = data;
			edges = TreeBasedTable.create(AbstractRelationalGraph.this::compare,
					(a, b) -> a.getUniqueName().compareTo(b.getUniqueName()));
		}

		public boolean hasConnections() {
			return !edges.isEmpty();
		}

		/**
		 * Used for traversals to indicate this node is visited by this traversal; the
		 * UUID should be unique to each traversal
		 * 
		 * @param id
		 */
		public void setVisitID(UUID id) {
			this.visitid = id;
		}

		public UUID getVisitID() {
			return this.visitid;
		}

		public Collection<IEdge<RelationType, RelationArguments, MemeType>> getAllEdges() {
			return edges.values();
		}

		public Map<MemeType, IEdge<RelationType, RelationArguments, MemeType>> getEdgesOfType(RelationType type) {
			return edges.row(type);
		}

		public Map<RelationType, IEdge<RelationType, RelationArguments, MemeType>> getEdgesWith(MemeType party) {
			return edges.column(party);
		}

		public IEdge<RelationType, RelationArguments, MemeType> getEdge(MemeType other, RelationType type) {
			return edges.get(type, other);
		}

		protected void putEdge(MemeType other, IEdge<RelationType, RelationArguments, MemeType> edge) {
			edges.put(edge.getType(), other, edge);
		}

		protected IEdge<RelationType, RelationArguments, MemeType> removeEdge(MemeType other, RelationType type) {
			return edges.remove(type, other);
		}

		protected IEdge<RelationType, RelationArguments, MemeType> attemptRemoveEdge(MemeType other, RelationType type,
				IThoughtMemory source) {
			IEdge<RelationType, RelationArguments, MemeType> edge = edges.get(type, other);
			if (edge == null)
				return null;
			if (edge.attemptDelete(source)) {
				return edges.remove(type, other);
			}
			return null;
		}

		protected Collection<IEdge<RelationType, RelationArguments, MemeType>> clearAllEdges() {
			Collection<IEdge<RelationType, RelationArguments, MemeType>> col = edges.values();
			edges = TreeBasedTable.create(AbstractRelationalGraph.this::compare,
					(a, b) -> a.getUniqueName().compareTo(b.getUniqueName()));
			return col;
		}

		protected Collection<IEdge<RelationType, RelationArguments, MemeType>> clearAllEdgesOfType(RelationType type) {
			Collection<IEdge<RelationType, RelationArguments, MemeType>> col = new LinkedList<>(
					edges.row(type).values());
			edges.row(type).clear();
			return col;
		}

		protected Collection<IEdge<RelationType, RelationArguments, MemeType>> clearAllEdgesWith(MemeType other) {
			Collection<IEdge<RelationType, RelationArguments, MemeType>> col = new LinkedList<>(
					edges.column(other).values());
			edges.column(other).clear();
			return col;
		}

		public MemeType getData() {
			return data;
		}

		@Override
		public int compareTo(AbstractRelationalGraph<RelationType, RelationArguments, MemeType>.Node o) {
			return this.data.getUniqueName().compareTo(o.data.getUniqueName());
		}

		@Override
		public int hashCode() {
			return data.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return obj instanceof AbstractRelationalGraph.Node && this.data.equals(((Node) obj).data);
		}

		@Override
		public String toString() {
			return "Node(" + data + ", d=" + edges.size() + ")";
		}

	}

	private TreeMap<MemeType, Node> nodes = new TreeMap<>((a, b) -> a.getUniqueName().compareTo(b.getUniqueName()));
	private HashSet<Edge> edges = new HashSet<>();
	private boolean allowBareNodes = false;

	public AbstractRelationalGraph(boolean allowBareNodes) {
		this.allowBareNodes = allowBareNodes;

	}

	public boolean isEmpty() {
		return edges.isEmpty();
	}

	/**
	 * Deletes a node and its connections.
	 * 
	 * @param one
	 * @return
	 */
	public Node removeNode(MemeType one) {
		Node nOne = this.nodes.remove(one);
		if (nOne != null) {
			for (IEdge<RelationType, RelationArguments, MemeType> edge : nOne.getAllEdges()) {
				if (edge.isInverseEdge())
					edges.remove(edge.inverseView());
				else
					edges.remove(edge);
				edge.getRight().removeEdge(one, edge.inverseView().getType());
			}
		}
		return nOne;
	}

	/**
	 * Removes all connections from this node, leaving it 'bare'. Does almost the
	 * same thing as removeNode, but if allowBareNodes is true, it won't delete the
	 * node itself
	 * 
	 * @param one
	 * @return
	 */

	public Collection<IEdge<RelationType, RelationArguments, MemeType>> removeAllEdges(MemeType one) {
		Node nOne = nodes.get(one);
		if (nOne == null)
			return Collections.emptySet();
		Collection<IEdge<RelationType, RelationArguments, MemeType>> edges = nOne.clearAllEdges();
		if (!allowBareNodes)
			nodes.remove(one);
		for (IEdge<RelationType, RelationArguments, MemeType> edge : edges) {
			if (edge.isInverseEdge())
				this.edges.remove(edge.inverseView());
			else
				this.edges.remove(edge);
			edge.getRight().removeEdge(one, edge.inverseView().getType());
		}
		return edges;
	}

	/**
	 * Remove all edges of type from the given node
	 * 
	 * @param one
	 * @param type
	 * @return
	 */
	public Collection<IEdge<RelationType, RelationArguments, MemeType>> removeAllEdgesFrom(MemeType one,
			RelationType type) {
		Node nOne = this.getNode(one);
		if (nOne == null)
			return Collections.emptySet();
		Collection<IEdge<RelationType, RelationArguments, MemeType>> rels = nOne.clearAllEdgesOfType(type);
		for (IEdge<RelationType, RelationArguments, MemeType> edge : rels) {
			if (edge.isInverseEdge())
				edges.remove(edge.inverseView());
			else
				edges.remove(edge);
			edge.getRight().removeEdge(one, edge.inverseView().getType());
		}
		if (!nOne.hasConnections() && !allowBareNodes)
			nodes.remove(one);
		return rels;
	}

	public Collection<IEdge<RelationType, RelationArguments, MemeType>> removeAllEdgesBetween(MemeType one,
			MemeType other) {
		Node nOne = this.getNode(one);
		if (nOne == null)
			return Collections.emptySet();
		Collection<IEdge<RelationType, RelationArguments, MemeType>> rels = nOne.clearAllEdgesWith(other);
		for (IEdge<RelationType, RelationArguments, MemeType> edge : rels) {
			if (edge.isInverseEdge())
				edges.remove(edge.inverseView());
			else
				edges.remove(edge);
		}
		Node nOther = this.getNode(other);
		nOther.clearAllEdgesWith(one);
		if (!nOne.hasConnections() && !allowBareNodes)
			nodes.remove(one);
		if (!nOther.hasConnections() && !allowBareNodes)
			nodes.remove(other);
		return rels;
	}

	public IEdge<RelationType, RelationArguments, MemeType> removeEdge(MemeType one, MemeType other,
			RelationType type) {
		Node nOne = this.getNode(one);
		if (nOne == null)
			return null;
		IEdge<RelationType, RelationArguments, MemeType> edge = nOne.removeEdge(other, type);
		if (edge == null)
			return null;
		if (edge.isInverseEdge())
			edges.remove(edge.inverseView());
		else
			edges.remove(edge);

		edge.getRight().removeEdge(one, edge.inverseView().getType());
		if (!nOne.hasConnections() && !allowBareNodes)
			nodes.remove(one);
		if (!edge.getRight().hasConnections() && !allowBareNodes)
			nodes.remove(other);
		return edge;
	}

	/**
	 * attempts to remove the given edge by using the thought memory as the source;
	 * return null if the edge was not deleted or if the source is not part of the
	 * edge
	 * 
	 * @param one
	 * @param other
	 * @param type
	 * @param source
	 * @return
	 */
	public IEdge<RelationType, RelationArguments, MemeType> attemptRemoveEdge(MemeType one, MemeType other,
			RelationType type, IThoughtMemory source) {
		Node nOne = this.getNode(one);
		if (nOne == null)
			return null;
		IEdge<RelationType, RelationArguments, MemeType> edge = nOne.attemptRemoveEdge(other, type, source);
		if (edge == null)
			return null;
		if (edge.isInverseEdge())
			edges.remove(edge.inverseView());
		else
			edges.remove(edge);

		edge.getRight().removeEdge(one, edge.inverseView().getType());
		if (!nOne.hasConnections() && !allowBareNodes)
			nodes.remove(one);
		if (!edge.getRight().hasConnections() && !allowBareNodes)
			nodes.remove(other);
		return edge;
	}

	public IEdge<RelationType, RelationArguments, MemeType> createEdge(MemeType one, MemeType other, RelationType type,
			RelationArguments args) {
		Node nOne = this._getOrCreateNode(one);
		Node nOther = this._getOrCreateNode(other);
		Edge edge = new Edge(nOne, nOther, type, args);
		nOne.putEdge(other, edge);
		edges.add(edge);
		nOther.putEdge(one, edge.inverseView());
		return edge;

	}

	/**
	 * either creates an edge of the given type, or grabs an existing edge, and
	 * makes the given memory one of its sources. The empty relation-args argument
	 * at the end is the relation args to add if the edge doesn't exist; they will
	 * not be applied if the edge does exist
	 * 
	 * @param one
	 * @param other
	 * @param type
	 * @param source
	 * @param emptyArgs
	 * @return
	 */
	public IEdge<RelationType, RelationArguments, MemeType> addSourceToEdge(MemeType one, MemeType other,
			RelationType type, IThoughtMemory source, RelationArguments emptyArgs) {
		IEdge<RelationType, RelationArguments, MemeType> edge = this.getEdge(one, other, type);
		if (edge == null)
			edge = this.createEdge(one, other, type, emptyArgs);
		edge.addSource(source);
		return edge;
	}

	public Collection<IEdge<RelationType, RelationArguments, MemeType>> getAllEdgesFrom(MemeType one) {
		Node a = nodes.get(one);
		if (a == null)
			return null;
		return a.getAllEdges();
	}

	public Map<MemeType, IEdge<RelationType, RelationArguments, MemeType>> getEdgesOfTypeFrom(MemeType one,
			RelationType type) {
		Node a = nodes.get(one);
		if (a == null)
			return Map.of();
		return a.getEdgesOfType(type);
	}

	public Map<RelationType, IEdge<RelationType, RelationArguments, MemeType>> getEdgesBetween(MemeType one,
			MemeType other) {
		Node a = nodes.get(one);
		if (a == null)
			return Map.of();
		return a.getEdgesWith(other);
	}

	public IEdge<RelationType, RelationArguments, MemeType> getEdge(MemeType one, MemeType other, RelationType type) {
		Node a = nodes.get(one);
		if (a == null)
			return null;
		return a.getEdge(other, type);
	}

	/**
	 * Only allowable if "allowBareNodes" is true; otherwise, throws an exception
	 * 
	 * @param concept
	 * @return
	 */
	public Node getOrCreateNode(MemeType concept) {
		if (allowBareNodes) {
			return _getOrCreateNode(concept);
		}
		throw new IllegalStateException("Graph does not allow bare nodes");
	}

	/**
	 * Get the node mapped to this key
	 * 
	 * @param concept
	 * @return
	 */
	public Node getNode(MemeType concept) {
		return nodes.get(concept);
	}

	protected Node _getOrCreateNode(MemeType concept) {
		return nodes.computeIfAbsent(concept, (a) -> new Node(a));
	}

	public Collection<Node> getAllNodes() {
		return nodes.values();
	}

	public Collection<Edge> getAllEdges() {
		return edges;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("{");
		builder.append("V=").append(nodes.values()).append(", E=[");
		for (Edge edge : this.edges) {
			builder.append(edge).append(",");
		}
		builder.deleteCharAt(builder.length() - 1);
		return builder.append("]}").toString();
	}

	/**
	 * Used to compare two relationType arguments for easy tree map storage
	 * 
	 * @param one
	 * @param two
	 * @return
	 */
	protected abstract int compare(RelationType one, RelationType two);

}
