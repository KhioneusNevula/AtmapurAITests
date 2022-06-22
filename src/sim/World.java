package sim;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import entity.Actor;
import entity.Eatable;
import entity.Thinker;
import processing.core.PApplet;
import processing.event.MouseEvent;
import sociology.sociocon.IPurposeSource;
import sociology.sociocon.Sociocat;
import sociology.sociocon.Sociocon;

public class World extends PApplet {

	private List<Actor> actors = new ArrayList<>();

	private TreeMap<Sociocat, Map<String, Sociocon>> sociocons = new TreeMap<>();
	private final int width, height;
	private final float fps;
	private Actor testActor;
	private Random rand = new Random();

	public World(int width, int height, float fps) {
		this.width = width;
		this.height = height;
		this.fps = fps;
	}

	public void spawnActor(Actor a) {
		this.actors.add(a);
		System.out.println("Spawned " + a);
	}

	public List<Actor> getActors() {
		return new ArrayList<>(actors);
	}

	public Map<String, Sociocon> getSocioconMap(Sociocat type) {
		return sociocons.computeIfAbsent(type, (a) -> new TreeMap<>());
	}

	/**
	 * Either gets an existing sociocon with the given type and name, or creates a
	 * new one using the type, name, and the sources given
	 * 
	 * @param type
	 * @param name
	 * @param sys
	 * @return
	 */
	public Sociocon getOrCreateSociocon(Sociocat type, String name, IPurposeSource... sys) {
		Sociocon existing = getSociocon(type, name);
		if (existing == null) {
			existing = type.createSociocon(this, name, sys);
		}
		return existing;
	}

	public void addSociocon(Sociocon con) {
		this.getSocioconMap(con.getCategory()).put(con.getName(), con);
	}

	public Sociocon getSociocon(Sociocat type, String name) {
		return getSocioconMap(type).get(name);
	}

	@Override
	public void settings() {
		super.settings();
		size(width, height);
	}

	@Override
	public void setup() {
		super.setup();
		frameRate(fps);
		testActor = new Thinker(this, "Stacy", 0, 0, 60);
		testActor.setOptionalColor(color(255, 50, 50));
		for (int i = 0; i < 70; i++) {
			this.spawnActor(new Thinker(this, "Stacy" + (i + 1), i, i, 50));
		}
		this.spawnActor(testActor);
		for (int i = 0; i < 200; i++) {
			this.spawnActor(new Eatable(this, "banana" + (this.width - i), this.width - i, this.height - i, 50, 4));
		}
		this.spawnActor(new Eatable(this, "apple", 500, 500, 40, 5));
		this.spawnActor(new Eatable(this, "khwabostu", 400, 400, 50, 2));

	}

	@Override
	public void mouseClicked(MouseEvent event) {
		super.mouseClicked(event);
		if (event.getButton() == RIGHT) {
			long time = System.currentTimeMillis();
			int c = 0;
			for (Actor a : actors) {
				if (a instanceof Thinker t) {
					t.getMind().getPersonalWill().debugGenerateActionPlan(3);
					c++;
				}
			}
			System.out.println("generating " + c + " action plans took "
					+ ((System.currentTimeMillis() - time) / 1000.0) + " seconds");
		}
	}

	@Override
	public void draw() {
		background(color(255, 255, 255));
		this.stroke(color(255, 255, 255));
		if (testActor != null) {
			testActor.move((mouseX - testActor.getX()) / 2, (mouseY - testActor.getY()) / 2);
		}
		this.worldTick();
	}

	public void worldTick() {
		for (Actor e : actors) {
			e.movementTick();
			e.tick();
			e.senseTick();
			e.thinkTick();
		}
		// world phenomena idk
		for (Actor e : actors) {
			e.actionTick();
			e.draw();
		}
	}

	public boolean isColliding(Actor a, Actor other) {
		return a.distance(other) <= (a.getRadius() + other.getRadius());
	}

	public Set<Actor> getCollisions(Actor for_, Predicate<Actor> pred) {
		return actors.stream().filter(pred).filter((a) -> a != for_ && isColliding(for_, a))
				.collect(Collectors.toSet());
	}

	public Set<Actor> getAt(int x, int y) {
		return actors.stream().filter((a) -> a.distance(x, y) <= a.getRadius()).collect(Collectors.toSet());
	}

}
