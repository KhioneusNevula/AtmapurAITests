package civilization_and_minds.social.concepts.collection;

import java.util.Collection;

import civilization_and_minds.social.concepts.IConcept;

/**
 * A small, immutable collection of concepts
 * 
 * @author borah
 *
 */
public abstract class ConceptCollection<T extends IConcept> implements IConcept {

	protected Collection<T> concepts;

	protected void setConcepts(Collection<T> concepts) {
		this.concepts = concepts;
	}

	@Override
	public String getUniqueName() {
		return "set_" + concepts;
	}

	public Collection<T> getConcepts() {
		return concepts;
	}

	@Override
	public ConceptType getConceptType() {
		return ConceptType.COLLECTION;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ConceptCollection othercol) {
			return this.concepts.equals(othercol.concepts);
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return this.concepts.hashCode();
	}

}
