package mind.thought_exp.memory.type;

import java.util.Collection;
import java.util.Collections;

import actor.IUniqueExistence;
import mind.concepts.relations.ConceptRelationType;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.goals.ITaskGoal;
import mind.thought_exp.IUpgradedMind;
import mind.thought_exp.memory.IBrainMemory;
import sim.interfaces.ILocatable;

/**
 * A memory to remember important world object
 * 
 * @author borah
 *
 */
public class ImportantWorldObjectsMemory extends AbstractMemory {

	private ITaskGoal goal;
	private Collection<? extends IUniqueExistence> objects;
	private Property property = Property.ANY;

	public ImportantWorldObjectsMemory(Collection<? extends IUniqueExistence> objects, ITaskGoal forGoal) {
		if (objects.isEmpty())
			throw new IllegalArgumentException();
		this.goal = forGoal;
		this.objects = objects;
	}

	public ImportantWorldObjectsMemory(IUniqueExistence target, ITaskGoal goal) {
		this(Collections.singleton(target), goal);
	}

	/**
	 * What property the items in this have
	 * 
	 * @return
	 */
	public Property getProperty() {
		return property;
	}

	public ImportantWorldObjectsMemory setProperty(Property property) {
		this.property = property;
		return this;
	}

	/**
	 * Returns the important world objects being remembered
	 * 
	 * @return
	 */
	public <T extends IUniqueExistence> Collection<T> getObjects() {
		return (Collection<T>) objects;
	}

	/**
	 * If this is remembering only one object
	 * 
	 * @return
	 */
	public boolean singleObject() {
		return objects.size() == 1;
	}

	/**
	 * If this remembers a single object, get that object
	 * 
	 * @return
	 */
	public <T extends IUniqueExistence> T getObject() {
		return (T) objects.iterator().next();
	}

	@Override
	public ITaskGoal getTopic() {
		return goal;
	}

	@Override
	public boolean applyMemoryEffects(IBrainMemory toMind) {
		for (IUniqueExistence ex : this.objects) {
			if (ex instanceof ILocatable iloc) {
				toMind.learnRelationAndAddSource(new Profile(ex), iloc.getLocation(), ConceptRelationType.FOUND_AT,
						this);
			}
		}
		return true;
	}

	@Override
	public void forgetMemoryEffects(IUpgradedMind toMind) {
		for (IUniqueExistence ex : this.objects) {
			if (ex instanceof ILocatable iloc) {
				toMind.getMemory().tryForgetRelationUsingSource(new Profile(ex), iloc.getLocation(),
						ConceptRelationType.FOUND_AT, this);
			}
		}
	}

	@Override
	public MemoryCategory getType() {
		return MemoryCategory.REMEMBER_FOR_PURPOSE;
	}

}
