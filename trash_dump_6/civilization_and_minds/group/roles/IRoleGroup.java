package civilization_and_minds.group.roles;

import civilization_and_minds.group.IGroup;

/**
 * A role in a society
 * 
 * @author borah
 *
 */
public interface IRoleGroup extends IGroup {
	/**
	 * Whether this role group is perceived to be based on an inherent trait, as
	 * opposed to being a taken job
	 * 
	 * @return
	 */
	public boolean isInherent();

}
