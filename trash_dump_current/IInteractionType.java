package mind.action;

public interface IInteractionType {

	public String name();

	/**
	 * whether this type represents a procedurally generated interaction
	 * 
	 * @return
	 */
	public boolean isGenerated();
}
