package actor;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import abilities.ESystem;
import abilities.SystemType;
import abilities.types.LifeSystem;
import main.ImmutableCollection;
import main.WorldGraphics;
import processing.core.PApplet;
import psychology.Soul;
import psychology.perception.Profile;
import psychology.perception.info.BruteTrait;
import psychology.social.concepts.TypeClass;
import sim.Location;
import sim.World;
import sim.interfaces.ICanHaveSoul;
import sim.interfaces.IPhysicalExistence;
import sim.interfaces.IRenderable;
import sim.interfaces.ISensable;

public abstract class Actor implements ICanHaveSoul, IPhysicalExistence, ISensable, IRenderable {

	public static enum PossessState {
		NONE, HOLD, WEAR
	}

	public final static int STEP = 5;
	public final static int REACH = 50;

	private Map<SystemType<?>, ESystem> systems = new TreeMap<>();

	private int x;
	private int y;

	private Actor possessor;
	private PossessState possessState = PossessState.NONE;

	private Actor clothing;
	private Actor held;
	protected Profile profile;

	protected Random rand = new Random();

	/**
	 * Probably gonna become unnecessary
	 */
	private String name;

	private World world;

	private int radius;

	private Integer optionalColor = null;
	private Location location;

	private UUID uuid = UUID.randomUUID();
	private int maxSouls = 7;

	private Soul soul;
	private TypeClass<? extends Actor> type;
	private Set<Soul> souls = new HashSet<>(0);
	private ImmutableCollection<Soul> soulsImmutable = new ImmutableCollection<>(souls);
	private Map<BruteTrait<?>, Object> traits = new HashMap<>(0);
	private Map<BruteTrait<?>, ESystem> updaters = new HashMap<>(0);

	public Actor(World world, TypeClass<? extends Actor> type, String name, int startX, int startY, int radius) {
		this.world = world;
		this.name = name;
		this.radius = radius;
		this.x = startX;
		this.y = startY;
		this.type = type;
		location = new Location(this, world);
		this.profile = new Profile(this);
	}

	@Override
	public Profile getProfile() {
		return profile;
	}

	@Override
	public TypeClass<?> getTypeClass() {
		return type;
	}

	protected void addSystems(ESystem... sys) {
		for (ESystem s : sys) {
			this.systems.put(s.getType(), s);
			Map<BruteTrait<?>, Object> o = s.initTraits();
			for (Map.Entry<BruteTrait<?>, Object> bt : o.entrySet()) {
				this.traits.put(bt.getKey(), bt.getValue());
				this.updaters.put(bt.getKey(), s);
			}

		}

	}

	/**
	 * creates a natural soul for this actor
	 */
	protected void createNaturalSoul() {
		this.soul = new Soul(this);
		this.souls.add(soul);
	}

	/**
	 * if the soul is not contained in here, then throw exception
	 * 
	 * @param to
	 */
	public void switchSoulControl(Soul to) {
		if (!souls.contains(to))
			throw new IllegalArgumentException(this + " " + to);
		this.soul = to;
	}

	/**
	 * removes the soul that is currently controlling
	 */
	public Soul removeSoul() {
		Soul s = this.soul;
		this.souls.remove(this.soul);
		this.soul = null;
		return s;
	}

	public void removeSoul(Soul toR) {
		if (!souls.contains(toR))
			throw new IllegalArgumentException(this + " " + toR);
		this.souls.remove(toR);
		if (this.soul != null && this.soul.equals(toR))
			this.soul = null;
	}

	@Override
	public boolean canRender() {
		return true;
	}

	@Override
	public Collection<ESystem> getSystems() {
		return systems.values();
	}

	@Override
	public boolean hasSystem(String name) {
		for (SystemType<?> t : this.systems.keySet())
			if (t.getId().equals(name))
				return true;
		return false;
	}

	public <T> T getTrait(BruteTrait<T> trait, boolean update) {
		if (update && this.updaters.containsKey(trait)) {
			this.traits.put(trait, this.updaters.get(trait).updateTrait(trait, traits.get(trait)));
		}
		return (T) this.traits.get(trait);
	}

	public boolean hasTrait(BruteTrait<?> trait) {
		return this.traits.containsKey(trait);
	}

	public Actor setOptionalColor(int optionalColor) {
		this.optionalColor = optionalColor;
		return this;
	}

	public int getOptionalColor() {
		return optionalColor;
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
		// TODO sensetick
	}

	public void thinkTick() {
		// TODO thinktick
	}

	public void actionTick() {
		// TODO actiontick
	}

	public void tick() {
		for (ESystem sys : this.getSystems()) {
			if (sys.canUpdate()) {
				sys._update(world.getTicks());
				for (BruteTrait<?> bt : this.updaters.keySet()) {
					if (updaters.get(bt) == sys) {
						sys.updateTrait(bt, this.traits.get(bt));
					}
				}
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
			this.location = new Location(this, world);
		}
		return location;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ":\"" + name + "\":" + this.uuid + ":{"
				+ /** TODO print actor profile */
				"}:[x=" + x + ",y=" + y + ",r=" + radius + "]";
	}

	@Override
	public Random rand() {
		return rand;
	}

	@Override
	public <T extends ESystem> T getSystem(SystemType<T> system) {
		return (T) this.systems.get(system);
	}

	@Override
	public Collection<SystemType<?>> getSystemTokens() {
		return this.systems.keySet();
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

	/**
	 * 
	 * @param soul
	 * @return
	 */
	public boolean addSoul(Soul soul) {
		if (this.souls.size() < this.maxSouls) {
			this.souls.add(soul);
			soul.setOwner(this);
			return true;
		}
		return false;
	}

	@Override
	public Collection<Soul> getContainedSouls() {
		return this.soulsImmutable;
	}

	@Override
	public int getMaxSouls() {
		return this.maxSouls;
	}

	@Override
	public Soul getNaturalSoul() {

		for (Soul s : this.souls) {
			if (s.getNaturalOwnerID().equals(this.uuid))
				return s;
		}
		return null;
	}

	@Override
	public Soul getSoul() {
		return soul;
	}

}
