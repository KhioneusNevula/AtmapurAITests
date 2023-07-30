package mind.goals;

import java.util.Collection;

import mind.concepts.type.IMeme;
import mind.feeling.IFeeling;

/**
 * TODO Need to figure out a trust scale
 * 
 * @author borah
 *
 */
public interface IPersonalRelationship extends IGoal {

	@Override
	default Type getGoalType() {
		return Type.PERSONAL;
	}

	/**
	 * This may be null for (for example) Groups; this is the weighted average of
	 * all the feelings relating to source events
	 * 
	 * @return
	 */
	public IFeeling feeling();

	/**
	 * The reasons contributing to this relationship; usually Events, but can be
	 * SenseProperties or Properties as well
	 * 
	 * @return
	 */
	public Collection<IMeme> reasons();

	/**
	 * The feeling associated with this specific cause
	 * 
	 * @return
	 */
	public IFeeling feelingFactor(IMeme event);

	/**
	 * the factor of trust added by this specific cause
	 * 
	 * @param event
	 * @return
	 */
	public float trustFactor(IMeme event);

	/**
	 * The factor of obedience added by this specific cause
	 * 
	 * @param event
	 * @return
	 */
	public float obedienceFactor(IMeme event);

	/**
	 * The level of trust added/subtracted by this relationship. Also counts as
	 * devotion, for deities
	 * 
	 * @return
	 */
	public float trust();

	/**
	 * The level of power the other end of this relationship has over this party; 1f
	 * means full power. Dominance = -obedience
	 * 
	 * @return
	 */
	public float obedience();

	/**
	 * The level of power this party perceives itself as having over the other
	 * party; 1f means full power. Dominance = -obedience
	 * 
	 * @return
	 */
	public float dominance();

}
