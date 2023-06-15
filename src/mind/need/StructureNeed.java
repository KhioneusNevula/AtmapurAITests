package mind.need;

import com.google.common.collect.ImmutableSet;

import mind.IGroup;
import mind.goals.IGoal;

public class StructureNeed extends AbstractNeed {

	private ImmutableSet<IGroup> groups;

	public StructureNeed(INeed.Degree degree, Iterable<IGroup> groups) {
		super(NeedType.STRUCTURE, degree);
		this.groups = ImmutableSet.copyOf(groups);
	}

	public StructureNeed(INeed.Degree degree, IGroup... groups) {
		super(NeedType.STRUCTURE, degree);
		this.groups = ImmutableSet.copyOf(groups);

	}

	public ImmutableSet<IGroup> getGroups() {
		return groups;
	}

	@Override
	public IGoal genIndividualGoal() {
		throw new UnsupportedOperationException("structure goals are societal level only");
	}

	@Override
	public IGoal genSocietalGoal() {
		// TODO structure goal role gen
		return null;
	}

}
