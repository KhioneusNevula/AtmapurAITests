package metaphysical.soul.generator;

import actor.Actor;

/**
 * Something which generates souls. Depending on the game, this may be different
 * things
 * 
 * @author borah
 *
 */
public interface ISoulGenerator {

	/**
	 * Called when entities are spawned for the first time
	 * 
	 * @param spawned
	 * @param firstSpawn
	 */
	void onSpawn(Actor spawned);

}
