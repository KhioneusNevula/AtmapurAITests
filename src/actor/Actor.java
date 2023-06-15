package actor;

import java.util.Collection;
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
import biology.systems.types.LifeSystem;
import mind.concepts.type.Property;
import mind.memory.IKnowledgeBase;
import mind.memory.IPropertyData;
import processing.core.PApplet;
import sim.Location;
import sim.World;
import sim.WorldGraphics;
import sim.interfaces.ILocatable;
import sim.interfaces.IPhysicalExistence;
import sim.interfaces.IRenderable;

public abstract class Actor implements IUniqueExistence, IRenderable, IPhysicalExistence, ISystemHolder {

	public static enum PossessState {
		NONE, HOLD, WEAR
	}

	public final static int STEP = 5;
	public final static int REACH = 15;

	private Map<SystemType<?>, ESystem> systems = new TreeMap<>();

	private int x;
	private int y;

	private Actor possessor;
	private PossessState possessState = PossessState.NONE;

	private Actor clothing;
	private Actor held;

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

	private Table<IKnowledgeBase, Property, IPropertyData> socialProperties;

	public Actor(World world, String name, int startX, int startY, int radius) {
		this.world = world;
		this.name = name;
		this.radius = radius;
		this.x = startX;
		this.y = startY;
		location = new Location(this);
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

	public World getWorld() {
		return world;
	}

	public void movementTick() {
		if (this.possessor != null) {
			this.setX(possessor.x);
			this.setY(possessor.y);
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
		if (clothing != null && clothing.isRemoved())
			clothing = null;
		if (held != null && held.isRemoved())
			held = null;
		for (ESystem sys : this.getSystems()) {
			if (sys.canUpdate()) {
				sys._update(world.getTicks());
			}
		}
	}

	public void finalTick() {
		if (world.getTicks() % 5 == 0) {
			// TODO post sensory
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
		if (this.possessor != null) {
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
		return possessor;
	}

	public Actor getHeld() {
		return held;
	}

	public Actor getClothing() {
		return clothing;
	}

	public void possess(Actor other, PossessState state) {
		if (other.isRemoved())
			throw new IllegalArgumentException("dead " + other);
		if (state == PossessState.HOLD) {
			this.held = other;
		} else {
			this.clothing = other;
		}
		other.possessor = this;
		other.possessState = state;
	}

	/**
	 * return actors possessed by this one TODO make a better possession system
	 * 
	 * @param other
	 * @return
	 */
	public Collection<Actor> getPossessed() {

		return clothing != null && held != null ? Set.of(this.clothing, this.held)
				: (clothing == null && held != null ? Set.of(held)
						: (held == null && clothing != null ? Set.of(clothing) : Set.of()));
	}

	public void wear(Actor other) {
		possess(other, PossessState.WEAR);
	}

	public boolean pickUp(Actor other) {
		if (this.distance(other) <= REACH) {
			possess(other, PossessState.HOLD);
			return true;
		}
		return false;
	}

	public void drop() {
		this.held = null;
		held.possessor = null;
		held.possessState = PossessState.NONE;
	}

	public void strip() {
		this.held = this.clothing;
		this.clothing = null;
		this.held.possessState = PossessState.HOLD;
	}

	public boolean reachable(ILocatable other) {
		return this.distance(other) <= this.REACH;
	}

	public boolean held(Actor other) {
		return other == this.held;
	}

	public boolean worn(Actor other) {
		return other == this.clothing;
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
		g.text(this.name, x, y);

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

	@Override
	public IPropertyData getPropertyData(IKnowledgeBase culture, Property property) {
		if (this.socialProperties == null)
			return null;
		return socialProperties.get(culture, property);
	}

	@Override
	public void assignProperty(IKnowledgeBase culture, Property property, IPropertyData data) {
		(socialProperties == null ? socialProperties = HashBasedTable.create() : socialProperties).put(culture,
				property, data);
	}

	public Map<IKnowledgeBase, Map<Property, IPropertyData>> getSocialProperties() {
		return socialProperties == null ? Map.of() : socialProperties.rowMap();
	}

	public boolean isMultipart() {
		return this instanceof MultipartActor;
	}

	public boolean isLiving() {
		return this instanceof LivingActor;
	}

	public MultipartActor getAsMultipart() {
		return (MultipartActor) this;
	}

	public LivingActor getAsLiving() {
		return (LivingActor) this;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void remove() {
		this.removed = true;
	}

}
