package mind.goals;

/**
 * A goal causing a recipe/blueprint to shift to accommodate a need
 * 
 * @author borah
 *
 */
public interface IRecipeShiftGoal extends IGoal {

	@Override
	default Type getGoalType() {
		return Type.STRUCTURE;
	}

	@Override
	default boolean individualGoal() {
		return false;
	}

	@Override
	default boolean societalGoal() {
		return true;
	}
}
