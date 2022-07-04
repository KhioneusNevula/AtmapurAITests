package sociology.sociocon;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;

import culture.Culture;
import psych_first.action.types.Action;
import sim.IHasCulture;
import sim.World;
import sociology.Profile;

/**
 * These are basic, fundamental ideas which sociocons can extend their
 * functionality from. They are used to confer functionalities to the sociocons
 * a culture produces
 * 
 * @author borah
 *
 */
public enum Sociocat implements IPurposeSource, IHasCulture {
	WORLD("world", Culture.SPACEBOUND), EVENT("event", Culture.TIMEBOUND, true),
	FOOD("food", Culture.ORGANIC, true, Socioprops.FOOD_NOURISHMENT),
	DRINK("drink", Culture.ORGANIC, true, Socioprops.DRINK_QUENCHING),
	DANGER("danger", Culture.MORTAL, Socioprops.DANGER_LEVEL),
	SHELTER("shelter", Culture.EMBODIED, Socioprops.SHELTER_LEVEL), CLOTHING("clothing", Culture.TOOL_USER),
	PERSON("person", Culture.SOCIAL, Socioprops.ACTOR_HELD, Socioprops.ACTOR_WORN);

	private Map<String, Socioprop<?>> properties = new TreeMap<>();
	private Set<Action> actions = new HashSet<>();
	private String name;
	private Sociocat parent = null;
	private String cultureID;
	private boolean singleton = false;

	private Sociocat(String name, String cultureID, IPurposeElement... props) {
		this.name = name;
		this.addelements(Arrays.asList(props));
		this.cultureID = cultureID;
	}

	/**
	 * 
	 * @param name
	 * @param cultureID
	 * @param props
	 */
	private Sociocat(String name, String cultureID, boolean singleton, IPurposeElement... props) {
		this(name, cultureID, props);
		this.singleton = singleton;
	}

	public Sociocon createSociocon(String name, Culture forCulture, IPurposeSource... sources) {
		if (!isSingleton() && name.startsWith("_"))
			throw new UnsupportedOperationException(
					"invalid name '" + name + "'; underscore names are reserved for singletons");
		Sociocon con = new Sociocon(this, isSingleton() && !name.startsWith("_") ? "_" + name : name, forCulture,
				sources);
		forCulture.addSociocon(con);
		return con;
	}

	/**
	 * whether this sociocat has only one sociocon for a given world, e.g. food is
	 * like this
	 */
	public boolean isSingleton() {
		return singleton;
	}

	public Sociocon getSingleSociocon(World world) {
		if (!isSingleton())
			throw new UnsupportedOperationException(this + " is not a singleton");

		return world.getCulture(cultureID).getOrCreateSociocon(this, name);
	}

	public String getCultureID() {
		return cultureID;
	}

	public Culture getCulture(World world) {
		return world.getCulture(cultureID);
	}

	private void addelements(Collection<IPurposeElement> props) {

		for (IPurposeElement prop : props) {
			if (prop instanceof Socioprop)
				this.properties.put(prop.getName(), (Socioprop<?>) prop);
			else if (prop instanceof Action)
				this.actions.add((Action) prop);
			prop.setOrigin(this);
		}
	}

	public <T> Socioprop<T> getProperty(String name) {
		return (Socioprop<T>) this.properties.get(name);
	}

	public Set<Action> getActions() {
		return actions;
	}

	private Sociocat(String name, Sociocat parent, String cultureID, IPurposeElement... props) {
		this(name, cultureID, props);
		this.properties.putAll(parent.properties);
		this.actions.addAll(parent.actions);
	}

	private Sociocat(String name, Sociocat parent, String cultureID, boolean singleton, IPurposeElement... props) {
		this(name, parent, cultureID, props);

		this.singleton = singleton;
	}

	/**
	 * Returns if this is a child of the other
	 * 
	 * @param other
	 * @return
	 */
	public boolean isChild(Sociocat other) {
		Sociocat cur = this.parent;
		while (cur != null) {
			if (cur == other)
				return true;
			cur = cur.parent;
		}
		return false;
	}

	/**
	 * Returns if other is a child of this
	 * 
	 * @param other
	 * @return
	 */
	public boolean isParent(Sociocat other) {
		return other.isChild(this);
	}

	public String getName() {
		return name;
	}

	@Override
	public Collection<Socioprop<?>> getPropertiesFor(Sociocon socio) {
		return properties.values();
	}

	public static <T> Socioprop<T> p(String name, Class<T> type, Function<Profile, T> defaultValue) {
		return new Socioprop<>(name, type, defaultValue);
	}

	public static <T> Socioprop<T> p(String name, Class<T> type, T defaultValue) {
		return new Socioprop<>(name, type, (a) -> defaultValue);
	}

	@Override
	public Collection<Action> getActionsFor(Sociocon socio) {
		return this.actions;
	}

}
