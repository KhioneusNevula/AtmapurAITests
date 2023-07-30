package main;

/**
 * @Immutable
 * 
 * @author borah
 *
 * @param <A> first parameter type
 * @param <B> second parameter type
 */
public class Triplet<A, B, C> {

	private A first;
	private B second;
	private C third;

	private static final Triplet EMPTY = new Triplet(null, null, null);

	public static final <A, B, C> Triplet<A, B, C> of(A first, B second, C third) {
		if (first == null && second == null && third == null) {
			return (Triplet<A, B, C>) EMPTY;
		}
		return new Triplet<>(first, second, third);
	}

	private Triplet(A first, B second, C third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	public A getFirst() {
		return first;
	}

	public B getSecond() {
		return second;
	}

	public C getThird() {
		return third;
	}

	public void setFirst(A first) {
		this.first = first;
	}

	public void setSecond(B second) {
		this.second = second;
	}

	public void setThird(C third) {
		this.third = third;
	}

	@Override
	public String toString() {
		return "<" + first + "," + second + "," + third + ">";
	}

	@Override
	public int hashCode() {
		return (first != null ? first.hashCode() : 0) + (second != null ? second.hashCode() : 0)
				+ (third != null ? third.hashCode() : 0);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Triplet tr) {
			return (this.first != null ? tr.first != null && this.first.equals(tr.first) : tr.first == null)
					&& (this.second != null ? tr.second != null && this.second.equals(tr.second) : tr.second == null)
					&& (this.third != null ? tr.third != null && this.third.equals(tr.third) : tr.third == null);
		}
		return super.equals(obj);
	}

}
