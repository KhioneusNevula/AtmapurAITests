package mind.memory;

import java.util.HashSet;
import java.util.Set;

import mind.concepts.type.IConcept;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;

public class RememberedProperties implements IPropertyData {
	private Integer intProp;
	private IConcept conceptProp;
	private Set<IConcept> conceptListProp;
	private Profile profileProp;
	private Set<Profile> profileListProp;
	private Property property;

	public RememberedProperties(Property prop) {
		this.property = prop;
	}

	public Integer getIntProp() {
		return intProp;
	}

	public Property getProperty() {
		return property;
	}

	public Set<Profile> getProfileList() {
		if (profileListProp == null)
			return Set.of();
		return profileListProp;
	}

	public Set<IConcept> getConceptList() {
		if (conceptListProp == null)
			return Set.of();
		return conceptListProp;
	}

	public void removeProfileFromList(Profile prof) {
		if (!property.hasProfileListProperty())
			throw new UnsupportedOperationException();
		if (profileListProp == null)
			return;
		profileListProp.remove(prof);
		if (profileListProp.isEmpty())
			profileListProp = null;
	}

	public void removeConceptFromList(IConcept con) {
		if (!property.hasConceptListProperty())
			throw new UnsupportedOperationException();
		if (conceptListProp == null)
			return;
		conceptListProp.remove(con);
		if (conceptListProp.isEmpty())
			conceptListProp = null;
	}

	public boolean hasProfileInList(Profile prof) {
		if (profileListProp == null)
			return false;
		return profileListProp.contains(prof);
	}

	public boolean hasConceptInList(IConcept con) {
		if (conceptListProp == null)
			return false;
		return conceptListProp.contains(con);
	}

	public void addConceptToList(IConcept con) {
		if (!property.hasConceptListProperty())
			throw new UnsupportedOperationException();
		if (conceptListProp == null) {
			conceptListProp = new HashSet<>();
		}
		conceptListProp.add(con);
	}

	public void addProfileToList(Profile prof) {
		if (!property.hasProfileListProperty())
			throw new UnsupportedOperationException();
		if (profileListProp == null) {
			profileListProp = new HashSet<>();
		}
		profileListProp.add(prof);
	}

	public IConcept getConceptProp() {
		return conceptProp;
	}

	public Profile getProfileProp() {
		return profileProp;
	}

	public RememberedProperties setProfile(Profile other) {
		if (!property.hasProfileProperty())
			throw new UnsupportedOperationException();
		profileProp = other;
		return this;
	}

	public RememberedProperties setInt(int other) {
		if (!property.hasIntegerProperty())
			throw new UnsupportedOperationException();
		intProp = other;
		return this;
	}

	public RememberedProperties setConcept(IConcept other) {
		if (!property.hasConceptProperty())
			throw new UnsupportedOperationException();
		conceptProp = other;
		return this;
	}

	@Override
	public void forgetAllConceptList() {
		this.conceptListProp = null;
	}

	@Override
	public void forgetAllProfileList() {
		this.profileListProp = null;
	}

	@Override
	public int getKnownCount() {
		return (intProp != null ? 1 : 0) + (conceptProp != null ? 1 : 0)
				+ (conceptListProp != null ? conceptListProp.size() : 0) + (profileProp != null ? 1 : 0)
				+ (profileListProp != null ? 1 : 0);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("{");
		if (intProp != null)
			builder.append(" int=").append(intProp).append(";");
		if (conceptProp != null)
			builder.append(" concept=").append(conceptProp).append(";");
		if (profileProp != null)
			builder.append(" profile=").append(profileProp).append(";");
		if (conceptListProp != null)
			builder.append(" concept[]=").append(conceptListProp).append(";");
		if (profileListProp != null)
			builder.append(" profile[]=").append(profileListProp);
		return builder.append("}").toString();
	}
}