package civilization_and_minds.mind.action.type;

import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;

import actor.construction.physical.IPartAbility;
import biology.anatomy.BodyAbility;
import civilization_and_minds.mind.IMind;
import civilization_and_minds.mind.action.IActionType;
import civilization_and_minds.mind.goals.IGoal;
import civilization_and_minds.mind.thoughts.IThought;

public class EatActionType<T extends IThought> implements IActionType<T> {

	private String name;
	private static final Multiset<IPartAbility> REQ_PARTS = ImmutableMultiset.of(BodyAbility.EAT);

	public EatActionType(String name) {
		this.name = name;
	}

	@Override
	public String getUniqueName() {
		return "action_" + name;
	}

	@Override
	public T generateThought(IGoal forGoal, IMind forMind) {
		return null;
	}

	@Override
	public Multiset<IPartAbility> requirePartsWithAbility(IGoal forGoal) {
		return REQ_PARTS;
	}

}
