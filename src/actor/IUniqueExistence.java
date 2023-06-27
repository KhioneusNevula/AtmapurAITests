package actor;

import java.util.Collection;
import java.util.Random;
import java.util.Set;

import biology.systems.types.ISensor;
import mind.concepts.type.Property;
import mind.memory.IKnowledgeBase;
import mind.memory.IPropertyData;
import mind.memory.RememberedProperties;
import sim.interfaces.IExistsInWorld;
import sim.interfaces.IUnique;

/**
 * Thing which can be perceived and assigned properties
 * 
 * @author borah
 *
 */
public interface IUniqueExistence extends IUnique, IExistsInWorld {

	public IPropertyData getPropertyData(IKnowledgeBase culture, Property property);

	public void assignProperty(IKnowledgeBase culture, Property property, IPropertyData data);

	default void assignProperty(IKnowledgeBase culture, Property property) {
		if (property.isOnlyPresence()) {
			this.assignProperty(culture, property, IPropertyData.PRESENCE);
		} else {
			this.assignProperty(culture, property, new RememberedProperties(property));
		}
	}

	default IPropertyData getPropertyHint(Property property) {
		return IPropertyData.UNKNOWN;
	}

	/**
	 * Returns a set of preferred sensors to sense this property (
	 * 
	 * @param property
	 * @return
	 */
	default Collection<ISensor> getPreferredSensesForHint(Property property) {
		return Set.of();
	}

	/**
	 * Return the primary vessel of sensing this thing
	 * 
	 * @return
	 */
	public IVisage getVisage();

	/**
	 * Gets the "species" (type) of actor, phenomenon, etc that this is
	 * 
	 * @return
	 */
	public ITemplate getSpecies();

	public Random rand();

}
