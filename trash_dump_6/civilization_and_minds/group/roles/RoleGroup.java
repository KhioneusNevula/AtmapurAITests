package civilization_and_minds.group.roles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import civilization_and_minds.IAgent;
import civilization_and_minds.group.ICultureKnowledge;
import civilization_and_minds.group.agents.IGroupAgent;
import civilization_and_minds.mind.goals.IGoal;
import civilization_and_minds.social.concepts.profile.Profile;
import sim.interfaces.IObjectType;

public class RoleGroup implements IRoleGroup {

	private RoleAgent agent;
	private List<IGoal> goals;

	public RoleGroup(RoleAgent agent) {
		this.agent = agent;
		this.goals = new ArrayList<IGoal>();
	}

	@Override
	public void tick(long ticks) {
		// TODO Auto-generated method stub

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
	public IRoleConcept getPurpose() {
		return agent.getPurpose();
	}

	@Override
	public ICultureKnowledge getKnowledge() {
		return agent.getKnowledgeBase();
	}

	@Override
	public boolean isSmallGroup() {
		return false;
	}

	@Override
	public boolean actsInAbstract() {
		// TODO Auto-generated method stub
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
	public Profile getSelfProfile() {
		return agent.getKnowledgeBase().getSelfProfile();
	}

	@Override
	public IGroupAgent getAgentRepresentation() {
		return agent;
	}

	@Override
	public IAgent getParentAgent() {
		return agent.getParent();
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
	public boolean isInherent() {
		return agent.isInherent();
	}

}
