package humans;

import actor.SentientActor;
import biology.anatomy.ISpeciesTemplate;
import biology.systems.types.HungerSystem;
import biology.systems.types.ISensor;
import biology.systems.types.LifeSystem;
import biology.systems.types.SenseSystem;
import mind.Mind;
import sim.World;

public class Person extends SentientActor {

	public Person(World world, String name, ISpeciesTemplate species, int startX, int startY, int radius) {
		super(world, name, species, startX, startY, radius);
		this.initMind();
		this.initBody();
		this.addSystems(new LifeSystem(this, 100),
				new SenseSystem(this, ((Mind) this.getMind()).getKnowledgeBase().getSenses(), 300, ISensor.SIGHT),
				new HungerSystem(this, 200 + rand.nextGaussian() * 30, 1));
	}

}
