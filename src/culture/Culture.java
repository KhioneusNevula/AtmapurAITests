package culture;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import main.ImmutableCollection;
import psych_first.perception.knowledge.IKnowledgeType;
import psych_first.perception.knowledge.Identity;
import psych_first.perception.knowledge.Noosphere;
import sim.IHasProfile;
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
	private static final String TIMEBOUND = "_timebound";
	/**
	 * the string id for the cultures of all entities bound in space (entities which
	 * recognize location)
	 */
	private static final String SPACEBOUND = "_spacebound";
	/**
	 * the string id for cultures of all entities that can die (entities which
	 * recognize danger and death)
	 */
	private static final String MORTAL = "_mortal";
	/**
	 * the string id for cultures of all entities that are sentient (can feel
	 * emotions; must be mortal)
	 */
	private static final String SENTIENT = "_sentient";
	/**
	 * the string id for cultures of all entities that are social (can form
	 * relationships and hierarchies)
	 */
	private static final String SOCIAL = "_social";

	/**
	 * the string id for cultures of all entities that can use language (must be
	 * social)
	 */
	private static final String LINGUISTIC = "_linguistic";

	/**
	 * the string id for cultures of all entities that can learn things (must be
	 * linguistic)
	 */
	private static final String LEARNING = "_learning";

	/**
	 * the string id for cultures of entities that can use tools (must be learning)
	 */
	private static final String TOOL_USER = "_tool_user";

	/**
	 * the string id for cultures of all entities that have bodies; must be
	 * spacebound and mortal
	 */
	private static final String EMBODIED = "_embodied";

	/**
	 * the string id for cultures of all entities that are organic and have hunger
	 * needs and the like
	 */
	private static final String ORGANIC = "_organic";

	private Culture surrounding = null;

	private String name;
	private World world;
	private Map<Sociocat, Map<String, Sociocon>> sociocons = new HashMap<>();
	private Map<IKnowledgeType<?>, UUID> information = new HashMap<>();
	private Map<IHasProfile, Identity> identities = new HashMap<>();
	private Noosphere noosphere;

	/**
	 * TODO make this more generalizable
	 * 
	 * @param forWorld
	 */
	public static void genEssentialCultures(World forWorld) {
		Culture root = new Culture(forWorld);
		forWorld.addCulture(root);

	}

	public Culture(String name, World world) {
		this(name, world, world.getCulture(ROOT));
	}

	public Culture(String name, World world, Culture surrounding) {

		if (name.startsWith("_"))
			throw new IllegalArgumentException(
					name + " is an invalid name; underscore names are only for essential cultures");
		if (world.getCulture(name) != null) {
			throw new UnsupportedOperationException("a culture of this name already exists");
		}
		this.world = world;
		this.surrounding = surrounding;
		this.noosphere = world.getNoosphere();
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
		this.world = world;
	}

	public Sociocon getSociocon(Sociocat type, String name) {
		return getSocioconMap(type).get(name);
	}

	public Collection<Sociocat> getSociocats() {
		return new ImmutableCollection<>(this.sociocons.keySet());
	}

	public void addSociocon(Sociocon con) {
		this.getSocioconMap(con.getCategory()).put(con.getName(), con);
	}

	public boolean hasSociocon(Sociocon con) {
		return sociocons.containsKey(con.getCategory()) ? sociocons.get(con.getCategory()).containsValue(con) : false;
	}

	public Map<String, Sociocon> getSocioconMap(Sociocat type) {
		return sociocons.computeIfAbsent(type, (a) -> new TreeMap<>());
	}

	public Identity getIdentity(IHasProfile f) {
		return this.identities.get(f);
	}

	public boolean knowsIdentity(IHasProfile e) {
		return this.identities.containsKey(e);
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

	public <T> T getInfo(IKnowledgeType<T> info) {
		if (info instanceof Sociocon con) {
			return (T) Boolean.valueOf(this.hasSociocon(con));
		} else if (info instanceof Sociocat cat) {
			return (T) Boolean.valueOf(this.sociocons.containsKey(cat));
		} else {
			return (T) this.noosphere.getInfo(this.information.get(info), info);
		}
	}

	public boolean hasInfo(IKnowledgeType<?> info) {
		if (info instanceof Sociocon con) {
			return this.getInfo(con);
		} else if (info instanceof Sociocat cat) {
			return this.getInfo(cat);
		} else {
			return this.information.containsKey(info);
		}
	}

	public World getWorld() {
		return world;
	}

	public String getName() {
		return name;
	}

	public boolean isRootCulture() {
		return this.name == ROOT;
	}

	public Culture getSurrounding() {
		return surrounding;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
