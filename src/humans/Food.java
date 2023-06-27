package humans;

import java.awt.Color;

import actor.Actor;
import actor.ActorType;
import actor.IVisage;
import actor.SimpleVisage;
import biology.systems.types.ISensor;
import mind.concepts.type.BasicProperties;
import mind.memory.IPropertyData;
import sim.World;

public class Food extends Actor {

	private IVisage visage = new SimpleVisage();
	public static final ActorType FOOD_TYPE = ActorType.Builder.start("food")
			.addHint(BasicProperties.FOOD, () -> IPropertyData.PRESENCE, ISensor.SIGHT).build();

	public Food(World world, String name, int startX, int startY, int radius) {
		super(world, name, FOOD_TYPE, startX, startY, radius);
		this.setOptionalColor(Color.GREEN.getRGB());

	}

	@Override
	public IVisage getVisage() {
		return visage;
	}

	// TODO food
	public int nourishment() {
		return 100;
	}

}
