package humans;

import actor.UpgradedSentientActor;
import biology.anatomy.ISpeciesTemplate;
import biology.systems.types.HungerSystem;
import biology.systems.types.LifeSystem;
import sim.World;

public class UpgradedPerson extends UpgradedSentientActor {

	public UpgradedPerson(World world, String name, ISpeciesTemplate species, int startX, int startY, int radius) {
		super(world, name, species, startX, startY, radius);
		this.initMind();
		this.initBody();
		this.addSystems(new LifeSystem(this, 100),
				new HungerSystem(this, Math.max(30 + rand.nextGaussian() * 30, 20), 1));
	}

}
