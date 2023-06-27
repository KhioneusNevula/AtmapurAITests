package mind.action;

public enum InteractionType implements IInteractionType {
	CONVERSATION, ITEM_TRANSFER;

	@Override
	public boolean isGenerated() {
		return false;
	}
}
