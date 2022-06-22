package entity;

import processing.core.PApplet;
import psych.mind.Mind;
import psych.mind.Need;
import sim.ICanHaveMind;
import sim.IHasProfile;
import sim.Location;
import sim.World;
import sociology.Profile;

public abstract class Actor implements ICanHaveMind, IHasProfile, IPhysicalExistence {

	public static enum PossessState {
		NONE, HOLD, WEAR
	}

	public final static int STEP = 5;
	public final static int REACH = 50;

	private int x;
	private int y;

	private Actor possessor;
	private PossessState possessState = PossessState.NONE;

	private Actor clothing;
	private Actor held;

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
	 * A characteristic of actors who can think
	 */
	protected Mind mind = null;

	public Actor(World world, String name, int startX, int startY, int radius) {
		this.profile = new Profile(this, name);
		this.world = world;
		this.name = name;
		this.radius = radius;
		this.x = startX;
		this.y = startY;
		location = new Location(this, world, true);
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
	public Mind createMind(Need... needs) {
		return this.mind = new Mind(this, needs);
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

		world.fill(0);
		world.text(this.name, x, y);

	}

	public Location getLocation() {
		if (this.location.getX() != this.x || this.location.getY() != this.y) {
			this.location = new Location(this, world, true);
		}
		return location;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " \"" + name + "\": {" + this.profile + "} xyr=[" + x + "," + y + ","
				+ radius + "]";
	}

}
