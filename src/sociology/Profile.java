package sociology;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import culture.CulturalContext;
import culture.Culture;
import sim.IHasCulture;
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
public abstract class Profile implements IProfile {
	private Map<Sociocat, Map<String, Sociocon>> sociocons = new HashMap<>();
	private Map<Socioprop<?>, PropertyHolder<?>> socioprops = new HashMap<>();
	private Map<Culture, Map<String, Sociocon>> socioconsByCulture = new HashMap<>();
	private HashMap<Sociocon, Set<PropertyHolder<?>>> sociopropsBySociocon = new HashMap<>();

	private String name;

	public Profile(String name) {
		this.name = name;
	}

	public abstract IHasProfile getOwner();

	public abstract void setOwner(IHasProfile owner);

	protected <T> PropertyHolder<T> getPropertyHolder(Socioprop<T> prop) {
		return (PropertyHolder<T>) socioprops.get(prop);
	}

	public Set<PropertyHolder<?>> getAssociatedProperties(Sociocon soc) {
		return sociopropsBySociocon.get(soc);
	}

	public boolean hasSociocon(Sociocon con) {
		return this.sociocons.computeIfAbsent(con.getCategory(), (a) -> new TreeMap<>()).containsValue(con);
	}

	public boolean hasSociocat(Sociocat cat, CulturalContext ctxt) {
		boolean cflag = true;
		if (!ctxt.isUniversal()) {
			Culture catc = cat.getCulture(this.getWorld());
			cflag = catc.isSuperiorToAny(ctxt);
		}
		return (this.sociocons.get(cat) != null ? !this.sociocons.get(cat).isEmpty() : false) && cflag;
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
	 * Returns the value of the given property as stored in the profile; null if not
	 * available or culturally inappropriate
	 * 
	 * @param <T>
	 * @param prop
	 * @return
	 */
	public <T> T getValue(Socioprop<T> prop, CulturalContext ctxt) {
		if (!ctxt.isUniversal() && prop.getOrigin() instanceof IHasCulture ic) {
			if (!ic.getCulture(getWorld()).isSuperiorToAny(ctxt)) {
				return null;
			}
		}
		return getPropertyHolder(prop).getValue();
	}

	public boolean hasValue(Socioprop<?> prop, CulturalContext ctxt) {
		if (!ctxt.isUniversal() && prop.getOrigin() instanceof IHasCulture ic) {
			if (!ic.getCulture(getWorld()).isSuperiorToAny(ctxt)) {
				return false;
			}
		}
		return this.getPropertyHolder(prop) != null;
	}

	public Collection<Sociocon> getSociocons(Sociocat cat, CulturalContext ctxt) {
		if (!ctxt.isUniversal()) {
			if (!cat.getCulture(getWorld()).isSuperiorToAny(ctxt)) {
				return Collections.emptySet();
			}
		}
		return this.sociocons.computeIfAbsent(cat, (a) -> new HashMap<String, Sociocon>()).values();
	}

	public Sociocon getSociocon(Sociocat cat, String name) {
		Sociocon c = this.sociocons.computeIfAbsent(cat, (a) -> new HashMap<>()).get(name);

		return c;
	}

	public void addSociocon(Sociocon con) {
		this.sociocons.computeIfAbsent(con.getCategory(), (a) -> new TreeMap<>()).put(con.getName(), con);
		this.socioconsByCulture.computeIfAbsent(con.getCulture(), (a) -> new TreeMap<>()).put(con.getName(), con);
		Set<Socioprop<?>> props = new HashSet<>(con.getProperties());
		for (Socioprop<?> prop : props) {
			PropertyHolder<?> pr = prop.createFor(con, this);
			socioprops.put(prop, pr);
			sociopropsBySociocon.computeIfAbsent(con, (a) -> new HashSet<>()).add(pr);

		}
		con.directAddMember(this);
	}

	public void removeSociocon(Sociocon con) {
		if (this.sociocons.containsKey(con.getCategory()))
			this.sociocons.get(con.getCategory()).remove(con.getName(), con);
		if (this.socioconsByCulture.containsKey(con.getCulture())) {
			this.sociocons.get(con.getCulture()).remove(con.getName(), con);
		}
		if (this.sociocons.get(con.getCategory()).isEmpty())
			sociocons.remove(con.getCategory());
		Set<PropertyHolder<?>> props = this.sociopropsBySociocon.remove(con);
		for (PropertyHolder<?> prop : props) {
			socioprops.remove(prop.getProperty());
		}
		con.directRemoveMember(this);
	}

	public String profileReport() {
		return " sociocons/properties-" + this.sociopropsBySociocon.toString();
	}

	@Override
	public String toString() {
		return "p;\"" + this.name + "\"";

	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Profile))
			return false;
		return this.getOwner() == ((Profile) obj).getOwner();
	}

	public String getName() {
		return name;
	}

	@Override
	public Profile getActualProfile() {
		return this;
	}

	/**
	 * get the type profile for this profile
	 * 
	 * @return
	 */
	public abstract TypeProfile getTypeProfile();

	/**
	 * whether this profile is a type profile
	 * 
	 * @return
	 */
	public abstract boolean isTypeProfile();

	@Override
	public Collection<Sociocon> getSocioconsFor(Culture cul) {
		if (this.socioconsByCulture.containsKey(cul)) {
			return socioconsByCulture.get(cul).values();
		}
		return Collections.emptySet();
	}

}
