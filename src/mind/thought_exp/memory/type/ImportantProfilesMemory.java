package mind.thought_exp.memory.type;

import java.util.Collection;
import java.util.Collections;

import mind.concepts.type.IProfile;
import mind.goals.ITaskGoal;
import mind.thought_exp.IUpgradedMind;
import mind.thought_exp.memory.IBrainMemory;

/**
 * A memory to remember an important profile/profiles
 * 
 * @author borah
 *
 */
public class ImportantProfilesMemory extends AbstractMemory {

	private ITaskGoal goal;
	private Collection<IProfile> profiles;

	public ImportantProfilesMemory(Collection<IProfile> profiles, ITaskGoal forGoal) {
		if (profiles.isEmpty())
			throw new IllegalArgumentException();
		this.goal = forGoal;
		this.profiles = profiles;
	}

	public ImportantProfilesMemory(IProfile target, ITaskGoal goal) {
		this(Collections.singleton(target), goal);
	}

	public Collection<IProfile> getProfiles() {
		return profiles;
	}

	/**
	 * If this is remembering only one profile
	 * 
	 * @return
	 */
	public boolean singleProfile() {
		return profiles.size() == 1;
	}

	/**
	 * If this remembers a single profile, get that profile
	 * 
	 * @return
	 */
	public IProfile getProfile() {
		return profiles.iterator().next();
	}

	@Override
	public ITaskGoal getTopic() {
		return goal;
	}

	@Override
	public boolean applyMemoryEffects(IBrainMemory toMind) {
		return true;
	}

	@Override
	public void forgetMemoryEffects(IUpgradedMind toMind) {

	}

	@Override
	public MemoryCategory getType() {
		return MemoryCategory.REMEMBER_FOR_PURPOSE;
	}

	@Override
	public int hashCode() {
		return this.profiles.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ImportantProfilesMemory ipm)
			return this.profiles.equals(ipm.profiles);
		return super.equals(obj);
	}

}
