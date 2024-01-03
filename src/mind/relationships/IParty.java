package mind.relationships;

import java.util.Collection;

import mind.concepts.type.IProfile;
import sim.interfaces.IUnique;

public interface IParty extends IUnique {

	/**
	 * Get agreements with another party
	 * 
	 * @param other
	 * @return
	 */
	public Collection<Relationship> getRelationshipsWith(IProfile other);

	/**
	 * Get relationships of a certain type
	 * 
	 * @param other
	 * @param type
	 * @return
	 */
	public Collection<Relationship> getRelationshipsOfTypeWith(IProfile other, RelationType type);

	/**
	 * Gets a relationship of the specified type, PROVIDED it is a singular
	 * relationship
	 */
	public Relationship getRelationship(IProfile with, RelationType type);

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
	public Collection<IProfile> getAllPartiesWithRelationships();

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
	public boolean hasRelationshipsWith(IProfile other);

	/**
	 * dissolve an agreement
	 * 
	 * @param agreement
	 */
	public void dissolveRelationship(IProfile with, Relationship agreement);

	/**
	 * Gets the total trust with the given party
	 * 
	 * @return
	 */
	public float getTrust(IProfile with);

	/** if this party is an individual being */
	public boolean isIndividual();

	/** if this party is a group entity */
	public boolean isGroup();

	/**
	 * Whether this party is no longer existent or otherwise viable
	 * 
	 * @return
	 */
	public boolean isNotViable();

}
