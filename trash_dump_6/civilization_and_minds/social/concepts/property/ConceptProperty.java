package civilization_and_minds.social.concepts.property;

import civilization_and_minds.social.concepts.IConcept;

/**
 * A simple, present/absent binary property, represented as a concept. Sicne
 * these are usually specific to social systems, they usually are represented
 * with a name of a social system
 * 
 * @author borah
 *
 */
public class ConceptProperty implements IConcept {

	private String socialSystem;
	private String name;

	/**
	 * 
	 * @param socialSystem the social system this concept belongs to, may be
	 *                     emptystring
	 * @param name         the name of this concept
	 */
	public ConceptProperty(String socialSystem, String name) {
		this.socialSystem = socialSystem;
		this.name = name;
	}

	/**
	 * Name of this concept
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * What "social system" this concept is part of. May be empty.
	 * 
	 * @return
	 */
	public String getSocialSystemName() {
		return socialSystem;
	}

	@Override
	public String getUniqueName() {
		return "conceptproperty_" + (this.socialSystem.isEmpty() ? "" : this.socialSystem + "_") + this.name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ConceptProperty cpp) {
			return this.getUniqueName().equals(cpp.getUniqueName());
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return this.getUniqueName().hashCode();
	}

	@Override
	public ConceptType getConceptType() {
		return ConceptType.CONCEPT_PROPERTY;
	}

}
