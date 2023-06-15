package mind.goals;

public interface IPersonalRelationship extends IGoal {

	@Override
	default Type getGoalType() {
		return Type.COMMUNITY;
	}

}
