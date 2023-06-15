package mind.relationships;

import java.util.Collection;
import java.util.UUID;

import sim.interfaces.IUnique;

public interface IParty extends IUnique {

	/**
	 * Get agreements with another party
	 * 
	 * @param other
	 * @return
	 */
	public Collection<Relationship> getRelationshipsWith(IParty other);

	/**
	 * Gets agreement by the given ID
	 * 
	 * @param agreementID
	 * @return
	 */
	public Relationship getRelationshipByID(UUID agreementID);

	/**
	 * establish an agreement with the given party
	 * 
	 * @param with
	 * @param agreement
	 */
	public void establishRelationship(IParty with, Relationship agreement);

	/**
	 * Returns all parties this object has an agreement of some kind with
	 * 
	 * @return
	 */
	public Collection<IParty> getAllPartiesWithRelationships();

	/**
	 * Return an iterable form of all the agreements this entity is involved in
	 * 
	 * @return
	 */
	public Collection<Relationship> getAllRelationships();

	/**
	 * whether this party has any agreements with the given entity
	 * 
	 * @param other
	 * @return
	 */
	public boolean hasRelationshipsWith(IParty other);

	/**
	 * dissolve an agreement
	 * 
	 * @param agreement
	 */
	public void dissolveRelationship(IParty with, Relationship agreement);

}
