package psych_first.perception.senses;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import main.ImmutableCollection;
import psych_first.perception.senses.SensoryAttribute.AttributeHolder;
import sim.IHasProfile;
import sim.ILocatable;
import sim.Location;
import sociology.Profile;

/**
 * this is the totality of what a thing shows to the world as stimuli
 * 
 * @author borah
 *
 */
public class SensoryOutput {

	private IHasProfile owner;
	private Map<SensoryAttribute<?>, AttributeHolder<?>> attributes = new HashMap<>();
	private Map<Sense, Integer> overallSenseLevel = new HashMap<>();

	public SensoryOutput(IHasProfile owner) {
		this.owner = owner;
	}

	public boolean hasLocation() {
		return owner instanceof ILocatable;
	}

	public Location getLocation() {
		if ((owner instanceof ILocatable l)) {
			return l.getLocation();
		}
		throw new IllegalStateException(owner + " is not a locatable entity");
	}

	public <T> SensoryOutput add(AttributeHolder<T> attr) {
		this.attributes.put(attr.getType(), attr);
		return this;
	}

	/**
	 * the 'gateway' sense level needed to sense this output with this sense
	 * 
	 * @return
	 */
	public int getOverallSenseLevel(Sense for_) {
		return overallSenseLevel.getOrDefault(for_, 0);
	}

	public void setOverallSenseLevel(Sense for_, int overallSenseLevel) {
		this.overallSenseLevel.put(for_, overallSenseLevel);
	}

	public <T> AttributeHolder<T> remove(SensoryAttribute<T> attr) {
		return (AttributeHolder<T>) this.attributes.remove(attr);
	}

	public <T> AttributeHolder<T> getAttribute(SensoryAttribute<T> attr) {
		return (AttributeHolder<T>) this.attributes.get(attr);
	}

	public boolean hasAttribute(SensoryAttribute<?> attr) {
		return this.attributes.containsKey(attr);
	}

	public boolean hasAttribute(AttributeHolder<?> attr) {
		return this.attributes.containsValue(attr);
	}

	/**
	 * returns the profile of this thing
	 * 
	 * @return
	 */
	public Profile getIdentity() {
		return this.owner.getProfile();
	}

	public IHasProfile getOwner() {
		return owner;
	}

	@Override
	public String toString() {
		return "sensory output for " + this.owner;
	}

	public Collection<SensoryAttribute<?>> getAttributes() {
		return new ImmutableCollection<>(this.attributes.keySet());
	}

}
