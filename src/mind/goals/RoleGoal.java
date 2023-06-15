package mind.goals;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import mind.memory.IHasKnowledge;

public class RoleGoal implements IRoleGoal {

	private Set<ITaskGoal> tasks;
	private String name;
	private boolean complete;

	public RoleGoal(ITaskGoal... tasks) {
		this.tasks = ImmutableSet.copyOf(tasks);
	}

	public RoleGoal(Iterable<? extends ITaskGoal> tasks) {
		this.tasks = ImmutableSet.copyOf(tasks);
	}

	private void generateName() {
		// generates a name for this role TODO
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
	public boolean isComplete(IHasKnowledge entity) {
		return complete;
	}

	@Override
	public boolean equivalent(IGoal other) {
		return IRoleGoal.super.equivalent(other);
	}
}