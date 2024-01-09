
package actor;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import biology.systems.ESystem;
import biology.systems.ISystemHolder;
import biology.systems.SystemType;
import biology.systems.types.ISensor;
import biology.systems.types.LifeSystem;
import main.Pair;
import mind.concepts.type.Property;
import mind.memory.IPropertyData;
import mind.thought_exp.memory.IUpgradedKnowledgeBase;
import processing.core.PApplet;
import sim.Location;
import sim.World;
import sim.WorldGraphics;
import sim.interfaces.ILocatable;
import sim.interfaces.IPhysicalExistence;
import sim.interfaces.IRenderable;

public abstract class Actor implements IUniqueExistence, IRenderable, IPhysicalExistence, ISystemHolder {

	private final static int STEP = 10;
	private final static int REACH = 15;

	private Map<SystemType<?>, ESystem> systems = new TreeMap<>();

	private int x;
	private int y;

	private WeakReference<Actor> possessor = new WeakReference<>(null);
	private PossessState possessState = PossessState.NONE;

	private WeakReference<Actor> clothing = new WeakReference<Actor>(null);
	private WeakReference<Actor> held = new WeakReference<>(null);

	protected Random rand = new Random();
	private boolean removed;

	/**
	 * Probably gonna become unnecessary
	 */
	private String name;

	private World world;

	private int radius;

	private Integer optionalColor = null;
	private Location location;

	private UUID uuid = UUID.randomUUID();

	private Table<IUpgradedKnowledgeBase, Property, Pair<IPropertyData, Long>> socialProperties;

	protected ITemplate species;

	/**
	 * Amount of milliseconds it takes properties to fade
	 */
	private long propertyDecayTime = 10000;// TODO change this to 86400000L;

	public Actor(World world, String name, ITemplate species, int startX, int startY, int radius) {
		this.world = world;
		this.name = name;
		this.radius = radius;
		this.x = startX;
		this.y = startY;
		location = new Location(this);
		this.species = species;
	}

	protected void addSystems(ESystem... sys) {
		for (ESystem s : sys) {
			this.systems.put(s.getType(), s);

		}

	}

	@Override
	public boolean canRender() {
		return true;
	}

	public Collection<ESystem> getSystems() {
		return systems.values();
	}

	public boolean hasSystem(String name) {
		for (SystemType<?> t : this.systems.keySet())
			if (t.getId().equals(name))
				return true;
		return false;
	}

	public Actor setOptionalColor(int optionalColor) {
		this.optionalColor = optionalColor;
		return this;
	}

	public int getOptionalColor() {
		return optionalColor;
	}

	@Override
	public UUID getUUID() {
		return this.uuid;
	}

	public String getName() {
		return name;
	}

	@Override
	public String getSimpleName() {
		return name;
	}

	public World getWorld() {
		return world;
	}

	public ITemplate getSpecies() {
		return species;
	}

	public void movementTick() {
		if (this.possessor.get() != null && !this.possessor.get().isRemoved()) {
			this.setX(possessor.get().x);
			this.setY(possessor.get().y);
		}
		if (this.held.get() != null && this.held.get().isRemoved()) {
			this.held = new WeakReference<>(null);
		}
		if (this.clothing.get() != null && this.clothing.get().isRemoved()) {
			this.clothing = new WeakReference<>(null);
		}
	}

	public void senseTick() {
		// TODO sensetick may be useless with systemholder

	}

	public void thinkTick() {
		// TODO thinktick
	}

	public void actionTick() {
		// TODO actiontick
	}

	public void tick() {

		for (ESystem sys : this.getSystems()) {
			if (sys instanceof LifeSystem && ((LifeSystem) sys).isDead()) {
				this.remove(); // TODO make some corpse feature idk idfk
			}
			if (sys.canUpdate()) {
				sys._update(world.getTicks());
			}
		}
	}

	public void finalTick() {
		if (world.getTicks() % 5 == 0) {
			// TODO post sensory
		}
		if (socialProperties != null) {
			Iterator<Pair<IPropertyData, Long>> iter = socialProperties.values().iterator();
			while (iter.hasNext()) {
				Pair<IPropertyData, Long> obj = iter.next();
				if (System.currentTimeMillis() - obj.getSecond() >= this.propertyDecayTime) {
					iter.remove();
				}
			}
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void move(int xplus, int yplus) {
		if (this.possessor.get() != null) {
			return;
		}
		IPhysicalExistence.super.move(xplus, yplus);
	}

	public void moveLeft() {
		move(-STEP, 0);
	}

	public void moveRight() {
		move(STEP, 0);
	}

	public void moveUp() {
		move(0, STEP);
	}

	public void moveDown() {
		move(0, -STEP);
	}

	public PossessState getPossessState() {
		return possessState;
	}

	public Actor getPossessor() {
		return possessor.get();
	}

	public Actor getHeld() {
		return held.get();
	}

	public Actor getClothing() {
		return clothing.get();
	}

	public void possess(Actor other, PossessState state) {
		if (other.isRemoved() || other == this)
			throw new IllegalArgumentException("dead or held " + other + " by " + this);
		if (state == PossessState.HOLD) {
			this.held = new WeakReference<>(other);
		} else {
			this.clothing = new WeakReference<>(other);
		}
		other.possessor = new WeakReference<>(this);
		other.possessState = state;
	}

	/**
	 * return actors possessed by this one TODO make a better possession system
	 * 
	 * @param other
	 * @return
	 */
	public Collection<Actor> getPossessed() {

		return clothing.get() != null && held.get() != null ? Set.of(this.clothing.get(), this.held.get())
				: (clothing.get() == null && held.get() != null ? Set.of(held.get())
						: (held.get() == null && clothing.get() != null ? Set.of(clothing.get()) : Set.of()));
	}

	public void wear(Actor other) {
		possess(other, PossessState.WEAR);
	}

	public boolean pickUp(Actor other) {
		if (this.reachable(other)) {
			possess(other, PossessState.HOLD);
			return true;
		}
		return false;
	}

	public void drop() {
		if (held.get() != null) {
			held.get().possessor = new WeakReference<>(null);
			held.get().possessState = PossessState.NONE;
			this.held = new WeakReference<>(null);
		}
	}

	public void strip() {
		if (this.clothing.get() != null) {
			this.held = this.clothing;
			this.clothing = null;
			this.held.get().possessState = PossessState.HOLD;
		}
	}

	public boolean reachable(ILocatable other) {
		return this.distance(other) <= REACH;
	}

	public int getReach() {
		return REACH;
	}

	public boolean held(Actor other) {
		return other == this.held.get();
	}

	public boolean worn(Actor other) {
		return other == this.clothing.get();
	}

	public boolean at(Actor other) {
		return other.x == this.x && other.y == this.y;
	}

	public String getStatus() {
		return this.name + " is at " + this.x + ", " + this.y;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getRadius() {
		return radius;
	}

	public final void draw(WorldGraphics g) {
		g.push();
		render(g);
		g.pop();
	}

	protected void render(WorldGraphics g) {
		g.ellipseMode(PApplet.RADIUS);
		if (optionalColor == null) {
			g.fill(255, 255, 0, 100);
			g.stroke(g.color(100, 100, 0));
		} else {
			g.fill(optionalColor, 100);
			g.stroke(optionalColor);
		}
		g.strokeWeight(1.4f);
		g.circle(x, y, radius);
		g.textAlign(PApplet.CENTER, PApplet.CENTER);
		boolean danger = false; // TODO make danger more clear
		boolean dead = false; // TODO make death more clear

		if (this.hasSystem(SystemType.LIFE)) {
			LifeSystem ensys = this.getSystem(SystemType.LIFE);
			if (ensys.isSevere())
				danger = true;
			if (ensys.isDead())
				dead = true;
		}
		if (dead) {
			g.fill(g.color(255, 255, 0));
		} else if (danger) {
			g.fill(g.color(255, 0, 0));
		} else {
			g.fill(0);

		}
		if (this.isUSentient()) {
			g.text(this.name, x, y);
		} else {
			g.text(this.name, x, y);
		}

	}

	public Location getLocation() {
		if (this.location.getX() != this.x || this.location.getY() != this.y) {
			this.location = new Location(this);
		}
		return location;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ":\"" + name + "\":(" + x + "," + y + ")";
	}

	public Random rand() {
		return rand;
	}

	public boolean hasSystem(SystemType<?> system) {
		return this.systems.containsKey(system);
	}

	public <T extends ESystem> T getSystem(SystemType<T> system) {
		return (T) this.systems.get(system);
	}

	public Collection<SystemType<?>> getSystemTokens() {
		return this.systems.keySet();
	}

	@Override
	public int hashCode() {
		return uuid.hashCode();
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Actor a && a.uuid.equals(this.uuid);
	}

	/**
	 * check = true if the property's "timer" should renew when checked
	 * 
	 * @param culture
	 * @param property
	 * @param check
	 * @return
	 */
	@Override
	public IPropertyData getPropertyData(IUpgradedKnowledgeBase culture, Property property, boolean check) {
		if (property == Property.ANY)
			return IPropertyData.PRESENCE;
		if (this.socialProperties == null)
			return IPropertyData.UNKNOWN;
		Pair<IPropertyData, Long> pair = socialProperties.get(culture, property);
		if (pair == null)
			return IPropertyData.UNKNOWN;
		if (check)
			pair.setSecond(System.currentTimeMillis());
		return pair.getFirst();
	}

	@Override
	public void assignProperty(IUpgradedKnowledgeBase culture, Property property, IPropertyData data) {
		(socialProperties == null ? socialProperties = HashBasedTable.create() : socialProperties).put(culture,
				property, Pair.of("data", data, "added_time", System.currentTimeMillis()));
	}

	public Map<IUpgradedKnowledgeBase, Map<Property, Pair<IPropertyData, Long>>> getSocialProperties() {
		return socialProperties == null ? Map.of() : socialProperties.rowMap();
	}

	public boolean isMultipart() {
		return this instanceof MultipartActor;
	}

	public boolean isUSentient() {
		return this instanceof UpgradedSentientActor;
	}

	public UpgradedSentientActor getAsUpgradedSentient() {
		return (UpgradedSentientActor) this;
	}

	public MultipartActor getAsMultipart() {
		return (MultipartActor) this;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void remove() {
		this.removed = true;
	}

	public double getStep() {
		return STEP;
	}

	@Override
	public IPropertyData getPropertyHint(Property property) {
		if (property == Property.ANY)
			return IPropertyData.PRESENCE;
		return this.species.getPropertyHint(property);
	}

	@Override
	public Collection<ISensor> getPreferredSensesForHint(Property property) {
		return this.species.getPreferredSensesForHint(property);
	}

}
