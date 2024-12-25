package civilization_and_minds.group.roles;

import java.util.UUID;

import civilization_and_minds.IAgent;
import civilization_and_minds.IIntelligent;
import civilization_and_minds.group.CultureKnowledge;
import civilization_and_minds.group.ICultureKnowledge;
import civilization_and_minds.group.agents.IGroupAgent;
import civilization_and_minds.social.concepts.profile.Profile;
import sim.interfaces.IObjectType;

public class RoleAgent implements IRoleAgent {

	private IRoleConcept role;
	private ICultureKnowledge knowledge;
	private IRoleGroup entity;
	private IGroupAgent parent;
	private UUID id;
	private boolean inherent;

	public RoleAgent(IRoleConcept role, Profile selfProfile, IGroupAgent parentSociety, boolean inherent) {
		this.role = role;
		this.id = selfProfile.getId();
		this.knowledge = new CultureKnowledge(selfProfile);
		this.parent = parentSociety;
		this.inherent = inherent;
	}

	public void setEntity(IRoleGroup entity) {
		this.entity = entity;
	}

	@Override
	public IRoleConcept getPurpose() {
		return role;
	}

	@Override
	public ICultureKnowledge getKnowledgeBase() {
		return knowledge;
	}

	@Override
	public ICultureKnowledge getReducedKnowledgeBase() {
		return knowledge;
	}

	@Override
	public boolean isSmallGroup() {
		return false;
	}

	@Override
	public int count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IAgent getCentralPower() {
		return null;
	}

	@Override
	public boolean hasCentralPower() {
		return false;
	}

	@Override
	public IIntelligent getEntity() {
		return this.entity;
	}

	@Override
	public boolean isInWorld() {
		return entity != null;
	}

	@Override
	public boolean isKnowledgeAccessible() {
		return true;
	}

	@Override
	public boolean isReducedKnowledgeAccessible() {
		return true;
	}

	@Override
	public boolean isGroup() {
		return true;
	}

	@Override
	public String getSimpleName() {
		return this.role.getName();
	}

	@Override
	public IGroupAgent getParent() {
		return this.parent;
	}

	@Override
	public UUID getUUID() {
		return id;
	}

	@Override
	public IObjectType getObjectType() {
		return this.role.getGroupType();
	}

	@Override
	public String getUniqueName() {
		return this.role.getName() + id;
	}

	@Override
	public boolean isInherent() {
		return inherent;
	}

}
