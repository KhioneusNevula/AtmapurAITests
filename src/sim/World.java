package sim;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import actor.Actor;
import actor.IUniqueExistence;
import actor.IVisage;
import actor.LivingActor;
import biology.anatomy.Species;
import humans.Food;
import humans.Person;
import main.ImmutableCollection;
import mind.Group;
import mind.concepts.type.Property;
import mind.memory.IKnowledgeBase;
import mind.memory.IPropertyData;
import mind.relationships.Relationship;

/**
 * TODO complete knowledge layer of world
 * 
 * @author borah
 *
 */
public class World implements IUniqueExistence {

	protected Map<UUID, Actor> actors = new HashMap<>();

	private final int width, height;
	private WorldGraphics worldGraphics;
	private LivingActor testActor;
	private Group testGroup;
	private Random rand = new Random();
	protected long ticks = 0;
	private ImmutableCollection<Actor> actorCollection = new ImmutableCollection<>(actors.values());
	private UUID id = UUID.randomUUID();
	private Map<String, Group> groups;

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
		this.actors.put(a.getUUID(), a);
		System.out.println("Spawned " + a);
		if (a == testActor && a.isMultipart())
			System.out.println(a.getAsMultipart().getBody().report());
		return a;
	}

	public Group makeGroup(String identifier) {
		Group group = new Group(identifier, this);
		(this.groups == null ? groups = new TreeMap<>() : groups).put(identifier, group);
		return group;
	}

	public Collection<Actor> getActors() {
		return actorCollection;
	}

	/**
	 * TODO make world load from save
	 */
	public void load() {

	}

	public void worldSetup() {
		System.out.println("setting up");
		this.spawnActor(testActor = new Person(this, "bobzy", Species.HUMAN, 300, 300, 10));
		testActor.setOptionalColor(Color.WHITE.getRGB());
		testGroup = this.makeGroup("TheGroup");
		testGroup.getCulture().usualInit();
		testActor.getMind().establishRelationship(testGroup, Relationship.be());
		testGroup.establishRelationship(testActor.getMind(), Relationship.include());
		Person idk = null;
		for (int i = 0; i < 100; i++) {
			int x = Math.max(0, Math.min(width, 501 + (int) (i * (rand().nextDouble() * 5 - 10))));
			int y = Math.max(0, Math.min(height, 501 + (int) (i * (rand().nextDouble() * 5 - 10))));

			this.spawnActor(idk = new Person(this, "baba" + i, Species.FAIRY, x, y, 10));
			if (rand().nextInt(10) < 2) {
				idk.getMind().establishRelationship(testGroup, Relationship.be());
				testGroup.establishRelationship(idk.getMind(), Relationship.include());
			}
			x = Math.max(0, Math.min(width, 501 + (int) (i * (rand().nextDouble() * 5 - 10))));
			y = Math.max(0, Math.min(height, 401 + (int) (i * (rand().nextDouble() * 5 - 10))));

			this.spawnActor(new Food(this, "food" + i, x, y, 5));
		}
	}

	public void worldTick() {

		for (Actor e : Set.copyOf(actors.values())) {
			if (e.isRemoved()) {
				actors.remove(e.getUUID());
				continue;
			}
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

	public LivingActor getTestActor() {
		return testActor;
	}

	public Group getTestGroup() {
		return testGroup;
	}

	public Map<String, Group> getGroups() {
		return groups;
	}

	public Random rand() {
		return rand;
	}

	@Override
	public UUID getUUID() {
		return id;
	}

	@Override
	public World getWorld() {
		return this;
	}

	@Override
	public void assignProperty(IKnowledgeBase culture, Property property, IPropertyData data) {
		// TODO assign property to world

	}

	@Override
	public IPropertyData getPropertyData(IKnowledgeBase culture, Property property) {
		return null;
	}

	@Override
	public IVisage getVisage() {
		return null;
	}
}
