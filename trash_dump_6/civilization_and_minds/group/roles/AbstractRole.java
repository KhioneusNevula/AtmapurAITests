package civilization_and_minds.group.roles;

/**
 * Abstract implementation of a social role
 * 
 * @author borah
 *
 */
public abstract class AbstractRole implements IRoleConcept {

	private String name;
	private String societyName;

	protected AbstractRole(String societyName, String name) {
		this.name = name;
		this.societyName = societyName;
	}

	@Override
	public String getUniqueName() {
		return "role_" + societyName + "_" + name;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AbstractRole role) {
			return this.societyName.equals(role.societyName) && this.name.equals(role.name);
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return (this.societyName.hashCode() + this.name.hashCode());
	}

}
