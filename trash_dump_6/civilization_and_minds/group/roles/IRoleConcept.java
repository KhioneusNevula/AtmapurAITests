package civilization_and_minds.group.roles;

import civilization_and_minds.group.purpose.IGroupPurpose;

/**
 * Denotes a social role which makes a social group of some kind
 * 
 * @author borah
 *
 */
public interface IRoleConcept extends IGroupPurpose {

	/**
	 * Whether this role takes on a community-guiding role in society. E.g. a priest
	 * 
	 * @return
	 */
	public boolean guides();

	/**
	 * Whether this role takes a governing role in society. E.g. a ruler
	 * 
	 * @return
	 */
	public boolean governs();

	/**
	 * Whether this role is based on (perceived or real) traits (as opposed to taken
	 * as a job). E.g. gender.
	 * 
	 * @return
	 */
	public boolean isTraitBased();

	/**
	 * Whether this role constitutes a group that does a job in a society. E.g.
	 * farmer.
	 * 
	 * @return
	 */
	public boolean works();

	/**
	 * -1.0f to 1.0f, where 1.0f is absolute reverence and -1.0f is absolute
	 * disgust. 0.0f is complete neutrality
	 * 
	 * @return
	 */
	public float prestige();

	@Override
	default GroupType getGroupType() {
		return GroupType.ROLE;
	}

}
