package sim;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import actor.Actor;
import actor.Eatable;
import actor.Thinker;
import main.ImmutableCollection;
import main.WorldGraphics;
import psychology.social.concepts.TypeClass;

/**
 * TODO complete knowledge layer of world
 * 
 * @author borah
 *
 */
public class World {

	protected Map<UUID, Actor> actors = new HashMap<>();

	private final int width, height;
	private WorldGraphics worldGraphics;
	private Actor testActor;
	private Random rand = new Random();
	protected long ticks = 0;
	private SensoryHandler sensoryHandler = new SensoryHandler(this);
	private ImmutableCollection<Actor> actorCollection = new ImmutableCollection<>(actors.values());
	private Map<String, TypeClass<?>> typeClasses = new HashMap<>();

	public World(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public WorldGraphics getWorldGraphics() {
		return worldGraphics;
	}

	/**
	 * only should be used by the WorldGraphics class
	 * 
	 * @param worldGraphics
	 */
	public void setWorldGraphics(WorldGraphics worldGraphics) {
		this.worldGraphics = worldGraphics;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public <T extends Actor> T spawnActor(T a) {
		this.actors.put(a.getUuid(), a);
		System.out.println("Spawned " + a);
		return a;
	}

	public SensoryHandler getSensoryHandler() {
		return sensoryHandler;
	}

	public Collection<Actor> getActors() {
		return actorCollection;
	}

	/**
	 * TODO make world load from save
	 */
	public void load() {

	}

	public <T> TypeClass<T> getTypeFor(String name) {
		return (TypeClass<T>) this.typeClasses.get(name);
	}

	public <T> TypeClass<T> getOrCreateTypeFor(String name, Class<T> clazz) {
		return (TypeClass<T>) typeClasses.computeIfAbsent(name, (a) -> new TypeClass<>(name, clazz));
	}

	public void worldSetup() {

		testActor = new Thinker(this, this.getOrCreateTypeFor("people", Thinker.class), "Stacy", 0, 0, 60);
		testActor.setOptionalColor(worldGraphics.color(255, 50, 50));
		Actor ra = null;
		for (int i = 0; i < 70; i++) {
			ra = this.spawnActor(new Thinker(this, this.getOrCreateTypeFor("people", Thinker.class), "Stacy" + (i + 1),
					i, i * 3, 50));
		}
		this.spawnActor(testActor);
		for (int i = 0; i < 200; i++) {
			this.spawnActor(new Eatable(this, this.getOrCreateTypeFor("banana", Eatable.class), this.width - 4 * i,
					this.height - i, 50, 4));
		}

	}

	public void worldTick() {

		if (testActor != null) {
			testActor.moveToward(worldGraphics.mouseX, worldGraphics.mouseY, Actor.STEP);
		}
		for (Actor e : actors.values()) {
			e.movementTick();
			e.tick();
			e.senseTick();
			e.thinkTick();

		}
		// world phenomena idk
		for (Actor e : actors.values()) {
			e.actionTick();
			e.finalTick();
			e.draw(this.worldGraphics);
			// TODO remove this debug stuff

		}
		if (this.rand.nextInt(50) < 4) {
			System.out.println(testActor.getSystemsReport());
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
		return actors.values().stream().filter(pred).filter((a) -> a != for_ && isColliding(for_, a))
				.collect(Collectors.toSet());
	}

	public Set<Actor> getAt(int x, int y) {
		return actors.values().stream().filter((a) -> a.distance(x, y) <= a.getRadius()).collect(Collectors.toSet());
	}

	public Random rand() {
		return rand;
	}

}
