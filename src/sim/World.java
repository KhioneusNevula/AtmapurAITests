package sim;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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
import actor.UpgradedSentientActor;
import biology.anatomy.ISpeciesTemplate;
import biology.anatomy.Species;
import humans.BadThing;
import humans.Flower;
import humans.Food;
import humans.UpgradedPerson;
import main.ImmutableCollection;
import mind.action.ActionType;
import mind.concepts.type.Property;
import mind.memory.IPropertyData;
import mind.relationships.Relationship;
import mind.thought_exp.culture.UpgradedCulture;
import mind.thought_exp.culture.UpgradedGroup;
import mind.thought_exp.memory.IUpgradedKnowledgeBase;
import phenomenon.IPhenomenon;
import sim.interfaces.IRenderable;

/**
 * TODO complete knowledge layer of world
 * 
 * @author borah
 *
 */
public class World implements IUniqueExistence, IRenderable {

	protected Map<UUID, Actor> actors = new HashMap<>();
	private Map<UUID, IPhenomenon> phenomena = new HashMap<>();

	private final int width, height;
	private UpgradedSentientActor testActor;
	private UpgradedGroup testGroup;
	private Random rand = new Random();
	protected long ticks = 0;
	private ImmutableCollection<Actor> actorCollection = new ImmutableCollection<>(actors.values());
	private ImmutableCollection<IPhenomenon> phenCollection = new ImmutableCollection<>(phenomena.values());
	private UUID id = UUID.randomUUID();
	private Map<String, UpgradedGroup> groups;
	private Map<ISpeciesTemplate, UpgradedCulture> defaultCultures;

	public World(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public String getSimpleName() {
		return "worldEntity";
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public synchronized <T extends Actor> T spawnActor(T a) {
		this.actors.put(a.getUUID(), a);
		System.out.println("Spawned " + a);
		if (a == testActor && a.isMultipart())
			System.out.println(a.getAsMultipart().getBody().report());
		return a;
	}

	public synchronized <T extends IPhenomenon> T createPhenomenon(T a) {
		this.phenomena.put(a.getUUID(), a);
		return a;
	}

	public synchronized UpgradedGroup makeGroup(String identifier) {
		UpgradedGroup group = new UpgradedGroup(identifier, this.rand);
		(this.groups == null ? groups = new TreeMap<>() : groups).put(identifier, group);
		return group;
	}

	public UpgradedCulture getDefaultCulture(ISpeciesTemplate forSp) {
		return defaultCultures == null ? null : defaultCultures.get(forSp);
	}

	public synchronized UpgradedCulture getOrGenDefaultCulture(ISpeciesTemplate forSp) {
		UpgradedCulture a = this.getDefaultCulture(forSp);
		if (a == null) {
			a = this.putDefaultCulture(forSp);
		}
		return a;
	}

	protected UpgradedCulture putDefaultCulture(ISpeciesTemplate forSp) {
		if (this.defaultCultures == null)
			defaultCultures = new HashMap<>();
		UpgradedCulture c;
		this.defaultCultures.put(forSp, c = forSp.genDefaultCulture(this));
		return c;
	}

	public Collection<Actor> getActors() {
		return actorCollection;
	}

	public Collection<IPhenomenon> getPhenomena() {
		return phenCollection;
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
		testGroup.getCulture().learnConcept(ActionType.EAT3);
		testGroup.getCulture().learnConcept(ActionType.EAT4);
		testGroup.getCulture().learnConcept(ActionType.EAT5);
		// testGroup.getCulture().languageInit(this.rand);
		this.makeTestActor();
		Actor idk = null;
		for (int i = 0; i < 100; i++) {
			int x = Math.max(0, Math.min(width, 501 + (int) (i * (rand().nextDouble() * 5 - 10))));
			int y = Math.max(0, Math.min(height, 501 + (int) (i * (rand().nextDouble() * 5 - 10))));
			if (i % 2 == 0)
				this.spawnActor((idk = new UpgradedPerson(this, "baba" + i, Species.ELF, x, y, 10)));
			/*
			 * else this.spawnActor((idk = new UpgradedPerson(this, "ubaba" + i,
			 * Species.FAIRY, x, y, 10)));
			 */
			(idk).setOptionalColor(Color.CYAN.getRGB());

			x = Math.max(0, Math.min(width, 501 + (int) (i * (rand().nextDouble() * 5 - 10))));
			y = Math.max(0, Math.min(height, 401 + (int) (i * (rand().nextDouble() * 5 - 10))));

			this.spawnActor(new Food(this, "food" + i, x, y, 5));

			x = Math.max(0, Math.min(width, 501 + (int) (i * (rand().nextDouble() * 5 - 10))));
			y = Math.max(0, Math.min(height, 401 + (int) (i * (rand().nextDouble() * 5 - 10))));

			if (i % 5 == 0) {

				this.spawnActor(new BadThing(this, "evil" + i, x, y, 10));
			} else {
				this.spawnActor(new Flower(this, "flower" + i, x, y, (rand().nextInt(5) + 5)));
			}
		}
	}

	public synchronized void worldTick() {
		if (this.testActor.isRemoved())
			makeTestActor();
		// synchronized (actors) {
		Iterator<Actor> iter = actors.values().iterator();
		while (iter.hasNext()) {
			Actor e = iter.next();
			if (e.isRemoved()) {
				if (e instanceof UpgradedSentientActor sa) {
					sa.getMind().kill();
				}
				iter.remove();
			}
			e.movementTick();
			e.tick();
			e.senseTick();
			e.thinkTick();

		}

		synchronized (phenomena) {
			Iterator<IPhenomenon> iter2 = phenomena.values().iterator();
			while (iter2.hasNext()) {
				IPhenomenon p = iter2.next();
				p.tick();
				if (p.isComplete()) {
					iter2.remove();
				}
			}
		}
		for (Actor e : actors.values()) {
			e.actionTick();
			e.finalTick();
		}
		// }
		for (UpgradedGroup g : groups.values()) {
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

			if (e.canRender()) {
				e.draw(graphics);
			}
		}
		for (IPhenomenon e : phenomena.values()) {
			if (e.canRender()) {
				e.draw(graphics);
			}
		}
		graphics.popMatrix();
		graphics.popStyle();
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

	public UpgradedGroup getTestGroup() {
		return testGroup;
	}

	public Map<String, UpgradedGroup> getGroups() {
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
	public void assignProperty(IUpgradedKnowledgeBase culture, Property property, IPropertyData data) {
		// TODO assign property to world

	}

	@Override
	public IPropertyData getPropertyData(IUpgradedKnowledgeBase culture, Property property, boolean check) {
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
