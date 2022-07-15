package entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

import abilities.EntitySystem;
import abilities.types.LifeSystem;
import abilities.types.SystemType;
import culture.CulturalContext;
import processing.core.PApplet;
import psych_first.mind.Mind;
import psych_first.perception.knowledge.IKnowledgeType;
import psych_first.perception.senses.SensoryAttribute;
import psych_first.perception.senses.SensoryOutput;
import sim.ICanHaveMind;
import sim.Location;
import sim.World;
import sociology.InstanceProfile;
import sociology.Profile;
import sociology.TypeProfile;

public abstract class Actor implements ICanHaveMind, IPhysicalExistence, ISensable {

	public static enum PossessState {
		NONE, HOLD, WEAR
	}

	public final static int STEP = 5;
	public final static int REACH = 50;

	private Map<SystemType<?>, EntitySystem> systems = new TreeMap<>();

	private int x;
	private int y;

	private Actor possessor;
	private PossessState possessState = PossessState.NONE;

	private Actor clothing;
	private Actor held;

	private SensoryOutput sensory = new SensoryOutput(this);

	protected Random rand = new Random();

	/**
	 * Probably gonna become unnecessary
	 */
	private String name;

	private World world;

	private Profile profile;

	private int radius;

	private Integer optionalColor = null;
	private Location location;
	/**
	 * TODO determine what info an entity would constitute
	 */
	private Map<IKnowledgeType<?>, Object> information = new HashMap<>();

	/**
	 * A characteristic of actors who can think
	 */
	protected Mind mind = null;

	private UUID uuid = UUID.randomUUID();

	public Actor(World world, TypeProfile tp, String name, int startX, int startY, int radius) {
		this.profile = new InstanceProfile(this, tp, name);
		this.world = world;
		this.name = name;
		this.radius = radius;
		this.x = startX;
		this.y = startY;
		location = new Location(this, world);
	}

	protected void addSystems(SystemType<?>... t) {
		for (SystemType<?> tt : t) {
			this.systems.put(tt, tt.instantiate(this));
		}
	}

	protected void addSystems(EntitySystem... sys) {
		for (EntitySystem s : sys) {
			this.systems.put(s.getType(), s);
		}

	}

	@Override
	public Collection<EntitySystem> getSystems() {
		return systems.values();
	}

	@Override
	public TypeProfile getType() {
		return this.profile.getTypeProfile();
	}

	@Override
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

	/**
	 * Creates a mind for this object and returns the created mind
	 * 
	 * @return
	 */
	public Mind createMind() {
		return this.mind = new Mind(this);
	}

	public String getName() {
		return name;
	}

	public World getWorld() {
		return world;
	}

	public Profile getProfile() {
		return profile;
	}

	public void movementTick() {
		if (this.possessor != null) {
			this.setX(possessor.x);
			this.setY(possessor.y);
		}
	}

	public void senseTick() {
		if (hasMind())
			mind.observe();
	}

	public void thinkTick() {
		if (hasMind()) {
			mind.think();
		}
	}

	public void actionTick() {
		if (hasMind())
			mind.act();
	}

	public boolean hasMind() {
		return this.mind != null;
	}

	/**
	 * May be null
	 * 
	 * @return
	 */
	public Mind getMind() {
		return this.mind;
	}

	public void setMind(Mind mind) {
		this.mind = mind;
	}

	public void tick() {
		for (EntitySystem sys : this.getSystems()) {
			if (sys.isConstantUpdate()) {
				sys._update(world.getTicks());
			}
		}
	}

	public void finalTick() {
		if (world.getTicks() % 5 == 0) {
			this.world.getSensoryHandler().postSensory(sensory);
		}
	}

	protected Actor addInfo(IKnowledgeType<?> t, Object val) {
		this.information.put(t, val);
		return this;
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
		if (state == PossessState.HOLD) {
			this.held = other;
		} else {
			this.clothing = other;
		}
		other.possessor = this;
		other.possessState = state;
	}

	public void wear(Actor other) {
		possess(other, PossessState.WEAR);
	}

	public boolean pickUp(Actor other) {
		if (this.distance(other) < REACH) {
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

	public boolean reachable(Actor other) {
		return this.at(other);
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

	public final void draw() {
		world.push();
		render();
		world.pop();
	}

	protected void render() {
		world.ellipseMode(PApplet.RADIUS);
		if (optionalColor == null) {
			world.fill(255, 255, 0, 100);
			world.stroke(world.color(100, 100, 0));
		} else {
			world.fill(optionalColor, 100);
			world.stroke(optionalColor);
		}
		world.strokeWeight(1.4f);
		world.circle(x, y, radius);
		world.textAlign(PApplet.CENTER, PApplet.CENTER);
		boolean danger = false; // TODO make this more logical and have more effects
		boolean dead = false; // lol definitely this too...

		if (this.hasSystem(SystemType.LIFE)) {
			LifeSystem ensys = this.getSystem(SystemType.LIFE);
			if (ensys.isSevere())
				danger = true;
			if (ensys.isDead())
				dead = true;
		}
		if (dead) {
			world.fill(world.color(255, 255, 0));
		} else if (danger) {
			world.fill(world.color(255, 0, 0));
		} else {
			world.fill(0);

		}
		world.text(this.name, x, y);

	}

	public Location getLocation() {
		if (this.location.getX() != this.x || this.location.getY() != this.y) {
			this.location = new Location(this, world);
		}
		return location;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ":\"" + name + "\":" + this.uuid + ":{" + this.profile + "}:[x=" + x
				+ ",y=" + y + ",r=" + radius + "]";
	}

	@Override
	public Random rand() {
		return rand;
	}

	@Override
	public <T extends EntitySystem> T getSystem(SystemType<T> system) {
		return (T) this.systems.get(system);
	}

	@Override
	public Collection<SystemType<?>> getSystemTokens() {
		return this.systems.keySet();
	}

	@Override
	public SensoryOutput getSensory() {
		return this.sensory;
	}

	@Override
	public <T> T getInfo(IKnowledgeType<T> type, CulturalContext ctxt) {
		if (type.isSocialKnowledge()) {
			return this.profile.getInfo(type, ctxt);
		} else if (type == Location.Fundamental.LOCATION) {
			return (T) this.location;
		} else if (type instanceof SensoryAttribute<?>att) {
			return (T) this.sensory.getAttribute(att);
		} else {
			return (T) this.information.get(type);
		}
	}

	@Override
	public boolean hasInfo(IKnowledgeType<?> type, CulturalContext ctxt) {
		if (type.isSocialKnowledge()) {
			return this.profile.hasInfo(type, ctxt);
		} else if (type == Location.Fundamental.LOCATION) {
			return true;
		} else if (type instanceof SensoryAttribute<?>att) {
			return this.sensory.hasAttribute(att);
		} else
			return this.information.containsKey(type);
	}

	@Override
	public int hashCode() {
		return uuid.hashCode();
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Actor a && a.uuid.equals(this.uuid);
	}

}
