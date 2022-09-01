package main;

import processing.core.PApplet;
import sim.World;

public class Main {

	public static void main(String[] args) {
		World world = new World(800, 500);
		PApplet.runSketch(new String[] { "World" }, new WorldGraphics(world, 30f));

	}

}
