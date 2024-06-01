package mind.relationships;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;

import mind.concepts.type.IProfile;
import sim.relationalclasses.AbstractRelationalGraph;

public class PartyRelationshipsGraph extends AbstractRelationalGraph<RelationType, Collection<Relationship>, IProfile> {

	public PartyRelationshipsGraph() {
		super(false);
	}

	@Override
	protected int compare(RelationType one, RelationType two) {
		return one.compareTo(two);
	}

	public void putRelationship(IProfile one, IProfile other, Relationship rel) {
		IEdge<RelationType, Collection<Relationship>, IProfile> edge = this.getEdge(one, other, rel.getType());
		if (edge == null) {
			edge = this.createEdge(one, other, rel.getType(), new LinkedList<>());
		}
		edge.getArgs().add(rel);
	}

	public Collection<Relationship> getAllRelationships(IProfile one, IProfile other) {
		return new RelationsCollection(this.getEdgesBetween(one, other));
	}

	public boolean removeRelationship(IProfile one, IProfile other, Relationship rel) {
		IEdge<RelationType, Collection<Relationship>, IProfile> edge = this.getEdge(one, other, rel.getType());
		boolean worked = edge.getArgs().remove(rel);
		if (edge.getArgs().isEmpty()) {
			this.removeEdge(one, other, rel.getType());
		}
		return worked;

	}

	protected class RelationsCollection extends AbstractCollection<Relationship> {

		private Map<RelationType, IEdge<RelationType, Collection<Relationship>, IProfile>> edges;
		private int size;

		private RelationsCollection(Map<RelationType, IEdge<RelationType, Collection<Relationship>, IProfile>> edges) {
			this.edges = edges;
			for (IEdge<RelationType, Collection<Relationship>, IProfile> edge : this.edges.values()) {
				size += edge.getArgs().size();
			}
		}

		@Override
		public boolean contains(Object o) {
			if (o instanceof Relationship rel) {
				IEdge<RelationType, Collection<Relationship>, IProfile> edg = edges.get(rel.getType());
				if (edg == null)
					return false;
				return edg.getArgs().contains(rel);
			}
			return false;
		}

		@Override
		public Iterator<Relationship> iterator() {
			return new RIterator();
		}

		@Override
		public int size() {
			return size;
		}

		private class RIterator implements Iterator<Relationship> {

			private Iterator<Relationship> curIter;
			private Iterator<IEdge<RelationType, Collection<Relationship>, IProfile>> outerIter;

			public RIterator() {
				outerIter = edges.values().iterator();
				if (outerIter.hasNext()) {
					curIter = outerIter.next().getArgs().iterator();
				}
			}

			@Override
			public boolean hasNext() {
				if (curIter == null)
					return false;
				return curIter.hasNext();
			}

			@Override
			public Relationship next() {
				if (curIter == null)
					throw new NoSuchElementException();
				Relationship r = curIter.next();
				if (!curIter.hasNext()) {
					curIter = null;
					if (outerIter.hasNext()) {
						curIter = outerIter.next().getArgs().iterator();
					}
				}
				return r;
			}

		}

	}

}
