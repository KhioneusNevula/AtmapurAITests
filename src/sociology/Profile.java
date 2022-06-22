package sociology;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import psych.action.types.Action;
import sim.IHasProfile;
import sociology.sociocon.PropertyHolder;
import sociology.sociocon.Sociocat;
import sociology.sociocon.Sociocon;
import sociology.sociocon.Socioprop;

/**
 * A storage of social traits of an object, phenomenon, etc, so that it can be
 * recognized culturally with its own properties
 * 
 * @author borah
 *
 */
public class Profile implements IProfile {
	private Map<Sociocat, Map<String, Sociocon>> sociocons = new HashMap<>();
	private Map<Socioprop<?>, PropertyHolder<?>> socioprops = new HashMap<>();
	private HashMap<Sociocon, Set<PropertyHolder<?>>> sociopropsBySociocon = new HashMap<>();
	private HashMap<Sociocon, Set<Action>> actionsBySociocon = new HashMap<>();
	private Set<Action> allActions = new HashSet<>();
	private IHasProfile owner;
	private String name;

	public Profile(IHasProfile owner, String name) {
		this.owner = owner;
		this.name = name;
	}

	public IHasProfile getOwner() {
		return owner;
	}

	public void setOwner(IHasProfile owner) {
		this.owner = owner;
	}

	public <T> PropertyHolder<T> getPropertyHolder(Socioprop<T> prop) {
		return (PropertyHolder<T>) socioprops.get(prop);
	}

	public Set<PropertyHolder<?>> getAssociatedProperties(Sociocon soc) {
		return sociopropsBySociocon.get(soc);
	}

	public Set<Action> getAllActions() {
		return this.allActions;
	}

	public Set<Action> getActionsFor(Sociocon con) {
		return this.actionsBySociocon.get(con);
	}

	public boolean hasSociocon(Sociocon con) {
		return this.sociocons.computeIfAbsent(con.getCategory(), (a) -> new TreeMap<>()).containsValue(con);
	}

	public boolean hasSociocat(Sociocat cat) {
		return this.sociocons.get(cat) != null ? !this.sociocons.get(cat).isEmpty() : false;
	}

	/**
	 * Changes the value of the given property and returns the old one
	 * 
	 * @param <T>
	 * @param prop
	 * @return
	 */
	public <T> T setValue(Socioprop<T> prop, T val) {
		PropertyHolder<T> d = getPropertyHolder(prop);
		T o = d.getValue();
		d.setValue(val);
		return o;
	}

	/**
	 * Returns the value of the given property as stored in the profile
	 * 
	 * @param <T>
	 * @param prop
	 * @return
	 */
	public <T> T getValue(Socioprop<T> prop) {
		return getPropertyHolder(prop).getValue();
	}

	public Collection<Sociocon> getSociocons(Sociocat cat) {
		return this.sociocons.computeIfAbsent(cat, (a) -> new HashMap<String, Sociocon>()).values();
	}

	public Sociocon getSociocon(Sociocat cat, String name) {
		return this.sociocons.computeIfAbsent(cat, (a) -> new HashMap<>()).get(name);
	}

	public void addSociocon(Sociocon con) {
		this.sociocons.computeIfAbsent(con.getCategory(), (a) -> new HashMap<>()).put(con.getName(), con);
		Set<Socioprop<?>> props = new HashSet<>(con.getProperties());
		for (Socioprop<?> prop : props) {
			PropertyHolder<?> pr = prop.createFor(con, this);
			socioprops.put(prop, pr);
			sociopropsBySociocon.computeIfAbsent(con, (a) -> new HashSet<>()).add(pr);

		}
		Set<Action> actions = con.getActions();
		this.actionsBySociocon.computeIfAbsent(con, (a) -> new HashSet<>()).addAll(actions);
		this.allActions.addAll(actions);
		con.directAddMember(this);
	}

	public void removeSociocon(Sociocon con) {
		this.sociocons.computeIfAbsent(con.getCategory(), (a) -> new TreeMap<>()).remove(con.getName(), con);
		if (this.sociocons.get(con.getCategory()).isEmpty())
			sociocons.remove(con.getCategory());
		Set<PropertyHolder<?>> props = this.sociopropsBySociocon.remove(con);
		for (PropertyHolder<?> prop : props) {
			socioprops.remove(prop.getProperty());
		}
		con.directRemoveMember(this);
		Set<Action> actions = this.actionsBySociocon.remove(con);
		if (con != null)
			this.allActions.removeAll(actions);
	}

	public String profileReport() {
		return " sociocons/properties-" + this.sociopropsBySociocon.toString() + " actions-" + this.allActions;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ":\"" + this.name + "\"";

	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Profile))
			return false;
		return this.owner == ((Profile) obj).owner;
	}

	public String getName() {
		return name;
	}

	@Override
	public Profile getActualProfile() {
		return this;
	}
}
