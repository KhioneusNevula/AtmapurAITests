package psych_first.perception.knowledge;

import java.util.Collection;

import psych_first.perception.knowledge.events.RelationType;
import sim.IHasProfile;

/**
 * indicates a state of affairs, an appraisal of a situation
 * 
 * @author borah
 *
 */
public interface IOccurrence extends IHasProfile, IInformation, ICircumstance {

	public String report();

	/**
	 * gets all involved profiles
	 * 
	 * @return
	 */
	public Collection<IHasProfile> getInvolved();

	/**
	 * gets the relation between the given profiles in this event, with "from" as
	 * subject and "to" as predicate
	 */
	public Collection<RelationType> getRelations(IHasProfile from, IHasProfile to);

	/**
	 * gets profiles of the given relationship type for whom the profile parameter
	 * is subject (if isProfileSubject is true) or predicate (if not true) if
	 * RelationType is null, get all profiles with relations to the given one
	 * 
	 * @param type
	 * @param prof
	 * @param isProfileSubject
	 * @return
	 */
	Collection<IHasProfile> getProfiles(RelationType type, IHasProfile prof, boolean isProfileSubject);

	/**
	 * if this profile has this relationship with the other given profile; null for
	 * type to check if there is any relationship at all
	 * 
	 * @param sub
	 * @param type
	 * @param pred
	 * @return
	 */
	public boolean hasRelation(IHasProfile sub, RelationType type, IHasProfile pred);

	Object clone() throws CloneNotSupportedException;

}
