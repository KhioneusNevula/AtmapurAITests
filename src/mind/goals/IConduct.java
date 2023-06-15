package mind.goals;

import mind.relationships.Role;

public interface IConduct extends IGoal {

	@Override
	default Type getGoalType() {
		return Type.CONDUCT;
	}

	/**
	 * A conduct is by nature the adoption of a specific role; this is that role
	 * 
	 * @return
	 */
	public Role getRequiredRole();

	@Override
	default boolean individualGoal() {
		return true;
	}

	@Override
	default boolean societalGoal() {
		return false;
	}
}
