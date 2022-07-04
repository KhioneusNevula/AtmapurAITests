package entity;

import abilities.types.HungerSystem;
import abilities.types.LifeSystem;
import abilities.types.SystemType;
import culture.Culture;
import psych_first.mind.Need;
import sim.World;

public class Thinker extends Actor {

	public Thinker(World world, String name, int startX, int startY, int radius) {
		super(world, world.getOrCreateTypeProfile("people"), name, startX, startY, radius);
		this.createMind(world.getCulture(Culture.ORGANIC), world.getCulture(Culture.TOOL_USER),
				world.getCulture(Culture.SENTIENT)).initNeeds(Need.SATIATION);
		this.addSystems(new LifeSystem(this, 100), new HungerSystem(this, 100, 1));
	}

	/**
	 * eat the thing; fail to eat if it can't be swallowed whole or provides no
	 * nourishment TODO make this more of a continuum
	 */
	public int eat(Eatable edible) {
		return this.getSystem(SystemType.HUNGER).eat(edible);
	}

	@Override
	public String toString() {
		return super.toString() + " mind: " + this.mind;
	}
}
