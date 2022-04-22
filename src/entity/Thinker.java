package entity;

import psych.Brain;
import sim.World;
import sociology.Sociocon;
import sociology.Sociocontype;

public class Thinker extends Actor {

	private Brain brain;

	public Thinker(World world, String name, int startX, int startY) {
		super(world, name, startX, startY);
		this.getProfile().addSociocon(world.getSocioconMap(Sociocontype.PERSON).computeIfAbsent("People",
				(n) -> new Sociocon("People", Sociocontype.PERSON)));
		this.brain = new Brain(this);
	}

	@Override
	public void tick() {
	}

	@Override
	public void notifyOfSpawn(Actor e) {
		this.brain.getMemory().actorCreated(e);
	}

}
