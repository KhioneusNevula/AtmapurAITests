package mind.thought_exp.memory.type;

import mind.concepts.type.IProfile;
import mind.thought_exp.IUpgradedMind;
import mind.thought_exp.memory.IBrainMemory;

public class LearnProfileMemory extends AbstractMemory {

	private IProfile profile;
	float uniqueness;

	public LearnProfileMemory(IProfile profile, float uniqueness) {
		this.profile = profile;
		this.uniqueness = uniqueness;
	}

	@Override
	public boolean applyMemoryEffects(IBrainMemory toMind) {
		toMind.learnProfile(profile);
		return true;
	}

	@Override
	public void forgetMemoryEffects(IUpgradedMind toMind) {
		if (Math.random() > uniqueness && !toMind.hasRelationshipsWith(profile)) {
			toMind.getKnowledgeBase().forgetProfile(profile);
			toMind.getKnowledgeBase().deepForgetProfile(profile);
		}
	}

	@Override
	public MemoryCategory getType() {
		return MemoryCategory.REMEMBER_PROFILE;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LearnProfileMemory lpm)
			return this.profile.equals(lpm.profile);
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return profile.hashCode() + Float.hashCode(uniqueness);
	}

	@Override
	public String toString() {
		return "learn-profile(" + this.profile + ",u:" + uniqueness + ")";
	}

}
