package mind.thought_exp.actions;

import actor.Actor;
import actor.IUniqueExistence;
import biology.systems.SystemType;
import mind.action.ActionType;
import mind.action.IActionType;
import mind.concepts.type.BasicProperties;
import mind.concepts.type.IMeme;
import mind.concepts.type.IProfile;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.goals.ITaskGoal;
import mind.goals.taskgoals.AcquireTaskGoal;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;
import mind.thought_exp.IThoughtMemory.MemoryCategory;
import mind.thought_exp.ThoughtType;
import mind.thought_exp.info_thoughts.CheckHeldItemsThought;
import mind.thought_exp.memory.type.ImportantWorldObjectsMemory;
import mind.thought_exp.memory.type.MemoryWrapper;

public class EatActionThought extends AbstractActionThought {

	private IMeme foodType;

	private boolean needFoodItem;

	private IUniqueExistence foodItem;

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
		if (!started()) {
			if (this.needFoodItem) {
				if (this.getPendingCondition(memory) == null) {
					this.postConditionForExecution(new AcquireTaskGoal(foodType));
					this.needFoodItem = false;

				}
			} else if (foodItem == null && this.childThoughts(ThoughtType.FIND_MEMORY_INFO).isEmpty()) {
				if (this.oughtToCheckShortTermMemories()) {
					for (MemoryWrapper mw : memory.getMindMemory()
							.getShortTermMemoriesOfType(MemoryCategory.REMEMBER_FOR_PURPOSE)) {
						if (mw.getMemory() instanceof ImportantWorldObjectsMemory iwom) {
							Property prop = iwom.getProperty();
							if (prop.equals(foodType)) { /*
															 * memory.getKnowledgeBase().isConceptSubtype( prop,
															 * BasicProperties.FOOD)) {
															 */
								// TODO check subtype properties or something idk
								foodItem = iwom.getObjects().iterator().next();
							} else if (foodType instanceof IProfile ip) {
								for (IUniqueExistence ex : iwom.getObjects()) {
									if (ex.getUUID().equals(ip.getUUID())) {
										foodItem = ex;
										break;
									}
								}
							}
						}
					}
				}
				if (foodItem == null) {
					this.postChildThought(
							foodType instanceof Profile ? new CheckHeldItemsThought((Profile) foodType, this.getGoal())
									: new CheckHeldItemsThought((Property) foodType, this.getGoal()),
							ticks);
					this.markAddedShortTermMemories();
				}
			}
			if (foodItem != null)
				this.markAsReadyToExecute();
		} else {
		}
	}

	@Override
	public boolean canExecuteIndividual(ICanThink user, int thoughtTicks, long worldTicks) {
		return foodItem != null;
	}

	@Override
	public void beginExecutingIndividual(ICanThink forUser, int thoughtTicks, long worldTicks) {

		if (foodItem == null) {
			for (MemoryWrapper mw : forUser.getMindMemory().getShortTermMemoriesOfType(MemoryCategory.RECENT_ACTION)) {
				if (mw.getMemory() instanceof EatActionThought eat && eat.isPondering() && eat.succeeded()) {
					this.foodItem = eat.foodItem;
					break;
				}
			}
		}
		Actor owner = forUser.getAsHasActor().getActor();
		if (foodItem instanceof Actor ac && forUser.getAsHasActor().getActor().reachable(ac)) {
			int numero = owner.getSystem(SystemType.HUNGER).eat(ac);
			succeeded = (numero == 1);
		} else {
			succeeded = false; // TODO add some kind of strangeness ability to thoughts to indicate that things
								// aren't making sense
			System.out.println(forUser + " tried to grab food " + foodItem + " but failed");
		}
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
	public IActionType<?> getActionType() {
		return ActionType.EAT;
	}

	@Override
	public boolean isLightweight() {
		return false;
	}

	@Override
	public void getInfoFromChild(ICanThink mind, IThought childThought, boolean interrupted, int ticks) {
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
