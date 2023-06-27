package sim;

import java.awt.Color;
import java.awt.event.KeyEvent;

import actor.Actor;
import actor.SentientActor;
import biology.anatomy.Species;
import biology.systems.ESystem;
import humans.Food;
import humans.Person;
import mind.Group;
import mind.need.INeed.Degree;
import mind.need.KnowledgeNeed;
import mind.relationships.Relationship;
import processing.core.PApplet;
import processing.event.MouseEvent;

public class WorldGraphics extends PApplet {

	private World world;
	private final float fps;
	private static final int BORDER = 30;

	public WorldGraphics(World world, float fps) {
		this.fps = fps;
		this.world = world;
		world.setWorldGraphics(this);
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
		// this.size(world.getWidth(), world.getHeight());
		world.load();
	}

	@Override
	public void settings() {
		super.settings();
		size(world.getWidth() + 2 * BORDER, world.getHeight() + 2 * BORDER);

	}

	@Override
	public void setup() {
		super.setup();
		frameRate(fps);
		world.worldSetup();
		this.windowResizable(true);
	}

	@Override
	public void keyPressed(processing.event.KeyEvent event) {
		super.keyPressed(event);
		if (event.getKeyCode() == KeyEvent.VK_W) {
			world.getTestActor().getMind().getMindMemory().setFeelingCurious(true);
			world.getTestActor().getMind().getMindMemory().addNeed(new KnowledgeNeed(Degree.MILD, null));
		} else if (event.getKeyCode() == KeyEvent.VK_SPACE) {
			Group group = world.getTestGroup();
			System.out.println(group.report());
		} else if (event.getKeyCode() == KeyEvent.VK_I) {
			System.out.print("actor:" + world.getTestActor().getName() + ">>>");
			System.out.print("properties:" + world.getTestActor().getSocialProperties());

			System.out.println((world.getTestActor()).getMind().report());

			for (ESystem sys : world.getTestActor().getSystems()) {
				System.out.print(";" + sys.report());
			}
			System.out.println();
		} else if (event.getKeyCode() == KeyEvent.VK_F) {
			world.spawnActor(new Food(world, "nom" + this.mouseX + "_" + this.mouseY, world.clampX(mouseX - BORDER),
					world.clampY(mouseY - BORDER), 5));
		}
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		super.mouseClicked(event);
		if (event.getButton() == RIGHT) {
			SentientActor a;
			world.spawnActor(a = new Person(world, "baba" + event.getX() + "" + event.getY(), Species.IMP,
					world.clampX(event.getX() - BORDER), world.clampY(event.getY() - BORDER), 5));
			a.setOptionalColor(Color.RED.getRGB());
			a.getMind().establishRelationship(world.getTestGroup(), Relationship.be());
			// world.getTestGroup().establishRelationship(a.getMind(),
			// Relationship.include());
		} else {
			Actor l = world.getActors().stream()
					.filter((a) -> a.distance(event.getX() - BORDER, event.getY() - BORDER) <= a.getRadius() + 5)
					.findAny().orElse(null);
			if (l != null) {
				System.out.print("actor:" + l.getName() + ">>>");
				System.out.print("properties:" + l.getSocialProperties());
				if (l instanceof SentientActor)
					System.out.println(((SentientActor) l).getMind().report());

				for (ESystem sys : l.getSystems()) {
					System.out.print(";" + sys.report());
				}
				System.out.println();
			}

		}
	}

	@Override
	public void draw() {
		background(color(0, 100, 100));
		this.pushMatrix();
		this.translate(BORDER, BORDER);
		this.fill(100, 0, 100);
		this.rect(0, 0, world.getWidth(), world.getHeight());
		this.stroke(color(255, 255, 255));
		world.worldTick();
		this.popMatrix();
		this.fill(color(255, 255, 255));
		this.textSize(10);
		this.text(world.getTestActor().getMind().getMindMemory().report() + "\n"
				+ world.getTestActor().getMind().getWill().report() + "\nheld:" + world.getTestActor().getHeld(), 10,
				10);
	}

}
