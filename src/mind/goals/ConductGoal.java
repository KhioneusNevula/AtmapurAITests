package mind.goals;

import mind.thought_exp.IUpgradedHasKnowledge;
import mind.relationships.Role;

public class ConductGoal implements IConduct {

	private Role requiredRole;
	private Priority priority = Priority.NORMAL;

	public ConductGoal(Role requiredRole) {
		this.requiredRole = requiredRole;
	}

	@Override
	public Role getRequiredRole() {
		return requiredRole;
	}

	public ConductGoal setPriority(Priority priority) {
		this.priority = priority;
		return this;
	}

	@Override
	public Priority getPriority() {
		return priority;
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
	public boolean isComplete(IUpgradedHasKnowledge entity) {
		return false;
	}

	@Override
	public boolean equivalent(IGoal other) {
		return IConduct.super.equivalent(other);
	}

}
