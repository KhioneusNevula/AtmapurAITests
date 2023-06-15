package mind.goals.taskgoals;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import biology.systems.ISystemHolder;
import biology.systems.SystemType;
import mind.concepts.type.BasicProperties;
import mind.concepts.type.IConcept;
import mind.concepts.type.IProfile;
import mind.goals.IGoal;
import mind.goals.ITaskGoal;
import mind.goals.ITaskHint;
import mind.goals.TaskHint;
import mind.memory.IHasKnowledge;

/**
 * The Task goal for eating to sustain
 * 
 * @author borah
 *
 */
public class EatTaskGoal implements ITaskGoal {

	private IConcept foodType;
	private ImmutableSet<IConcept> foodSet;
	private IProfile target;
	public static final double DEFAULT_PERCENT = 0.4;
	private double percent = DEFAULT_PERCENT;

	/**
	 * optional food type or specific food target; can be null
	 * 
	 * @param food
	 */
	public EatTaskGoal(IConcept foodType, double percent) {
		this.foodType = foodType == null ? BasicProperties.FOOD : null;
		this.target = IProfile.SELF;
		this.foodSet = ImmutableSet.of(this.foodType);
		this.percent = percent;
	}

	/**
	 * How much hunger should be satiated
	 * 
	 * @return
	 */
	public double getRequiredPercent() {
		return percent;
	}

	@Override
	public ITaskHint getActionHint() {
		return TaskHint.CONSUME;
	}

	public IConcept getFoodType() {
		return foodType;
	}

	@Override
	public IProfile getTarget() {
		return target;
	}

	@Override
	public Set<IConcept> useTarget() {
		return foodSet;
	}

	@Override
	public boolean isComplete(IHasKnowledge entity) {
		return (((ISystemHolder) entity.getAsHasActor().getActor()).getSystem(SystemType.HUNGER)
				.getPercent() > percent);
	}

	@Override
	public boolean individualGoal() {
		return true;
	}

	@Override
	public boolean societalGoal() {
		return false;
	}

	@Override
	public boolean isInvalid(IHasKnowledge knower) {
		return knower.hasActor() ? !(knower.getAsHasActor().getActor() instanceof ISystemHolder) : true;
	}

	@Override
	public String toString() {
		return "EatGoal" + (foodType != BasicProperties.FOOD ? "{" + this.foodType + "}" : "");
	}

	@Override
	public String getUniqueName() {
		return "goal_task_eat" + (foodType != BasicProperties.FOOD ? "_" + this.foodType : "");
	}

	@Override
	public boolean equivalent(IGoal other) {
		return ITaskGoal.super.equivalent(other) && other instanceof EatTaskGoal && (((EatTaskGoal) other).foodType
				.equals(this.foodType)
				|| (((EatTaskGoal) other).foodType == BasicProperties.FOOD && this.foodType != BasicProperties.FOOD));
	}

}
