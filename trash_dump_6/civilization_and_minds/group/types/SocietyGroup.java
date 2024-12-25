package civilization_and_minds.group.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import civilization_and_minds.IAgent;
import civilization_and_minds.group.CultureKnowledge;
import civilization_and_minds.group.agents.IGroupAgent;
import civilization_and_minds.group.agents.SocietyGroupAgent;
import civilization_and_minds.group.purpose.IGroupPurpose;
import civilization_and_minds.group.roles.IRoleGroup;
import civilization_and_minds.mind.goals.IGoal;
import civilization_and_minds.social.concepts.profile.Profile;
import sim.interfaces.IObjectType;

public abstract class SocietyGroup implements ISocietyGroup {

	private Profile self;
	private SocietyGroupAgent agent;
	private CultureKnowledge culture;
	private Collection<IRoleGroup> roles;
	protected List<IGoal> goals;

	public SocietyGroup(SocietyGroupAgent agent, Profile self) {
		this.self = self;
		this.agent = agent;
		agent.setEntity(this);
		this.culture = new CultureKnowledge(self);
		if (agent.getParent() != null && agent.getParent().isKnowledgeAccessible())
			this.culture.setParent(agent.getParent().getKnowledgeBase());
		if (agent.getParent() != null && agent.getParent().isReducedKnowledgeAccessible())
			this.culture.setParent(agent.getParent().getReducedKnowledgeBase());
		roles = new ArrayList<>();
		goals = new ArrayList<>();
	}

	@Override
	public Profile getSelfProfile() {
		return self;
	}

	@Override
	public IGroupAgent getAgentRepresentation() {
		return agent;
	}

	@Override
	public IAgent getParentAgent() {
		return this.agent.getParent();
	}

	@Override
	public UUID getUUID() {
		return agent.getUUID();
	}

	@Override
	public IObjectType getObjectType() {
		return agent.getObjectType();
	}

	@Override
	public String getUniqueName() {
		return agent.getUniqueName();
	}

	@Override
	public Stream<IGoal> getGoals() {
		return goals.stream();
	}

	@Override
	public IGoal getMainGoal() {
		return goals.get(0);
	}

	@Override
	public int goalCount() {
		return goals.size();
	}

	@Override
	public IGroupPurpose getPurpose() {
		return agent.getPurpose();
	}

	@Override
	public CultureKnowledge getKnowledge() {
		return culture;
	}

	@Override
	public Collection<IRoleGroup> getRoles() {
		return roles;
	}

}
