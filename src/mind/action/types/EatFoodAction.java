package mind.action.types;

import java.util.Collection;
import java.util.Set;

import actor.Actor;
import biology.systems.SystemType;
import mind.ICanAct;
import mind.action.ActionType;
import mind.action.IAction;
import mind.concepts.type.BasicProperties;
import mind.concepts.type.IMeme;
import mind.concepts.type.IProfile;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.goals.ITaskGoal;
import mind.goals.taskgoals.AcquireTaskGoal;
import mind.goals.taskgoals.EatTaskGoal;
import mind.memory.Memory;
import mind.memory.SenseMemory.TraitsMemory;

public class EatFoodAction implements IAction {

	private IMeme foodType;

	private Actor foodItem;

	private String failure = "n/a";
	private boolean succ;

	public EatFoodAction(IMeme foodType) {
		this.foodType = foodType == null ? BasicProperties.FOOD : foodType;
		if (!(this.foodType instanceof IProfile || this.foodType instanceof Property)) {
			throw new IllegalArgumentException("Food type must be property/profile; cannot be " + foodType.getClass());
		}
	}

	@Override
	public Collection<ITaskGoal> genConditionGoal(ICanAct user) {
		if (foodItem == null) {
			return Set.of(new AcquireTaskGoal(foodType));
		}
		return Set.of();
	}

	@Override
	public ActionType<EatFoodAction> getType() {
		return ActionType.EAT;
	}

	public static EatFoodAction genAction(ITaskGoal from) {
		EatTaskGoal goal = (EatTaskGoal) from;

		return new EatFoodAction(goal.getFoodType());
	}

	@Override
	public boolean canExecuteIndividual(ICanAct user, boolean pondering) {

		if (user.hasMindAndMultipartBody()) {
			Actor possible = null;
			Memory memory = user.getMindMemory();
			// MultipartActor actor = (MultipartActor) user.getAsHasActor().getActor();
			TraitsMemory<Actor> selfMemory = memory.getSenses().getTraits(memory.getSelfProfile());
			if (selfMemory == null) {
				failure = "mind cannot perceive itself (?)";
				return false;
			}
			possible = user.getAsHasActor().getActor().getHeld();
			if (possible == null) {
				failure = "nothing held";
				return false;
			}
			Profile possibleProfile = memory.getProfileFor(possible.getUUID());
			if (foodType instanceof Property) {

				if (memory.hasProperty(possibleProfile, (Property) foodType)
						|| ITaskGoal.getProperty(possible, (Property) foodType, user).isPresent()) {

					/*
					 * System.out.println("" + foodType + " " + possible.getName() + " " +
					 * memory.hasProperty(possibleProfile, (Property) foodType) + " " +
					 * ITaskGoal.getProperty(possible, (Property) foodType, user) + " \t" +
					 * memory.getProperties(possibleProfile, (Property) foodType) +
					 * memory.getPropertiesFromCulture(possibleProfile, (Property)
					 * foodType).values() + " \t" + possible.getSocialProperties());
					 */
					foodItem = possible;
					return true;
				} else {
					failure = "no food held";
				}
			} else if (foodType instanceof Profile) {
				if (possibleProfile.equals(foodType)) {
					foodItem = possible;
					return true;
				}
			}
			failure = "no food items held";
			// TODO make a sense based system of food detection, e.g. an octopus with eight
			// arms should be able to eat any of the things in its eight arms
			/*
			 * for (IComponentPart part :
			 * actor.getBody().getPartsWithAbility(Abilities.GRASP)) {
			 * TraitsMemory<Actor>.Traits<Actor> tra = selfMemory.getTraits(part);
			 * 
			 * if (tra == null) { failure = "all body parts unperceivable"; continue; }
			 * Actor possible1 = tra.getTrait(ActorReferentProperty.HOLDING); if (possible1
			 * == null) { failure = "not holding anything"; continue; } possible =
			 * possible1; Profile possibleProfile =
			 * memory.getProfileFor(possible.getUUID()); if (foodType instanceof Property) {
			 * if (memory.hasProperty(possibleProfile, (Property) foodType)) { foodItem =
			 * possible; return true; } } else if (foodType instanceof Profile) { if
			 * (possibleProfile.equals(foodType)) { foodItem = possible; return true; } }
			 * failure = "no food items held";
			 * 
			 * }
			 */
			return false;

		}
		failure = "lack of body(?)";
		return false;
	}

	@Override
	public void beginExecutingIndividual(ICanAct forUser) {
	}

	@Override
	public boolean canContinueExecutingIndividual(ICanAct individual, int tick) {
		return tick < 10;
	}

	@Override
	public boolean finishActionIndividual(ICanAct individual, int tick) {
		succ = individual.getAsHasActor().getActor().getSystem(SystemType.HUNGER).eat(foodItem) == 1;

		return succ;
	}

	@Override
	public String reasonForLastFailure() {
		return failure;
	}

	@Override
	public String toString() {
		return "EatA{" + this.foodType + "}";
	}

}
