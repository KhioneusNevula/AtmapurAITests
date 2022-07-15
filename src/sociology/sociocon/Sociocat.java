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
import psych_first.perception.knowledge.IKnowledgeType.IBooleanKnowledge;
import sociology.Profile;

/**
 * These are basic, fundamental ideas which sociocons can extend their
 * functionality from. They are used to confer functionalities to the sociocons
 * a culture produces
 * 
 * @author borah
 *
 */
public enum Sociocat implements IPurposeSource, IBooleanKnowledge {
	FOOD("food", true, Socioprops.FOOD_NOURISHMENT), DANGER("danger", Socioprops.DANGER_LEVEL),
	SHELTER("shelter", Socioprops.SHELTER_LEVEL), CLOTHING("clothing"),
	PERSON("person", Socioprops.ACTOR_HELD, Socioprops.ACTOR_WORN);

	private Map<String, Socioprop<?>> properties = new TreeMap<>();
	private Set<Action> actions = new HashSet<>();
	private String name;
	private Sociocat parent = null;
	private boolean singleton = false;

	private Sociocat(String name, IPurposeElement... props) {
		this.name = name;
		this.addelements(Arrays.asList(props));
	}

	/**
	 * 
	 * @param name
	 * @param cultureID
	 * @param props
	 */
	private Sociocat(String name, boolean singleton, IPurposeElement... props) {
		this(name, props);
		this.singleton = singleton;
	}

	public Sociocon createSociocon(String name, Culture forCulture, IPurposeSource... sources) {
		if (name.startsWith("_"))
			throw new UnsupportedOperationException(
					"invalid name '" + name + "'; underscore names are reserved for singletons");
		if (this.isSingleton() && forCulture.getSociocon(this, this.name) != null) {
			throw new UnsupportedOperationException(
					"a sociocon for this singleton " + this.name + " exists in " + forCulture);

		}
		Sociocon con = new Sociocon(this, isSingleton() && !name.startsWith("_") ? "_" + name : name, forCulture,
				sources);
		forCulture.addSociocon(con);
		return con;
	}

	/**
	 * whether this sociocat has only one sociocon for a given Culture
	 */
	public boolean isSingleton() {
		return singleton;
	}

	public Sociocon getSingleSociocon(Culture culture) {
		if (!isSingleton())
			throw new UnsupportedOperationException(this + " is not a singleton");

		return culture.getOrCreateSociocon(this, name);
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

	private Sociocat(String name, Sociocat parent, IPurposeElement... props) {
		this(name, props);
		this.properties.putAll(parent.properties);
		this.actions.addAll(parent.actions);
	}

	private Sociocat(String name, Sociocat parent, boolean singleton, IPurposeElement... props) {
		this(name, parent, props);

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

	@Override
	public boolean isSocialKnowledge() {
		return true;
	}

	@Override
	public boolean isIdentitySpecific() {
		return true;
	}

}
