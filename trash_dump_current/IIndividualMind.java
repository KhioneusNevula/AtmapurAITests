package mind;

import java.util.Collection;
import java.util.Random;

import actor.IUniqueExistence;
import mind.concepts.type.IProfile;
import mind.memory.IEmotions;
import mind.memory.IMindMemory;
import mind.personality.Personality;

public interface IIndividualMind extends IEntity {

	public IUniqueExistence getOwner();

	public boolean isPartOf(IProfile group);

	IMindMemory getKnowledgeBase();

	/**
	 * The part of the mind that takes actions
	 */
	public IWill getWill();

	public Random rand();

	/**
	 * the personality of this mind
	 * 
	 * @return
	 */
	public Personality personality();

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

	public boolean hasEmotions();

	public IEmotions getEmotions();

	public boolean isNotViable();

	public void kill();

	/**
	 * Which cultures this mind can read from (i.e. know knowledge from) but cannot
	 * affect (i.e. cannot send trends to). <br>
	 * TODO would it be better to just have isolated minds make some form of copy of
	 * the culture(?)
	 * 
	 * @param culture
	 * @return
	 */
	public boolean isIsolatedFrom(Culture culture);

	@Override
	default boolean isIndividual() {
		return true;
	}

	@Override
	default boolean isGroup() {
		return false;
	}

}
