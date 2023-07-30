package mind.relationships;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;

import mind.Group;
import mind.IGroup;
import mind.concepts.type.IProfile;
import mind.concepts.type.Profile;

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
	private int maxMemberCount = Integer.MAX_VALUE;
	private int minMemberCount = 1;
	private SetMultimap<IProfile, Relationship> agreements = MultimapBuilder.hashKeys().hashSetValues().build();
	private Map<UUID, Relationship> agreementsById = new TreeMap<>();
	private Profile self;
	private boolean active = true;
	/**
	 * TODO consider whether we allow Perform relationships with a role -- maybe
	 * only if exact?
	 */
	private static final EnumSet<RelationType> allowedRelationTypes = EnumSet.of(RelationType.PERFORM,
			RelationType.REQUIRE_CONDUCT, RelationType.GET, RelationType.GIVE, RelationType.DO,
			RelationType.REQUIRE_TASK, RelationType.GOVERN, RelationType.GOVERNED_BY, RelationType.FEEL);

	/** TODO different kinds of roles, with associated jobs */

	public Role(String identifier) {
		this.identifier = identifier;
		this.id = UUID.randomUUID();
		this.self = new Profile(this);
	}

	public Profile getSelfProfile() {
		return self;
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
		agreements.put(owner.getCulture().getSelfProfile(), Relationship.governedBy());
		return this;
	}

	/**
	 * make this a role with only one holder, i.e. a monarch
	 * 
	 * @return
	 */
	public Role soloRole() {
		return setRequiredMemberCount(1, 1);
	}

	/**
	 * set an optional maximum number of members and an optional minimum number
	 * 
	 * @param count
	 * @return
	 */
	public Role setRequiredMemberCount(Integer count, Integer minimum) {
		if (count == null || count <= 0) {
			maxMemberCount = Integer.MAX_VALUE;
		} else {
			maxMemberCount = count;
		}
		if (minimum == null || minimum <= 0) {
			minMemberCount = 1;
		} else if (minimum < maxMemberCount) {
			throw new IllegalArgumentException(count + " < " + minimum);
		} else {
			this.minMemberCount = minimum;
		}

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
	 * Min number of members in this role
	 * 
	 * @return
	 */
	public int minMemberCount() {
		return minMemberCount;
	}

	/**
	 * whether this role requires an exact number of members (i.e. max and min are
	 * equivalent, e.g. a king is a single member role)
	 * 
	 * @return
	 */
	public boolean requiresExact() {
		return maxMemberCount == minMemberCount;
	}

	@Override
	public Collection<Relationship> getRelationshipsWith(IProfile other) {
		return agreements.get(other);
	}

	@Override
	public Relationship getRelationshipByID(UUID agreementID) {
		return agreementsById.get(agreementID);
	}

	@Override
	public String getUnitString() {
		return "role";
	}

	@Override
	public void establishRelationship(IParty with, Relationship agreement) {
		IProfile profile = new Profile(with);
		if (!allowedRelationTypes.contains(agreement.getType()))
			throw new IllegalArgumentException(agreement.getType() + "");
		if (agreement.getType().isSingular()) {
			this.agreementsById.values().removeAll(agreements.get(profile));
			this.agreements.get(profile).clear();
		}
		this.agreements.put(profile, agreement);
		this.agreementsById.put(agreement.getAgreementID(), agreement);
	}

	/**
	 * increments the count of individuals in this role; return if it happened
	 * successfully (unsuccessful if already at max member count)
	 */
	public boolean incrementMemberCount() {
		this.memberCount++;
		if (memberCount <= this.maxMemberCount) {
			return true;
		}
		return false;
	}

	/**
	 * If this role has fewer members than its minimum or more than its maximum
	 * 
	 * @return
	 */
	public boolean hasInvalidMemberCount() {
		return this.memberCount < this.minMemberCount || this.memberCount > this.maxMemberCount;
	}

	/**
	 * Remove members from this role; throw exception if the role has no members
	 * 
	 * @return
	 */
	public boolean decrementMemberCount() {
		if (memberCount == 0)
			throw new IllegalArgumentException();
		this.memberCount--;
		if (memberCount >= this.minMemberCount)
			return true;
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

	/**
	 * If the member count is 0
	 * 
	 * @return
	 */
	public boolean hasNoMembers() {
		return this.memberCount == 0;
	}

	/**
	 * If members can be removed without violating the min member count
	 * 
	 * @return
	 */
	public boolean canRemoveMembers() {
		return this.memberCount > this.minMemberCount;
	}

	@Override
	public Collection<IProfile> getAllPartiesWithRelationships() {
		return agreements.keySet();
	}

	@Override
	public Collection<Relationship> getAllRelationships() {
		return agreements.values();
	}

	@Override
	public void dissolveRelationship(IProfile with, Relationship agreement) {
		if (this.agreements.remove(with, agreement)) {
			this.agreementsById.remove(agreement.getAgreementID());
			if (this.owner != null && owner.getCulture().getSelfProfile().equals(with)) {
				throw new IllegalArgumentException(
						"Tried to remove role relationship between " + this + " and its owner " + owner);
			}
		}
	}

	@Override
	public boolean hasRelationshipsWith(IProfile other) {
		return !this.agreements.get(other).isEmpty();
	}

	/**
	 * maybe make this stuff more efficient
	 */
	@Override
	public boolean isMember(IParty other) {
		return other.hasRelationshipsWith(this.self)
				&& other.getRelationshipsWith(this.self).stream().anyMatch((r) -> r.getType().isMembership());
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

	/**
	 * Roles do not have a trust mechanic
	 */
	@Override
	public float getTrust(IProfile with) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Relationship getRelationship(IProfile with, RelationType type) {
		return this.agreements.get(with).stream().filter((a) -> a.getType() == type).findAny().orElse(null);
	}

	@Override
	public Collection<Relationship> getRelationshipsOfTypeWith(IProfile other, RelationType type) {
		return this.getRelationshipsWith(other).stream().filter((a) -> a.getType() == type)
				.collect(Collectors.toUnmodifiableSet());
	}

	public void deactivate() {
		this.active = false;
	}

	@Override
	public boolean isNotViable() {
		return !active;
	}

}
