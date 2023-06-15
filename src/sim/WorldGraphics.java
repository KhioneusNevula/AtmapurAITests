package sim;

import java.awt.event.KeyEvent;

import actor.Actor;
import actor.LivingActor;
import biology.systems.ESystem;
import mind.Group;
import mind.need.INeed.Degree;
import mind.need.KnowledgeNeed;
import processing.core.PApplet;
import processing.event.MouseEvent;

public class WorldGraphics extends PApplet {

	private World world;
	private final float fps;

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
		this.size(world.getWidth(), world.getHeight());
		world.load();
	}

	@Override
	public void settings() {
		super.settings();
		size(world.getWidth(), world.getHeight());

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
		if (event.getKeyCode() == KeyEvent.VK_SPACE) {
			world.getTestActor().getMind().getMindMemory().addNeed(new KnowledgeNeed(Degree.MILD, null));

		}
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		super.mouseClicked(event);
		if (event.getButton() == RIGHT) {
			Group group = world.getTestGroup();
			System.out.println(group.report());
		} else {
			Actor l = world.getActors().stream().filter((a) -> a.distance(event.getX(), event.getY()) <= a.getRadius())
					.findAny().orElse(null);
			if (l != null) {
				System.out.print("actor:" + l.getName() + ">>>");
				System.out.print("properties:" + l.getSocialProperties());
				if (l instanceof LivingActor)
					System.out.println(((LivingActor) l).getMind().report());

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
		this.fill(100, 0, 100);
		this.rect(0, 0, world.getWidth(), world.getHeight());
		this.stroke(color(255, 255, 255));

		world.worldTick();
		this.fill(color(255, 255, 255));
		this.textSize(15);
		this.text(world.getTestActor().getMind().getMindMemory().report() + "\n"
				+ world.getTestActor().getMind().getWill().report(), 10, 10);
	}

}
