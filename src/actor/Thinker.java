package actor;

import abilities.SystemType;
import abilities.types.BreathSystem;
import abilities.types.HungerSystem;
import abilities.types.LifeSystem;
import psychology.social.concepts.TypeClass;
import sim.World;

public class Thinker extends Actor {

	public Thinker(World world, TypeClass<? extends Thinker> type, String name, int startX, int startY, int radius) {
		super(world, type, name, startX, startY, radius);

		this.addSystems(new LifeSystem(this, 100), new HungerSystem(this, 400, 0.1), new BreathSystem(this, 10, "air"));
		this.createNaturalSoul();
	}

	public int eat(Eatable edible) {
		return this.getSystem(SystemType.HUNGER).eat(edible);
	}

}
