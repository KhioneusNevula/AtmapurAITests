package main;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

public class ImmutableCollection<T> extends AbstractCollection<T> {

	private Collection<T> inner;

	public ImmutableCollection(Collection<T> inner) {
		this.inner = inner;
	}

	@Override
	public Iterator<T> iterator() {
		return new ImIterator();
	}

	@Override
	public int size() {
		return inner.size();
	}

	private class ImIterator implements Iterator<T> {
		private Iterator<T> other;

		private ImIterator() {
			this.other = inner.iterator();
		}

		@Override
		public boolean hasNext() {
			return other.hasNext();
		}

		@Override
		public T next() {
			return other.next();
		}
	}

}
