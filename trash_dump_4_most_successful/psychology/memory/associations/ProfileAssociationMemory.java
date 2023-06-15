package psychology.memory.associations;

import java.util.HashMap;
import java.util.Map;

import psychology.IHasKnowledge;
import psychology.perception.Profile;
import psychology.perception.Profile.TempProfile;

public class ProfileAssociationMemory {

	private Map<Profile, TempProfile> profileAssociations = new HashMap<>(0);

	private IHasKnowledge owner;

	public ProfileAssociationMemory(IHasKnowledge owner) {
		this.owner = owner;
	}

	public IHasKnowledge getOwner() {
		return owner;
	}

	public TempProfile getTempProfile(Profile forOther) {
		return profileAssociations.get(forOther);
	}

	public boolean hasTempProfile(Profile forOther) {
		return profileAssociations.containsKey(forOther);
	}

	public TempProfile getOrCreateTempProfile(Profile forOther) {
		return profileAssociations.computeIfAbsent(forOther, (a) -> new TempProfile(a));
	}

	/**
	 * cleans up all temp profiles as outlined in {@link #cleanTempProfile(Profile)}
	 */
	public void cleanAllTempProfiles() {
		for (Profile p : this.profileAssociations.keySet()) {
			this.cleanTempProfile(p);
		}
	}

	/**
	 * if the temp profile is empty, delete it; return true if this occurred
	 * 
	 * @param forOther
	 * @return
	 */
	public boolean cleanTempProfile(Profile forOther) {
		TempProfile tp = getTempProfile(forOther);
		if (tp != null && tp.isEmpty()) {
			this.profileAssociations.remove(forOther);
			return true;
		}
		return false;

	}

}
