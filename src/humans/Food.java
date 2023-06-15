package humans;

import java.awt.Color;

import actor.Actor;
import actor.IVisage;
import actor.SimpleVisage;
import sim.World;

public class Food extends Actor {

	private IVisage visage = new SimpleVisage();

	public Food(World world, String name, int startX, int startY, int radius) {
		super(world, name, startX, startY, radius);
		this.setOptionalColor(Color.GREEN.getRGB());

	}

	@Override
	public IVisage getVisage() {
		return visage;
	}

	// TODO food
	public int nourishment() {
		return 50;
	}

}
