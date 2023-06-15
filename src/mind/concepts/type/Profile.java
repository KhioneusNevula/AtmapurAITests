package mind.concepts.type;

import java.util.UUID;

/**
 * A class representing
 * 
 * @author borah
 *
 */
public class Profile implements IProfile {

	private UUID ownerID;
	private String uniqueName;
	private String type;

	/**
	 * "type" is an identifier; a single entity may be "unit" for example and a
	 * group may be "group", so their names will appear as, e.g.,
	 * "unit1294f-blah-blah" <br>
	 */
	public Profile(UUID profileHolder, String type) {
		this.ownerID = profileHolder;
		this.type = type;
		this.uniqueName = "profile_" + type + ownerID;
	}

	@Override
	public UUID getUUID() {
		return ownerID;
	}

	@Override
	public String getUniqueName() {
		return uniqueName;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Profile))
			return false;
		Profile other = (Profile) obj;
		return other.ownerID.equals(this.ownerID);
	}

	@Override
	public int hashCode() {
		return this.ownerID.hashCode();
	}

	@Override
	public String toString() {
		return "profile_" + type + this.ownerID.toString().substring(0, 4) + "...";
	}

}
