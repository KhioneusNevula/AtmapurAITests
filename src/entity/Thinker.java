package entity;

import abilities.types.HungerSystem;
import abilities.types.LifeSystem;
import abilities.types.SystemType;
import psych_first.mind.Need;
import psych_first.perception.emotions.EmotionType;
import psych_first.perception.emotions.ILevel;
import psych_first.perception.senses.Sense;
import sim.World;

public class Thinker extends Actor {

	public Thinker(World world, String name, int startX, int startY, int radius) {
		super(world, world.getOrCreateTypeProfile("people"), name, startX, startY, radius);
		this.addInfo(Sense.SOUND.DISTANCE, 100).addInfo(Sense.SMELL.DISTANCE, 50).addInfo(Sense.SIGHT.DISTANCE, 100)
				.addInfo(Sense.SIGHT.ANGLE, 180); // TODO generalize
		this.createMind().initNeeds(Need.SATIATION).initEmotions(ILevel.ELevel.getAll(), EmotionType.getAll())
				.initSenses(Sense.SOUND, Sense.SMELL, Sense.SIGHT, Sense.TOUCH);

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
