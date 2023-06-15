package mind.memory;

import mind.Culture;
import mind.Group;
import mind.IHasActor;
import mind.Mind;

public interface IHasKnowledge {

	public IKnowledgeBase getKnowledgeBase();

	default Mind getAsMind() {
		return (Mind) this;
	}

	default Group getAsGroup() {
		return (Group) this;
	}

	default Memory getMindMemory() {
		return (Memory) getKnowledgeBase();
	}

	default Culture getCulture() {
		return (Culture) getKnowledgeBase();
	}

	default boolean isMindMemory() {
		return getKnowledgeBase() instanceof Memory;
	}

	default boolean isCulture() {
		return getKnowledgeBase() instanceof Culture;
	}

	default boolean hasActor() {
		return this instanceof IHasActor;
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

	public long worldTicks();

}
