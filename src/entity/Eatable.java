package entity;

import abilities.ISystemHolder;
import sim.World;

public class Eatable extends Actor {

	private int nourishment;

	/**
	 * If there is already a sociocon, its nourishment will be changed. Nourishment
	 * may be null.
	 * 
	 * @param world
	 * @param foodName
	 * @param startX
	 * @param startY
	 * @param nourishment
	 */
	public Eatable(World world, String foodName, int startX, int startY, int radius, Integer nourishment) {
		super(world, world.getOrCreateTypeProfile(foodName), foodName, startX, startY, radius);

		this.nourishment = nourishment;

	}

	public int getNourishment() {
		return nourishment;
	}

	public void getEaten(ISystemHolder byActor) {
		this.nourishment = 0;
		this.setRadius((int) (this.getRadius() / 4.0));
	}

	@Override
	protected void render() {
		this.getWorld().alpha(50);
		super.render();
	}

}
