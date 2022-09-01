package actor;

import java.util.HashMap;
import java.util.Map;

import abilities.ISystemHolder;
import main.WorldGraphics;
import psychology.social.concepts.TypeClass;
import sim.World;

public class Eatable extends Actor {

	private int nourishment;
	private static Map<TypeClass<?>, Integer> counts = new HashMap<>();

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
	public Eatable(World world, TypeClass<? extends Eatable> foodType, int startX, int startY, int radius,
			Integer nourishment) {
		super(world, foodType,
				foodType.getName() + counts.computeIfAbsent(foodType, (a) -> counts.getOrDefault(foodType, 0) + 1),
				startX, startY, radius);

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
	protected void render(WorldGraphics g) {
		g.alpha(50);
		super.render(g);
	}

}
