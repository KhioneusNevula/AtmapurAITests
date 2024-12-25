package civilization_and_minds.social.concepts.collection;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;

import civilization_and_minds.social.concepts.IConcept;

/**
 * An immutable set of concepts
 * 
 * @author borah
 *
 * @param <T>
 */
public class ConceptSet<T extends IConcept> extends ConceptCollection<T> {

	private boolean ordered;

	private ConceptSet(boolean ordered) {
		this.ordered = ordered;
	}

	/**
	 * Ordered = sorted set, otherwise just an immutable set
	 * 
	 * @param ordered
	 */
	private ConceptSet(boolean ordered, Iterator<T> elements, Comparator<T> comp) {
		this.ordered = ordered;
		if (ordered) {
			ImmutableSortedSet.Builder<T> imm = new ImmutableSortedSet.Builder<>(comp);
			imm.addAll(elements);
			this.setConcepts(imm.build());
		} else {
			this.setConcepts(ImmutableSet.copyOf(elements));
		}
	}

	/**
	 * Returns new concept set with the added element(s)
	 * 
	 * @param element
	 * @return
	 */
	public ConceptSet<T> addAll(Iterable<T> element) {
		ConceptSet<T> newSet = new ConceptSet<T>(ordered);
		if (ordered) {
			ImmutableSortedSet.Builder<T> imm = new ImmutableSortedSet.Builder<T>(
					((ImmutableSortedSet<T>) concepts).comparator());
			imm.addAll(concepts);
			imm.addAll(element);
			newSet.setConcepts(imm.build());
		} else {
			ImmutableSet.Builder<T> imm = new ImmutableSet.Builder<>();
			imm.addAll(concepts);
			imm.addAll(element);
			newSet.setConcepts(imm.build());
		}
		return newSet;
	}

	/**
	 * Returns new concept set with the added element(s)
	 * 
	 * @param element
	 * @return
	 */
	public ConceptSet<T> add(T... element) {
		ConceptSet<T> newSet = new ConceptSet<T>(ordered);
		if (ordered) {
			ImmutableSortedSet.Builder<T> imm = new ImmutableSortedSet.Builder<T>(
					((ImmutableSortedSet<T>) concepts).comparator());
			imm.addAll(concepts);
			imm.add(element);
			newSet.setConcepts(imm.build());
		} else {
			ImmutableSet.Builder<T> imm = new ImmutableSet.Builder<>();
			imm.addAll(concepts);
			imm.add(element);
			newSet.setConcepts(imm.build());
		}
		return newSet;
	}

	public static <T extends IConcept> ConceptSet<T> unordered(T... elements) {
		return new ConceptSet<T>(false, Arrays.stream(elements).iterator(), null);
	}

	public static <T extends IConcept> ConceptSet<T> unordered(Iterable<T> elements) {
		return new ConceptSet<>(false, elements.iterator(), null);
	}

	public static <T extends IConcept> ConceptSet<T> ordered(Comparator<T> comp, T... elements) {
		return new ConceptSet<T>(true, Arrays.stream(elements).iterator(), comp);
	}

	public static <T extends IConcept> ConceptSet<T> ordered(Comparator<T> comp, Iterable<T> elements) {
		return new ConceptSet<>(true, elements.iterator(), comp);
	}
}
