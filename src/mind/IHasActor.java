package mind;

import actor.Actor;
import actor.MultipartActor;

public interface IHasActor {

	public Actor getActor();

	default boolean isMultipart() {
		return getActor() instanceof MultipartActor;
	}

	default <T extends MultipartActor> T getActorAsMultipart() {
		return (T) getActor();
	}
}
