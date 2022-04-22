package entity;

import sim.World;
import sociology.Profile;

public abstract class Actor {

	public static enum PossessState {
		NONE, HOLD, WEAR
	}

	public final static int STEP = 5;
	public final static int REACH = 2;

	private int x;
	private int y;

	private Actor possessor;
	private PossessState possessState = PossessState.NONE;

	private Actor clothing;
	private Actor held;

	private String name;

	private World world;

	private Profile profile;

	public Actor(World world, String name, int startX, int startY) {
		this.profile = new Profile(this);
		this.world = world;
		this.name = name;

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

	public void baseTick() {
		if (this.possessor != null) {
			this.setX(possessor.x);
			this.setY(possessor.y);
		}
		tick();
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
		setX(x + xplus);
		setY(y + yplus);
	}

	public void moveLeft() {
		move(-5, 0);
	}

	public void moveRight() {
		move(5, 0);
	}

	public void moveUp() {
		move(0, 5);
	}

	public void moveDown() {
		move(0, -5);
	}

	public double distance(Actor other) {
		return Math.sqrt(Math.pow((other.x - this.x), 2) + Math.pow((other.y - this.y), 2));
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

	/**
	 * Notification to this actor that an actor was spawned in the world
	 * 
	 * @param e
	 */
	public void notifyOfSpawn(Actor e) {

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
}
