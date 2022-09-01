package main;

import actor.Actor;
import processing.core.PApplet;
import processing.event.MouseEvent;
import sim.World;

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
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		super.mouseClicked(event);
		if (event.getButton() == RIGHT) {
			String doing = "nothing";
			long time = System.currentTimeMillis();
			int c = 0;
			for (Actor a : world.getActors()) {
				// TODO for testing right click
				c++;
			}
			System.out.println("doing " + doing + " for " + c + " actors took "
					+ ((System.currentTimeMillis() - time) / 1000.0) + " seconds");
		} else {
			for (Actor a : world.getActors()) {
				// TODO for testing left click
			}
		}
	}

	@Override
	public void draw() {
		background(color(255, 255, 255));
		this.stroke(color(255, 255, 255));

		world.worldTick();
	}

}
