package mind;

import mind.relationships.IParty;

public interface IGroup extends IParty {
	/**
	 * roles probably don't need to track their own members; rather, the members
	 * track their own membership to the role, right
	 */
	/*
	 * public Collection<? extends IParty> members();
	 * 
	 */
	public int memberCount();

	/**
	 * Whether this party is a member of this group
	 * 
	 * @param other
	 * @return
	 */
	public boolean isMember(IParty other);

	@Override
	default boolean isGroup() {
		return true;
	}

	@Override
	default boolean isIndividual() {
		return false;
	}

}
