package sim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import culture.Culture;
import entity.Actor;
import entity.Eatable;
import entity.Thinker;
import processing.core.PApplet;
import processing.event.MouseEvent;
import sociology.TypeProfile;

public class World extends PApplet {

	private List<Actor> actors = new ArrayList<>();

	private HashMap<String, Culture> cultures = new HashMap<>();
	private final int width, height;
	private final float fps;
	private Actor testActor;
	private Random rand = new Random();
	private long ticks = 0;
	private Map<String, TypeProfile> typeProfiles = new TreeMap<>();

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

	public Map<String, TypeProfile> getTypeProfiles() {
		return typeProfiles;
	}

	public Culture getCulture(String name) {
		return this.cultures.get(name);
	}

	public Collection<Culture> getCultures() {
		return this.cultures.values();
	}

	public void addCulture(Culture c) {
		this.cultures.put(c.getName(), c);
	}

	public TypeProfile getTypeProfile(String name) {
		return this.typeProfiles.get(name);
	}

	public Collection<TypeProfile> getAllTypeProfiles() {
		return this.typeProfiles.values();
	}

	public TypeProfile getOrCreateTypeProfile(String name) {
		TypeProfile existing = getTypeProfile(name);
		if (existing == null) {
			existing = new TypeProfile(name, this);
			this.addTypeProfile(existing);
		}
		return existing;
	}

	public void addTypeProfile(TypeProfile type) {
		this.typeProfiles.put(type.getName(), type);
	}

	@Override
	public void settings() {
		super.settings();
		size(width, height);
	}

	@Override
	public void setup() {
		super.setup();
		Culture.genEssentialCultures(this);
		frameRate(fps);
		testActor = new Thinker(this, "Stacy", 0, 0, 60);
		testActor.setOptionalColor(color(255, 50, 50));
		for (int i = 0; i < 70; i++) {
			this.spawnActor(new Thinker(this, "Stacy" + (i + 1), i, i * 3, 50));
		}
		this.spawnActor(testActor);
		for (int i = 0; i < 200; i++) {
			this.spawnActor(new Eatable(this, "banana" + i, this.width - 4 * i, this.height - i, 50, 4));
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
				if (a instanceof ICanHaveMind t && t.hasMind()) {
					t.getMind().getPersonalWill().debugGenerateActionPlan(3);
					c++;
				}
			}
			System.out.println("generating " + c + " action plans took "
					+ ((System.currentTimeMillis() - time) / 1000.0) + " seconds");
		} else {
			for (Actor a : actors) {
				if (a instanceof ICanHaveMind t && t.hasMind()) {
					t.getMind().getPersonalWill().debugExecuteActionPlans();
				}
			}
		}
	}

	@Override
	public void draw() {
		background(color(255, 255, 255));
		this.stroke(color(255, 255, 255));
		if (testActor != null) {
			testActor.moveToward(mouseX, mouseY, testActor.STEP);
		}
		this.worldTick();
	}

	public void worldTick() {

		for (Actor e : actors) {
			e.movementTick();
			e.tick();
			e.senseTick();
			e.thinkTick();
			if (rand.nextInt(70000) < 19 && e instanceof ICanHaveMind mm && mm.hasMind()) {
				System.out.println(e.getMind() + " " + e.getSystemsReport());
			}

		}
		// world phenomena idk
		for (Actor e : actors) {
			e.actionTick();
			e.draw();

		}
		ticks++;
	}

	public long getTicks() {
		return ticks;
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

	public Random rand() {
		return rand;
	}

}
