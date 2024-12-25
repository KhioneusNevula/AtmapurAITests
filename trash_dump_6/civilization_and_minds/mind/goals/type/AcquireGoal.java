package civilization_and_minds.mind.goals.type;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import com.google.common.collect.ImmutableSet;

import actor.Actor;
import actor.construction.properties.ISensableTrait;
import actor.construction.properties.SenseProperty;
import civilization_and_minds.IDirective;
import civilization_and_minds.mind.IMind;
import civilization_and_minds.mind.goals.Necessity;
import civilization_and_minds.mind.memories.type.GoalMemory.CompletionState;
import civilization_and_minds.mind.thoughts.IThought;
import civilization_and_minds.social.concepts.IConcept;
import civilization_and_minds.social.concepts.profile.Profile;
import civilization_and_minds.social.concepts.property.ConceptProperty;
import civilization_and_minds.social.concepts.relation.ISensePropertyRelationType.HasSensePropertyRelationType;
import civilization_and_minds.social.concepts.relation.RelationType;

public class AcquireGoal extends AbstractGoal {

	public static class AcquireCheckerThought implements IThought {

		private boolean complete;
		private AcquireGoal goal;

		public AcquireCheckerThought(AcquireGoal goal) {
			this.goal = goal;
		}

		@Override
		public boolean isComplete(IMind mind, long ticks) {
			return complete;
		}

		@Override
		public void runTick(IMind mind, long ticks) {
			Actor a = mind.getContainingSoul().getContainerEntity();
			CompletionState com = CompletionState.INCOMPLETE;
			if (a == null || !a.getPhysical().getProfile().equals(goal.patient.iterator().next())) { // if no body, or
																										// if
				com = CompletionState.IMPOSSIBLE;
			} else {
				if (goal.atype instanceof Profile prof) {
					if (a.getPhysical().getHeld().stream()
							.anyMatch((held) -> held.getPhysical().getProfile().equals(prof))) {
						com = CompletionState.COMPLETE;
					}
				} else if (goal.atype.isProperty()) {
					Iterator<Profile> pros;
					if (goal.property == null) { // if this is a conceptproperty
						pros = mind.getKnowledge().getConnectedConcepts(goal.atype, RelationType.IS_PROPERTY_OF, true)
								.filter((p) -> p instanceof Profile).map((p) -> (Profile) p).iterator();
					} else { // if this is a sensable property
						pros = mind.getKnowledge()
								.getConnectedConcepts(goal.atype, HasSensePropertyRelationType.of(goal.property), true)
								.filter((p) -> p instanceof Profile).map((p) -> (Profile) p).iterator();
					}
					while (pros.hasNext()) {
						Profile pro = pros.next();
						if (a.getPhysical().getHeld().stream()
								.anyMatch((held) -> held.getPhysical().getProfile().equals(pro))) {
							com = CompletionState.COMPLETE;
						}
					}

				}
			}
			complete = true;
		}

		@Override
		public boolean checksGoals() {
			return true;
		}

	}

	private Collection<IConcept> acquireTypes;
	private IConcept atype;
	private SenseProperty<?> property;

	/**
	 * Acquire something with a specific sensable property
	 * 
	 * @param <T>
	 * @param severity
	 * @param patient
	 * @param property
	 * @param trait
	 */
	public <T extends ISensableTrait> AcquireGoal(Necessity severity, Profile forProfile, SenseProperty<T> property,
			T trait) {
		super(severity, Collections.singleton(forProfile), IntentionType.ACQUIRE);
		this.acquireTypes = ImmutableSet.of(trait);
		this.atype = trait;
		this.property = property;
	}

	/**
	 * Acquire this specific item
	 * 
	 * @param nec
	 * @param patient
	 * @param item
	 */
	public AcquireGoal(Necessity nec, Profile patient, Profile item) {
		this(nec, patient, (IConcept) item);
	}

	/**
	 * Acquire something with a specific concept property
	 * 
	 * @param severity
	 * @param patient
	 * @param acquireType
	 */
	public AcquireGoal(Necessity nec, Profile patient, ConceptProperty property) {
		this(nec, patient, (IConcept) property);
	}

	private AcquireGoal(Necessity severity, Profile patient, IConcept acquireType) {
		super(severity, Collections.singleton(patient), IntentionType.ACQUIRE);
		this.acquireTypes = ImmutableSet.of(acquireType);
		this.atype = acquireType;
	}

	@Override
	public Profile getBeneficiary() {
		return null;
	}

	@Override
	public Collection<? extends IConcept> getTheme() {
		return acquireTypes;
	}

	@Override
	public IDirective<?> makeGoalChecker() {
		return new AcquireCheckerThought(this);
	}

	@Override
	public String getUniqueName() {
		return "goal_acquire_" + this.acquireTypes;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AcquireGoal ag) {
			return this.acquireTypes.equals(ag.acquireTypes) && this.patient.equals(ag.patient)
					&& this.severity == ag.severity
					&& (this.property != null ? this.property.equals(ag.property) : true);
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return this.acquireTypes.hashCode() + this.patient.hashCode() + this.severity.hashCode()
				+ (this.property != null ? this.property.hashCode() : 0);
	}

}
