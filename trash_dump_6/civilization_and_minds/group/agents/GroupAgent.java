package civilization_and_minds.group.agents;

import java.util.UUID;

import civilization_and_minds.group.ICultureKnowledge;
import civilization_and_minds.group.IGroup;
import civilization_and_minds.group.purpose.IGroupPurpose;
import sim.interfaces.IObjectType;

public abstract class GroupAgent implements IGroupAgent {

	private String simpleName;
	private UUID id;
	private IGroupPurpose purpose;
	private IGroup entity;
	private boolean knowledgeAccessible;
	private ICultureKnowledge knowledge;
	private IGroupAgent parent;

	public GroupAgent(IGroupPurpose purpose, UUID id) {
		this.simpleName = purpose.getName();
		this.id = id;
		this.purpose = purpose;
	}

	public GroupAgent setParent(IGroupAgent parent) {
		this.parent = parent;
		return this;
	}

	public GroupAgent setEntity(IGroup entity) {
		this.entity = entity;
		this.knowledgeAccessible = true;
		this.knowledge = entity.getKnowledge();
		return this;
	}

	public IGroupAgent getParent() {
		return parent;
	}

	@Override
	public IGroup getEntity() {
		return entity;
	}

	@Override
	public boolean isInWorld() {
		return entity != null;
	}

	@Override
	public boolean isKnowledgeAccessible() {
		return knowledgeAccessible;
	}

	@Override
	public boolean isReducedKnowledgeAccessible() {
		return knowledgeAccessible;
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
	public boolean isGroup() {
		return true;
	}

	@Override
	public UUID getUUID() {
		return id;
	}

	@Override
	public IObjectType getObjectType() {
		return purpose.getGroupType();
	}

	@Override
	public String getUniqueName() {
		return this.simpleName + this.id;
	}

	@Override
	public IGroupPurpose getPurpose() {
		return this.purpose;
	}

	@Override
	public String getSimpleName() {
		return simpleName;
	}

}
