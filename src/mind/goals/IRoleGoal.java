package mind.goals;

import java.util.Set;

/**
 * TODO goal that creates a role
 * 
 * @author borah
 *
 */
public interface IRoleGoal extends IGoal {

	@Override
	default Type getGoalType() {
		return Type.STRUCTURE;
	}

	public Set<ITaskGoal> getRequiredTasks();
	/*
	 * public SelectionMethod getSelectionType();
	 */

	@Override
	default boolean individualGoal() {
		return false;
	}

	@Override
	default boolean societalGoal() {
		return true;
	}

}
