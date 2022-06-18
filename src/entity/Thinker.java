package entity;

import java.util.Random;
import java.util.Set;

import psych.mind.Need;
import sim.World;
import sociology.sociocon.Sociocat;

public class Thinker extends Actor {

	private static int MAX_HUNGER = 100;
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
		if (3 > rand.nextInt(15))
			setHunger(getHunger() - 1);
		Set<Actor> colls = getWorld().getCollisions(this, (a) -> true);
		for (Actor a : colls) {
			if (a instanceof Eatable e && rand.nextInt(18) < 7) {
				this.setHunger(this.getHunger() + e.getNourishment());
			}
		}

	}

	public int getHunger() {
		return satiation;
	}

	public void setHunger(int hunger) {
		this.satiation = Math.min(Math.max(0, hunger), MAX_HUNGER);
	}

	@Override
	public String toString() {
		return super.toString() + " mind: " + this.mind;
	}
}
