package mind.thought_exp.memory.type;

import java.util.Collections;
import java.util.Map;

import mind.concepts.relations.ConceptRelationType;
import mind.concepts.type.ILocationMeme;
import mind.concepts.type.IMeme;
import mind.concepts.type.IProfile;
import mind.thought_exp.IUpgradedMind;
import mind.thought_exp.memory.IBrainMemory;

public class RememberLocationMemory extends RelationMemory {

	private IMeme location;

	public RememberLocationMemory(IMeme forThing, IProfile location) {
		super(forThing, location, Map.of(ConceptRelationType.FOUND_AT, Collections.emptySet()));
		this.location = location;
	}

	public RememberLocationMemory(IMeme forThing, ILocationMeme location) {
		super(forThing, location, Map.of(ConceptRelationType.FOUND_AT, Collections.emptySet()));
		this.setTopic(forThing);
		this.location = location;
	}

	public IMeme getLocation() {
		return location;
	}

	public IProfile getLocationAsProfile() {
		return (IProfile) location;
	}

	public ILocationMeme getLocationAsLocation() {
		return (ILocationMeme) location;
	}

	/**
	 * Whether the given location is a profile
	 * 
	 * @return
	 */
	public boolean isLocationProfile() {
		return this.location instanceof IProfile;
	}

	/**
	 * Whether the given location is a coordinate
	 * 
	 * @return
	 */
	public boolean isLocationCoordinates() {
		return this.location instanceof ILocationMeme;
	}

	@Override
	public boolean applyMemoryEffects(IBrainMemory toMind) {
		toMind.forgetAllRelationsOfType(this.getTopic(), ConceptRelationType.FOUND_AT);
		boolean a = super.applyMemoryEffects(toMind);
		return a;
	}

	@Override
	public void forgetMemoryEffects(IUpgradedMind toMind) {
		toMind.getKnowledgeBase().forgetRelation(this.getTopic(), location, ConceptRelationType.FOUND_AT);
	}

}
