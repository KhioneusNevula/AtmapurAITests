package culture;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import sim.World;

public class CulturalContext implements Iterable<Culture> {

	private Set<Culture> cultures = new HashSet<>();

	private static final CulturalContext universal = new CulturalContext();
	private World world;

	public static CulturalContext of(World worldOf, Culture... cultures) {
		if (cultures.length == 0)
			return universal;
		CulturalContext c = new CulturalContext(cultures);
		c.world = worldOf;
		return c;
	}

	public World getWorld() {
		return world;
	}

	/**
	 * whether this context contains a culture of the given name in its hierarchy
	 * 
	 * @param culture
	 * @return
	 */
	public boolean containsInHierarchy(String culture) {
		Culture m;
		return (m = world.getCulture(culture)) == null ? false : m.isSuperiorToAny(this);
	}

	/**
	 * whether this context contains the given culture somewhere in its hierarchy
	 * 
	 * @param culture
	 * @return
	 */
	public boolean containsInHierarchy(Culture culture) {
		return culture.isSuperiorToAny(this);
	}

	public static CulturalContext getUniversal() {
		return universal;
	}

	public boolean isUniversal() {
		return this.cultures.isEmpty();
	}

	private CulturalContext(Culture... cultures) {
		for (Culture cul : cultures) {
			this.cultures.add(cul);
		}
	}

	/**
	 * adds cultures to this context by making a new version of this one
	 * 
	 * @param adds
	 * @return
	 */
	public CulturalContext add(Culture... adds) {
		if (this.isUniversal()) {
			return of(null, adds);
		}
		CulturalContext new_ = new CulturalContext(adds);
		new_.cultures.addAll(this.cultures);
		new_.world = this.world;
		return new_;
	}

	/**
	 * removes cultures from a copy of this one
	 * 
	 * @param rems
	 * @return
	 */
	public CulturalContext remove(Culture... rems) {
		if (this.isUniversal())
			return this;
		boolean flag = true;
		for (Culture c : rems) {
			if (cultures.contains(c)) {
				flag = false;
			}
		}
		if (flag)
			return this;
		Set<Culture> ofrems = Set.of(rems);
		CulturalContext new_ = new CulturalContext();
		new_.world = this.world;
		new_.cultures.addAll(this.cultures);
		new_.cultures.removeAll(ofrems);
		return new_;
	}

	@Override
	public Iterator<Culture> iterator() {
		return cultures.iterator();
	}

	public boolean contains(Culture c) {
		return cultures.contains(c);
	}

	@Override
	public String toString() {
		return "c;" + this.cultures;
	}

}
