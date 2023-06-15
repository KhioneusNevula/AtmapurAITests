package humans;

import actor.LivingActor;
import biology.anatomy.ISpeciesTemplate;
import biology.systems.types.HungerSystem;
import biology.systems.types.ISensor;
import biology.systems.types.LifeSystem;
import biology.systems.types.SenseSystem;
import mind.Mind;
import sim.World;

public class Person extends LivingActor {

	public Person(World world, String name, ISpeciesTemplate species, int startX, int startY, int radius) {
		super(world, name, species, startX, startY, radius);
		this.setMind(new Mind(this));
		this.addSystems(new LifeSystem(this, 100), new SenseSystem(this,
				((Mind) this.getMind()).getKnowledgeBase().getSenses(), 10 * radius, ISensor.SIGHT),
				new HungerSystem(this, 30, 1));
		this.initBody();
	}

}
