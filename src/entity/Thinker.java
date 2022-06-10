package entity;

import java.util.Random;

import psych.Need;
import sim.World;
import sociology.sociocon.Sociocat;

public class Thinker extends Actor {

	private int satiation;
	private Random rand = new Random();

	public Thinker(World world, String name, int startX, int startY, int radius) {
		super(world, name, startX, startY, radius);
		this.getProfile().addSociocon(world.getOrCreateSociocon(Sociocat.PERSON, "people"));
		this.createMind(Need.SATIATION);
		this.satiation = 50;
	}

	@Override
	public void tick() {
		if (3 > rand.nextInt(6))
			satiation = Math.max(0, satiation - 1);

	}

	public int getHunger() {
		return satiation;
	}

	public void setHunger(int hunger) {
		this.satiation = hunger;
	}

	@Override
	public String toString() {
		return super.toString() + " mind: " + this.mind;
	}
}
