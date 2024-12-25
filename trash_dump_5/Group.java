package mind;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;

import mind.concepts.type.IProfile;
import mind.memory.IHasKnowledge;
import mind.relationships.IParty;
import mind.relationships.RelationType;
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
	private Map<IProfile, IParty> members;
	private Map<IProfile, Group> subGroups;
	private int memberCount = 0;
	private Group parentGroup;
	private Map<IProfile, Role> roles;
	private SetMultimap<IProfile, Relationship> agreements;
	private Map<IProfile, Float> trust;
	private Culture culture;
	private World world;
	private static final int TYPICAL_MEMORY_PASS_COUNT = 10;
	private boolean active = true;

	public Group(String identifier, World world) {
		this.identifier = identifier;
		this.id = UUID.randomUUID();
		this.culture = new Culture(this, "group");
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
	public Collection<Role> roles() {
		return roles == null ? Set.of() : roles.values();
	}

	/**
	 * the set of *actual members* (not roles and subgroups)
	 * 
	 * @return
	 */
	public Collection<IParty> members() {
		return members == null ? Set.of() : members.values();
	}

	@Override
	public float getTrust(IProfile with) {
		return this.trust == null ? 0 : this.trust.getOrDefault(with, 0f);
	}

	/**
	 * updates member count based on factors
	 */
	public void updateMemberCount() {
		memberCount = 0;
		if (members != null)
			memberCount += this.members.size();
		if (subGroups != null) {
			for (Group g : this.subGroups.values()) {
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

	private SetMultimap<IProfile, Relationship> findAgreements() {
		if (agreements == null) {
			return agreements = MultimapBuilder.hashKeys().hashSetValues().build();
		}
		return agreements;
	}

	@Override
	public Collection<Relationship> getRelationshipsWith(IProfile other) {
		return agreements == null ? Set.of() : agreements.get(other);
	}

	@Override
	public boolean isMember(IParty other) {
		return !other.getRelationshipsOfTypeWith(this.getKnowledgeBase().getSelfProfile(), RelationType.BE).isEmpty();
	}

	@Override
	public Collection<Relationship> getRelationshipsOfTypeWith(IProfile other, RelationType type) {

		return agreements != null
				? agreements.get(other).stream().filter((a) -> a.getType() == type).collect(Collectors.toSet())
				: Set.of();
	}

	public Group getParentGroup() {
		return parentGroup;
	}

	@Override
	public Relationship getRelationship(IProfile with, RelationType type) {
		if (agreements != null) {
			return agreements.get(with).stream().filter((a) -> a.getType().equals(type)).findAny().orElse(null);
		}
		return null;
	}

	/**
	 * note that a membership relation to a parent group necessitates the erasure of
	 * all other parent relations
	 */
	@Override
	public void establishRelationship(IParty with, Relationship agreement) {
		IProfile profile = this.culture.recognizeProfile(with);
		if (agreement.getType().isSingular() && agreements != null) {
			agreements.get(profile).removeIf((r) -> r.getType() == agreement.getType());

		}
		if (agreement.getType().isMembership()) {
			if (agreement.getType().benefits()) {
				if (with.isIndividual()) {
					(this.members == null ? members = new HashMap<>() : members).put(profile, with);
				} else if (with instanceof Role) {
					(this.roles == null ? roles = new HashMap<>() : roles).put(profile, (Role) with);
				} else if (with instanceof Group) {
					(this.subGroups == null ? subGroups = new HashMap<>() : subGroups).put(profile, (Group) with);
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
		} else if (agreement.getType() == RelationType.FEEL) {
			this.changeTrust(profile, agreement.getGoal().asPersonalRelationship().trust());
		}
		this.findAgreements().put(profile, agreement);
	}

	@Override
	public Collection<IProfile> getAllPartiesWithRelationships() {
		return agreements == null ? Set.of() : agreements.keySet();
	}

	@Override
	public Collection<Relationship> getAllRelationships() {
		return agreements == null ? Set.of() : agreements.values();
	}

	private void changeTrust(IProfile with, float trust) {
		if (this.trust == null)
			this.trust = new HashMap<>();
		this.trust.put(with, this.trust.getOrDefault(with, 0f) + trust);
	}

	@Override
	public void dissolveRelationship(IProfile with, Relationship agreement) {
		if (agreements == null)
			return;
		if (this.agreements.remove(with, agreement)) {
			if (agreement.getType().isMembership()) {
				if (agreement.getType().benefits()) {
					if (members != null)
						this.members.remove(with);
					if (roles != null)
						this.roles.remove(with);
					if (subGroups != null)
						this.subGroups.remove(with);
				} else if (agreement.getType().provides()) {
					if (this.parentGroup != null && with.equals(this.parentGroup.culture.getSelfProfile()))
						this.parentGroup = null;
				}

			} else if (agreement.getType() == RelationType.FEEL) {
				this.changeTrust(with, -agreement.getGoal().asPersonalRelationship().trust());
			}
		}
	}

	@Override
	public boolean hasRelationshipsWith(IProfile other) {
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
		return "Group(" + this.identifier + ",\"" + "\")";
	}

	@Override
	public int hashCode() {
		return super.hashCode() + this.getUUID().hashCode();
	}

	@Override
	public String getUnitString() {
		return "group";
	}

	@Override
	public IWill getWill() {
		// TODO create a Will for groups
		return null;
	}

	public void tick(long worldTicks) {
		if (members != null) {
			IParty mind = null;
			for (Iterator<IParty> iter = members.values().iterator(); iter.hasNext();) {
				mind = iter.next();
				if (mind.isNotViable()) {
					iter.remove();
				}
			}
		}
		if (worldTicks % 20 < 3 || this.world.rand().nextInt(20) < 3) {
			this.culture.prune(TYPICAL_MEMORY_PASS_COUNT);
		}
	}

	public void deactivate() {
		this.active = false;
	}

	@Override
	public boolean isNotViable() {
		return !active;
	}

	@Override
	public void kill() {
		this.deactivate();
	}

}
