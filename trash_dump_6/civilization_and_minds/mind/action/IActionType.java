package civilization_and_minds.mind.action;

import com.google.common.collect.Multiset;

import actor.construction.physical.IPartAbility;
import civilization_and_minds.mind.IMind;
import civilization_and_minds.mind.goals.IGoal;
import civilization_and_minds.mind.thoughts.IThought;
import civilization_and_minds.social.concepts.IConcept;

/**
 * A type of action that an individual can perform
 * 
 * @author borah
 * @param <ActionThought> the kind of thought that represents this action
 *
 */
public interface IActionType<ActionThought extends IThought> extends IConcept {

	/**
	 * Generate an action thought appropriate to this action type
	 * 
	 * @param forGoal
	 * @param forMind
	 * @return
	 */
	public ActionThought generateThought(IGoal forGoal, IMind forMind);

	/**
	 * Return a multiset of all abilities required to do this action, and how many
	 * body parts with this ability are needed
	 */
	public Multiset<IPartAbility> requirePartsWithAbility(IGoal forGoal);

	@Override
	default ConceptType getConceptType() {
		return ConceptType.ACTION;
	}
}
