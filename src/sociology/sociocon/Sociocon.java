package sociology.sociocon;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import psych.action.types.Action;
import sociology.Profile;

/**
 * All sociocons are categories, distinguished by unique names, which exist in a
 * culture and can be used to quantify objects as having specific uses or
 * qualities
 * 
 * @author borah
 *
 */
public class Sociocon implements Comparable<Sociocon> {

	private Set<Profile> members = new HashSet<>();
	private String name;
	private Sociocat category;
	private Map<String, Socioprop<?>> properties = new TreeMap<>();
	private Set<Action> actions = new HashSet<>();

	Sociocon(Sociocat category, String name, IPurposeSource... sociosource) {
		this.category = category;
		this.name = name;
		this.addPropertiesAndActions(category);
		for (IPurposeSource p : sociosource) {
			this.addPropertiesAndActions(p);
		}
		System.out.println("Created sociocon " + this + this.socioconReport());
	}

	public void addPropertiesAndActions(IPurposeSource source) {
		Collection<Socioprop<?>> s = source.getPropertiesFor(this);
		Collection<Action> a = source.getActionsFor(this);
		for (Socioprop<?> so : s) {
			this.properties.put(so.getName(), so);
		}
		this.actions.addAll(a);
	}

	/**
	 * DO NOT USE THIS except from Profile.addSociocon
	 * 
	 * @param p
	 */
	public void directAddMember(Profile p) {
		this.members.add(p);
	}

	/**
	 * DO NOT USE THIS except from Profile.removeSociocon
	 * 
	 * @param p
	 */
	public void directRemoveMember(Profile p) {
		this.members.remove(p);
	}

	public Set<Profile> getMembers() {
		return members;
	}

	public Collection<Socioprop<?>> getProperties() {
		return this.properties.values();
	}

	public Socioprop<?> getProperty(String name) {
		return properties.get(name);
	}

	public Set<Action> getActions() {
		return actions;
	}

	public String getName() {
		return name;
	}

	public Sociocat getCategory() {
		return category;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Sociocon))
			return false;
		Sociocon other = (Sociocon) obj;
		return this.name.equals(other.name) && this.category == other.category;
	}

	private String getCatWithName() {
		return this.category + "." + this.name;
	}

	@Override
	public int compareTo(Sociocon o) {
		return this.getCatWithName().compareTo(o.getCatWithName());
	}

	public String socioconReport() {
		return "actions: " + this.actions + ", properties: " + this.properties.values();
	}

	@Override
	public String toString() {
		return "Sociocon: " + this.name + ", category: " + this.category + " ";
	}
}
