package sim;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

import actor.Actor;
import civilization_and_minds.Noosphere;
import civilization_and_minds.group.IGroup;
import main.WorldGraphics;
import metaphysical.soul.generator.ISoulGenerator;
import metaphysical.soul.generator.SoulGenerator;
import phenomenon.IPhenomenon;
import sim.interfaces.IObjectType;
import sim.interfaces.IUniqueThing;

/**
 * TODO complete knowledge layer of world
 * 
 * @author borah
 *
 */
public class GameUniverse implements IUniqueThing {

	public static enum WorldType implements IObjectType {
		INSTANCE;

		@Override
		public String getUniqueName() {
			return "worldtype";
		}

		@Override
		public ConceptType getConceptType() {
			return ConceptType.WORLD_TYPE;
		}

		@Override
		public float averageUniqueness() {
			return 1.0f;
		}
	}

	protected Tile[][] contiguousGameMap;
	private int maprowcount;
	private int mapcolcount;
	protected Map<String, Tile> noncontiguousGameMap;
	protected GameMapTile currentTile;
	private Actor testActor;
	private Random rand = new Random();
	protected long ticks = 0;
	private ISoulGenerator soulGen;
	private Noosphere noosphere;
	private UUID id;
	private Map<UUID, IGroup> groups;

	public GameUniverse(UUID id, int widthInTiles, int heightInTiles) {
		this.soulGen = new SoulGenerator();
		this.noosphere = new Noosphere(this);
		this.id = id;
		this.mapcolcount = heightInTiles;
		this.maprowcount = widthInTiles;
		this.contiguousGameMap = new Tile[heightInTiles][widthInTiles];
		this.noncontiguousGameMap = new HashMap<>();
		this.groups = new TreeMap<>();
	}

	public Map<UUID, IGroup> getGroups() {
		return groups;
	}

	public void addNewGroup(IGroup group) {
		this.groups.put(group.getUUID(), group);
	}

	public IGroup removeGroup(UUID groupID) {
		return groups.remove(groupID);
	}

	/**
	 * Add a ttile to the map which is non contiguous
	 * 
	 * @param tile
	 * @return
	 */
	public GameUniverse addNonContiguousTile(Tile tile) {
		this.noncontiguousGameMap.put(tile.getUniqueName(), tile);
		return this;
	}

	/**
	 * initializes tiles in the tile map
	 * 
	 * @return
	 */
	public GameUniverse initTileMap() {
		for (int row = 0; row < maprowcount; row++) {
			for (int col = 0; col < mapcolcount; col++) {
				this.contiguousGameMap[row][col] = new Tile(row, col);
			}
		}
		return this;
	}

	/**
	 * Get number of rows of main tilemap
	 * 
	 * @return
	 */
	public int getRowCount() {
		return this.maprowcount;
	}

	/**
	 * Get number of columns of main tilemap
	 * 
	 * @return
	 */
	public int getColumnCount() {
		return this.mapcolcount;
	}

	/**
	 * Gets tile at position
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public Tile getTile(int row, int col) {
		return this.contiguousGameMap[row][col];
	}

	/**
	 * Get container of all knowledge
	 * 
	 * @return
	 */
	public Noosphere getNoosphere() {
		return noosphere;
	}

	/**
	 * If this game has a main generator of soulss
	 * 
	 * @return
	 */
	public boolean hasSoulGenerator() {
		return this.soulGen != null;
	}

	public ISoulGenerator getMainSoulGenerator() {
		return soulGen;
	}

	public void setMainSoulGenerator(ISoulGenerator soulGen) {
		this.soulGen = soulGen;
	}

	public void setCurrentTile(GameMapTile currentWorld) {
		this.currentTile = currentWorld;
	}

	public GameMapTile getCurrentTile() {
		return currentTile;
	}

	public int getWidth() {
		return currentTile.getWidth();
	}

	public int getHeight() {
		return currentTile.getHeight();
	}

	public synchronized <T extends Actor> T spawnActor(T a, boolean firstSpawn) {
		this.currentTile.spawnActor(a, firstSpawn);
		if (a == testActor && a.isMultipart())
			System.out.println(a.getAsMultipart().getBody().report());
		return a;
	}

	public synchronized <T extends IPhenomenon> T createPhenomenon(T a) {
		this.currentTile.createPhenomenon(a);
		return a;
	}

	public Collection<Actor> getActors() {
		return this.currentTile.getActors();
	}

	public Collection<IPhenomenon> getPhenomena() {
		return this.currentTile.getPhenomena();
	}

	private Actor makeTestActor() {

		/*
		 * testActor = this.spawnActor(new FoodActor(currentWorld, "food0", getHeight()
		 * / 2, getWidth() / 2, 10, 5f) .setColor(BasicColor.GREEN));
		 */
		// this.spawnActor(testActor = new UpgradedPerson(this, "bobzy", Species.HUMAN,
		// 300, 300, 10));
		// testActor.setOptionalColor(Color.GREEN.getRGB());

		return testActor;
	}

	public void worldSetup() {
		this.currentTile.load();
		System.out.println("setting up");
		this.makeTestActor();
		// Actor idk = null;
		/*
		 * (for (int i = 0; i < 100; i++) { int x = Math.max(0, Math.min(width, 501 +
		 * (int) (i * (rand().nextDouble() * 5 - 10)))); int y = Math.max(0,
		 * Math.min(height, 501 + (int) (i * (rand().nextDouble() * 5 - 10)))); if (i %
		 * 2 == 0) this.spawnActor((idk = new UpgradedPerson(this, "baba" + i,
		 * Species.ELF, x, y, 10)));
		 * 
		 * (idk).setOptionalColor(Color.CYAN.getRGB());
		 * 
		 * x = Math.max(0, Math.min(width, 501 + (int) (i * (rand().nextDouble() * 5 -
		 * 10)))); y = Math.max(0, Math.min(height, 401 + (int) (i *
		 * (rand().nextDouble() * 5 - 10))));
		 * 
		 * this.spawnActor(new Food(this, "food" + i, x, y, 5));
		 * 
		 * x = Math.max(0, Math.min(width, 501 + (int) (i * (rand().nextDouble() * 5 -
		 * 10)))); y = Math.max(0, Math.min(height, 401 + (int) (i *
		 * (rand().nextDouble() * 5 - 10))));
		 * 
		 * if (i % 5 == 0) {
		 * 
		 * this.spawnActor(new BadThing(this, "evil" + i, x, y, 10)); } else {
		 * this.spawnActor(new Flower(this, "flower" + i, x, y, (rand().nextInt(5) +
		 * 5))); } }
		 */
	}

	public synchronized void worldTick() {
		if (this.testActor != null && this.testActor.isRemoved())
			makeTestActor();
		this.currentTile.worldTick(ticks);
		this.groups.values().forEach((group) -> group.tick(ticks));
		ticks++;
	}

	public synchronized void draw(WorldGraphics graphics) {
		graphics.pushMatrix();
		graphics.pushStyle();
		graphics.translate(WorldGraphics.BORDER, WorldGraphics.BORDER);
		graphics.fill(100, 0, 100);
		graphics.rect(0, 0, getWidth(), getHeight());
		graphics.stroke(graphics.color(255, 255, 255));

		for (Actor e : currentTile.getActors()) {

			if (e.canRender()) {
				e.draw(graphics);
			}
		}
		for (IPhenomenon e : currentTile.getPhenomena()) {
			if (e.canRender()) {
				e.draw(graphics);
			}
		}
		graphics.popMatrix();
		graphics.popStyle();
	}

	public long getTicks() {
		return ticks;
	}

	public Actor getTestActor() {
		return testActor;
	}

	public Random rand() {
		return rand;
	}

	@Override
	public UUID getUUID() {
		return id;
	}

	@Override
	public IObjectType getObjectType() {
		return WorldType.INSTANCE;
	}

	@Override
	public String getUniqueName() {
		return "game" + id;
	}

}
