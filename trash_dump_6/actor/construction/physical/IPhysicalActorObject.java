package actor.construction.physical;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import actor.Actor;
import biology.sensing.ISense;
import metaphysical.ISpiritObject;
import metaphysical.ISpiritObject.SpiritType;
import metaphysical.soul.ISoul;
import metaphysical.soul.generator.ISoulGenerator;

/**
 * Used to represent an objct or visage of a physical actor, with multiple parts
 * and whatnot
 * 
 * @author borah
 *
 */
public interface IPhysicalActorObject extends IVisage {

	public static enum HitboxType {
		CIRCLE, RECTANGLE;

		public boolean isCircle() {
			return this == CIRCLE;
		}

		public boolean isRect() {
			return this == RECTANGLE;
		}
	}

	/**
	 * Whether hitbox is circle or rect
	 * 
	 * @return
	 */
	public HitboxType getHitboxType();

	/**
	 * radius of hitbox, if circle. undefined behavior if not
	 * 
	 * @return
	 */
	public int getHitboxRadius();

	/**
	 * width if rectangle
	 * 
	 * @return
	 */
	public int getHitboxWidth();

	public int getHitboxHeight();

	/**
	 * Mass of this object in kilgorams
	 */
	public float getMass();

	/**
	 * Whether all the necessary steps have been performed to properly make this
	 * body
	 * 
	 * @return
	 */
	public boolean isBuilt();

	/**
	 * Get parts that do not move with another part
	 * 
	 * @return
	 */
	public Map<UUID, ? extends IComponentPart> getPartsWithoutParent();

	/**
	 * Gets the specific type of this thing
	 * 
	 * @return
	 */
	public IActorType getObjectType();

	/**
	 * Get all parts with this specific ability
	 * 
	 * @param ability
	 * @return
	 */
	public Collection<? extends IComponentPart> getPartsWithAbility(IPartAbility ability);

	Map<String, ? extends IComponentType> getPartTypes();

	/**
	 * Return a list of body parts, tissues, and other information
	 * 
	 * @return
	 */
	public String report();

	/**
	 * What physicality this body has
	 * 
	 * @return
	 */
	int physicalityMode();

	/**
	 * Change the physicality of this thing
	 * 
	 * @param newPhysicality
	 */
	public void changePhysicality(int newPhysicality);

	/**
	 * Return true if this actor is compltely destroyed and ought to be removed
	 * 
	 * @return
	 */
	public boolean completelyDestroyed();

	/**
	 * Cause a physical change to the given part and update any relevant tracking,
	 * including attached spirits!
	 * 
	 * @param part
	 */
	public void updatePart(IComponentPart part);

	/**
	 * Return true if this is an ensouled being which is now dead. Not relevant for
	 * non-souled beings
	 * 
	 * @return
	 */
	public boolean isDead();

	/**
	 * Get all spirits in this actor of given type
	 * 
	 * @return
	 */
	public Collection<? extends ISpiritObject> getContainedSpirits(SpiritType type);

	/**
	 * Gets all spirits on the given part
	 * 
	 * @param part
	 * @return
	 */
	public Collection<? extends ISpiritObject> getContainedSpirits(IComponentPart part);

	/**
	 * Whether the part contains the spirit
	 * 
	 * @param spir
	 * @param part
	 * @return
	 */
	public boolean containsSpirit(ISpiritObject spir, IComponentPart part);

	/**
	 * Update stored data, removing references to this spirit. Updating tether
	 * references in spirit itself is the responsibility of the caller.
	 * 
	 * @param spirit
	 */
	public void removeSpirit(ISpiritObject spirit);

	/**
	 * Attach the given spirit to this object. Leave tethers as null if tethered to
	 * whole. Updating tether references in spirit itself is the responsibility of
	 * the caller.
	 * 
	 * @param spirit
	 * @param tethers
	 */
	public void tetherSpirit(ISpiritObject spirit, Collection<IComponentPart> tethers);

	/**
	 * If the given spirit is contained in this object
	 * 
	 * @param spirit
	 * @return
	 */
	public boolean containsSpirit(ISpiritObject spirit);

	/**
	 * Simple method called right after when a soul generator gives a soul to a
	 * being upon its first creation. not called for subsequent creations.
	 * 
	 * @param soul
	 * @return
	 */
	public default void onGiveFirstSoul(ISoul soul, ISoulGenerator soulgen) {

	}

	/**
	 * Whether this entity has a soul
	 * 
	 * @return
	 */
	public boolean hasSoul();

	/**
	 * Gets the main soul of this being, or null if it lacks a soul or has multiple
	 * souls
	 * 
	 * @return
	 */
	default ISoul getSoulReference() {
		return null;
	}

	/**
	 * Get all actors held by this
	 * 
	 * @return
	 */
	public Collection<Actor> getHeld();

	/**
	 * Get actor held by this part
	 * 
	 * @param byPart
	 * @return
	 */
	public Actor getHeld(IComponentPart byPart);

	/**
	 * If this actor is held by this physical
	 * 
	 * @param a
	 * @return
	 */
	public boolean isHolding(Actor a);

	/**
	 * Get all parts holding this actor
	 * 
	 * @param a
	 * @return
	 */
	public Collection<? extends IComponentPart> getHoldingParts(Actor a);

	/**
	 * Return false if the part cannot pick up the actor due to strength or
	 * something similar. Throw exception if part lacks ability to grasp or the self
	 * is held by the given actor. Succeed if the actor is already held by another
	 * part of the body; in fact, just register the actor as being held by two
	 * parts, basically.
	 * 
	 * @param withPart
	 * @param actor
	 * @return
	 */
	public boolean pickUp(IComponentPart withPart, Actor actor);

	/**
	 * Put down whatever is held by the given part; return what was put down. Might
	 * not completely put down the actor, since it may be held by multiple parts
	 * 
	 * @param partHolding
	 */
	public Actor putDown(IComponentPart partHolding);

	/**
	 * Put down the given actor; return true if it was held
	 * 
	 * @param actor
	 */
	public boolean putDown(Actor actor);

	/**
	 * Put down the given actor by the given part; return true if the given actor
	 * was put down from the given part. Might not put the actor down fully if it si
	 * held by multiple parts.
	 * 
	 * @param put
	 * @param down
	 * @return
	 */
	public default boolean putDown(IComponentPart put, Actor down) {
		return this.putDown(put) == down;
	}

	/**
	 * Get all senses this actor object has access to
	 */
	public Stream<? extends ISense> getSenses();

}
