package actor.types;

import java.awt.Color;
import java.util.Set;

import actor.Actor;
import actor.construction.physical.IPhysicalActorObject;
import actor.construction.physical.IVisage;
import actor.construction.properties.SenseProperty;
import actor.construction.properties.SenseProperty.IColor;
import actor.construction.simple.SimpleActorPhysicalObject;
import actor.construction.simple.SimpleActorType;
import actor.construction.simple.SimpleMaterialLayer;
import actor.construction.simple.SimpleMaterialType;
import actor.construction.simple.SimpleMultilayerPart;
import actor.construction.simple.SimplePartType;
import sim.GameMapTile;

public class FoodActor extends Actor {

	public static final SimplePartType FOOD_PART_TYPE = SimplePartType.builder("food_chunk", 1).setDefaultNutrition(5f)
			.build();

	public static final SimpleActorType FOOD_TYPE = SimpleActorType.builder("food").setPartType(FOOD_PART_TYPE).build();

	private SimpleActorPhysicalObject physical;
	private IColor color;

	public FoodActor(GameMapTile world, String name, int startX, int startY, int radius, Float nutrition,
			float mass) {
		super(world, name, FOOD_TYPE, startX, startY);
		physical = new SimpleActorPhysicalObject(this,
				new SimpleMultilayerPart(FOOD_PART_TYPE, getUUID(),
						Set.of(new SimpleMaterialLayer(SimpleMaterialType.VEGGIE_MATERIAL),
								new SimpleMaterialLayer(SimpleMaterialType.MEATY_MATERIAL))).setNutrition(nutrition),
				mass).makeRectangle(radius * 2, radius * 2);
	}

	public FoodActor setColor(IColor color) {
		this.color = color;
		this.physical.mainComponent().changeProperty(SenseProperty.COLOR, color);
		return this;
	}

	@Override
	public Color getOptionalColor() {
		return this.color.getColor();
	}

	@Override
	public IVisage getVisage() {
		return physical;
	}

	@Override
	public IPhysicalActorObject getPhysical() {
		return physical;
	}

}
