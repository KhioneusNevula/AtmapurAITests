package actor.construction.properties;

import java.util.UUID;

import civilization_and_minds.social.concepts.IConcept;

/**
 * Merely indicates a specific, sensable trait.
 * 
 * @author borah
 *
 */
public interface ISensableTrait extends IConcept {

	/**
	 * If this particular trait is unique to the entity it is on.
	 * 
	 * @return
	 */
	public boolean isUnique();

	/**
	 * If {@link #isUnique()} is false, undefined behavior. Returns a unique
	 * specific id for a trait.
	 * 
	 * @return
	 */
	public default UUID getUniqueID() {
		return null;
	}

	/**
	 * whther this trait can actually be sensed
	 * 
	 * @return
	 */
	public default boolean canBeSensed() {
		return true;
	}

	/**
	 * For Unique traits, this is the value it most closely resembles. Return self
	 * if not unique, or especially unique with no resemblance to other things
	 * 
	 * @return
	 */
	public default ISensableTrait resembles() {
		return this;
	}
}
