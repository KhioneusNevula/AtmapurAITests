package humans;

import java.awt.Color;

import actor.Actor;
import actor.ActorType;
import actor.IVisage;
import actor.SimpleVisage;
import biology.systems.SystemType;
import biology.systems.types.ISensor;
import biology.systems.types.LifeSystem;
import mind.concepts.type.BasicProperties;
import mind.memory.IPropertyData;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import sim.World;
import sim.WorldGraphics;

public class Flower extends Actor {

	private IVisage visage = new SimpleVisage(this);
	private static PImage flowerImg;
	public static final ActorType FLOWER_TYPE = ActorType.Builder.start("flower")
			.addHint(BasicProperties.AWESOME, () -> IPropertyData.PRESENCE, ISensor.SIGHT).build();

	public Flower(World world, String name, int startX, int startY, int radius) {
		super(world, name, FLOWER_TYPE, startX, startY, radius);
		this.setOptionalColor(Color.PINK.getRGB());
	}

	@Override
	public IVisage getVisage() {
		return visage;
	}

	@Override
	protected void render(WorldGraphics g) {
		if (flowerImg == null) {
			flowerImg = g.loadImage("pink_flower.png");
		}
		g.ellipseMode(PApplet.RADIUS);
		g.fill(getOptionalColor(), 100);
		g.stroke(getOptionalColor());
		g.strokeWeight(1.4f);
		g.imageMode(PConstants.CENTER);
		g.image(flowerImg, this.getX(), this.getY(), this.getRadius() * 2, this.getRadius() * 2);
		g.textAlign(PApplet.CENTER, PApplet.CENTER);
		boolean danger = false; // TODO make danger more clear
		boolean dead = false; // TODO make death more clear

		if (this.hasSystem(SystemType.LIFE)) {
			LifeSystem ensys = this.getSystem(SystemType.LIFE);
			if (ensys.isSevere())
				danger = true;
			if (ensys.isDead())
				dead = true;
		}
		if (dead) {
			g.fill(g.color(255, 255, 0));
		} else if (danger) {
			g.fill(g.color(255, 0, 0));
		} else {
			g.fill(0);
		}
		g.text(this.getName(), this.getX(), this.getY());

	}

}
