package culture;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import sim.World;
import sociology.sociocon.IPurposeSource;
import sociology.sociocon.Sociocat;
import sociology.sociocon.Sociocon;

public class Culture {

	/**
	 * the string id of the universal root culture
	 */
	public static final String ROOT = "_root";
	/**
	 * the string id for the cultures of all entities bound in time (entities which
	 * record the past rather than living in the moment)
	 */
	public static final String TIMEBOUND = "_timebound";
	/**
	 * the string id for the cultures of all entities bound in space (entities which
	 * recognize location)
	 */
	public static final String SPACEBOUND = "_spacebound";
	/**
	 * the string id for cultures of all entities that can die (entities which
	 * recognize danger and death)
	 */
	public static final String MORTAL = "_mortal";
	/**
	 * the string id for cultures of all entities that are sentient (can feel
	 * emotions; must be mortal)
	 */
	public static final String SENTIENT = "_sentient";
	/**
	 * the string id for cultures of all entities that are social (can form
	 * relationships and hierarchies)
	 */
	public static final String SOCIAL = "_social";

	/**
	 * the string id for cultures of all entities that can use language (must be
	 * social)
	 */
	public static final String LINGUISTIC = "_linguistic";

	/**
	 * the string id for cultures of all entities that can learn things (must be
	 * linguistic)
	 */
	public static final String LEARNING = "_learning";

	/**
	 * the string id for cultures of entities that can use tools (must be learning)
	 */
	public static final String TOOL_USER = "_tool_user";

	/**
	 * the string id for cultures of all entities that have bodies; must be
	 * spacebound and mortal
	 */
	public static final String EMBODIED = "_embodied";

	/**
	 * the string id for cultures of all entities that are organic and have hunger
	 * needs and the like
	 */
	public static final String ORGANIC = "_organic";

	private Set<Culture> parents = new HashSet<>();

	private Set<Culture> children = new HashSet<>();

	private Set<Culture> ancestors = new HashSet<>();

	private String name;
	private boolean isEssential;
	private World world;
	private BasicIterable iter = new BasicIterable(children);

	private BasicIterable iterp = new BasicIterable(parents);

	private BasicIterable itera = new BasicIterable(ancestors);

	private TreeMap<Sociocat, Map<String, Sociocon>> sociocons = new TreeMap<>();

	/**
	 * TODO make this more generalizable
	 * 
	 * @param forWorld
	 */
	public static void genEssentialCultures(World forWorld) {
		Culture root = new Culture(forWorld);
		forWorld.addCulture(root);
		Culture timebound = new Culture(TIMEBOUND, forWorld, true);
		forWorld.addCulture(timebound);
		Culture spacebound = new Culture(SPACEBOUND, forWorld, true);
		forWorld.addCulture(spacebound);
		Culture mortal = new Culture(MORTAL, forWorld, true);
		forWorld.addCulture(mortal);
		Culture sentient = new Culture(SENTIENT, forWorld, true);
		forWorld.addCulture(sentient.setParents(mortal));
		Culture social = new Culture(SOCIAL, forWorld, true);
		forWorld.addCulture(social);
		Culture linguistic = new Culture(LINGUISTIC, forWorld, true);
		forWorld.addCulture(linguistic.setParents(social));
		Culture learning = new Culture(LEARNING, forWorld, true);
		forWorld.addCulture(learning.setParents(linguistic));
		Culture tool_user = new Culture(TOOL_USER, forWorld, true);
		forWorld.addCulture(tool_user.setParents(learning));
		Culture embodied = new Culture(EMBODIED, forWorld, true);
		forWorld.addCulture(embodied.setParents(spacebound, mortal));
		Culture organic = new Culture(ORGANIC, forWorld, true);
		forWorld.addCulture(organic.setParents(organic));
	}

	public Culture(String name, World world) {
		this(name, world, false);
	}

	private Culture(String name, World world, boolean isEssential) {
		this.isEssential = isEssential;
		if (!isEssential && name.startsWith("_"))
			throw new IllegalArgumentException(
					name + " is an invalid name; underscore names are only for essential cultures");
		this.name = (isEssential ? (name.startsWith("_") ? name : "_" + name) : name);
		if (world.getCulture(name) != null) {
			throw new UnsupportedOperationException("a culture of this name already exists");
		}
		this.world = world;
		this.parents.add(world.getCulture(ROOT));
	}

	/**
	 * creates the root culture
	 * 
	 * @param world
	 */
	private Culture(World world) {
		if (world.getCulture(ROOT) != null)
			throw new UnsupportedOperationException("Cannot create root culture in world where one exists");
		this.name = ROOT;
		this.isEssential = true;
		this.world = world;
	}

	public Sociocon getSociocon(Sociocat type, String name) {
		return getSocioconMap(type).get(name);
	}

	public void addSociocon(Sociocon con) {
		this.getSocioconMap(con.getCategory()).put(con.getName(), con);
	}

	public Map<String, Sociocon> getSocioconMap(Sociocat type) {
		return sociocons.computeIfAbsent(type, (a) -> new TreeMap<>());
	}

	/**
	 * Either gets an existing sociocon with the given type and name, or creates a
	 * new one using the type, name, and the sources given
	 * 
	 * @param type
	 * @param name
	 * @param sys
	 * @return
	 */
	public Sociocon getOrCreateSociocon(Sociocat type, String name, IPurposeSource... sys) {
		Sociocon existing = getSociocon(type, name);
		if (existing == null) {
			existing = type.createSociocon(name, this, sys);
			this.addSociocon(existing);
		}
		return existing;
	}

	public Culture setParents(Culture... parents) {
		this.parents.clear();

		for (Culture c : parents) {

			this.parents.add(c);
		}
		if (this.parents.isEmpty()) {
			Culture root = world.getCulture(ROOT);
			this.parents.add(root);
		}
		initAncestorsAndChildren();
		return this;
	}

	public Culture setParents(Set<Culture> parents) {
		this.parents.clear();
		this.parents.addAll(parents);
		if (parents.isEmpty())
			parents.add(world.getCulture(ROOT));
		initAncestorsAndChildren();
		return this;
	}

	private void initAncestorsAndChildren() {
		for (Culture c : this.parents) {
			c.children.add(this);
		}
		this.ancestors.clear();
		for (Culture c : this.parents) {
			ancestors.addAll(c.parents);
			ancestors.addAll(c.ancestors);
		}

	}

	/**
	 * gets an iterable of all this culture's children
	 * 
	 * @return
	 */
	public Iterable<Culture> getChildren() {
		return this.iter;
	}

	/**
	 * gets an iterable of this culture's parents
	 * 
	 * @return
	 */
	public Iterable<Culture> getParents() {
		return this.iterp;
	}

	/**
	 * gets an iterable of this culture's ancestors
	 * 
	 * @return
	 */
	public Iterable<Culture> getAncestors() {
		return this.itera;
	}

	/**
	 * whether the given culture is a child of this culture
	 */
	public boolean hasChild(Culture child) {
		return children.contains(child);
	}

	public World getWorld() {
		return world;
	}

	/**
	 * If this culture is an essential culture
	 * 
	 * @return
	 */
	public boolean isEssential() {
		return isEssential;
	}

	public String getName() {
		return name;
	}

	public boolean isRootCulture() {
		return this.parents.isEmpty();
	}

	/**
	 * if the given culture is an immediate parent of this one
	 */
	public boolean isParent(Culture cul) {
		return this.parents.contains(cul);
	}

	/**
	 * if the given culture is higher in the hierarchy than parent for this culture
	 */
	public boolean isAncestor(Culture cul) {
		return this.ancestors.contains(cul);
	}

	/**
	 * whether this culture is ancestor, parent, or equivalent to any in the context;
	 * return true if the context is universal
	 * 
	 * @param ctxt
	 * @return
	 */
	public boolean isSuperiorToAny(CulturalContext ctxt) {
		if (ctxt.isUniversal()) return true;
		for (Culture c : ctxt) {
			if (this == c || c.isParent(this) || c.isAncestor(this)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * whether this culture is the ancestor of any in this context; return true if universal
	 * 
	 * @param ctxt
	 * @return
	 */
	public boolean isAncestorOfAny(CulturalContext ctxt) {
		if (ctxt.isUniversal()) return true;
		for (Culture c : ctxt) {
			if (c.isAncestor(this)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * if this culture is a child, descendant, or equivalent to any of these; true if universal
	 */
	public boolean isSubordinateToAny(CulturalContext ctxt) {
		if (ctxt.isUniversal()) return true;
		for (Culture c : ctxt) {
			if (this == c || this.isParent(c) || this.isAncestor(c)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * whether any culture in the context is an ancestor of this one; true if universal
	 * 
	 * @param ctxt
	 * @return
	 */
	public boolean isDescendantOfAny(CulturalContext ctxt) {
		if (ctxt.isUniversal()) return true;
		for (Culture c : ctxt) {
			if (this.isAncestor(c)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return this.name;
	}

	private class BasicIterable implements Iterable<Culture> {

		Set<Culture> de;

		private BasicIterable(Set<Culture> de) {
			this.de = de;
		}

		@Override
		public Iterator<Culture> iterator() {
			return new BasicIterator(de.iterator());
		}

	}

	private class BasicIterator implements Iterator<Culture> {

		final Iterator<Culture> iter;

		private BasicIterator(Iterator<Culture> it) {
			this.iter = it;
		}

		@Override
		public boolean hasNext() {
			return iter.hasNext();
		}

		@Override
		public Culture next() {
			return iter.next();
		}

	}
}
