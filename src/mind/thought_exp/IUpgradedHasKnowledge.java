package mind.thought_exp;

import mind.IHasActor;
import mind.thought_exp.culture.UpgradedCulture;
import mind.thought_exp.culture.UpgradedGroup;
import mind.thought_exp.memory.IBrainMemory;
import mind.thought_exp.memory.IUpgradedKnowledgeBase;

public interface IUpgradedHasKnowledge {

	public IUpgradedKnowledgeBase getKnowledgeBase();

	default IUpgradedMind getAsMind() {
		return (IUpgradedMind) this;
	}

	default UpgradedGroup getAsGroup() {
		return (UpgradedGroup) this;
	}

	default IBrainMemory getMindMemory() {
		return (IBrainMemory) getKnowledgeBase();
	}

	default UpgradedCulture getCulture() {
		return (UpgradedCulture) getKnowledgeBase();
	}

	default boolean isMind() {
		return this instanceof IUpgradedMind;
	}

	default boolean isMindMemory() {
		return getKnowledgeBase() instanceof IBrainMemory;
	}

	default boolean isCulture() {
		return getKnowledgeBase() instanceof UpgradedCulture;
	}

	default boolean hasActor() {
		return this instanceof IHasActor && this.getAsHasActor().getActor() != null;
	}

	default boolean hasMultipartActor() {
		return this instanceof IHasActor && getAsHasActor().isMultipart();
	}

	default boolean hasMindAndMultipartBody() {
		return isMindMemory() && hasMultipartActor();
	}

	default IHasActor getAsHasActor() {
		return (IHasActor) this;
	}

	public String report();

}
