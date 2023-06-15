package mind.memory;

import java.util.Set;

import mind.concepts.type.IConcept;
import mind.concepts.type.Profile;

public interface IPropertyData {
	public static final IPropertyData PRESENCE = new IPropertyData() {
		@Override
		public Integer getIntProp() {
			return null;
		}

		@Override
		public Set<Profile> getProfileList() {
			return null;
		}

		@Override
		public Set<IConcept> getConceptList() {
			return null;
		}

		@Override
		public boolean hasProfileInList(Profile prof) {
			return false;
		}

		@Override
		public boolean hasConceptInList(IConcept con) {
			return false;
		}

		@Override
		public IConcept getConceptProp() {
			return null;
		}

		@Override
		public Profile getProfileProp() {
			return null;
		}

		@Override
		public void addConceptToList(IConcept con) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void addProfileToList(Profile prof) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void removeConceptFromList(IConcept con) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void removeProfileFromList(Profile prof) {
			throw new UnsupportedOperationException();
		}

		@Override
		public RememberedProperties setConcept(IConcept other) {
			throw new UnsupportedOperationException();
		}

		@Override
		public RememberedProperties setInt(int other) {
			throw new UnsupportedOperationException();
		}

		@Override
		public RememberedProperties setProfile(Profile other) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void forgetAllConceptList() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void forgetAllProfileList() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isPresence() {
			return true;
		}

		@Override
		public String toString() {
			return "YES";
		}
	};

	public Integer getIntProp();

	public Set<Profile> getProfileList();

	public Set<IConcept> getConceptList();

	public boolean hasProfileInList(Profile prof);

	public boolean hasConceptInList(IConcept con);

	public IConcept getConceptProp();

	public Profile getProfileProp();

	public void removeProfileFromList(Profile prof);

	public void removeConceptFromList(IConcept con);

	public void forgetAllConceptList();

	public void forgetAllProfileList();

	public void addConceptToList(IConcept con);

	default void addConceptsToList(Iterable<IConcept> cons) {
		for (IConcept con : cons) {
			addConceptToList(con);
		}
	}

	public void addProfileToList(Profile prof);

	default void addProfilesToList(Iterable<Profile> profs) {
		for (Profile prof : profs) {
			addProfileToList(prof);
		}
	}

	public RememberedProperties setProfile(Profile other);

	public RememberedProperties setInt(int other);

	public RememberedProperties setConcept(IConcept other);

	/**
	 * Return how many of the properties stored in this are known
	 * 
	 * @return
	 */
	default int getKnownCount() {
		return 1;
	}

	/**
	 * Returns true if this property data is only a marker of presence
	 * 
	 * @return
	 */
	default boolean isPresence() {
		return false;
	}

}
