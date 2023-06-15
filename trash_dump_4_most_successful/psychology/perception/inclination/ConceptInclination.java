package psychology.perception.inclination;

import psychology.Soul;
import psychology.perception.Profile;
import psychology.perception.memes.Association;
import psychology.social.concepts.Concept;

public class ConceptInclination extends Inclination {

	protected Concept concept;

	public ConceptInclination(Concept concept) {
		super(InclinationType.CONCEPT);
		this.concept = concept;
	}

	@Override
	public boolean onAssociation(Association<?> forAssociation, Soul ofSoul, Profile target) {
		ofSoul.getProfileMemory().getOrCreateTempProfile(target).addConcept(concept);
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ConceptInclination other) {
			return super.equals(obj) && this.concept.equals(other.concept);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode() + concept.hashCode();
	}

}
