package mind.goals;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import mind.thought_exp.IUpgradedHasKnowledge;

public class RoleGoal implements IRoleGoal {

	private Set<ITaskGoal> tasks;
	private String name;
	private boolean complete;
	private Priority priority = Priority.NORMAL;

	public RoleGoal(ITaskGoal... tasks) {
		this.tasks = ImmutableSet.copyOf(tasks);
	}

	public RoleGoal(Iterable<? extends ITaskGoal> tasks) {
		this.tasks = ImmutableSet.copyOf(tasks);
	}

	@Override
	public Priority getPriority() {
		return priority;
	}

	public RoleGoal setPriority(Priority priority) {
		this.priority = priority;
		return this;
	}

	private void generateName() {
		// TODO generates a name for this role
	}

	@Override
	public Set<ITaskGoal> getRequiredTasks() {
		return tasks;
	}

	@Override
	public String toString() {
		return "RoleGoal" + tasks;
	}

	@Override
	public String getUniqueName() {
		return "goal_role_" + name;
	}

	@Override
	public boolean isComplete(IUpgradedHasKnowledge entity) {
		return complete;
	}

	@Override
	public boolean equivalent(IGoal other) {
		return IRoleGoal.super.equivalent(other);
	}
}
