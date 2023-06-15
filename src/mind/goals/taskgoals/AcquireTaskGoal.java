package mind.goals.taskgoals;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import actor.Actor;
import mind.concepts.type.IConcept;
import mind.concepts.type.IProfile;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.goals.IGoal;
import mind.goals.ITaskGoal;
import mind.goals.ITaskHint;
import mind.goals.TaskHint;
import mind.memory.IHasKnowledge;

public class AcquireTaskGoal implements ITaskGoal {

	private IConcept item;
	private IProfile target;
	private Set<IConcept> productSet;

	public AcquireTaskGoal(IConcept item) {
		this(item, IProfile.SELF);
	}

	public AcquireTaskGoal(IConcept item, IProfile target) {
		if (!(item instanceof Property || item instanceof Profile))
			throw new IllegalArgumentException("" + item.getClass());
		this.item = item;
		this.target = target;
		this.productSet = ImmutableSet.of(item);
	}

	@Override
	public ITaskHint getActionHint() {
		return TaskHint.ACQUIRE;
	}

	@Override
	public boolean isComplete(IHasKnowledge entity) {
		if (item instanceof Property)
			return ((entity.getAsHasActor().getActor()).getPossessed().stream()
					.anyMatch((a) -> ITaskGoal.getProperty(a, (Property) item, entity) != null));
		if (item instanceof Profile) {
			return ((entity.getAsHasActor().getActor()).getPossessed().stream()
					.anyMatch((a) -> a.getUUID().equals(((Profile) item).getUUID())));
		}
		return false;
	}

	public IConcept getItem() {
		return item;
	}

	@Override
	public Collection<IConcept> producedItem() {
		return productSet;
	}

	@Override
	public IProfile getTarget() {
		return target;
	}

	@Override
	public IConcept transferTarget() {
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
		return "AcquireGoal{" + this.item + "}";
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
