package psychology;

import java.util.UUID;

import psychology.memory.associations.MemeAssociationMemory;
import psychology.memory.associations.ProfileAssociationMemory;
import sim.interfaces.ICanHaveSoul;

/**
 * Mind of an individual. The Soul will consist of the identity of the
 * individual
 * 
 * @author borah
 *
 */
public class Soul implements IHasKnowledge {

	private ICanHaveSoul owner;
	private UUID naturalOwnerID;
	private MemeAssociationMemory associationMemory;
	private ProfileAssociationMemory profileMemory;

	public Soul(ICanHaveSoul owner) {
		this.owner = owner;
		this.naturalOwnerID = owner.getUuid();
		this.associationMemory = new MemeAssociationMemory(this);
		this.profileMemory = new ProfileAssociationMemory(this);
	}

	/**
	 * ID of this soul's natural owner, i.e. which it was born in
	 * 
	 * @return
	 */
	public UUID getNaturalOwnerID() {
		return naturalOwnerID;
	}

	/**
	 * set the current owner of this soul
	 * 
	 * @param owner
	 */
	public void setOwner(ICanHaveSoul owner) {
		this.owner = owner;
	}

	public ICanHaveSoul getOwner() {
		return owner;
	}

	@Override
	public MemeAssociationMemory getMemeAssociationMemory() {
		return this.associationMemory;
	}

	public ProfileAssociationMemory getProfileMemory() {
		return profileMemory;
	}

}
