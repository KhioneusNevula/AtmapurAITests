package metaphysical;

import java.util.Collection;

import actor.Actor;
import actor.construction.physical.IComponentPart;
import civilization_and_minds.mind.IMind;
import sim.interfaces.IUniqueThing;

/**
 * A "Spirit Object" is something which cannot exist independently like an
 * Actor, and instead is applied to an Actor. This can be things like
 * enchantments, but also souls. Spirit objects can be tethered to actors, or to
 * specific components of their body/structure
 * 
 * @author borah
 *
 */
public interface ISpiritObject extends IUniqueThing {

	public static enum SpiritType {
		/** souls, for living things */
		SOUL,
		/** enchantments, as cast by magic */
		ENCHANTMENT,
		/** things which possess, such as ghosts or whatever idk */
		POSSESSOR,

		/** other things not in previous categories */
		OTHER;

		public boolean isSoul() {
			return this == SOUL;
		}
	}

	/**
	 * Gets the type of spirit object this is.
	 * 
	 * @return
	 */
	public SpiritType getSpiritType();

	/**
	 * Returns the thing which contains this spirit
	 * 
	 * @return
	 */
	public Actor getContainerEntity();

	/**
	 * How many parts this spirit is tethered to currently. This should give the
	 * size of the collection returned by getTethers
	 * 
	 * @return
	 */
	public int tetherCount();

	/**
	 * Returns the parts of the container that this spirit is attached to; behavior
	 * is undefined if tethered to whole.
	 * 
	 * @return
	 */
	public Collection<IComponentPart> getTethers();

	/**
	 * Get the single tether of this entity, if it has only a single tether.
	 * Behavior is undefined if it has multiple tethers or is tethered to whole
	 * 
	 * @return
	 */
	public IComponentPart getTether();

	/**
	 * If this spirit object is not tethered to a specific part
	 * 
	 * @return
	 */
	public boolean isTetheredToWhole();

	/**
	 * If this spirit object is only tethered to one part
	 * 
	 * @return
	 */
	public boolean isTetheredToSinglePart();

	/**
	 * Whether this spirit is tethered to anything
	 * 
	 * @return
	 */
	public boolean isTethered();

	/**
	 * Reattach this spirit to a new entity and give it new tethers. Can have
	 * undefined behavior, and should only be used in specific interactions. Put
	 * null for newparts if tethering to whole. Updating tether references in
	 * component part is responsibility of caller
	 * 
	 * @param newActor
	 * @param newPart
	 */
	public void tetherSpirit(Actor newActor, Collection<IComponentPart> newParts);

	/**
	 * Update this spirit per tick
	 * 
	 * @param worldTick
	 */
	public void tick(long worldTick);

	/**
	 * Called when a part of the containing being is updated, e.g. state change.
	 * Recheck tethers if needed.
	 * 
	 * @param part
	 */
	public void onPartUpdate(IComponentPart part);

	/**
	 * Whether the containing entity should remove this spirit from itself. Spirits
	 * are always removed if the part they are attached to is destroyed
	 * 
	 * @param worldTick
	 * @return
	 */
	public boolean shouldRemove(long worldTick);

	/**
	 * Called when spirit is marked for removal to ensure it handles all necessary
	 * things relating to removal. Only called after all spirits in an entity
	 * update.
	 * 
	 * @param worldTick
	 */
	public void onRemove(long worldTick);

	/**
	 * Whether this spirit has a mind (i.e. a {@link IMind})
	 * 
	 * @return
	 */
	public boolean hasMind();

	/**
	 * Gets the mind of this spirit; undefined behavior if this spirit does not have
	 * a mind
	 * 
	 * @return
	 */
	public IMind getMind();
}
