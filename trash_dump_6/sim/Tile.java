package sim;

import sim.interfaces.IObjectType;

/**
 * A signifier for a specific world tile on a world map. Contiguous tiles are
 * tiles which are connected on the main world map, while !contiguous tiles are
 * not connected.
 * 
 * @author borah
 *
 */
public class Tile implements IObjectType, Comparable<Tile> {

	private static final String MAIN_SIGNIFIER = "overworld";
	private String name;
	private int col;
	private int row;
	/**
	 * If the tile is contiguous with the rest of the map.
	 */
	private boolean contiguous;

	/**
	 * 
	 * @param name
	 * @param row  the row of this tile on the map
	 * @param col  the col of this tile on the map
	 */
	public Tile(int row, int col) {
		this.name = MAIN_SIGNIFIER;
		this.row = row;
		this.col = col;
		this.contiguous = true;
	}

	/**
	 * Form a noncontiguous tile on the map
	 * 
	 * @param name
	 */
	public Tile(String name) {
		this.name = name;
		this.row = -1;
		this.col = -1;
		this.contiguous = false;
	}

	/**
	 * Whether this tile is on the main world map
	 * 
	 * @return
	 */
	public boolean isContiguous() {
		return contiguous;
	}

	/**
	 * Return the geographic column of this tile. Results undefined if noncontiguous
	 * 
	 * @return
	 */
	public int getCol() {
		return col;
	}

	/**
	 * Return the geographic row of this tile. Results undefine if noncontiguous.
	 * 
	 * @return
	 */
	public int getRow() {
		return row;
	}

	@Override
	public String getUniqueName() {
		return "tile_" + name + (contiguous ? ("_" + row + "_" + col) : "_noncontiguous");
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Tile tile) {
			return this.name.equals(tile.name)
					&& (this.contiguous ? this.row == tile.row && this.col == tile.col : !tile.contiguous);
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return this.name.hashCode() + (this.contiguous ? this.row * this.col + 1 : 0);
	}

	@Override
	public ConceptType getConceptType() {
		return ConceptType.MAP_TILE;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public float averageUniqueness() {
		return 1.0f;
	}

	@Override
	public int compareTo(Tile o) {
		if (this.contiguous && o.contiguous) {
			return this.row * this.col - o.row * o.col;
		} else {
			return this.name.compareTo(o.name);
		}
	}

	@Override
	public String toString() {
		return this.contiguous ? "Tile(r=" + row + ",col=" + col + ")" : "Tile(" + this.name + ")";
	}

}
