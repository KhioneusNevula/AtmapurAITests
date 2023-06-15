package mind.relationships;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;

import mind.Group;
import mind.IGroup;

/**
 * Formally, roles only have agreements with other roles and with groups.
 * Individual agreements are tracked per individual.
 * 
 * @author borah
 *
 */
public class Role implements IGroup {

	/** TODO The schedule members of this role follow */
	/* private Schedule schedule; */
	/** TODO the method by which members are selected */
	/* private SelectionMethod selection; */
	/** TODO the set of tasks this role does */
	/* private Set<ITask> tasks; */
	/** the name of this role */
	private String identifier;
	private UUID id;
	private Group owner;
	private int memberCount;
	private boolean requiresMax;
	private int maxMemberCount = Integer.MAX_VALUE;
	private SetMultimap<IParty, Relationship> agreements = MultimapBuilder.hashKeys().hashSetValues().build();
	private Map<UUID, Relationship> agreementsById = new TreeMap<>();

	/** TODO different kinds of roles, with associated jobs */

	public Role(String identifier) {
		this.identifier = identifier;
		this.id = UUID.randomUUID();
	}

	/**
	 * set an owner; return self for easy chaining
	 * 
	 * @param owner
	 * @return
	 */
	public Role setOwner(Group owner) {
		if (this.owner != null)
			throw new IllegalStateException("Owner already present");
		if (owner == this.owner)
			throw new IllegalStateException("owner is the same");
		this.owner = owner;
		agreements.put(owner, Relationship.governedBy());
		return this;
	}

	/**
	 * make this a role with only one holder, i.e. a monarch
	 * 
	 * @return
	 */
	public Role soloRole() {
		return setMaxMemberCount(1, true);
	}

	/**
	 * set a maximum number of members, and if requireAmount= true, require this
	 * amount of members
	 * 
	 * @param count
	 * @return
	 */
	public Role setMaxMemberCount(int count, boolean requireAmount) {
		if (count <= 0) {
			maxMemberCount = Integer.MAX_VALUE;
		} else {
			maxMemberCount = count;
		}
		this.requiresMax = requireAmount;
		return this;
	}

	public String getIdentifier() {
		return identifier;
	}

	/**
	 * the max members in this role
	 * 
	 * @return
	 */
	public int maxMemberCount() {
		return maxMemberCount;
	}

	/**
	 * whether this role requires its maximum number of members
	 * 
	 * @return
	 */
	public boolean requiresMax() {
		return requiresMax;
	}

	@Override
	public Collection<Relationship> getRelationshipsWith(IParty other) {
		return agreements.get(other);
	}

	@Override
	public Relationship getRelationshipByID(UUID agreementID) {
		return agreementsById.get(agreementID);
	}

	@Override
	public void establishRelationship(IParty with, Relationship agreement) {
		this.agreements.put(with, agreement);
		this.agreementsById.put(agreement.getAgreementID(), agreement);
	}

	/**
	 * increments the count of individuals in this role; return if it happened
	 * successfully (unsuccessful if already at max member count)
	 */
	public boolean incrementMemberCount() {
		if (memberCount < this.maxMemberCount) {
			this.memberCount++;
			return true;
		}
		return false;
	}

	/**
	 * whether the role can gain new members currently
	 * 
	 * @return
	 */
	public boolean canAddMembers() {
		return memberCount < this.maxMemberCount;
	}

	@Override
	public Collection<IParty> getAllPartiesWithRelationships() {
		return agreements.keySet();
	}

	@Override
	public Collection<Relationship> getAllRelationships() {
		return agreements.values();
	}

	@Override
	public void dissolveRelationship(IParty with, Relationship agreement) {
		if (this.agreements.remove(with, agreement)) {
			this.agreementsById.remove(agreement.getAgreementID());
		}
	}

	@Override
	public boolean hasRelationshipsWith(IParty other) {
		return !this.agreements.get(other).isEmpty();
	}

	/**
	 * maybe make this stuff more efficient
	 */
	@Override
	public boolean isMember(IParty other) {
		return other.hasRelationshipsWith(this)
				&& other.getRelationshipsWith(this).stream().anyMatch((r) -> r.getType().isMembership());
	}

	@Override
	public UUID getUUID() {
		return id;
	}

	@Override
	public int hashCode() {
		return super.hashCode() + this.getUUID().hashCode();
	}

	@Override
	public int memberCount() {
		return memberCount;
	}

}
