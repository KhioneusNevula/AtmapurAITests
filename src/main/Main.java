package main;
import processing.core.PApplet;
import sim.World;

public class Main {

	public static void main(String[] args) {
		World world = new World(800, 500, 30f);
		PApplet.runSketch(new String[] { "World" }, world);

	}

}
