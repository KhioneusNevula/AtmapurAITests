package mind.goals.taskgoals;

import java.util.Collection;
import java.util.Set;

import mind.concepts.type.IProfile;
import mind.concepts.type.Profile;
import mind.goals.IGoal;
import mind.goals.ITaskGoal;
import mind.goals.ITaskHint;
import mind.goals.TaskHint;
import mind.speech.IUtterance;
import mind.thought_exp.IThought;
import mind.thought_exp.IUpgradedHasKnowledge;

public class TalkTaskGoal implements ITaskGoal {

	private Profile target;
	private Collection<Profile> targets;
	private IUtterance communication;
	private Priority priority = Priority.NORMAL;

	public TalkTaskGoal(IUtterance commu, Collection<Profile> with) {
		this.targets = Set.copyOf(with);
		this.communication = commu;
	}

	public TalkTaskGoal(IUtterance commu, Profile with) {
		this.target = with;
		this.communication = commu;
		targets = Set.of(with);
	}

	public TalkTaskGoal setPriority(Priority priority) {
		this.priority = priority;
		return this;
	}

	public Priority getPriority() {
		return priority;
	}

	@Override
	public ITaskHint getActionHint() {
		return TaskHint.COMMUNICATE;
	}

	@Override
	public boolean isComplete(IUpgradedHasKnowledge entity) {
		// TODO check if a communication goal is complete
		return false;
	}

	@Override
	public IThought checkCompletion(IUpgradedHasKnowledge mind) {
		return null;
	}

	@Override
	public boolean useThoughtToCheckCompletion(IUpgradedHasKnowledge mind) {
		return false;
	}

	public IUtterance communicationInfo() {
		return communication;
	}

	@Override
	public Profile socialTarget() {
		return target;
	}

	@Override
	public Collection<Profile> socialTargets() {
		return targets;
	}

	@Override
	public boolean societalGoal() {
		return true;
	}

	@Override
	public boolean individualGoal() {
		return true;
	}

	@Override
	public boolean isInvalid(IUpgradedHasKnowledge knower) {
		return false;
	}

	@Override
	public String toString() {
		return "CommunicateTG{" + this.communication + "}";
	}

	@Override
	public String getUniqueName() {
		return "goal_task_communicate_" + this.communication.representation().getRepresentation();
	}

	@Override
	public boolean equivalent(IGoal other) {
		return ITaskGoal.super.equivalent(other) && other instanceof TalkTaskGoal
				&& ((TalkTaskGoal) other).communication.equals(this.communication);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TalkTaskGoal other) {
			return this.targets.equals(other.targets) && this.communication.equals(other.communication);
		}
		return super.equals(obj);
	}

	@Override
	public IProfile beneficiary() {
		return IProfile.SELF;
	}

}
