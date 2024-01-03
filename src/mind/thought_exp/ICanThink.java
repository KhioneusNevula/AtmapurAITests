package mind.thought_exp;

import java.util.Collection;
import java.util.Random;

import actor.Actor;
import actor.IPartAbility;
import biology.systems.types.ISensor;
import mind.concepts.type.IProfile;
import mind.concepts.type.SenseProperty;
import mind.personality.Personality;
import mind.relationships.IParty;
import mind.thought_exp.IThought.IThoughtType;
import mind.thought_exp.actions.IActionThought;
import mind.thought_exp.memory.UpgradedTraitsMemory;
import phenomenon.IPhenomenon;
import sim.World;

public interface ICanThink extends IUpgradedHasKnowledge, IParty {

	/**
	 * Releases all ability slots owned by this thought
	 * 
	 * @param thought
	 */
	void releaseAbilitySlots(IThought thought);

	/**
	 * reserve the given ability slots for the given thought
	 * 
	 * @param slots
	 */
	void reserveAbilitySlots(Collection<IPartAbility> slots, IThought thought);

	/**
	 * Return true if any of these slots are full
	 * 
	 * @param slots
	 * @return
	 */
	boolean isAnySlotFull(Collection<IPartAbility> slots);

	/**
	 * Gets the required ability slots that would be taken up by the given action
	 * 
	 * @param action
	 * @return
	 */
	Collection<IPartAbility> getRequiredAbilitySlots(IActionThought action);

	/**
	 * The default chance this individual loses focus on a thought
	 * 
	 * @return
	 */
	float loseFocusChance();

	/**
	 * Clears the mind of thoughts
	 */
	void forgetAllThoughts();

	/**
	 * Forget all thoughts of the given type, returning the forgotten thoughts
	 * 
	 * @param type
	 */
	Collection<IThought> forgetAllThoughts(IThoughtType type);

	/**
	 * @Override
	 * 
	 *           Forgets the given thought, calling "interrupt" on it if it is not
	 *           already finished. Throw exception if the thought is somehow not
	 *           from this mind
	 * 
	 * @param thought
	 * @return
	 */
	void forgetThought(IThought thought);

	/**
	 * Return true if we haven't yet reached the thought cap for this thought type
	 * 
	 * @param type
	 * @return
	 */
	boolean canHaveMoreThoughts(IThoughtType type);

	/**
	 * Insert a specific thought into the mind. If the mind is overfull, compare
	 * this thought @Override
	 * 
	 * @Override with thoughts in the mind of this individual, and roll a random
	 *           chance as to whether to replace the given thought (based on the
	 *           force); if none of that succeeds, return false
	 * 
	 * @param thought
	 * @param force   the probability this thought will replace another thought.
	 *                usually determined using stress of its associated emotion. If
	 *                force is 1 or higher, the thought will be forced in regardless
	 *                of size
	 * @return whether the thought could be inserted
	 */
	boolean insertThought(IThought thought, float force);

	/**
	 * Active thoughts of the given type
	 * 
	 * @param type
	 * @return
	 */
	Collection<IThought> getActiveThoughtsOfType(IThoughtType type);

	/**
	 * Thoughts which are to be revisited later
	 * 
	 * @return
	 */
	public Collection<IThought> getPausedThoughts();

	/**
	 * Thoughts which are currently active
	 * 
	 * @return
	 */
	public Collection<IThought> getActiveThoughts();

	/**
	 * Paused thoughts of the given type
	 * 
	 * @param type
	 * @return
	 */
	public Collection<IThought> getPausedThoughtsOfType(IThoughtType type);

	/**
	 * Get all the paused and active thoughts in this mind as an iterable
	 * 
	 * @return
	 */
	public Iterable<IThought> thoughts();

	/**
	 * Gets a collection of thoughts by type
	 * 
	 * @param type
	 * @return
	 */
	public Iterable<IThought> getThoughtsByType(IThoughtType type);

	/**
	 * Whether this mind has active or paused thoughts of the given type (or whether
	 * there are no such thoughts)
	 * 
	 * @param type
	 * @return
	 */
	public boolean hasThoughtsOfType(IThoughtType type);

	/**
	 * Gets the maximum thoughts of a given type
	 * 
	 * @param type
	 * @return
	 */
	public int getMaxThoughtsFor(IThoughtType type);

	/**
	 * The mind's random generator
	 * 
	 * @return
	 */
	public Random rand();

	/**
	 * for thoughts focusing on multiple objects
	 * 
	 * @return
	 */
	public int getMaxFocusObjects();

	/**
	 * Get the personality values of this entity
	 * 
	 * @return
	 */
	public Personality personality();

	/**
	 * Gets all mechanisms of sensing this entity uses
	 * 
	 * @return
	 */
	Collection<ISensor> getSenses();

	/**
	 * Senses the traits of this specific actor
	 * 
	 * @param actor
	 * @param worldTick
	 * @return
	 */
	public UpgradedTraitsMemory senseTraits(Actor actor, World world, long worldTick);

	/**
	 * Gets the sensed actor associated with this profile
	 * 
	 * @param profile
	 * @param worldTick
	 * @return
	 */
	public Actor getSensedActor(IProfile profile);

	/**
	 * Returns the actors that can be sensed by this mind using its existing senses.
	 * Every tick, this list is updated once when the method is first called, and
	 * will not be updated until the next tick
	 * 
	 * @param worldTick
	 * @return
	 */
	public Collection<Actor> senseActors(World world, long worldTick);

	/**
	 * Returns the phenomena that can be sensed by this mind using its existing
	 * senses when the method is first called
	 * 
	 * @param worldTick
	 * @return
	 */
	public Collection<IPhenomenon> sensePhenomena(World world, long worldTick);

	/**
	 * Sense ambient properties of the environment (ambient smells, sounds, etc)
	 * 
	 * @param <T>
	 * @param property
	 * @param worldTick
	 * @return
	 */
	public <T> T senseThings(SenseProperty<T> property, World world, long worldTick);

	/**
	 * Insert thought; if this fails, insert it but paused it
	 * 
	 * @param thought
	 * @param force
	 */
	void insertOrPauseThought(IThought thought, float force);

	boolean isPaused(IThought thought);

	/**
	 * Whether the mind is thinking this thought, paused or otherwsie
	 * 
	 * @param thought
	 * @return
	 */
	boolean hasThought(IThought thought);

	public void kill();

}
