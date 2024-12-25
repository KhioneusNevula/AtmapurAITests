package civilization_and_minds.social.concepts.profile;

import java.util.UUID;

import civilization_and_minds.social.concepts.IConcept;

/**
 * a conceptual projection of an individual being, group, or something more
 * complex such as a language, location, or natural landform. Includes a type
 * designation, but this is not factored into hashcode or equality checks
 * 
 * @author borah
 *
 */
public class Profile implements IConcept {

	private UUID id;
	private ProfileType type;
	private String optionalName;

	public Profile(UUID id, ProfileType type) {
		this.id = id;
		this.type = type;
	}

	public String getOptionalName() {
		return optionalName;
	}

	/**
	 * Set an optional name to make profile more easily recognizable. Not factored
	 * into hashcode or equality checks
	 * 
	 * @param optionalName
	 * @return
	 */
	public Profile setOptionalName(String optionalName) {
		this.optionalName = optionalName;
		return this;
	}

	@Override
	public ConceptType getConceptType() {
		return type.getConceptType();
	}

	@Override
	public String getUniqueName() {
		return "profile_" + id.getMostSignificantBits() + "_" + id.getLeastSignificantBits();
	}

	@Override
	public String toString() {
		return "profile_" + (optionalName == null ? this.type.toString().toLowerCase() : optionalName) + "("
				+ id.toString().substring(0, 5) + ")";
	}

	/**
	 * Profile type
	 * 
	 * @return
	 */
	public ProfileType getType() {
		return type;
	}

	/**
	 * Unique id of this profile
	 * 
	 * @return
	 */
	public UUID getId() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Profile p) {
			return this.id.equals(p.id);
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

}
