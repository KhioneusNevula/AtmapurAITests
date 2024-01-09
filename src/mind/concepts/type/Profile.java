package mind.concepts.type;

import java.util.UUID;

import sim.interfaces.IUnique;

/**
 * A class representing a profile. A profile is comparable to other profiles
 * exclusively by means of its UUID; the other information in it is merely for
 * display purposes
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

	public Profile(UUID holder) {
		this(holder, "unit");
	}

	public Profile(IUnique unique) {
		this(unique.getUUID(), unique.getUnitString());
		this.uniqueName = "profile_" + unique.getSimpleName();
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
	public String getUnitString() {
		return type;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IProfile other) {
			return other.getUUID().equals(this.ownerID);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.ownerID.hashCode() * 2;
	}

	@Override
	public String toString() {
		return "profile_" + type + this.ownerID.toString().substring(0, 4) + "...";
	}

}
