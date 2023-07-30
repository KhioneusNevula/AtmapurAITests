package mind.goals.taskgoals;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import actor.Actor;
import mind.concepts.type.IMeme;
import mind.concepts.type.IProfile;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.goals.IGoal;
import mind.goals.ITaskGoal;
import mind.goals.ITaskHint;
import mind.goals.TaskHint;
import mind.memory.IHasKnowledge;

public class AcquireTaskGoal implements ITaskGoal {

	private IMeme item;
	private IProfile beneficiary;
	private Set<IMeme> productSet;
	private Priority priority = Priority.NORMAL;

	public AcquireTaskGoal(IMeme item) {
		this(item, IProfile.SELF);
	}

	public AcquireTaskGoal(IMeme item, IProfile beneficiary) {
		if (!(item instanceof Property || item instanceof Profile))
			throw new IllegalArgumentException("" + item.getClass());
		this.item = item;
		this.beneficiary = beneficiary;
		this.productSet = ImmutableSet.of(item);
	}

	public AcquireTaskGoal setPriority(Priority priority) {
		this.priority = priority;
		return this;
	}

	public Priority getPriority() {
		return priority;
	}

	@Override
	public ITaskHint getActionHint() {
		return TaskHint.ACQUIRE;
	}

	@Override
	public boolean isComplete(IHasKnowledge entity) {
		if (item instanceof Property)
			return ((entity.getAsHasActor().getActor()).getPossessed().stream()
					.anyMatch((a) -> ITaskGoal.getProperty(a, (Property) item, entity).isPresent()));
		if (item instanceof Profile) {
			return ((entity.getAsHasActor().getActor()).getPossessed().stream()
					.anyMatch((a) -> a.getUUID().equals(((Profile) item).getUUID())));
		}
		return false;
	}

	public IMeme getItem() {
		return item;
	}

	@Override
	public Collection<IMeme> producedItem() {
		return productSet;
	}

	@Override
	public IProfile beneficiary() {
		return beneficiary;
	}

	@Override
	public IMeme transferItem() {
		return this.item;
	}

	@Override
	public boolean societalGoal() {
		return true;
	}

	@Override
	public boolean individualGoal() {
		return true;
	}

	@Override
	public boolean isInvalid(IHasKnowledge knower) {

		return !(knower instanceof Actor);
	}

	@Override
	public String toString() {
		return "AcquireTG{" + this.item + "}";
	}

	@Override
	public String getUniqueName() {
		return "goal_task_acquire_" + this.item.getUniqueName();
	}

	@Override
	public boolean equivalent(IGoal other) {
		return ITaskGoal.super.equivalent(other) && other instanceof AcquireTaskGoal
				&& ((AcquireTaskGoal) other).item.equals(this.item);
	}

}
