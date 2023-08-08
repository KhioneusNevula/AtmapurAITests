package mind.thought_exp.actions;

import actor.Actor;
import biology.systems.SystemType;
import mind.action.ActionType;
import mind.action.IActionType;
import mind.concepts.type.BasicProperties;
import mind.concepts.type.IMeme;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.goals.ITaskGoal;
import mind.goals.taskgoals.AcquireTaskGoal;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;
import mind.thought_exp.ThoughtType;
import mind.thought_exp.info_thoughts.CheckHeldItemsThought;

public class EatActionThought extends AbstractActionThought {

	private IMeme foodType;

	private boolean needFoodItem;

	private Actor foodItem;
	private boolean succeeded;

	public EatActionThought(ITaskGoal goal) {
		super(goal);
		IMeme foodType = goal.usedItem().stream().findAny()
				.orElseThrow(() -> new IllegalArgumentException(goal + " lacks food type"));
		this.foodType = foodType == null ? BasicProperties.FOOD : foodType;
		if (!(this.foodType instanceof Profile || this.foodType instanceof Property)) {
			throw new IllegalArgumentException("Food type must be property/profile; cannot be " + foodType.getClass());
		}
	}

	@Override
	public void startThinking(ICanThink memory, long worldTick) {
	}

	@Override
	public void thinkTick(ICanThink memory, int ticks, long worldTick) {
		if (!this.started()) {
			if (foodItem == null && this.childThoughts(ThoughtType.FIND_MEMORY_INFO).isEmpty()
					&& ticks % 5 >= memory.rand().nextInt(5)) {
				this.postChildThought(foodType instanceof Profile ? new CheckHeldItemsThought((Profile) foodType)
						: new CheckHeldItemsThought((Property) foodType), ticks);
			}
			if (this.needFoodItem) {
				if (this.getPendingCondition(memory) == null) {
					this.postConditionForExecution(new AcquireTaskGoal(foodType));
					this.needFoodItem = false;
				}
			}
		}
	}

	@Override
	public boolean canExecuteIndividual(ICanThink user, int thoughtTicks, long worldTicks) {
		if (foodItem != null) {
			return foodItem != null;
		}
		return false;
	}

	@Override
	public void beginExecutingIndividual(ICanThink forUser, int thoughtTicks, long worldTicks) {
		Actor owner = forUser.getAsHasActor().getActor();
		int numero = owner.getSystem(SystemType.HUNGER).eat(foodItem);
		succeeded = numero == 1;
	}

	@Override
	public boolean canContinueExecutingIndividual(ICanThink individual, int actionTick, int thoughtTick) {
		return false;
	}

	@Override
	public void executionTickIndividual(ICanThink individual, int actionTick, int thoughtTick) {

	}

	@Override
	public boolean finishActionIndividual(ICanThink individual, int actionTick, int thoughtTick, boolean interruption) {
		return succeeded;
	}

	@Override
	public IActionType<?> getType() {
		return ActionType.EAT;
	}

	@Override
	public boolean isLightweight() {
		return false;
	}

	@Override
	public void getInfoFromChild(IThought childThought, boolean interrupted, int ticks) {
		if (childThought instanceof CheckHeldItemsThought chifpt) {
			if (!chifpt.getInformation().isEmpty()) {
				this.foodItem = chifpt.getInformation().iterator().next();
			} else {
				this.setFailureReason(chifpt.getFailureReason());
				this.needFoodItem = true;
			}
		}
	}

	@Override
	public boolean equivalent(IThought other) {
		if (other instanceof EatActionThought eat) {
			return eat.foodType.equals(eat.foodType);
		}
		return this.equals(other);
	}

}
