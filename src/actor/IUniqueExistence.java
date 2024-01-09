package actor;

import java.util.Collection;
import java.util.Random;
import java.util.Set;

import biology.systems.types.ISensor;
import mind.concepts.type.Property;
import mind.memory.IPropertyData;
import mind.memory.RememberedProperties;
import mind.thought_exp.memory.IUpgradedKnowledgeBase;
import phenomenon.IPhenomenon;
import sim.interfaces.IExistsInWorld;
import sim.interfaces.IUnique;

/**
 * Thing which can be perceived and assigned properties
 * 
 * @author borah
 *
 */
public interface IUniqueExistence extends IUnique, IExistsInWorld {

	public IPropertyData getPropertyData(IUpgradedKnowledgeBase culture, Property property, boolean b);

	public void assignProperty(IUpgradedKnowledgeBase culture, Property property, IPropertyData data);

	default void assignProperty(IUpgradedKnowledgeBase culture, Property property) {
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
	 * Return the primary vessel of sensing this thing. <br>
	 * TODO Later, change this to also depend on the sense? maybe
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

	/**
	 * How different this thing is from the 'prototypical' example of its template.
	 * 
	 * @return
	 */
	public default float uniqueness() {
		return this.getSpecies().averageUniqueness();
	}

	public default boolean isActor() {
		return this instanceof Actor;
	}

	public default Actor getAsActor() {
		return (Actor) this;
	}

	public default boolean isPhenomenon() {
		return this instanceof IPhenomenon;
	}

	public default IPhenomenon getAsPhenomenon() {
		return (IPhenomenon) this;
	}

	public Random rand();

	/**
	 * Get the name used for display purposes in testing this from other similar
	 * entities
	 * 
	 * @return
	 */
	String getSimpleName();

}
