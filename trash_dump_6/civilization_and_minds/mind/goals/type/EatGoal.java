package civilization_and_minds.mind.goals.type;

import java.util.Collection;
import java.util.Collections;

import actor.Actor;
import biology.systems.SystemType;
import civilization_and_minds.mind.IMind;
import civilization_and_minds.mind.goals.IGoal;
import civilization_and_minds.mind.goals.Necessity;
import civilization_and_minds.mind.memories.IMemoryItem.Section;
import civilization_and_minds.mind.memories.MemoryWrapper;
import civilization_and_minds.mind.memories.type.GoalMemory;
import civilization_and_minds.mind.memories.type.GoalMemory.CompletionState;
import civilization_and_minds.mind.thoughts.IThought;
import civilization_and_minds.social.concepts.profile.Profile;
import civilization_and_minds.social.concepts.property.ConceptProperty;

public class EatGoal extends AbstractGoal {

	public static class HungerCheckerThought implements IThought {

		private boolean complete;

		private IGoal goal;

		public HungerCheckerThought(IGoal goal) {
			this.goal = goal;
		}

		@Override
		public boolean isComplete(IMind mind, long ticks) {
			return complete;
		}

		@Override
		public boolean checksGoals() {
			return true;
		}

		@Override
		public void runTick(IMind mind, long ticks) {
			CompletionState condition = CompletionState.INCOMPLETE;
			Actor container = mind.getContainingSoul().getContainerEntity();
			if (container != null && container.hasSystem(SystemType.HUNGER)) {
				if (container.getSystem(SystemType.HUNGER).getSeverity().ordinal() < goal.getNecessity().ordinal()) {
					condition = CompletionState.COMPLETE;
				}
			} else {
				condition = CompletionState.IMPOSSIBLE;
			}
			mind.getKnowledge().pushMemory(Section.SHORT_TERM,
					MemoryWrapper.of(new GoalMemory(goal, condition), ticks));
			complete = true;
		}

	}

	/**
	 * 
	 * @param severity
	 * @param patient  those who are to be fed
	 * @param types    acceptable kinds of food; if empty, that means to just accept
	 *                 any kind of food consumable by given patients
	 */
	public EatGoal(Necessity severity, Collection<Profile> patient) {
		super(severity, patient, IntentionType.SATIATION);
	}

	@Override
	public Profile getBeneficiary() {
		return null;
	}

	@Override
	public Collection<ConceptProperty> getTheme() {
		return Collections.<ConceptProperty>emptySet();
	}

	@Override
	public HungerCheckerThought makeGoalChecker() {
		return new HungerCheckerThought(this);
	}

	@Override
	public String getUniqueName() {
		return "goal_eat_" + this.severity.name().toLowerCase();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EatGoal eg) {
			return this.patient.equals(eg.patient) && this.severity == eg.severity;
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return this.patient.hashCode() + this.severity.hashCode();
	}

}
