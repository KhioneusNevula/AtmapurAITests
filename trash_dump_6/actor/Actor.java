
package actor;

import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

import actor.construction.ISystemHolder;
import actor.construction.physical.IActorType;
import actor.construction.physical.IComponentPart;
import actor.construction.physical.IPhysicalActorObject;
import actor.construction.physical.IPhysicalActorObject.HitboxType;
import actor.types.abstract_classes.MultipartActor;
import biology.systems.ESystem;
import biology.systems.SystemType;
import biology.systems.types.LifeSystem;
import main.WorldGraphics;
import metaphysical.ISpiritObject;
import metaphysical.ISpiritObject.SpiritType;
import processing.core.PApplet;
import sim.GameMapTile;
import sim.interfaces.IDynamicsObject;
import sim.interfaces.ILocatable;
import sim.interfaces.IRenderable;
import sim.physicality.ExistencePlane;
import sim.physicality.IInteractability.CollisionType;
import utilities.Location;
import utilities.MathHelp;

public abstract class Actor implements IUniqueEntity, IRenderable, IDynamicsObject, ISystemHolder {

	private final static int STEP = 10;
	private final static int REACH = 15;

	private Map<SystemType<?>, ESystem> systems = new TreeMap<>();

	private float x;
	private float y;

	private float xVelocity;
	private float yVelocity;

	private boolean frictionActive = true;
	private boolean dragActive = false;

	protected Random rand = new Random();
	private boolean removed;

	/**
	 * Probably gonna become unnecessary
	 */
	private String name;

	private GameMapTile world;

	private Location location;

	private UUID uuid = UUID.randomUUID();

	protected IActorType species;

	private Actor holder;

	/**
	 * Amount of milliseconds it takes properties to fade
	 */
	// private long propertyDecayTime = 10000;// change this to 86400000L;

	public Actor(GameMapTile world, String name, IActorType species, int startX, int startY) {
		this.world = world;
		this.name = name;
		this.x = startX;
		this.y = startY;
		location = new Location(startX, startY);
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

	public Color getOptionalColor() {
		return Color.green;
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

	@Override
	public String getUniqueName() {
		return this.name + this.uuid;
	}

	public GameMapTile getWorld() {
		return world;
	}

	public IActorType getObjectType() {
		return species;
	}

	@Override
	public int physicality() {
		return this.getPhysical().physicalityMode();
	}

	/**
	 * Returns the physical representation of the actor
	 * 
	 * @return
	 */
	public abstract IPhysicalActorObject getPhysical();

	public Force calculateDynamicFriction() {
		float normalMagnitude = getMass() * world.getG(this.getX(), this.getY());
		float mu = world.getFrictionMu(this, this.getX(), this.getY());
		Force fricForce = Force.fromAngleInRadians(Math.atan2(-yVelocity, -xVelocity), normalMagnitude * mu);
		// Force fricForce = Force.fromVectorAndMagnitude(-xVelocity, -yVelocity,
		// normalMagnitude * mu);
		return fricForce;
	}

	public void movementTick(long tick) {

		if (this.getPhysical().completelyDestroyed()) {
			System.out.println("Removing " + this + " due to physical being destroyed. \nPhysical report: "
					+ this.getPhysical().report());
			this.remove();
		}
		if (!this.isHeld()) {
			if (!this.isStationary() && !this.removed) {
				this.x += xVelocity;
				this.y += yVelocity;
				Force fricForce = this.calculateDynamicFriction();

				// System.out.println("dynamic friction: x=" + fricForce.getXForce() + "N,y=" +
				// fricForce.getYForce() + "N");
				// System.out.println("velocity: x=" + xVelocity + "m/s,y=" + yVelocity +
				// "m/s");

				float xacc = fricForce.getXForce() / this.getMass();
				float yacc = fricForce.getYForce() / this.getMass();
				if (Math.abs(xacc) > Math.abs(xVelocity)) {
					xVelocity = 0;
				} else {
					xVelocity += xacc;
				}
				if (Math.abs(yacc) > Math.abs(yVelocity)) {
					yVelocity = 0;
				} else {
					yVelocity += yacc;
				}
			}
		} else {
			this.applyForce(Force.fromVectorAndMagnitude(holder.x - this.x, holder.y - this.y,
					world.getStaticFrictionMu(this, getX(), getY()) * 1.5f));
		}

		world.getActors().forEach((act) -> {
			CollisionType ct = this.isCollidingWith(act);
			if (ct.intersecting()) {
				if (this.processIntersection(tick, act, ct)) {
					// TODO collision handling
				}
			}
		});
	}

	public void tick(long tick) {

		for (ESystem sys : this.getSystems()) {
			if (sys instanceof LifeSystem && ((LifeSystem) sys).isDead()) {
				// this.remove(); // TODO make some corpse feature idk idfk
			}
			if (sys.canUpdate()) {
				sys._update(tick);
			}
		}
	}

	public void senseTick(long tick) {
		// TODO sensetick may be useless with systemholder

	}

	public void thinkTick(long tick) {
		// TODO thinktick
		List<ISpiritObject> toRemove = new LinkedList<>();
		for (SpiritType stype : SpiritType.values()) {
			Iterator<? extends ISpiritObject> iter = this.getPhysical().getContainedSpirits(stype).iterator();
			while (iter.hasNext()) {
				ISpiritObject spir = iter.next();
				spir.tick(tick);
				if (spir.shouldRemove(tick)) {
					toRemove.add(spir);
				}
				if (!spir.isTetheredToWhole()) {
					int count = spir.tetherCount();
					for (IComponentPart part : spir.getTethers()) {
						if (part.isGone())
							count--;
					}
					if (count <= 0) {
						toRemove.add(spir);
					}
				}
			}
		}
		for (ISpiritObject s : toRemove) {
			s.onRemove(tick);
			this.getPhysical().removeSpirit(s);
		}
	}

	public void actionTick(long tick) {
		// TODO actiontick (probably remove this)
	}

	/**
	 * Process the intersection/collision between this and the ggiven actor. Return
	 * true if the two given entities should not be separated by motion; return
	 * false if they should clip together
	 * 
	 * @param tick
	 * @param collidingWith
	 * @param type
	 */
	public boolean processIntersection(long tick, Actor collidingWith, CollisionType type) {
		return type.colliding();
	}

	public void finalTick(long tick) {
		if (tick % 5 == 0) {
			// TODO post sensory
		}
	}

	@Override
	public boolean hasFriction() {
		return this.frictionActive;
	}

	@Override
	public boolean experiencesDrag() {
		return this.dragActive;
	}

	protected void setFrictionActive(boolean frictionActive) {
		this.frictionActive = frictionActive;
	}

	protected void setDragActive(boolean dragActive) {
		this.dragActive = dragActive;
	}

	/**
	 * Whether this actor is held
	 * 
	 * @return
	 */
	public boolean isHeld() {
		return holder != null;
	}

	/**
	 * Get the actor holding this one, if any
	 * 
	 * @return
	 */
	public Actor getHolder() {
		return holder;
	}

	/**
	 * Set the actor holding this one
	 * 
	 * @param holder
	 */
	public void setHolder(Actor holder) {
		this.holder = holder;
	}

	@Override
	public void accelerate(float xVec, float yVec) {
		this.xVelocity += xVec;
		this.yVelocity += yVec;
	}

	private float calculateStaticFriction() {
		float normalMagnitude = this.getMass() * world.getG(this.getX(), this.getY());
		float staticMu = world.getStaticFrictionMu(this, this.getX(), this.getY());
		float staticForce = normalMagnitude * staticMu;
		return staticForce;
	}

	@Override
	public void applyForce(Force force, boolean ignoreFriction) {
		if (ignoreFriction || !this.hasFriction()) {
			this.xVelocity += force.getXForce() / this.getMass();
			this.yVelocity += force.getYForce() / this.getMass();
			return;
		}
		float xForce = force.getXForce();
		float yForce = force.getYForce();
		boolean canMove = false;
		if (this.isStationary()) {
			float staticForce = this.calculateStaticFriction();
			if (force.getMagnitude() > staticForce) {
				canMove = true;
			}

		}
		if (canMove) {
			this.xVelocity += xForce / this.getMass();
			this.yVelocity += yForce / this.getMass();
		}

	}

	@Override
	public float getMass() {
		return this.getPhysical().getMass();
	}

	@Override
	public float getXVelocity() {
		return this.xVelocity;
	}

	@Override
	public float getYVelocity() {
		return this.yVelocity;
	}

	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}

	public float getTrueX() {
		return x;
	}

	public float getTrueY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void move(int xplus, int yplus) {
		IDynamicsObject.super.move(xplus, yplus);
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

	public boolean reachable(ILocatable other) {
		return this.distance(other) <= REACH;
	}

	public boolean reachable(int x, int y) {
		return this.distance(x, y) <= REACH;
	}

	public int getReach() {
		return REACH;
	}

	public boolean at(Actor other) {
		return other.getX() == this.getX() && other.getY() == this.getY();
	}

	public String getStatus() {
		return this.name + " is at " + this.x + ", " + this.y;
	}

	public int getRadius() {
		return this.getPhysical().getHitboxRadius();
	}

	public final void draw(WorldGraphics g) {
		g.push();
		render(g);
		g.pop();
	}

	protected void render(WorldGraphics g) {
		if (!isRemoved()) {
			g.ellipseMode(PApplet.RADIUS);
			if (this.getOptionalColor() == null) {
				g.fill(255, 255, 0, 100);
				g.stroke(g.color(100, 100, 0));
			} else {
				g.fill(this.getOptionalColor().getRGB(), 100);
				g.stroke(this.getOptionalColor().getRGB());
			}
			g.strokeWeight(1.4f);
			boolean rectMode = false;
			if (this.getPhysical().getHitboxType().isCircle()) {

				g.circle((int) x, (int) y, getPhysical().getHitboxRadius());
			} else {
				rectMode = true;
				g.rectMode(PApplet.CORNER);
				g.rect((int) x, (int) y, getPhysical().getHitboxWidth(), getPhysical().getHitboxHeight());
			}
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
			if (!rectMode) {
				g.text(this.name, (int) x, (int) y);
			} else {
				g.text(this.name, (int) x + getPhysical().getHitboxWidth() / 2,
						(int) y + getPhysical().getHitboxHeight() / 2);
			}
		} else {
			float minX = getPhysical().getHitboxType().isCircle() ? getTrueX() - getPhysical().getHitboxRadius()
					: getTrueX();
			float minY = getPhysical().getHitboxType().isCircle() ? getTrueY() - getPhysical().getHitboxRadius()
					: getTrueY();
			float maxX = getPhysical().getHitboxType().isCircle() ? getTrueX() + getPhysical().getHitboxRadius()
					: getTrueX() + getPhysical().getHitboxWidth();
			float maxY = getPhysical().getHitboxType().isCircle() ? getTrueY() + getPhysical().getHitboxRadius()
					: getTrueY() + getPhysical().getHitboxHeight();
			int iters = (int) g.random(5, 20);
			for (int i = 0; i < iters; i++) {
				g.stroke(this.getOptionalColor().getRGB(), 200);
				g.fill(this.getOptionalColor().getRGB(), 160);
				float x = g.random(minX - 5, maxX + 5);
				float y = g.random(minY - 5, maxY + 5);
				float rad = g.random(2, 10);
				g.ellipseMode(PApplet.CENTER);
				g.circle(x, y, rad);
			}

		}

	}

	public Location getLocation() {
		if (this.location.getX() != this.getX() || this.location.getY() != this.getY()) {
			this.location = new Location((int) x, (int) y);
		}
		return location;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ":\"" + name + "\":(" + x + "," + y + ")";
	}

	public String report() {

		return "actor:" + this.name + " of mass " + this.getMass() + "kg" + "\nbody:" + this.getPhysical().report()
				+ "\nphysical planes:" + ExistencePlane.decomposeCombinedValue(this.physicality(), true) + "\n"
				+ (isStationary() ? "static friction: " + this.calculateStaticFriction() + "N"
						: "dynamic friction: " + this.calculateDynamicFriction())
				+ (this.getSystems().isEmpty() ? "" : "\nsystems:" + this.getSystemsReport());
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

	public boolean isMultipart() {
		return this instanceof MultipartActor;
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

	public CollisionType isCollidingWith(Actor other) {
		return this.world.areColliding(this, other);
	}

	public boolean pointInHitbox(int x, int y) {
		if (this.getPhysical().getHitboxType() == HitboxType.CIRCLE) {
			return this.distance(x, y) < this.getPhysical().getHitboxRadius();
		} else {
			return MathHelp.pointInRect(x, y, this.getX(), this.getY(), this.getPhysical().getHitboxWidth(),
					this.getPhysical().getHitboxHeight());
		}
	}

	/**
	 * Do things when actor is spawned
	 */
	public void onSpawn() {

	}

}
