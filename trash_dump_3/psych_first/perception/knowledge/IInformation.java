package psych_first.perception.knowledge;

import psych_first.perception.knowledge.events.EventType;
import psych_first.perception.knowledge.events.IEvent;

public interface IInformation extends Cloneable {

	public default boolean isTraitNode() {
		return this instanceof TraitNode;
	}

	public default <T> boolean isTraitNode(IKnowledgeType<T> type) {
		return this instanceof TraitNode t && t.getType().equals(type);
	}

	public default <T> TraitNode<T> asTraitNode(IKnowledgeType<T> type) {
		return this.isTraitNode(type) ? (TraitNode<T>) this : null;
	}

	public default boolean isOccurrence() {
		return this instanceof IOccurrence;
	}

	public default <T extends IOccurrence> T asOccurrence() {
		return this.isOccurrence() ? (T) this : null;
	}

	public default boolean isEvent() {
		return this instanceof IEvent;
	}

	public default <T extends IEvent> T asEvent() {
		return this.isEvent() ? (T) this : null;
	}

	public default boolean isIdentity() {
		return this instanceof Identity;
	}

	public default Identity asIdentity() {
		return this.isIdentity() ? (Identity) this : null;

	}

	public default boolean isEventType() {
		return this instanceof EventType;
	}

	public default <T extends IEvent> EventType<T> asEventType() {
		return this.isEventType() ? (EventType<T>) this : null;
	}
}
