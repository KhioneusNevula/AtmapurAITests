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
import actor.ITemplate;
import actor.IUniqueExistence;
import actor.IVisage;
import actor.SentientActor;
import actor.UpgradedSentientActor;
import biology.anatomy.ISpeciesTemplate;
import biology.anatomy.Species;
import humans.Food;
import humans.Person;
import humans.UpgradedPerson;
import main.ImmutableCollection;
import mind.Culture;
import mind.Group;
import mind.concepts.type.Property;
import mind.memory.IKnowledgeBase;
import mind.memory.IPropertyData;
import mind.relationships.Relationship;
import sim.interfaces.IRenderable;

/**
 * TODO complete knowledge layer of world
 * 
 * @author borah
 *
 */
public class World implements IUniqueExistence, IRenderable {

	protected Map<UUID, Actor> actors = new HashMap<>();

	private final int width, height;
	private UpgradedSentientActor testActor;
	private Group testGroup;
	private Random rand = new Random();
	protected long ticks = 0;
	private ImmutableCollection<Actor> actorCollection = new ImmutableCollection<>(actors.values());
	private UUID id = UUID.randomUUID();
	private Map<String, Group> groups;
	private Map<ISpeciesTemplate, Culture> defaultCultures;

	public World(int width, int height) {
		this.width = width;
		this.height = height;
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

	public Culture getDefaultCulture(ISpeciesTemplate forSp) {
		return defaultCultures == null ? null : defaultCultures.get(forSp);
	}

	public Culture getOrGenDefaultCulture(ISpeciesTemplate forSp) {
		Culture a = this.getDefaultCulture(forSp);
		if (a == null) {
			a = this.putDefaultCulture(forSp);
		}
		return a;
	}

	protected Culture putDefaultCulture(ISpeciesTemplate forSp) {
		if (this.defaultCultures == null)
			defaultCultures = new HashMap<>();
		Culture c = null;
		this.defaultCultures.put(forSp, c = forSp.genDefaultCulture());
		return c;
	}

	public Collection<Actor> getActors() {
		return actorCollection;
	}

	/**
	 * TODO make world load from save
	 */
	public void load() {

	}

	private Actor makeTestActor() {
		if (testActor != null)
			testActor.setOptionalColor(Color.yellow.getRGB());
		this.spawnActor(testActor = new UpgradedPerson(this, "bobzy", Species.HUMAN, 300, 300, 10));
		testActor.setOptionalColor(Color.WHITE.getRGB());
		if (testGroup != null) {

			testActor.getMind().establishRelationship(testGroup, Relationship.be());
			testGroup.establishRelationship(testActor.getMind(), Relationship.include());
		}
		return testActor;
	}

	public void worldSetup() {
		System.out.println("setting up");
		testGroup = this.makeGroup("TheGroup");
		testGroup.getCulture().usualInit();
		testGroup.getCulture().languageInit(this.rand);
		this.makeTestActor();
		Actor idk = null;
		for (int i = 0; i < 100; i++) {
			int x = Math.max(0, Math.min(width, 501 + (int) (i * (rand().nextDouble() * 5 - 10))));
			int y = Math.max(0, Math.min(height, 501 + (int) (i * (rand().nextDouble() * 5 - 10))));
			if (i % 2 == 0)
				this.spawnActor((idk = new Person(this, "baba" + i, Species.ELF, x, y, 10)));
			/*
			 * else this.spawnActor((idk = new UpgradedPerson(this, "ubaba" + i,
			 * Species.FAIRY, x, y, 10)));
			 */
			(idk).setOptionalColor(Color.CYAN.getRGB());

			x = Math.max(0, Math.min(width, 501 + (int) (i * (rand().nextDouble() * 5 - 10))));
			y = Math.max(0, Math.min(height, 401 + (int) (i * (rand().nextDouble() * 5 - 10))));

			this.spawnActor(new Food(this, "food" + i, x, y, 5));
		}
	}

	public synchronized void worldTick() {

		for (Actor e : Set.copyOf(actors.values())) {
			if (e.isRemoved()) {
				if (e instanceof SentientActor sa) {
					sa.getMind().kill();
				} else if (e instanceof UpgradedSentientActor sa) {
					sa.getMind().kill();
				}
				actors.remove(e.getUUID());
				if (e == this.testActor)
					makeTestActor();
			}
			e.movementTick();
			e.tick();
			e.senseTick();
			e.thinkTick();

		}
		for (Actor e : actors.values()) {
			e.actionTick();
			e.finalTick();
		}
		for (Group g : groups.values()) {
			g.tick(this.ticks);
		}
		doTestThings();
		ticks++;
	}

	/**
	 * TODO remove this after testing idk
	 */
	private void doTestThings() {
		if (actors.size() < 3) {
			boolean imps = false;
			for (int i = 0; i < rand.nextInt(6) + 4; i++) {
				int x = rand.nextInt(width);
				int y = rand.nextInt(height);
				for (int j = 0; j < rand.nextInt(6) + 1; j++) {
					x += rand.nextInt(11) - 5;
					y += rand.nextInt(11) - 5;
					if (imps) {
						Actor a;
						spawnActor(a = new UpgradedPerson(this, "baba" + x + "" + y, Species.IMP, x, y, 5));
						a.setOptionalColor(Color.RED.getRGB());
						(((UpgradedSentientActor) a).getMind()).establishRelationship(getTestGroup(),
								Relationship.be());

					} else {

						spawnActor(new Food(this, "nom" + x + "_" + y, x, y, 5));
					}
				}
				imps = !imps;
			}
		}
	}

	public synchronized void draw(WorldGraphics graphics) {
		graphics.pushMatrix();
		graphics.pushStyle();
		graphics.translate(WorldGraphics.BORDER, WorldGraphics.BORDER);
		graphics.fill(100, 0, 100);
		graphics.rect(0, 0, getWidth(), getHeight());
		graphics.stroke(graphics.color(255, 255, 255));

		for (Actor e : actors.values()) {

			if (e.canRender())
				e.draw(graphics);
		}
		graphics.popStyle();
		graphics.popMatrix();
	}

	@Override
	public boolean canRender() {
		return true;
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

	public UpgradedSentientActor getTestActor() {
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

	public int clampX(int x) {
		return Math.max(Math.min(x, this.width), 0);
	}

	public int clampY(int y) {
		return Math.max(Math.min(y, this.height), 0);
	}

	@Override
	public void assignProperty(IKnowledgeBase culture, Property property, IPropertyData data) {
		// TODO assign property to world

	}

	@Override
	public IPropertyData getPropertyData(IKnowledgeBase culture, Property property) {
		return IPropertyData.UNKNOWN;
	}

	@Override
	public IVisage getVisage() {
		return null;
	}

	@Override
	public ITemplate getSpecies() {
		return ITemplate.WORLD;
	}
}
