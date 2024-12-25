package actor.construction.physical;

import java.util.Collection;

import actor.construction.properties.IAbilityStat;
import actor.construction.properties.ISensableTrait;
import actor.construction.properties.SenseProperty;
import biology.anatomy.IBodyPartType;
import biology.sensing.ISense;

/**
 * A type of a part of an actor
 * 
 * @author borah
 *
 */
public interface IComponentType extends Comparable<IComponentType> {

	public static enum Side {
		LEFT, MIDDLE, RIGHT
	}

	public static enum Height {
		BELOW, ALIGNED, ABOVE
	}

	public static enum Face {
		FRONT, MIDDLE, BACK
	}

	public String getName();

	/**
	 * some body parts are attached to another larger (or otherwise more closely
	 * connected) part. These are such parts. Every body part must have a parent,
	 * except for the Root part. Most body parts parent off the part known as simply
	 * "Body". Parts without a parent are considered as floating separately from the
	 * body in some way.
	 * 
	 * @return
	 */
	public String getParent();

	/**
	 * some body parts are within other body parts; this is the part that surrounds
	 * those parts. Note that a parent body part that is within another body part
	 * considers all its children body parts as *also* within that body part.
	 * 
	 * @return
	 */
	public String getSurroundingPart();

	/**
	 * whether this body part is a hole, like the mouth
	 * 
	 * @return
	 */
	public boolean isHole();

	/**
	 * Whether this is the "root" part, i.e. the main part that is used to track
	 * movement. There can only be one of these.
	 * 
	 * @return
	 */
	public boolean isRoot();

	/**
	 * If this is a body part with multiple versions of itself in the same area
	 * (e.g. spiders have eight legs altogether) then this allows you to change that
	 * count (given that it is a positive number)
	 * 
	 * @return
	 */
	public int count();

	/**
	 * If this part is uncovered by the part surrounding its Parent part. E.g. horns
	 * are uncovered by the skin surrounding them
	 * 
	 * @return
	 */
	public boolean uncovered();

	/**
	 * The size of this body part relative to all the others; consider 1 as the size
	 * of the body as a whole.
	 * 
	 * @return
	 */
	public float size();

	/**
	 * whether this is left, right, or in the middle of its parent part
	 * 
	 * @return
	 */
	public Side getSide();

	/**
	 * See {@link IBodyPartType#getSide()}
	 * 
	 * @return
	 */
	default boolean onLeft() {
		return getSide() == Side.LEFT;
	}

	/**
	 * See {@link IBodyPartType#getSide()}
	 * 
	 * @return
	 */
	default boolean onRight() {
		return getSide() == Side.RIGHT;
	}

	/**
	 * Whether this part is above, below, or aligned with its parent part
	 * 
	 * @return
	 */
	public Height getHeight();

	/**
	 * See {@link IBodyPartType#getHeight()}
	 * 
	 * @return
	 */
	default boolean above() {
		return getHeight() == Height.ABOVE;
	}

	/**
	 * See {@link IBodyPartType#getHeight()}
	 * 
	 * @return
	 */
	default boolean below() {
		return getHeight() == Height.BELOW;
	}

	/**
	 * Whether this body part is in front, back, or middle of its parent part
	 * 
	 * @return
	 */
	public Face getFace();

	/**
	 * See {@link IBodyPartType#getFace()}
	 * 
	 * @return
	 */
	default boolean inFront() {
		return getFace() == Face.FRONT;
	}

	/**
	 * See {@link IBodyPartType#getFace()}
	 * 
	 * @return
	 */
	default boolean inBack() {
		return getFace() == Face.BACK;
	}

	/**
	 * If this body part is part of a class that it is NOT parented to (e.g. a left
	 * and right foot) this can be used to group them; give the same category name
	 * for both; else return null
	 * 
	 * @return
	 */
	public String category();

	/**
	 * whether this part has a specific ability
	 * 
	 * @param ability
	 * @return
	 */
	public boolean hasAbility(IPartAbility ability);

	@Override
	default int compareTo(IComponentType o) {
		return getName().compareTo(o.getName());
	}

	public <T extends ISensableTrait> T getTrait(SenseProperty<T> prop);

	public Collection<SenseProperty<?>> getSensableProperties();

	/**
	 * Return the default value of the given ability stat for component parts like
	 * this for the specific ability
	 * 
	 * @param <T>
	 * @param stat
	 * @return
	 */
	public <T> T getDefaultAbilityStat(IPartAbility ability, IAbilityStat<T> stat);

	/**
	 * Gets all used ability stats for the given ability
	 * 
	 * @return
	 */
	public Collection<IAbilityStat<?>> getDefaultAbilityStats(IPartAbility ability);

	/**
	 * Gets default senses of a component part like this
	 * 
	 * @return
	 */
	public Collection<ISense> getDefaultSenses();

	/**
	 * The abilities this part has
	 * 
	 * @return
	 */
	public Collection<IPartAbility> abilities();

	/**
	 * Gets default nutrition content of this bdoy part
	 * 
	 * @return
	 */
	public float defaultNutritionContent();

}
