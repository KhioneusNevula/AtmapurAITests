package entity;

import sim.World;
import sociology.sociocon.Sociocat;

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
		super(world, foodName, startX, startY, radius);

		this.getProfile().addSociocon(world.getOrCreateSociocon(Sociocat.FOOD, foodName));
		if (nourishment != null)
			this.getProfile().setValue(Sociocat.FOOD.getProperty("nourishment"), nourishment);

	}

	public int getNourishment() {
		return nourishment;
	}

}
