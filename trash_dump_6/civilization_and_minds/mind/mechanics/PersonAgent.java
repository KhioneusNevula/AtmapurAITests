package civilization_and_minds.mind.mechanics;

import java.util.UUID;

import actor.construction.physical.IActorType;
import civilization_and_minds.IAgent;
import civilization_and_minds.IKnowledgeBase;
import civilization_and_minds.group.agents.IGroupAgent;
import civilization_and_minds.mind.type.SapientMind;
import sim.interfaces.IObjectType;

/**
 * Agent class representing an individual
 * 
 * @author borah
 *
 */
public class PersonAgent implements IAgent {

	private UUID id;
	private IActorType type;
	private SapientMind mind;
	private String simpleName;
	private IGroupAgent parent;

	public PersonAgent(UUID id, IActorType type, String simpleName) {
		this.id = id;
		this.type = type;
		this.simpleName = simpleName;
	}

	public void setEntity(SapientMind mind) {
		this.mind = mind;
	}

	public void setParent(IGroupAgent parent) {
		this.parent = parent;
	}

	@Override
	public UUID getUUID() {
		return id;
	}

	@Override
	public IObjectType getObjectType() {
		return type;
	}

	@Override
	public String getUniqueName() {
		return this.type.getUniqueName() + "_" + this.simpleName + "_" + id;
	}

	@Override
	public SapientMind getEntity() {
		return mind;
	}

	@Override
	public boolean isInWorld() {
		return mind != null;
	}

	@Override
	public boolean isKnowledgeAccessible() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isReducedKnowledgeAccessible() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IMemoryKnowledge getKnowledgeBase() {
		// TODO Auto-generated method stub
		return mind.getKnowledge();
	}

	@Override
	public IKnowledgeBase getReducedKnowledgeBase() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isGroup() {
		return false;
	}

	@Override
	public String getSimpleName() {
		return simpleName;
	}

	@Override
	public IGroupAgent getParent() {
		return this.parent;
	}

}
