package mind.memory;

import mind.Culture;
import mind.Group;
import mind.IHasActor;
import mind.IIndividualMind;
import mind.linguistics.NameWord;

public interface IHasKnowledge {

	public IKnowledgeBase getKnowledgeBase();

	default IIndividualMind getAsMind() {
		return (IIndividualMind) this;
	}

	default Group getAsGroup() {
		return (Group) this;
	}

	default IMindMemory getMindMemory() {
		return (IMindMemory) getKnowledgeBase();
	}

	default Culture getCulture() {
		return (Culture) getKnowledgeBase();
	}

	default boolean isMind() {
		return this instanceof IIndividualMind;
	}

	default boolean isMindMemory() {
		return getKnowledgeBase() instanceof IMindMemory;
	}

	default boolean isCulture() {
		return getKnowledgeBase() instanceof Culture;
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

	public NameWord getNameWord();

	public void kill();

}
