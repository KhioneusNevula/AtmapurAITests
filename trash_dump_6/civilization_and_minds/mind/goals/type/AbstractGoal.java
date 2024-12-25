package civilization_and_minds.mind.goals.type;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import civilization_and_minds.mind.goals.IGoal;
import civilization_and_minds.mind.goals.Necessity;
import civilization_and_minds.social.concepts.profile.Profile;

public abstract class AbstractGoal implements IGoal {

	protected Necessity severity;
	protected Set<IntentionType> intentions;
	protected Collection<Profile> patient;

	public AbstractGoal(Necessity severity, Collection<Profile> patient, IntentionType... intentions) {
		this.severity = severity;
		this.intentions = ImmutableSet.copyOf(intentions);
		this.patient = ImmutableSet.copyOf(patient);
	}

	@Override
	public ConceptType getConceptType() {
		return ConceptType.GOAL;
	}

	@Override
	public Collection<Profile> getPatient() {
		return this.patient;
	}

	@Override
	public Collection<IntentionType> getGoalIntentionTypes() {
		return intentions;
	}

	@Override
	public Necessity getNecessity() {
		return this.severity;
	}

}
