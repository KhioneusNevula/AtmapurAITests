package psych_first.action.types;

import abilities.types.SystemType;
import culture.CulturalContext;
import culture.Culture;
import entity.Actor;
import entity.Eatable;
import psych_first.action.ActionType;
import psych_first.action.goal.Goal;
import psych_first.action.goal.NeedGoal;
import psych_first.action.goal.RequirementGoal;
import psych_first.action.goal.RequirementWrapper;
import psych_first.action.goal.Task;
import psych_first.action.goal.Goal.Priority;
import psych_first.actionstates.checks.Check;
import psych_first.actionstates.checks.numeric.IntCheck;
import psych_first.actionstates.states.StateBuilder;
import psych_first.mind.Mind;
import psych_first.mind.Need;
import psych_first.mind.Will;
import sim.IHasProfile;
import sociology.ProfilePlaceholder;
import sociology.ProfileType;
import sociology.sociocon.Sociocat;
import sociology.sociocon.Socioprops;

public class EatAction extends Action {

	public EatAction() {
		super("eat");
		setActionType(ActionType.MOUTH, ActionType.INTERACTION);
	}

	@Override
	protected String complete(IHasProfile actor, Will will, Task task, Goal reqs, Goal result, int ticksLeft) {
		IHasProfile food = ((RequirementGoal) reqs).getState().getProfile(ProfileType.TARGET).getActualProfile()
				.getOwner();
		switch (ticksLeft) {

		case -1:
			return "acting entity is not able to eat food";
		case -2:
			return "target entity " + food + " is not a food";
		case -3:
			return "target entity " + food + " is too big to eat";
		case -4:
			return "target entity " + food + " has no satiation value";
		}
		return null;
	}

	@Override
	protected int startExecution(IHasProfile ih, Will will, Task task, Goal requirements, Goal result) {

		RequirementGoal goal = (RequirementGoal) requirements;
		ProfilePlaceholder pp = goal.getState().getProfile(ProfileType.TARGET);
		ProfilePlaceholder uu = goal.getState().getProfile(ProfileType.USER);

		if (ih instanceof Actor actor && actor.hasSystem(SystemType.HUNGER)) {
			if (pp.getActualProfile().getOwner() instanceof Eatable ed) {
				int ate = actor.getSystem(SystemType.HUNGER).eat(ed);

				return ate == -1 ? -3 : (ate == 0 ? -4 : 0);
			}
			return -2;
		}
		return -1;

	}

	@Override
	public RequirementWrapper generateRequirements(Mind fromMind, Goal goal, StateBuilder builder) {
		if (!fromMind.getCulture().containsInHierarchy(Culture.ORGANIC)) {
			return null;
		}

		if (goal instanceof NeedGoal && ((NeedGoal) goal).getFocus() == Need.SATIATION) {

			RequirementGoal foodGoal = builder
					.addProfileMatchingCondition(ProfileType.USER, Socioprops.ACTOR_HELD, ProfileType.TARGET)
					.addConditions(ProfileType.TARGET, Check.createSociocatCheck(Sociocat.FOOD, true),
							Check.checkIfKnown())
					.addConditions(ProfileType.TARGET, new IntCheck<>(Socioprops.FOOD_NOURISHMENT, 1, null))
					.buildGoal(Priority.NECESSITY);

			RequirementWrapper reqs = RequirementWrapper.create(foodGoal);
			return reqs;
		}
		return null;
	}

	@Override
	public String interpretExecutionProblem(Goal requirements, Goal result, Object execInfo, CulturalContext ctxt) {
		if (requirements instanceof RequirementGoal goal) {
			StringBuilder cons = new StringBuilder("|");

			if (!doesPlaceholderSatisfy(requirements, ProfileType.USER, Socioprops.ACTOR_HELD, ctxt)) {
				cons.append("not holding a food item|");
			} else {

				if (goal.getState().getFor(ProfileType.TARGET).getCondition(Socioprops.FOOD_NOURISHMENT).satisfies(
						goal.getState().getProfile(ProfileType.TARGET).getActualProfile(), ctxt) != Boolean.TRUE) {
					cons.append("food item invalid|");
				}

			}

			return cons.toString();
		}
		return super.interpretExecutionProblem(requirements, result, execInfo, ctxt);
	}

}
