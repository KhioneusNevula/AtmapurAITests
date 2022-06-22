package sociology.sociocon;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;

import psych.action.types.Action;
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
public enum Sociocat implements IPurposeSource {
	FOOD("food", Socioprops.FOOD_NOURISHMENT), DRINK("drink", Socioprops.DRINK_QUENCHING),
	DANGER("danger", Socioprops.DANGER_LEVEL), SHELTER("shelter", Socioprops.SHELTER_LEVEL), CLOTHING("clothing"),
	PERSON("person", Socioprops.ACTOR_HELD, Socioprops.ACTOR_WORN);

	private Map<String, Socioprop<?>> properties = new TreeMap<>();
	private Set<Action> actions = new HashSet<>();
	private String name;
	private Sociocat parent = null;

	private Sociocat(String name, IPurposeElement... props) {
		this.name = name;
		this.addelements(Arrays.asList(props));
	}

	public Sociocon createSociocon(World world, String name, IPurposeSource... sources) {
		Sociocon con = new Sociocon(this, name, sources);
		world.addSociocon(con);
		return con;
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

	private Sociocat(String name, Sociocat parent, Socioprop<?>... props) {
		this.name = name;
		this.addelements(Arrays.asList(props));
		this.properties.putAll(parent.properties);
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
