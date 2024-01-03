package mind.thought_exp;

import java.util.Collection;

import actor.UpgradedSentientActor;
import mind.action.WillingnessMatrix;
import mind.memory.IEmotions;
import mind.thought_exp.IThought.IThoughtType;
import mind.thought_exp.memory.IBrainMemory;

public interface IUpgradedMind extends ICanThink {

	/**
	 * Tick the mind's thoughts while it is conscious
	 * 
	 * @param worldTicks
	 */
	public void tickConsciousThoughts(long worldTicks);

	/**
	 * Tick the mind's actions while it is conscious
	 * 
	 * @param worldTicks
	 */
	public void tickActions(long worldTicks);

	/**
	 * Tick the mind's thoughts while it is unconscious
	 * 
	 * @param worldTicks
	 */
	public void tickUnconscious(long worldTicks);

	public IBrainMemory getMemory();

	public IEmotions emotions();

	/**
	 * Calculates the willingness matrix of a thought so it can be compared to
	 * another thought to replace it; usually based on stress and priority. A result
	 * of >=1 means the thought will be thunk regardless of present thought count
	 * 
	 * @param thought
	 * @return
	 */
	public WillingnessMatrix calculateForce(IThought thought);

	/**
	 * Whether this mind is functionally alive/active
	 * 
	 * @return
	 */
	public boolean isActive();

	/**
	 * Marks the mind as no longer active/alive
	 * 
	 * @return
	 */
	public void deactivate();

	/**
	 * Marks the mind as active/alive once more
	 * 
	 * @return
	 */
	public void reactivate();

	/**
	 * If the mind is currently conscious
	 * 
	 * @return
	 */
	public boolean conscious();

	/**
	 * If this mind is asleep; if conscious is false and asleep is false, this mind
	 * has been knocked out assumably
	 * 
	 * @return
	 */
	public boolean asleep();

	/**
	 * Makes this mind asleep
	 */
	public void sleep();

	/**
	 * Makes this mind unconscious but not asleep
	 */
	public void knockOut();

	/**
	 * Makes this mind conscious
	 */
	public void wake();

	/**
	 * ticks since the mind was last asleep
	 * 
	 * @return
	 */
	public int ticksSinceLastRest();

	public UpgradedSentientActor getOwner();

	@Override
	default boolean isIndividual() {
		return true;
	}

	@Override
	default boolean isGroup() {
		return false;
	}

	IUpgradedMind init(Collection<? extends IThoughtType> types);

	void tickSenses(long worldTicks);
}
