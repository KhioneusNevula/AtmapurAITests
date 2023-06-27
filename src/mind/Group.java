package mind;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;

import mind.memory.IHasKnowledge;
import mind.relationships.IParty;
import mind.relationships.Relationship;
import mind.relationships.Role;
import sim.World;

/**
 * Note that a group can only have one kind of membership relationship with
 * another entity, no more
 * 
 * @author borah
 *
 */
public class Group implements IGroup, IEntity {

	private String identifier;
	private UUID id;
	private Set<IMind> members;
	private Set<Group> subGroups;
	private int memberCount = 0;
	private Group parentGroup;
	private Set<Role> roles;
	private SetMultimap<IParty, Relationship> agreements;
	private Map<UUID, Relationship> agreementsById;
	private Culture culture;
	private World world;

	public Group(String identifier, World world) {
		this.identifier = identifier;
		this.id = UUID.randomUUID();
		this.culture = new Culture(id, "group", identifier);
		this.world = world;
	}

	public String getIdentifier() {
		return identifier;
	}

	/**
	 * the set of roles
	 * 
	 * @return
	 */
	public Set<Role> roles() {
		return roles == null ? Set.of() : roles;
	}

	/**
	 * the set of *actual members* (not roles and subgroups)
	 * 
	 * @return
	 */
	public Set<IMind> members() {
		return members == null ? Set.of() : members;
	}

	/**
	 * updates member count based on factors
	 */
	public void updateMemberCount() {
		if (members != null)
			memberCount += this.members.size();
		if (subGroups != null) {
			for (Group g : this.subGroups) {
				memberCount += g.memberCount;
			}
		}
	}

	/**
	 * Count of *actual members* (not roles and subgroups)
	 * 
	 * @return
	 */
	public int memberCount() {
		return memberCount;
	}

	private SetMultimap<IParty, Relationship> findAgreements() {
		if (agreements == null) {
			return agreements = MultimapBuilder.hashKeys().hashSetValues().build();
		}
		return agreements;
	}

	@Override
	public Collection<Relationship> getRelationshipsWith(IParty other) {
		return agreements == null ? Set.of() : agreements.get(other);
	}

	@Override
	public boolean isMember(IParty other) {
		return agreements == null ? false : agreements.get(other).stream().anyMatch((r) -> r.getType().isMembership());
	}

	@Override
	public Relationship getRelationshipByID(UUID agreementID) {
		return agreementsById == null ? null : agreementsById.get(agreementID);
	}

	public Group getParentGroup() {
		return parentGroup;
	}

	/**
	 * note that a membership relation to a parent group necessitates the erasure of
	 * all other parent relations
	 */
	@Override
	public void establishRelationship(IParty with, Relationship agreement) {
		if (agreement.getType().isMembership()) {
			if (agreements != null)
				agreements.get(with).removeIf((r) -> r.getType().isMembership());
			if (agreement.getType().benefits()) {
				if (with instanceof IMind) {
					(this.members == null ? members = new HashSet<>() : members).add((IMind) with);
				} else if (with instanceof Role) {
					(this.roles == null ? roles = new HashSet<>() : roles).add((Role) with);
				} else if (with instanceof Group) {
					(this.subGroups == null ? subGroups = new HashSet<>() : subGroups).add((Group) with);
				} else {
					throw new IllegalArgumentException(
							"type of party: " + with.getClass().getSimpleName() + " for agreement with " + this + "??");
				}
				this.updateMemberCount();
			} else if (agreement.getType().provides()) {
				if (with instanceof Group) {
					this.parentGroup = (Group) with;
				} else {
					throw new IllegalArgumentException(
							"type of party: " + with.getClass().getSimpleName() + " for agreement with " + this + "??");
				}
			}
		}
		this.findAgreements().put(with, agreement);
		(this.agreementsById == null ? agreementsById = new TreeMap<>() : agreementsById)
				.put(agreement.getAgreementID(), agreement);
	}

	@Override
	public Collection<IParty> getAllPartiesWithRelationships() {
		return agreements == null ? Set.of() : agreements.keySet();
	}

	@Override
	public Collection<Relationship> getAllRelationships() {
		return agreements == null ? Set.of() : agreements.values();
	}

	@Override
	public void dissolveRelationship(IParty with, Relationship agreement) {
		if (agreements == null || this.agreementsById == null)
			return;
		if (this.agreements.remove(with, agreement)) {
			this.agreementsById.remove(agreement.getAgreementID());
			if (agreement.getType().isMembership()) {
				if (agreement.getType().benefits()) {
					if (with instanceof IMind)
						this.members.remove(with);
					if (with instanceof Role && roles != null)
						this.roles.remove(with);
					if (with instanceof Group && subGroups != null)
						this.subGroups.remove(with);
				} else if (agreement.getType().provides()) {
					if (with == this.parentGroup)
						this.parentGroup = null;
				}

			}
		}
	}

	@Override
	public boolean hasRelationshipsWith(IParty other) {
		return agreements == null ? false : !this.agreements.get(other).isEmpty();
	}

	/**
	 * same thing as {@link Group#getCulture()}, just for the purposes of filling
	 * the {@link IHasKnowledge} class template
	 */
	@Override
	public Culture getKnowledgeBase() {
		return culture;
	}

	@Override
	public UUID getUUID() {
		return id;
	}

	public String report() {
		StringBuilder builder = new StringBuilder("Group(" + this.identifier + "," + this.id + "):\n");
		builder.append("\tmembers[" + memberCount + "]:" + this.members);
		builder.append("\n\tparent:" + this.parentGroup);
		builder.append("\n\tsubgroups:" + this.subGroups);
		builder.append("\n\troles:" + this.roles);
		builder.append("\n\tagreements:" + this.agreements);
		builder.append("\n  Culture->" + this.culture.report());

		return builder.toString();
	}

	@Override
	public String toString() {
		return "Group(" + this.identifier + ",\"" + this.culture.getNameWord().getDisplay() + "\")";
	}

	@Override
	public int hashCode() {
		return super.hashCode() + this.getUUID().hashCode();
	}

	@Override
	public long worldTicks() {
		return world.getTicks();
	}

	@Override
	public IWill getWill() {
		// TODO create a Will for groups
		return null;
	}

}
