package main;

/**
 * @Immutable
 * 
 * @author borah
 *
 * @param <A> first parameter type
 * @param <B> second parameter type
 */
public class Pair<A, B> {

	private A first;
	private B second;

	private static final Pair EMPTY = new Pair(null, null);

	public static final <A, B> Pair<A, B> of(A first, B second) {
		if (first == null && second == null) {
			return (Pair<A, B>) EMPTY;
		}
		return new Pair<>(first, second);
	}

	private Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}

	public A getFirst() {
		return first;
	}

	public B getSecond() {
		return second;
	}

	public void setFirst(A first) {
		this.first = first;
	}

	public void setSecond(B second) {
		this.second = second;
	}

	@Override
	public String toString() {
		return "pair<" + first + "," + second + ">";
	}

}
