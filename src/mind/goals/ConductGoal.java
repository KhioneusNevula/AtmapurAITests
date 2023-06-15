package mind.goals;

import mind.memory.IHasKnowledge;
import mind.relationships.Role;

public class ConductGoal implements IConduct {

	private Role requiredRole;

	public ConductGoal(Role requiredRole) {
		this.requiredRole = requiredRole;
	}

	@Override
	public Role getRequiredRole() {
		return requiredRole;
	}

	@Override
	public String toString() {
		return "ConductGoal{" + this.requiredRole + "}";
	}

	@Override
	public String getUniqueName() {
		return "goal_conduct_" + this.requiredRole;
	}

	@Override
	public boolean isComplete(IHasKnowledge entity) {
		return false;
	}

	@Override
	public boolean equivalent(IGoal other) {
		return IConduct.super.equivalent(other);
	}

}
