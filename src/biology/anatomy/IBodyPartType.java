package biology.anatomy;

import java.util.Collection;

import actor.IComponentType;
import actor.IPartAbility;

/**
 * A generally static class representing the body parts of a creature. For any
 * class of this nature, editing properties should not work (if anything, it
 * should create a clone of the instance with the different properties). <br>
 * TODO allow for different materials to compose the body parts (tissue,
 * obviously) to govern physical properties. Allow for tracking of what has
 * muscle and what doesn't. Allow for skin-like body parts that wrap around
 * stuff idk. Allow for some way to assign sensable traits to the body parts.
 * Maybe some sort of "magical essence" type thing, or storage sacs for
 * projectile attacks and whatnot? Also connections, like if you cut the neck
 * you die lmao
 */

public interface IBodyPartType extends IComponentType {

	public static enum Abilities implements IPartAbility {
		THINK, PUMP_LIFE_ESSENCE, GESTATE, STORE_EGGS, GIVE_BIRTH, FERTILIZE, STORE_SEED, PREHENSILE, GRASP, WALK,
		SPEAK, EAT, CAST_POWER, FLY;

		@Override
		public String getName() {
			return name();
		}
	}

	/**
	 * If this part allows for thoughts and thereby anchors the consciousness and
	 * whatnot
	 * 
	 * @return
	 */
	public boolean thinks();

	/**
	 * If this part is fundamental to the movement of blood/life essence through the
	 * body
	 * 
	 * @return
	 */
	public boolean pumpsLifeEssence();

	/**
	 * If this body part has life essence (e.g. blood) moving through it
	 */
	/* public boolean hasLifeEssence(); */

	/**
	 * If this body part can feel pain and the like
	 * 
	 * @return
	 */
	/* public boolean hasNerves(); */

	/**
	 * If this body part eats food
	 * 
	 * @return
	 */
	/* public boolean eats(); */

	/**
	 * If this body part gestates offspring
	 * 
	 * @return
	 */
	public boolean gestates();

	/**
	 * If this body part stores eggs
	 * 
	 * @return
	 */
	public boolean storesEggs();

	/**
	 * If this body part is needed to give birth to live young/ produce an egg;
	 * obviously, needs to be a hole or else lots of pain
	 * 
	 * @return
	 */
	public boolean givesBirth();

	/**
	 * IF this body part digests food
	 * 
	 * @return
	 */
	/* public boolean digests(); */

	/**
	 * If this body part is needed to fertilize offspring. Needs a part that stores
	 * seed to function ofc
	 * 
	 * @return
	 */
	public boolean fertilizes();

	/**
	 * If this body part stores the seed needed to produce offspring
	 */
	public boolean storesSeed();

	/**
	 * Whether this body part is a coiled up stringy mass (basically just used for
	 * guts so they can (?) trail behind if damaged (?))
	 * 
	 * @return
	 */
	/* public boolean stringy(); */

	/**
	 * Whether this body part is a long, thin, prehensile appendage, such as a tail
	 * or tentacle
	 * 
	 * @return
	 */
	public boolean prehensile();

	/**
	 * Indicates what forms of tissue should consist this body layer, e.g. flesh,
	 * fat, muscle. Tissue tags do not transfer to subparts of parent parts
	 * 
	 * @return
	 */
	public Collection<String> tissueTags();

}
