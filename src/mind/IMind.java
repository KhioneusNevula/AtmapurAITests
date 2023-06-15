package mind;

import java.util.Collection;
import java.util.Random;

import actor.IUniqueExistence;
import mind.memory.IHasKnowledge;
import mind.relationships.IParty;

public interface IMind extends IParty, IHasKnowledge {

	public IUniqueExistence getOwner();

	public boolean isPartOf(IGroup group);

	/**
	 * The part of the mind that takes actions
	 */
	public Will getWill();

	public Random rand();

	/**
	 * Whether this mind is conscious; if unconscious, it should run unconscious
	 * ticks; otherwise conscious ticks
	 * 
	 * @return
	 */
	public boolean isConscious();

	public void consciousTick(long tick);

	public void unconsciousTick(long tick);

	/**
	 * gets the amount of time this mind has been awake (or awake before sleeping,
	 * if it is now sleeping);
	 * 
	 * @return
	 */
	public int getTimeAwake();

	/**
	 * The amount of time the mind slept; or the amount of time it was asleep last
	 * tie it slept
	 * 
	 * @Override
	 * 
	 * @return
	 */
	int getTimeAsleep();

	public default void mindTick(long tick) {
		if (this.isConscious()) {
			this.consciousTick(tick);
		} else {
			this.unconsciousTick(tick);
		}
	}

	/**
	 * Make the mind conscious
	 */
	void wakeUp(long time);

	/**
	 * Put the mind to sleep; make it unconscious
	 */
	void sleep(long time);

	/**
	 * Ticks when the mind is executing actions
	 */
	void actionTicks(long tick);

	public Collection<Culture> cultures();

}
