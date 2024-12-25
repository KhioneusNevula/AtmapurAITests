package civilization_and_minds.social.concepts.collection;

import java.util.Arrays;
import java.util.Iterator;

import com.google.common.collect.ImmutableList;

import civilization_and_minds.social.concepts.IConcept;

/**
 * An immutable list of concepts
 * 
 * @author borah
 *
 * @param <T>
 */
public class ConceptList<T extends IConcept> extends ConceptCollection<T> {

	private ConceptList() {

	}

	/**
	 * 
	 */
	private ConceptList(Iterator<T> elements) {

		ImmutableList.Builder<T> imm = new ImmutableList.Builder<>();
		imm.addAll(elements);
		this.setConcepts(imm.build());

	}

	/**
	 * Returns new concept list with the added element(s)
	 * 
	 * @param element
	 * @return
	 */
	public ConceptList<T> addAll(Iterable<T> element) {
		ConceptList<T> newSet = new ConceptList<T>();

		ImmutableList.Builder<T> imm = new ImmutableList.Builder<>();
		imm.addAll(concepts);
		imm.addAll(element);
		newSet.setConcepts(imm.build());

		return newSet;
	}

	/**
	 * Returns new concept list with the added element(s)
	 * 
	 * @param element
	 * @return
	 */
	public ConceptList<T> add(T... element) {
		ConceptList<T> newSet = new ConceptList<T>();

		ImmutableList.Builder<T> imm = new ImmutableList.Builder<>();
		imm.addAll(concepts);
		imm.add(element);
		newSet.setConcepts(imm.build());

		return newSet;
	}

	public static <T extends IConcept> ConceptList<T> create(T... elements) {
		return new ConceptList<T>(Arrays.stream(elements).iterator());
	}

	public static <T extends IConcept> ConceptList<T> create(Iterable<T> elements) {
		return new ConceptList<>(elements.iterator());
	}
}
