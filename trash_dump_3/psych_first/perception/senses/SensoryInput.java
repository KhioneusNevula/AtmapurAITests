package psych_first.perception.senses;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import main.ImmutableCollection;
import psych_first.perception.senses.SensoryAttribute.AttributeHolder;
import sociology.Profile;

/**
 * this represents a sensory output but on the perception end by a sensing
 * entity
 * 
 * @author borah
 *
 */
public class SensoryInput {

	private SensoryOutput output;
	private Set<SensoryAttribute<?>> sensed = new HashSet<>();
	private boolean unsensable = true;

	public SensoryInput(SensoryOutput from) {
		this.output = from;
	}

	/**
	 * returns the profile of this sensoryinput; only use if the profile is known
	 * 
	 * @return
	 */
	public Profile getIdentity() {
		return this.output.getIdentity();
	}

	/**
	 * maybe not used?
	 * 
	 * @return
	 */
	public boolean isUnsensable() {
		return unsensable;
	}

	public Collection<SensoryAttribute<?>> getAttributes() {
		return new ImmutableCollection<>(this.sensed);
	}

	/**
	 * returns this attribute if present AND sensed, null if not
	 * 
	 * @param <T>
	 * @param attr
	 * @return
	 */
	public <T> AttributeHolder<T> getAttribute(SensoryAttribute<T> attr) {
		if (sensed.contains(attr)) {
			return output.getAttribute(attr);
		}
		return null;
	}

	/**
	 * returns true if attribute is present AND sensed
	 */
	public boolean hasAttribute(SensoryAttribute<?> attr) {
		if (sensed.contains(attr)) {
			return output.hasAttribute(attr);
		}
		return false;
	}

	public boolean hasAttribute(AttributeHolder<?> attr) {
		if (sensed.contains(attr)) {
			return output.hasAttribute(attr);
		}
		return false;
	}

	public boolean retainTraits(Collection<SensoryAttribute<?>> col) {
		return this.sensed.retainAll(col);
	}

	/**
	 * adds these attributes to the list of sensed traits, i.e. that which the
	 * sensor sensed
	 * 
	 * @param att
	 * @return
	 */
	public SensoryInput sense(SensoryAttribute<?>... att) {
		for (SensoryAttribute<?> at : att) {
			this.sensed.add(at);
			unsensable = false;
		}
		return this;
	}

	/**
	 * Enforces that the given traits have not been sensed for whatever reason
	 * 
	 * @param att
	 * @return
	 */
	public SensoryInput block(SensoryAttribute<?>... att) {
		for (SensoryAttribute<?> at : att) {
			this.sensed.remove(at);
		}
		if (sensed.isEmpty())
			unsensable = true;
		return this;
	}

	/**
	 * returns the SensoryOutput that this is formed from
	 * 
	 * @return
	 */
	public SensoryOutput getOutput() {
		return output;
	}

	@Override
	public String toString() {
		return "sensory input from " + this.output.getIdentity();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof SensoryInput a && a.output.getOwner().equals(this.output.getOwner());
	}

}
