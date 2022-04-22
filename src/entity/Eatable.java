package entity;

import sim.World;
import sociology.Sociocon;
import sociology.Sociocontype;
import sociology.Sociocontype.SocioconArgument;

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
	public Eatable(World world, String foodName, int startX, int startY, Integer nourishment) {
		super(world, foodName, startX, startY);

		this.getProfile().addSociocon(world.getSocioconMap(Sociocontype.FOOD).computeIfAbsent(foodName,
				(n) -> new Sociocon(foodName, Sociocontype.FOOD)));
		if (nourishment != null)
			this.getProfile().getSociocon(Sociocontype.FOOD).setValue(SocioconArgument.FOOD_NOURISHMENT, nourishment);

	}

	public int getNourishment() {
		return nourishment;
	}

}
