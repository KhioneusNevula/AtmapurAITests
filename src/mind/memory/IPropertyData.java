package mind.memory;

import java.util.Set;

import mind.concepts.type.IMeme;
import mind.concepts.type.Profile;

public interface IPropertyData {

	public static final IPropertyData UNKNOWN = new IPropertyData() {
		@Override
		public Integer getIntProp() {
			return null;
		}

		@Override
		public Set<Profile> getProfileList() {
			return null;
		}

		@Override
		public Set<IMeme> getConceptList() {
			return null;
		}

		@Override
		public boolean hasProfileInList(Profile prof) {
			return false;
		}

		@Override
		public boolean hasConceptInList(IMeme con) {
			return false;
		}

		@Override
		public IMeme getConceptProp() {
			return null;
		}

		@Override
		public Profile getProfileProp() {
			return null;
		}

		@Override
		public void addConceptToList(IMeme con) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void addProfileToList(Profile prof) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void removeConceptFromList(IMeme con) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void removeProfileFromList(Profile prof) {
			throw new UnsupportedOperationException();
		}

		@Override
		public RememberedProperties setConcept(IMeme other) {
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
		public boolean onlyMarksPresence() {
			return true;
		}

		@Override
		public boolean isUnknown() {
			return true;
		}

		@Override
		public int getKnownCount() {
			return -1;
		}

		@Override
		public boolean isPresent() {
			return false;
		}

		@Override
		public boolean isAbsent() {
			return true;
		}

		@Override
		public String toString() {
			return "UNKNOWN";
		}
	};
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
		public Set<IMeme> getConceptList() {
			return null;
		}

		@Override
		public boolean hasProfileInList(Profile prof) {
			return false;
		}

		@Override
		public boolean hasConceptInList(IMeme con) {
			return false;
		}

		@Override
		public IMeme getConceptProp() {
			return null;
		}

		@Override
		public Profile getProfileProp() {
			return null;
		}

		@Override
		public void addConceptToList(IMeme con) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void addProfileToList(Profile prof) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void removeConceptFromList(IMeme con) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void removeProfileFromList(Profile prof) {
			throw new UnsupportedOperationException();
		}

		@Override
		public RememberedProperties setConcept(IMeme other) {
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
		public boolean onlyMarksPresence() {
			return true;
		}

		@Override
		public String toString() {
			return "YES";
		}
	};

	public static final IPropertyData ABSENCE = new IPropertyData() {
		@Override
		public Integer getIntProp() {
			return null;
		}

		@Override
		public Set<Profile> getProfileList() {
			return null;
		}

		@Override
		public Set<IMeme> getConceptList() {
			return null;
		}

		@Override
		public boolean hasProfileInList(Profile prof) {
			return false;
		}

		@Override
		public boolean hasConceptInList(IMeme con) {
			return false;
		}

		@Override
		public IMeme getConceptProp() {
			return null;
		}

		@Override
		public Profile getProfileProp() {
			return null;
		}

		@Override
		public void addConceptToList(IMeme con) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void addProfileToList(Profile prof) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void removeConceptFromList(IMeme con) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void removeProfileFromList(Profile prof) {
			throw new UnsupportedOperationException();
		}

		@Override
		public RememberedProperties setConcept(IMeme other) {
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
		public boolean onlyMarksPresence() {
			return true;
		}

		@Override
		public boolean isPresent() {
			return false;
		}

		@Override
		public boolean isAbsent() {
			return true;
		}

		@Override
		public String toString() {
			return "NO";
		}
	};

	public Integer getIntProp();

	public Set<Profile> getProfileList();

	public Set<IMeme> getConceptList();

	public boolean hasProfileInList(Profile prof);

	public boolean hasConceptInList(IMeme con);

	public IMeme getConceptProp();

	public Profile getProfileProp();

	public void removeProfileFromList(Profile prof);

	public void removeConceptFromList(IMeme con);

	public void forgetAllConceptList();

	public void forgetAllProfileList();

	public void addConceptToList(IMeme con);

	default void addConceptsToList(Iterable<IMeme> cons) {
		for (IMeme con : cons) {
			addConceptToList(con);
		}
	}

	default void updateInformation(IPropertyData fromOther) {
		throw new UnsupportedOperationException();
	}

	public void addProfileToList(Profile prof);

	default void addProfilesToList(Iterable<Profile> profs) {
		for (Profile prof : profs) {
			addProfileToList(prof);
		}
	}

	public RememberedProperties setProfile(Profile other);

	public RememberedProperties setInt(int other);

	public RememberedProperties setConcept(IMeme other);

	/**
	 * Return how many of the properties stored in this are known
	 * 
	 * @return
	 */
	default int getKnownCount() {
		return 1;
	}

	/**
	 * Returns true if this property data is only a marker of presence or absence
	 * 
	 * @return
	 */
	default boolean onlyMarksPresence() {
		return false;
	}

	/**
	 * Return true if this property data indicates absence
	 * 
	 * @return
	 */
	default boolean isAbsent() {
		return false;
	}

	default boolean isPresent() {
		return true;
	}

	default boolean isUnknown() {
		return false;
	}

}
