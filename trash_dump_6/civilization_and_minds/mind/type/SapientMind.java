package civilization_and_minds.mind.type;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import actor.Actor;
import civilization_and_minds.IAgent;
import civilization_and_minds.group.ICultureKnowledge;
import civilization_and_minds.group.agents.IGroupAgent;
import civilization_and_minds.mind.IMind;
import civilization_and_minds.mind.goals.IGoal;
import civilization_and_minds.mind.mechanics.IMemoryKnowledge;
import civilization_and_minds.mind.mechanics.MemoryKnowledge;
import civilization_and_minds.mind.mechanics.PersonAgent;
import civilization_and_minds.social.concepts.profile.Profile;
import metaphysical.soul.SapientSoul;
import sim.interfaces.IObjectType;

public abstract class SapientMind implements IMind {

	private Profile self;
	private MemoryKnowledge memory;
	private IGroupAgent parent;
	private PersonAgent agent;
	private Collection<IGroupAgent> groups;
	private SapientSoul soul;
	private Collection<IGoal> goals;
	private IGoal mainGoal;

	public SapientMind(Profile self, PersonAgent agent) {
		this.self = self;
		this.memory = new MemoryKnowledge(self);
		this.agent = agent;
		groups = new HashSet<>();
		goals = new HashSet<>();
	}

	@Override
	public void tick(long ticks) {
		// TODO Auto-generated method stub

	}

	public void setSoul(SapientSoul soul) {
		this.soul = soul;
	}

	public void setParentCulture(IGroupAgent parent, ICultureKnowledge culture) {
		this.parent = parent;
		this.memory.setParent(culture);
		this.agent.setParent(parent);
	}

	@Override
	public Profile getSelfProfile() {
		return self;
	}

	@Override
	public IAgent getAgentRepresentation() {
		return agent;
	}

	@Override
	public IAgent getParentAgent() {
		return parent;
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
	public IMemoryKnowledge getKnowledge() {
		return this.memory;
	}

	@Override
	public Stream<? extends IGroupAgent> getImmediateGroups() {
		return this.groups.stream();
	}

	@Override
	public boolean memberOf(IGroupAgent group) {
		return groups.contains(group);
	}

	@Override
	public SapientSoul getContainingSoul() {
		return this.soul;
	}

	@Override
	public Actor getContainingActor() {
		return this.soul.getContainerEntity();
	}

	@Override
	public Stream<IGoal> getGoals() {
		return this.goals.stream();
	}

	@Override
	public IGoal getMainGoal() {
		return mainGoal;
	}

	protected boolean addGoal(IGoal goal) {
		return this.goals.add(goal);
	}

	protected boolean removeGoal(IGoal goal) {
		if (this.mainGoal != null && this.mainGoal.equals(goal))
			mainGoal = null;
		return this.goals.remove(goal);
	}

	@Override
	public int goalCount() {
		return this.goals.size();
	}

	/**
	 * Set the main goal and return the previous main goal, if any. Adds main goal
	 * to goal list
	 * 
	 * @param mainGoal
	 * @return
	 */
	protected IGoal setMainGoal(IGoal mainGoal) {
		this.goals.add(mainGoal);
		this.mainGoal = mainGoal;
		return mainGoal;
	}

	protected boolean hasGoal(IGoal goal) {
		return this.goals.contains(goal);
	}

	@Override
	public String report() {
		Stream<? extends IGroupAgent> gros = this.getImmediateGroups();
		Set<? extends IGroupAgent> grs = gros.collect(Collectors.toSet());
		return "mind(" + (this.parent != null ? this.parent + ", " : "") + (grs.isEmpty() ? "" : "groups=" + grs + ", ")
				+ (goals.isEmpty() ? "" : "goals=" + this.goals + ", ") + "main-goal=" + mainGoal + ", " + "knowledge="
				+ this.getKnowledge().report() + ")";
	}

}
