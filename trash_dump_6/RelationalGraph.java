package utilities;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Table;

/**
 * 
 * @author borah
 *
 * @param <NodeData> what kind of data is stored in nodes
 * @param <EdgeType> organizing datatype for edges
 * @param <EdgeData> what kind of data is stored in edges
 */
public class RelationalGraph<NodeData, EdgeType extends IInvertibleRelationType<? extends EdgeType>, EdgeData> {

	/**
	 * Represents a single node in the graph. Nodes store all their connections for
	 * indirection, but do not use these connections to check equality, calculate
	 * hashcode, etc.
	 * 
	 * @author borah
	 *
	 * @param <NodeData>
	 * @param <EdgeType>
	 * @param <EdgeData>
	 */
	public static interface INode<NodeData, EdgeType extends IInvertibleRelationType<? extends EdgeType>, EdgeData> {

		/**
		 * IF this node is a root node, i.e. it does not get deleted in any circumstance
		 * 
		 * @return
		 */
		public boolean isRoot();

		/**
		 * Whether this node is still in the graph. Intended for ensuring that
		 * references to nodes know when they are removed from the graph
		 * 
		 * @return
		 */
		public boolean inGraph();

		public NodeData getData();

		/**
		 * Get the edge of the given type connecting these nodes
		 * 
		 * @param otherNode
		 * @param type
		 * @return
		 */
		public IEdge<NodeData, EdgeType, EdgeData> getEdgeOfTypeWith(INode<NodeData, EdgeType, EdgeData> otherNode,
				EdgeType type);

		/**
		 * Get an iterable of all edges of a given type
		 * 
		 * @param type
		 * @return
		 */
		public Iterable<IEdge<NodeData, EdgeType, EdgeData>> getAllEdgesOfType(EdgeType type);

		/**
		 * Get an iterable of all edges with the given node
		 * 
		 * @param otherNode
		 * @return
		 */
		public Iterable<IEdge<NodeData, EdgeType, EdgeData>> getAllEdgesWith(
				INode<NodeData, EdgeType, EdgeData> otherNode);

		/**
		 * Get an iterable of all edges of a given set of types
		 * 
		 * @param type
		 * @return
		 */
		public default Iterable<IEdge<NodeData, EdgeType, EdgeData>> getAllEdgesOfTypes(EdgeType... types) {
			Iterable<IEdge<NodeData, EdgeType, EdgeData>>[] iters = new Iterable[types.length];
			for (int i = 0; i < types.length; i++) {
				iters[i] = this.getAllEdgesOfType(types[i]);
			}
			return Iterables.concat(iters);
		}

		/**
		 * Get an iterable of all edges of types selected with the given predicate
		 * 
		 * @param type
		 * @return
		 */
		public Iterable<IEdge<NodeData, EdgeType, EdgeData>> getAllEdgesOfTypes(Predicate<EdgeType> predicate);

		/**
		 * Total number of edges connecting to this node (the Degree of the node)
		 * 
		 * @return
		 */
		public int countEdges();

		/**
		 * Return number of edges of this type
		 * 
		 * @param type
		 * @return
		 */
		public int countEdgesOfType(EdgeType type);

		/**
		 * Return number of edges with this node
		 * 
		 * @param otherNode
		 * @return
		 */
		public int countEdgesWith(INode<NodeData, EdgeType, EdgeData> otherNode);

		/**
		 * If this node connects to an edge matching the given edge
		 * 
		 * @param edge
		 * @return
		 */
		public boolean hasEdge(IEdge<NodeData, EdgeType, EdgeData> edge);

		/**
		 * Returns all nodes connected to this one
		 * 
		 * @return
		 */
		public Collection<INode<NodeData, EdgeType, EdgeData>> getAllConnectedNodes();

		/**
		 * Returns all nodes connected to this one of the given type
		 * 
		 * @return
		 */
		public Collection<INode<NodeData, EdgeType, EdgeData>> getAllConnectedNodes(EdgeType type);

		/**
		 * Return types of all relations connected to this node
		 * 
		 * @return
		 */
		public Collection<EdgeType> getAllConnectedRelationTypes();

		/**
		 * Return types of all relations between this node and the given other node
		 * 
		 * @return
		 */
		public Collection<EdgeType> getAllConnectedRelationTypes(INode<NodeData, EdgeType, EdgeData> node);

	}

	/**
	 * Represents a single edge in the graph. Note: equality checks for edges only
	 * need to check if start, end, and type are the same. Data sameness should not
	 * have to be checked and can be checked separately if relevant.
	 * 
	 * @author borah
	 *
	 * @param <NodeData>
	 * @param <EdgeType>
	 * @param <EdgeData>
	 */
	public interface IEdge<NodeData, EdgeType extends IInvertibleRelationType<? extends EdgeType>, EdgeData> {

		/**
		 * Whether this edge is still in the graph. Intended for ensuring that
		 * references to edges know when they are removed from the graph
		 * 
		 * @return
		 */
		public boolean inGraph();

		/**
		 * Get the node that is on the left, i.e. the "closer" node
		 * 
		 * @return
		 */
		public INode<NodeData, EdgeType, EdgeData> getLeftNode();

		/**
		 * Get node on the right, i.e .the "further" node
		 * 
		 * @return
		 */
		public INode<NodeData, EdgeType, EdgeData> getRightNode();

		/**
		 * Get a variant of the edge but in the opposite direction
		 * 
		 * @return
		 */
		public IEdge<NodeData, EdgeType, EdgeData> inverse();

		public EdgeType getType();

		public EdgeData getData();

		/**
		 * get the strength of this edge, whatever that represents
		 * 
		 * @return
		 */
		public float getStrength();

		/**
		 * set the strength of this edge, whatever that represents
		 */
		public void setStrength(float strength);

		/**
		 * Set edge data
		 * 
		 * @return
		 */
		public void setData(EdgeData data);

		/**
		 * Whether this edge doesn't actually store any data and only represents the
		 * inverse view of another edge
		 * 
		 * @return
		 */
		public boolean isInverseView();
	}

	private class Node implements INode<NodeData, EdgeType, EdgeData> {

		private NodeData data;
		private boolean isRoot;
		private Table<INode<NodeData, EdgeType, EdgeData>, EdgeType, IEdge<NodeData, EdgeType, EdgeData>> edges;
		private boolean active = true;

		private Node(NodeData data, boolean isRoot) {
			this.data = data;
			this.isRoot = isRoot;
		}

		@Override
		public boolean isRoot() {
			return isRoot;
		}

		public boolean inGraph() {
			return active;
		}

		@Override
		public int countEdges() {

			return edges == null ? 0 : edges.size();
		}

		public NodeData getData() {
			return data;
		}

		@Override
		public IEdge<NodeData, EdgeType, EdgeData> getEdgeOfTypeWith(INode<NodeData, EdgeType, EdgeData> otherNode,
				EdgeType type) {
			return edges == null ? null : edges.get(otherNode, type);
		}

		@Override
		public Iterable<IEdge<NodeData, EdgeType, EdgeData>> getAllEdgesOfType(EdgeType type) {
			return edges == null ? Collections.emptySet() : edges.column(type).values();
		}

		@Override
		public Iterable<IEdge<NodeData, EdgeType, EdgeData>> getAllEdgesWith(
				INode<NodeData, EdgeType, EdgeData> otherNode) {
			return edges == null ? Collections.emptySet() : edges.row(otherNode).values();
		}

		@Override
		public int countEdgesOfType(EdgeType type) {
			return edges == null ? 0 : edges.column(type).size();
		}

		@Override
		public int countEdgesWith(INode<NodeData, EdgeType, EdgeData> otherNode) {
			return edges == null ? 0 : edges.row(otherNode).size();
		}

		@Override
		public boolean hasEdge(IEdge<NodeData, EdgeType, EdgeData> edge) {
			if (edges == null)
				return false;
			Object e1 = edges.get(edge.getLeftNode(), edge.getType());
			Object e2 = edges.get(edge.getRightNode(), edge.getType());
			return e1 != null && edge.equals(e1) || e2 != null && edge.equals(e2);
		}

		/**
		 * Stores this edge in the node. Assumes edge's left node is this. If not, throw
		 * exception.
		 * 
		 * @param edge
		 */
		private void addEdge(IEdge<NodeData, EdgeType, EdgeData> edge) {
			if (edges == null)
				edges = HashBasedTable.create();
			if (edge.getLeftNode() == this) {
				this.edges.put(edge.getRightNode(), edge.getType(), edge);
			} else {
				throw new IllegalArgumentException("" + edge);
			}
		}

		/**
		 * create an edge and return it. Does not change data or edge storages in the
		 * other node
		 * 
		 * @return
		 */
		private Edge createEdgeWith(INode<NodeData, EdgeType, EdgeData> otherNode, EdgeType type) {
			Edge edge = new Edge(this, otherNode, type);
			this.addEdge(edge);
			return edge;
		}

		private IEdge<NodeData, EdgeType, EdgeData> removeEdge(INode<NodeData, EdgeType, EdgeData> otherNode,
				EdgeType type) {
			if (edges == null)
				return null;
			return this.edges.remove(otherNode, type);
		}

		private boolean removeEdge(IEdge<NodeData, EdgeType, EdgeData> otherNode) {
			if (edges == null)
				return false;
			if (!this.edges.row(otherNode.getRightNode()).remove(otherNode.getType(), otherNode)) {
				return this.edges.row(otherNode.getLeftNode()).remove(otherNode.getType(), otherNode);
			}
			return true;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof INode node) {
				return this.data.equals(node.getData());
			}
			return super.equals(obj);
		}

		@Override
		public int hashCode() {
			return data.hashCode();
		}

		@Override
		public String toString() {
			return (this.isRoot ? "[" : "(") + this.data + (this.isRoot ? "]" : ")");
		}

		@Override
		public Iterable<IEdge<NodeData, EdgeType, EdgeData>> getAllEdgesOfTypes(Predicate<EdgeType> predicate) {
			Stream<EdgeType> stream = this.edges.columnKeySet().stream().filter(predicate);
			int count = (int) stream.count();
			Iterable<IEdge<NodeData, EdgeType, EdgeData>>[] iters = new Iterable[count];
			Iterator<EdgeType> striter = stream.iterator();
			for (int i = 0; i < count; i++) {
				iters[i] = this.getAllEdgesOfType(striter.next());
			}
			return Iterables.concat(iters);
		}

		@Override
		public Collection<INode<NodeData, EdgeType, EdgeData>> getAllConnectedNodes() {
			return this.edges == null ? Collections.emptySet() : this.edges.rowKeySet();
		}

		@Override
		public Collection<INode<NodeData, EdgeType, EdgeData>> getAllConnectedNodes(EdgeType type) {
			return this.edges == null ? Collections.emptySet() : this.edges.column(type).keySet();
		}

		@Override
		public Collection<EdgeType> getAllConnectedRelationTypes() {
			return this.edges == null ? Collections.emptySet() : this.edges.columnKeySet();
		}

		@Override
		public Collection<EdgeType> getAllConnectedRelationTypes(INode<NodeData, EdgeType, EdgeData> node) {
			return this.edges == null ? Collections.emptySet() : this.edges.row(node).keySet();
		}

	}

	private class Edge implements IEdge<NodeData, EdgeType, EdgeData> {

		private INode<NodeData, EdgeType, EdgeData> left;
		private INode<NodeData, EdgeType, EdgeData> right;
		private EdgeType type;
		private EdgeData data;
		private float strength;
		private InverseEdge inverse;
		private boolean active;

		private Edge(INode<NodeData, EdgeType, EdgeData> left, INode<NodeData, EdgeType, EdgeData> right,
				EdgeType type) {
			this.left = left;
			this.right = right;
			this.type = type;
			this.inverse = new InverseEdge();
		}

		@Override
		public boolean inGraph() {
			return active;
		}

		@Override
		public INode<NodeData, EdgeType, EdgeData> getLeftNode() {
			return this.left;
		}

		@Override
		public INode<NodeData, EdgeType, EdgeData> getRightNode() {
			return this.right;
		}

		@Override
		public IEdge<NodeData, EdgeType, EdgeData> inverse() {
			return inverse;
		}

		@Override
		public EdgeType getType() {
			return this.type;
		}

		@Override
		public EdgeData getData() {
			return this.data;
		}

		@Override
		public float getStrength() {
			return this.strength;
		}

		@Override
		public void setStrength(float strength) {
			this.strength = strength;
		}

		@Override
		public void setData(EdgeData data) {
			this.data = data;
		}

		@Override
		public String toString() {
			return left + "==" + type + (data != null ? "(" + data + ")" : "") + "==" + right;
		}

		@Override
		public boolean isInverseView() {
			return false;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof IEdge edge) {
				return this.getLeftNode().equals(edge.getLeftNode()) && this.getRightNode().equals(edge.getRightNode())
						&& this.type.equals(edge.getType());
			}
			return super.equals(obj);
		}

		@Override
		public int hashCode() {
			return this.left.hashCode() + this.right.hashCode() + this.type.hashCode();
		}

		private class InverseEdge implements IEdge<NodeData, EdgeType, EdgeData> {

			@Override
			public boolean inGraph() {
				return Edge.this.inGraph();
			}

			private void setActive(boolean active) {
				Edge.this.active = active;
			}

			@Override
			public INode<NodeData, EdgeType, EdgeData> getLeftNode() {
				return Edge.this.getRightNode();
			}

			@Override
			public INode<NodeData, EdgeType, EdgeData> getRightNode() {
				return Edge.this.getLeftNode();
			}

			@Override
			public IEdge<NodeData, EdgeType, EdgeData> inverse() {
				return Edge.this;
			}

			@Override
			public EdgeType getType() {
				return Edge.this.getType().inverse();
			}

			@Override
			public EdgeData getData() {
				return Edge.this.getData();
			}

			@Override
			public float getStrength() {
				return Edge.this.getStrength();
			}

			@Override
			public void setStrength(float s) {
				Edge.this.setStrength(s);
			}

			@Override
			public void setData(EdgeData data) {
				Edge.this.setData(data);
			}

			@Override
			public String toString() {
				return right + "==" + type.inverse() + (data != null ? "(" + data + ")" : "") + "==" + left;
			}

			@Override
			public boolean equals(Object obj) {
				if (obj instanceof IEdge edge) {
					return this.getLeftNode().equals(edge.getLeftNode())
							&& this.getRightNode().equals(edge.getRightNode()) && this.getType().equals(edge.getType())
							&& (this.getData() == null ? edge.getData() == null
									: (edge.getData() == null ? false : this.getData().equals(edge.getData())));
				}
				return super.equals(obj);
			}

			@Override
			public int hashCode() {
				return Edge.this.hashCode();
			}

			@Override
			public boolean isInverseView() {
				return true;
			}

		}

	}

	private HashMap<NodeData, Node> nodes;
	private HashSet<Edge> edges;
	private ImmutableCollection<Node> iNodes;
	private ImmutableCollection<Edge> iEdges;

	public RelationalGraph() {
		nodes = new HashMap<>();
		iNodes = new ImmutableCollection<>(nodes.values());
		edges = new HashSet<>();
		iEdges = new ImmutableCollection<>(edges);
	}

	/**
	 * @param allowBareNodes Whether this graph should allow nodes with no
	 *                       connections
	 */
	public RelationalGraph(NodeData... rootNodes) {
		this();
		for (NodeData nd : rootNodes) {
			this.addNewRootNode(nd);
		}

	}

	/**
	 * Gets (the immutable representation of) the set of nodes
	 * 
	 * @return
	 */
	public Collection<? extends INode<NodeData, EdgeType, EdgeData>> getNodeSet() {
		return iNodes;
	}

	/**
	 * Gets (the immutable representation of) the set of edges
	 * 
	 * @return
	 */
	public Collection<? extends IEdge<NodeData, EdgeType, EdgeData>> getEdgeSet() {
		return iEdges;
	}

	/**
	 * Gets the node of this data, or null if it doesn't exist
	 * 
	 * @param concept
	 * @return
	 */
	public INode<NodeData, EdgeType, EdgeData> getNode(NodeData concept) {
		return this.nodes.get(concept);
	}

	/**
	 * Whether this graph contains the given node
	 * 
	 * @param data
	 * @return
	 */
	public boolean containsNode(NodeData data) {
		return this.nodes.containsKey(data);
	}

	/**
	 * Make a certain node a root node
	 * 
	 * @param node
	 */
	public void setAsRootNode(Node node) {
		node.isRoot = true;
	}

	/**
	 * Whether the graph contains this node
	 * 
	 * @param node
	 * @return
	 */
	public boolean containsNode(Node node) {
		return this.nodes.get(node.data) == node;
	}

	/**
	 * Whether there is an edge here of this type
	 * 
	 * @param one
	 * @param two
	 * @param type
	 * @return
	 */
	public boolean edgeExists(NodeData one, NodeData two, EdgeType type) {
		Node o = this.getExistingNode(one);
		Node t = this.getExistingNode(two);
		if (o == null || t == null)
			return false;
		return o.getEdgeOfTypeWith(t, type) != null;
	}

	/**
	 * Count edges between these nodes
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public int countEdgesBetween(NodeData from, NodeData to) {
		Node o = this.getExistingNode(from);
		Node t = this.getExistingNode(to);
		if (o == null || t == null)
			return 0;
		return o.countEdgesWith(t);
	}

	/**
	 * Get a specific edge
	 * 
	 * @param one
	 * @param two
	 * @param type
	 * @return
	 */
	public IEdge<NodeData, EdgeType, EdgeData> getEdge(NodeData one, NodeData two, EdgeType type) {
		Node o = this.getExistingNode(one);
		Node t = this.getExistingNode(two);
		if (o == null || t == null)
			return null;
		return o.getEdgeOfTypeWith(t, type);
	}

	/**
	 * Create a new <em> root </em> node in the graph with no connections.
	 * 
	 * @param data
	 */
	public INode<NodeData, EdgeType, EdgeData> addNewRootNode(NodeData data) {
		Node a = new Node(data, true);
		this.nodes.put(data, a);
		return a;
	}

	public IEdge<NodeData, EdgeType, EdgeData> createEdge(NodeData one, NodeData two, EdgeType type) {
		Node a = getOrCreateNode(one);
		Node b = getOrCreateNode(two);
		Edge newEdge = a.createEdgeWith(b, type);
		b.addEdge(newEdge.inverse());
		this.edges.add(newEdge);
		return newEdge;
	}

	/**
	 * Remove this node and all its connections from the graph. Return the node
	 * object. The node will still retain all edges that indicate all its
	 * connections but the graph's own nodes will not have the returning connections
	 * 
	 * @param node
	 * @return
	 */
	public Node removeNode(NodeData node) {
		Node o = this.getExistingNode(node);
		if (o == null)
			return null;
		this.removeNode(o);
		return o;
	}

	/**
	 * Remove this node and all its connections from the graph. The node will still
	 * retain all edges that indicate all its connections but the graph's own nodes
	 * will not have the returning connections
	 * 
	 * @param node
	 * @return
	 */
	public void removeNode(Node node) {
		this.deleteNode(node);
		for (IEdge<NodeData, EdgeType, EdgeData> edge : node.edges.values()) {
			Node right = (Node) edge.getRightNode();
			right.removeEdge(edge.inverse());
			if (right.countEdges() == 0 && !right.isRoot)
				this.deleteNode(right);
			this.edges.remove(edge);
		}
	}

	/**
	 * Delete the given edge
	 * 
	 * @param one
	 * @param two
	 * @param type
	 * @return
	 */
	public IEdge<NodeData, EdgeType, EdgeData> removeEdge(NodeData one, NodeData two, EdgeType type) {
		Node a = getExistingNode(one);
		Node b = getExistingNode(two);
		if (a == null || b == null)
			return null;
		return removeEdge(a, b, type);
	}

	/**
	 * Delete the ggiven edge
	 * 
	 * @param one
	 * @param two
	 * @param type
	 * @return
	 */
	public IEdge<NodeData, EdgeType, EdgeData> removeEdge(Node one, Node two, EdgeType type) {
		IEdge<NodeData, EdgeType, EdgeData> edge = one.removeEdge(two, type);
		two.removeEdge(edge.inverse());
		if (edge.isInverseView())
			this.edges.remove(edge.inverse());
		else
			this.edges.remove(edge);
		if (edge instanceof Edge e) {
			e.active = false;
		} else if (edge instanceof Edge.InverseEdge ie) {
			ie.setActive(false);
		}
		if (!one.isRoot && one.countEdges() <= 0) {
			deleteNode(one);
		}
		if (!two.isRoot && two.countEdges() <= 0) {
			deleteNode(two);
		}

		return edge;
	}

	/**
	 * Serialize this graph. Assumes all data types are serializable. TODO
	 * serialization
	 * 
	 * @param toStream
	 */
	public void serialize(OutputStream toStream) {

	}

	/**
	 * Deserialize this graph. TODO deserialization
	 * 
	 * @param toStream
	 */
	public void deserialize(InputStream fromStream) {

	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RelationalGraph other) {
			return this.nodes.equals(other.nodes) && this.edges.equals(other.edges);
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return this.nodes.hashCode() + this.edges.hashCode();
	}

	private void deleteNode(Node n) {
		n.active = false;
		this.nodes.remove(n.data, n);
	}

	private Node getOrCreateNode(NodeData dat) {
		return this.nodes.computeIfAbsent(dat, (a) -> new Node(a, false));
	}

	private Node getExistingNode(NodeData dat) {
		return this.nodes.get(dat);
	}

	@Override
	public String toString() {
		return "{roots=" + this.nodes.values().stream().filter(Node::isRoot).collect(Collectors.toSet()) + ", edges="
				+ this.edges + "}";
	}

}
