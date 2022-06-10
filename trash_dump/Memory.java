package _nonsense;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import entity.Actor;

public class Memory {

	private Brain brain;
	private Random rand = new Random();
	private Set<Actor> forgotten = new HashSet<>();

	public Memory(Brain brain) {
		this.brain = brain;
	}

	public void actorCreated(Actor a) {
		if (rand.nextInt(14) < 5) {
			forgotten.add(a);
		}
	}

	/**
	 * Return null if none remembered (random chance)
	 * 
	 * @param cond
	 * @return
	 */
	public Profile findRandom(ProfileCondition<?> cond) {
		Set<Actor> acts = brain.getOwner().getWorld().getFor(cond, brain.getOwner());

		acts.removeAll(forgotten);
		return acts.stream().map((ao) -> ao.getProfile()).findAny().orElse(null);
	}
}
