package actor.construction.physical;

import java.util.stream.Stream;

import actor.IUniqueEntity;
import actor.construction.properties.ISensableTrait;
import actor.construction.properties.SenseProperty;
import biology.sensing.ISense;
import civilization_and_minds.social.concepts.profile.Profile;
import sim.interfaces.IObjectType;
import sim.physicality.IInteractability;

public interface IVisage {

	/**
	 * Using {@link IInteractability} to indicate sensability mode to the given
	 * sense
	 * 
	 * @return
	 */
	public int sensabilityMode(ISense toSense);

	public Stream<? extends IComponentPart> getParts();

	/**
	 * Get parts that are outermost (and usually sense-able)
	 * 
	 * @return
	 */
	public Stream<? extends IComponentPart> getOutermostParts();

	/**
	 * Get (intact) parts that are either outermost or that have a surrounding part
	 * which is no longer present (or a hole), therefore exposing it
	 * 
	 * @return
	 */
	public Stream<? extends IComponentPart> getExposedParts();

	/**
	 * Get a salient general property of this entity, if it has properties which are
	 * not localized to specific parts
	 * 
	 * @param <A>
	 * @param property
	 * @param ignoreType whether to only get traits specific to the body part and
	 *                   not the type as a whole
	 * @return
	 */
	public <A extends ISensableTrait> A getGeneralProperty(SenseProperty<A> property, boolean ignoreType);

	/**
	 * Get all general properties that are not tied to specific parts
	 * 
	 * @return
	 */
	public Stream<SenseProperty<?>> getGeneralSensableProperties();

	/**
	 * If this template consists only of one main part. For example, a rock. s
	 * 
	 * @return
	 */
	public boolean hasSinglePart();

	/**
	 * For single-part actors, the component encompassing its entire entity. Throw
	 * exception if not single-part
	 * 
	 * @return
	 */
	public IComponentPart mainComponent();

	/**
	 * Change the sensability of this thing
	 * 
	 * @param newVisibility
	 */
	public void changeSensability(int newVisibility);

	/**
	 * gets the "type" of this visage, the species, etc
	 * 
	 * @return
	 */
	public IObjectType getObjectType();

	/**
	 * If this visage is an actor
	 * 
	 * @return
	 */
	default boolean isActorObject() {
		return this instanceof IPhysicalActorObject;
	}

	/**
	 * Returns this as a multipart entity
	 * 
	 * @param <A>
	 * @param <B>
	 * @param <C>
	 * @return
	 */
	default IPhysicalActorObject getAsActorObject() {
		return (IPhysicalActorObject) this;
	}

	/**
	 * Returns the owner of this visage
	 * 
	 * @return
	 */
	public IUniqueEntity getOwner();

	/**
	 * What profile this visage projects
	 */
	public Profile getProfile();

}
