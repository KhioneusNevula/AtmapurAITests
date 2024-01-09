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
import phenomenon.type.DamagePhenomenon;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import sim.World;
import sim.WorldGraphics;

public class BadThing extends Actor {

	private IVisage visage = new SimpleVisage(this);
	private static PImage img;
	public static final ActorType BAD_TYPE = ActorType.Builder.start("bad")
			.addHint(BasicProperties.DANGER, () -> IPropertyData.PRESENCE, ISensor.SIGHT)
			.addHint(BasicProperties.AWESOME, () -> IPropertyData.PRESENCE, ISensor.SIGHT).build();

	public BadThing(World world, String name, int startX, int startY, int radius) {
		super(world, name, BAD_TYPE, startX, startY, radius);
		this.setOptionalColor(Color.black.getRGB());
	}

	@Override
	public IVisage getVisage() {
		return visage;
	}

	@Override
	public void tick() {
		super.tick();
		if (rand().nextInt(60) < 1) {
			for (Actor actor : this.getWorld().getCollisions(this, (a) -> a.hasSystem(SystemType.LIFE))) {
				this.getWorld().createPhenomenon(new DamagePhenomenon(5, this, actor, ISensor.SIGHT));
			}
		}
	}

	@Override
	protected void render(WorldGraphics g) {
		if (img == null) {
			img = g.loadImage("skull.png");
		}
		g.ellipseMode(PApplet.RADIUS);
		g.fill(getOptionalColor(), 100);
		g.stroke(getOptionalColor());
		g.strokeWeight(1.4f);
		g.imageMode(PConstants.CENTER);
		g.image(img, this.getX(), this.getY(), this.getRadius() * 2, this.getRadius() * 2);
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
