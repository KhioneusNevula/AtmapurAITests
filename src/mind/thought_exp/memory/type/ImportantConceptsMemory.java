package mind.thought_exp.memory.type;

import java.util.Collection;
import java.util.Collections;

import mind.concepts.type.IMeme;
import mind.goals.ITaskGoal;
import mind.thought_exp.IUpgradedMind;
import mind.thought_exp.memory.IBrainMemory;

/**
 * A memory to remember important concepts
 * 
 * @author borah
 *
 */
public class ImportantConceptsMemory extends AbstractMemory {

	private ITaskGoal goal;
	private Collection<? extends IMeme> objects;

	public ImportantConceptsMemory(Collection<? extends IMeme> objects, ITaskGoal forGoal) {
		if (objects.isEmpty())
			throw new IllegalArgumentException();
		this.goal = forGoal;
		this.objects = objects;
	}

	public ImportantConceptsMemory(IMeme target, ITaskGoal goal) {
		this(Collections.singleton(target), goal);
	}

	/**
	 * Returns the important concepts being remembered
	 * 
	 * @return
	 */
	public <T extends IMeme> Collection<T> getConcepts() {
		return (Collection<T>) objects;
	}

	/**
	 * If this is remembering only one concept
	 * 
	 * @return
	 */
	public boolean singleConcept() {
		return objects.size() == 1;
	}

	/**
	 * If this remembers a single concept, get that concept
	 * 
	 * @return
	 */
	public <T extends IMeme> T getConcept() {
		return (T) objects.iterator().next();
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
		return this.objects.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ImportantConceptsMemory icm)
			return this.objects.equals(icm.objects);
		return super.equals(obj);
	}

}
